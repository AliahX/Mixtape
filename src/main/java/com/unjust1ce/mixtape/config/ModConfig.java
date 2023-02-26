package com.unjust1ce.mixtape.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@SuppressWarnings("unused")
@Config(name = "mixtape")
@Config.Gui.CategoryBackground(category = "jukebox", background = "minecraft:textures/block/jukebox_side.png")
@Config.Gui.CategoryBackground(category = "credits", background = "minecraft:textures/block/bedrock.png")
//@Config.Gui.CategoryBackground(category = "soulSandValley", background = "minecraft:textures/block/soul_soil.png")
//@Config.Gui.CategoryBackground(category = "basaltDelta", background = "minecraft:textures/block/basalt_side.png")
//@Config.Gui.CategoryBackground(category = "crimsonForest", background = "minecraft:textures/block/crimson_nylium.png")
//@Config.Gui.CategoryBackground(category = "netherWastes", background = "minecraft:textures/block/netherrack.png")
@Config.Gui.CategoryBackground(category = "nether", background = "minecraft:textures/block/netherrack.png")
@Config.Gui.CategoryBackground(category = "end", background = "minecraft:textures/block/end_stone.png")
@Config.Gui.CategoryBackground(category = "underwater", background = "minecraft:textures/block/sand.png")
//@Config.Gui.CategoryBackground(category = "grove", background = "minecraft:textures/block/snow.png")
//@Config.Gui.CategoryBackground(category = "meadow", background = "mixtape:textures/block/meadow_grass.png")
//@Config.Gui.CategoryBackground(category = "dripstoneCaves", background = "minecraft:textures/block/dripstone_block.png")
//@Config.Gui.CategoryBackground(category = "lushCaves", background = "minecraft:textures/block/moss_block.png")
//@Config.Gui.CategoryBackground(category = "stonyPeaks", background = "minecraft:textures/block/stone.png")
//@Config.Gui.CategoryBackground(category = "snowySlopes", background = "minecraft:textures/block/powder_snow.png")
//@Config.Gui.CategoryBackground(category = "frozenPeaks", background = "minecraft:textures/block/packed_ice.png")
//@Config.Gui.CategoryBackground(category = "jaggedPeaks", background = "minecraft:textures/block/stone.png")

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

    public enum MixtapeButtonLocation {
        IN_MUSIC_BUTTON,
        BESIDE_MUSIC_BUTTON
    }

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

//    @ConfigEntry.Category("netherWastes")
//    @ConfigEntry.Gui.TransitiveObject
//    public NetherWastesConfig netherWastesConfig = new NetherWastesConfig();

//    @ConfigEntry.Category("crimsonForest")
//    @ConfigEntry.Gui.TransitiveObject
//    public CrimsonForestConfig crimsonForestConfig  = new CrimsonForestConfig();

//    @ConfigEntry.Category("basaltDelta")
//    @ConfigEntry.Gui.TransitiveObject
//    public BasaltDeltaConfig basaltDeltaConfig  = new BasaltDeltaConfig();

//    @ConfigEntry.Category("soulSandValley")
//    @ConfigEntry.Gui.TransitiveObject
//    public SoulSandValleyConfig soulsandValleyConfig  = new SoulSandValleyConfig();

//    @ConfigEntry.Category("grove")
//    @ConfigEntry.Gui.TransitiveObject
//    public GroveConfig groveConfig  = new GroveConfig();

//    @ConfigEntry.Category("dripstoneCaves")
//    @ConfigEntry.Gui.TransitiveObject
//    public DripstoneCavesConfig dripstoneCavesConfig  = new DripstoneCavesConfig();

//    @ConfigEntry.Category("jaggedPeaks")
//    @ConfigEntry.Gui.TransitiveObject
//    public JaggedPeaksConfig jaggedPeaksConfig  = new JaggedPeaksConfig();

//    @ConfigEntry.Category("lushCaves")
//    @ConfigEntry.Gui.TransitiveObject
//    public LushCavesConfig lushCavesConfig  = new LushCavesConfig();

//    @ConfigEntry.Category("meadow")
//    @ConfigEntry.Gui.TransitiveObject
//    public MeadowConfig meadowConfig  = new MeadowConfig();

//    @ConfigEntry.Category("frozenPeaks")
//    @ConfigEntry.Gui.TransitiveObject
//    public FrozenPeaksConfig frozenPeaksConfig  = new FrozenPeaksConfig();

//    @ConfigEntry.Category("snowySlopes")
//    @ConfigEntry.Gui.TransitiveObject
//    public SnowySlopesConfig snowySlopesConfig  = new SnowySlopesConfig();

//    @ConfigEntry.Category("stonyPeaks")
//    @ConfigEntry.Gui.TransitiveObject
//    public StonyPeaksConfig stonyPeaksConfig  = new StonyPeaksConfig();

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
        @ConfigEntry.BoundedDiscrete(min = -12, max = 0)
        public int minNoteChange = -3;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 12)
        public int maxNoteChange = 3;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public MixtapeButtonLocation mixtapeButtonLocation = MixtapeButtonLocation.IN_MUSIC_BUTTON;

        public MusicType musicType = MusicType.AUTOMATIC;
    }

    @Config(name = "menu")
    public static class MenuConfig implements ConfigData {
        public boolean enabled = true;
        public int minSongDelay = 20;
        public int maxSongDelay = 600;
        public float volume = 100;

//        @ConfigEntry.Gui.PrefixText //menu
//        public boolean Mutation;
//        public boolean MoogCity2;
//        public boolean Beginning2;
//        public boolean FloatingTrees;
//
//        @ConfigEntry.Gui.PrefixText //credits
//        public boolean Alpha;
//
//        @ConfigEntry.Gui.PrefixText //end music
//        public boolean TheEnd;
//
//        @ConfigEntry.Gui.PrefixText //dragon music
//        public boolean Boss;
//
//        @ConfigEntry.Gui.PrefixText //nether
//        public boolean ConcreteHalls;
//        public boolean DeadVoxel;
//        public boolean Warmth;
//        public boolean BalladOfTheCats;
//
//        @ConfigEntry.Gui.PrefixText //crimson forest
//        public boolean Chrysopoeia;
//
//        @ConfigEntry.Gui.PrefixText //nether wastes
//        public boolean Rubedo;
//
//        @ConfigEntry.Gui.PrefixText //basalt delta/soul sand valley
//        public boolean SoBelow;
//
//        @ConfigEntry.Gui.PrefixText //underwater
//        public boolean Axolotl;
//        public boolean DragonFish;
//        public boolean Shuniji;
//
//        public boolean Minecraft;
//        public boolean Clark;
//        public boolean Sweden;
//        public boolean BiomeFest;
//        public boolean BlindSpots;
//        public boolean HauntMuskie;
//        public boolean AriaMath;
//        public boolean Dreiton;
//        public boolean Taswell;
//        public boolean SubwooferLullaby;
//        public boolean LivingMice;
//        public boolean Haggstrom;
//        public boolean Danny;
//        public boolean Key;
//        public boolean Oxygene;
//        public boolean DryHands;
//        public boolean WetHands;
//        public boolean MiceOnVenus;
//
//        public boolean _13;
//        public boolean Cat;
//        public boolean Blocks;
//        public boolean Chirp;
//        public boolean Far;
//        public boolean Mall;
//        public boolean Mellohi;
//        public boolean Stal;
//        public boolean Strad;
//        public boolean Ward;
//        public boolean _11;
//        public boolean Wait;
//        public boolean Pigstep;
//        public boolean Dog;
//        public boolean Eleven;
//        public boolean Door;
//        public boolean Death;
//        public boolean MoogCity;
//        public boolean Equinoxe;
//        public boolean Chris;
//        public boolean Excuse;
//        public boolean Beginning;
//        public boolean DroopyLikesRicochet;
//        public boolean DroopyLikesYourFace;
//        public boolean Ki;
//        public boolean Flake;
//        public boolean Kyoto;
//        public boolean Intro;
//
//        @ConfigEntry.Gui.PrefixText //148
//        public boolean Beta;
//        public boolean Vierton;
//        public boolean AriaEconomy;
//        public boolean BiomeParty;
//
//        @ConfigEntry.Gui.PrefixText //0x10c
//        public boolean _0;
//        public boolean _0x10c;
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

//    @Config(name = "netherWastes")
//    public static class NetherWastesConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "crimsonForest")
//    public static class CrimsonForestConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "basaltDelta")
//    public static class BasaltDeltaConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "soulSandValley")
//    public static class SoulSandValleyConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "dripstoneCaves")
//    public static class DripstoneCavesConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "grove")
//    public static class GroveConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "jaggedPeaks")
//    public static class JaggedPeaksConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "lushCaves")
//    public static class LushCavesConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "meadow")
//    public static class MeadowConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "frozenPeaks")
//    public static class FrozenPeaksConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "snowySlopes")
//    public static class SnowySlopesConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }
//
//    @Config(name = "stonyPeaks")
//    public static class StonyPeaksConfig implements ConfigData {
//        public boolean enabled = true;
//        public float volume = 100;
//    }

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