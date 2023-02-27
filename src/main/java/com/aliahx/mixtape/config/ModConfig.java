package com.aliahx.mixtape.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "mixtape")
@Config.Gui.CategoryBackground(category = "jukebox", background = "minecraft:textures/block/jukebox_side.png")
@Config.Gui.CategoryBackground(category = "credits", background = "minecraft:textures/block/bedrock.png")
@Config.Gui.CategoryBackground(category = "nether", background = "minecraft:textures/block/netherrack.png")
@Config.Gui.CategoryBackground(category = "end", background = "minecraft:textures/block/end_stone.png")
@Config.Gui.CategoryBackground(category = "underwater", background = "minecraft:textures/block/sand.png")

public class ModConfig implements ConfigData {
    public enum MusicType {
        AUTOMATIC,
        CREATIVE,
        CREDITS,
        DRAGON,
        END,
        GAME,
        MENU,
        NETHER_BASALT_DELTAS,
        NETHER_CRIMSON_FOREST,
        NETHER_NETHER_WASTES,
        NETHER_SOUL_SAND_VALLEY,
        NETHER_WARPED_FOREST,
        OVERWORLD_DEEP_DARK,
        OVERWORLD_DRIPSTONE_CAVES,
        OVERWORLD_FROZEN_PEAKS,
        OVERWORLD_GROVE,
        OVERWORLD_JAGGED_PEAKS,
        OVERWORLD_JUNGLE_AND_FOREST,
        OVERWORLD_LUSH_CAVES,
        OVERWORLD_MEADOW,
        OVERWORLD_OLD_GROWTH_TAIGA,
        OVERWORLD_SNOWY_SLOPES,
        OVERWORLD_STONY_PEAKS,
        OVERWORLD_SWAMP,
        UNDER_WATER
    }

//    public enum MixtapeButtonLocation {
//        IN_MUSIC_BUTTON,
//        BESIDE_MUSIC_BUTTON
//    }

    @ConfigEntry.Category("main")
    @ConfigEntry.Gui.TransitiveObject
    public MainConfig mainConfig = new MainConfig();

    @ConfigEntry.Category("menu")
    @ConfigEntry.Gui.TransitiveObject
    public MenuConfig menuConfig = new MenuConfig();

    @ConfigEntry.Category("creative")
    @ConfigEntry.Gui.TransitiveObject
    public CreativeConfig creativeConfig = new CreativeConfig();

    @ConfigEntry.Category("game")
    @ConfigEntry.Gui.TransitiveObject
    public gameConfig gameConfig = new gameConfig();

    @ConfigEntry.Category("underwater")
    @ConfigEntry.Gui.TransitiveObject
    public UnderwaterConfig underwaterConfig = new UnderwaterConfig();

    @ConfigEntry.Category("end")
    @ConfigEntry.Gui.TransitiveObject
    public EndConfig endConfig = new EndConfig();

    @ConfigEntry.Category("nether")
    @ConfigEntry.Gui.TransitiveObject
    public NetherConfig netherConfig = new NetherConfig();

    @ConfigEntry.Category("credits")
    @ConfigEntry.Gui.TransitiveObject
    public CreditsConfig creditsConfig  = new CreditsConfig();

    @ConfigEntry.Category("jukebox")
    @ConfigEntry.Gui.TransitiveObject
    public JukeboxConfig jukeboxConfig  = new JukeboxConfig();

    @Config(name = "main")
    public static class MainConfig implements ConfigData {
        public boolean enabled = false;
        public boolean varyPitch = false;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 12)
        public int maxNoteChange = 3;
        @ConfigEntry.BoundedDiscrete(min = -12, max = 0)
        public int minNoteChange = -3;

//        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
//        public MixtapeButtonLocation mixtapeButtonLocation = MixtapeButtonLocation.IN_MUSIC_BUTTON;

        public MusicType musicType = MusicType.AUTOMATIC;
    }

    @Config(name = "menu")
    public static class MenuConfig implements ConfigData {
        public boolean enabled = true;
        public int minSongDelay = 20;
        public int maxSongDelay = 600;
        public float volume = 100;
    }

    @Config(name = "creative")
    public static class CreativeConfig implements ConfigData {
        public boolean enabled = true;
        public int minSongDelay = 12000;
        public int maxSongDelay = 24000;
        public float volume = 100;
    }

    @Config(name = "game")
    public static class gameConfig implements ConfigData {
        public boolean enabled = true;
        public int minSongDelay = 12000;
        public int maxSongDelay = 24000;
        public boolean creativeMusicPlaysInSurvival = false;
        public float volume = 100;
    }

    @Config(name = "underwater")
    public static class UnderwaterConfig implements ConfigData {
        public boolean enabled = true;
        public int minSongDelay = 12000;
        public int maxSongDelay = 24000;
        public float volume = 100;
    }

    @Config(name = "end")
    public static class EndConfig implements ConfigData {
        public boolean enabled = true;
        public int minSongDelay = 6000;
        public int maxSongDelay = 24000;
        public float volume = 100;
    }

    @Config(name = "nether")
    public static class NetherConfig implements ConfigData {
        public boolean enabled = true;
        public int minSongDelay = 12000;
        public int maxSongDelay = 24000;
        public float volume = 100;
    }

    @Config(name = "credits")
    public static class CreditsConfig implements ConfigData {
        public boolean enabled = true;
        public float volume = 100;
    }

    @Config(name = "jukebox")
    public static class JukeboxConfig implements ConfigData {
        public boolean enabled = true;
        public boolean mono = false;
        public boolean elevenReplaces11 = false;
        public boolean dogReplacesCat = false;
        public float volume = 400;
    }
}