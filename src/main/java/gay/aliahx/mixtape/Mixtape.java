package gay.aliahx.mixtape;

import gay.aliahx.mixtape.config.ModConfig;
import gay.aliahx.mixtape.toast.MusicToast;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Float.POSITIVE_INFINITY;
import static net.minecraft.sound.SoundCategory.MUSIC;

@Environment(EnvType.CLIENT)
public class Mixtape implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_VERSION = "v1.6.2";
    public static final String MOD_ID = "mixtape";
    private static KeyBinding skipKey;
    private static KeyBinding pauseKey;
    private static KeyBinding playKey;

    public static int debugTimeUntilNextSong = Integer.MAX_VALUE;
    public static int debugMaxTimeUntilNextSong = Integer.MAX_VALUE;
    public static boolean discPlaying = false;
    public static boolean startSong = false;
    public static float volumeScale = 1.0f;
    public static boolean paused = false;
    public static String debugCurrentMusicType = "minecraft:music.game";
    public static String debugNextMusicType = "minecraft:music.game";
    public static String currentSong = "";
    public static Map<BlockPos, Boolean> jukeboxes = new ConcurrentHashMap<>() {};
    private static final String MUSIC_LIST_JSON = "music_list.json";
    private static final String ALBUM_LIST_JSON = "album_list.json";

    public static MinecraftClient client;
    public static MusicManager musicManager;
    public static ResourceManager resourceManager;
    public static SoundManager soundManager;
    public static ModConfig config;

    @Override
    public void onInitializeClient() {
        config = ModConfig.INSTANCE;
        client = MinecraftClient.getInstance();

        MixtapePacks.init();

        skipKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.mixtape.skip", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "category.mixtape"));
        pauseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.mixtape.pause", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.mixtape"));
        playKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.mixtape.play", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.mixtape"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (skipKey.wasPressed()) {
                MinecraftClient.getInstance().getSoundManager().stopSounds(null, MUSIC);
                if(config.main.skipKeybindStartsNextSong) {
                    startSong = true;
                }
            }

            while (pauseKey.wasPressed()) {
                paused = !paused;
            }

            while (playKey.wasPressed()) {
                if(config.main.playKeybindReplacesCurrentSong) {
                    MinecraftClient.getInstance().getSoundManager().stopSounds(null, MUSIC);
                } else {
                    if(Mixtape.debugTimeUntilNextSong == Mixtape.debugMaxTimeUntilNextSong) {
                        return;
                    }
                }
                MinecraftClient.getInstance().getMusicTracker().play(MinecraftClient.getInstance().getMusicType());
            }
        });

        LOGGER.info("Mixtape " + MOD_VERSION + " loaded!");
    }

    public static SoundInstanceListener SoundListener = (soundInstance, soundSet, range) -> {
        if (soundInstance.getCategory() == SoundCategory.MUSIC) {
            currentSong = soundInstance.getSound().getIdentifier().toString();
            if(Mixtape.volumeScale != 0.001f) {
                String[] arr = soundInstance.getSound().getIdentifier().toString().split("/");
                if(client.options.getSoundVolume(SoundCategory.MASTER) != 0.0) {
                    MusicToast.show(client.getToastManager(), Mixtape.musicManager.getEntry(arr[arr.length - 1]), ItemStack.EMPTY);
                }
            }
        }
    };

    public static JsonObject[] resourceLoader(ResourceManager manager) {
        JsonObject musicListJson = new JsonObject();
        for (String string : manager.getAllNamespaces()) {
            List<Resource> list = manager.getAllResources(new Identifier(string, MUSIC_LIST_JSON));
            try {
                for (Resource resource : list) {
                    try (BufferedReader reader = resource.getReader()) {
                        JsonObject jsonFile = JsonHelper.deserialize(reader);
                        for (Map.Entry<String, JsonElement> entry : jsonFile.entrySet()) {
                            musicListJson.add(entry.getKey(), entry.getValue());
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        LOGGER.warn("Invalid {} in resourcepack: '{}'", MUSIC_LIST_JSON, resource.getResourcePackName(), runtimeException);
                    }
                }
            } catch (IOException ignored) {}
        }

        JsonObject albumListJson = new JsonObject();
        for (String string : manager.getAllNamespaces()) {
            List<Resource> list = manager.getAllResources(new Identifier(string, ALBUM_LIST_JSON));
            try {
                for (Resource resource : list) {
                    try (BufferedReader reader = resource.getReader()) {
                        JsonObject jsonFile = JsonHelper.deserialize(reader);
                        for (Map.Entry<String, JsonElement> entry : jsonFile.entrySet()) {
                            albumListJson.add(entry.getKey(), entry.getValue());
                        }
                    }
                    catch (RuntimeException runtimeException) {
                        LOGGER.warn("Invalid {} in resourcepack: '{}'", ALBUM_LIST_JSON, resource.getResourcePackName(), runtimeException);
                    }
                }
            } catch (IOException ignored) {}
        }

        return new JsonObject[]{musicListJson, albumListJson};
    }
}
