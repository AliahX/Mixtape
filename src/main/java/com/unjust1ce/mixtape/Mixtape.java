package com.unjust1ce.mixtape;

import com.unjust1ce.mixtape.config.MixtapePacks;
import com.unjust1ce.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import static net.minecraft.sound.SoundCategory.MUSIC;

@Environment(EnvType.CLIENT)
public class Mixtape implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_VERSION = "1.1";
    private static KeyBinding skipKey;
    private static KeyBinding pauseKey;

    public static String debugCurrentMusicType = "minecraft:music.game";
    public static String debugNextMusicType = "minecraft:music.game";
    public static int debugTimeUntilNextSong = Integer.MAX_VALUE;
    public static int debugMaxTimeUntilNextSong = Integer.MAX_VALUE;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        MixtapePacks.init();
        LOGGER.info("Mixtape version " + MOD_VERSION + " loaded!");

        skipKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.mixtape.skip", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "category.mixtape"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (skipKey.wasPressed()) {
                MinecraftClient.getInstance().getSoundManager().stopSounds(null, MUSIC);
            }
        });

        pauseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.mixtape.pause", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category.mixtape"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (pauseKey.wasPressed()) {
                ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
                config.mainConfig.paused = !config.mainConfig.paused;
            }
        });

    }

    public static boolean buttonMatchesKey(ClickableWidget button, String key) {
        Text buttonMessage = button.getMessage();
        if (buttonMessage instanceof TranslatableTextContent) {
            String buttonKey = ((TranslatableTextContent) buttonMessage).getKey();
            return buttonKey.equals(key);
        }
        return false;
    }
}
