package gay.aliahx.mixtape.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.NameableEnum;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ModConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("mixtape.json").toFile();
    public static ModConfig INSTANCE = loadConfigFile(CONFIG_FILE);

    public enum MusicType implements NameableEnum {
        AUTOMATIC,
        CREATIVE,
        CREDITS,
        DRAGON,
        END,
        GAME,
        MENU,
        NETHER_BASALT_DELTAS,
        NETHER_CRIMSON_FOREST,
        OVERWORLD_DEEP_DARK,
        OVERWORLD_DRIPSTONE_CAVES,
        OVERWORLD_GROVE,
        OVERWORLD_JAGGED_PEAKS,
        OVERWORLD_LUSH_CAVES,
        OVERWORLD_SWAMP,
        OVERWORLD_FOREST,
        OVERWORLD_OLD_GROWTH_TAIGA,
        OVERWORLD_MEADOW,
        OVERWORLD_CHERRY_GROVE,
        NETHER_NETHER_WASTES,
        OVERWORLD_FROZEN_PEAKS,
        OVERWORLD_SNOWY_SLOPES,
        NETHER_SOUL_SAND_VALLEY,
        OVERWORLD_STONY_PEAKS,
        NETHER_WARPED_FOREST,
        OVERWORLD_FLOWER_FOREST,
        OVERWORLD_DESERT,
        OVERWORLD_BADLANDS,
        OVERWORLD_JUNGLE,
        OVERWORLD_SPARSE_JUNGLE,
        OVERWORLD_BAMBOO_JUNGLE,
        UNDER_WATER;

        @Override
        public Text getDisplayName() {
            return Text.translatable("config.mixtape.musicType." + this.name().toLowerCase());
        }
    }
    public MainConfig main = new MainConfig();
    public MusicToastConfig musicToast = new MusicToastConfig();
    public MenuConfig menu = new MenuConfig();
    public CreativeConfig creative = new CreativeConfig();
    public GameConfig game = new GameConfig();
    public UnderwaterConfig underwater = new UnderwaterConfig();
    public EndConfig end = new EndConfig();
    public NetherConfig nether = new NetherConfig();
    public CreditsConfig credits = new CreditsConfig();
    public JukeboxConfig jukebox = new JukeboxConfig();

    public static class MainConfig {
        public boolean enabled = false;
        public boolean varyPitch = false;
        public long maxNoteChange = 3;
        public long minNoteChange = -3;
        public boolean noDelayBetweenSongs = false;
        public boolean playKeybindReplacesCurrentSong = false;
        public boolean skipKeybindStartsNextSong = false;
        public MusicType musicType = MusicType.AUTOMATIC;
        public boolean pauseMusicWhenGamePaused = true;
        public boolean stopMusicWhenSwitchingDimensions = true;
        public boolean stopMusicWhenLeftGame = true;
        public boolean enableDebugInfo = true;
    }

    public static class MusicToastConfig {
        public boolean enabled = true;
        public boolean showArtistName = true;
        public boolean showAlbumName = true;
        public boolean showAlbumCover = true;
        public boolean useDiscItemAsAlbumCover = false;
        public boolean hideJukeboxHotbarMessage = true;
        public boolean useHotbarInsteadOfToast = false;
        public boolean toastMakesSound = false;
        public boolean remainForFullSong = false;
        public int toastDisplayTime = 7500;
    }

    public static class MenuConfig {
        public boolean enabled = true;
        public int minSongDelay = 20;
        public int maxSongDelay = 600;
        public float volume = 100;
    }

    public static class CreativeConfig {
        public boolean enabled = true;
        public int minSongDelay = 12000;
        public int maxSongDelay = 24000;
        public float volume = 100;
    }

    public static class GameConfig {
        public boolean enabled = true;
        public int minSongDelay = 12000;
        public int maxSongDelay = 24000;
        public boolean creativeMusicPlaysInSurvival = false;
        public float volume = 100;
    }

    public static class UnderwaterConfig {
        public boolean enabled = true;
        public int minSongDelay = 12000;
        public int maxSongDelay = 24000;
        public float volume = 100;
    }

    public static class EndConfig {
        public boolean enabled = true;
        public int minSongDelay = 6000;
        public int maxSongDelay = 24000;
        public float volume = 100;
    }

    public static class NetherConfig {
        public boolean enabled = true;
        public int minSongDelay = 12000;
        public int maxSongDelay = 24000;
        public float volume = 100;
    }

    public static class CreditsConfig {
        public boolean enabled = true;
        public float volume = 100;
    }

    public static class JukeboxConfig {
        public boolean mono = false;
        public int distance = 56;
        public boolean elevenReplaces11 = false;
        public boolean dogReplacesCat = false;
        public boolean droopyLikesYourFaceReplacesWard = false;
        public boolean turnDownMusic = true;
        public float volume = 400;
    }

    public static Object getCategoryDefaults(String category) {
        return switch(category) {
            case "main" -> new MainConfig();
            case "musicToast" -> new MusicToastConfig();
            case "menu" -> new MenuConfig();
            case "creative" -> new CreativeConfig();
            case "game" -> new GameConfig();
            case "underwater" -> new UnderwaterConfig();
            case "end" -> new EndConfig();
            case "nether" -> new NetherConfig();
            case "credits" -> new CreditsConfig();
            case "jukebox" -> new JukeboxConfig();
            default -> throw new IllegalStateException("Unexpected value: " + category);
        };
    }

    public static Object getCategory(String category) {
        return switch (category) {
            case "main" -> ModConfig.INSTANCE.main;
            case "musicToast" -> ModConfig.INSTANCE.musicToast;
            case "menu" -> ModConfig.INSTANCE.menu;
            case "creative" -> ModConfig.INSTANCE.creative;
            case "game" -> ModConfig.INSTANCE.game;
            case "underwater" -> ModConfig.INSTANCE.underwater;
            case "end" -> ModConfig.INSTANCE.end;
            case "nether" -> ModConfig.INSTANCE.nether;
            case "credits" -> ModConfig.INSTANCE.credits;
            case "jukebox" -> ModConfig.INSTANCE.jukebox;
            default -> null;
        };
    }

    public void save() {
        saveConfigFile(CONFIG_FILE);
    }

    private static ModConfig loadConfigFile(File file) {
        ModConfig config = null;

        if (file.exists()) {
            try (BufferedReader fileReader = new BufferedReader( new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                config = GSON.fromJson(fileReader, ModConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Problem occurred when trying to load config: ", e);
            }
        }

        if (config == null) {
            config = new ModConfig();
        }

        config.saveConfigFile(file);
        return config;
    }

    private void saveConfigFile(File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}