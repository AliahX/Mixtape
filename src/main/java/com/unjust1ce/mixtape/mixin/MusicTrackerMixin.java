package com.unjust1ce.mixtape.mixin;

import com.unjust1ce.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;getMinDelay()I"))
    private int getMinDelayMixin(MusicSound musicSound) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.mainConfig.enabled) {
            return musicSound.getMinDelay();
        }
        if(config.mainConfig.paused) {
            return Integer.MAX_VALUE;
        }
        return switch (musicSound.getSound().getId().toString()) {
            case "minecraft:music.menu" -> config.menuConfig.minSongDelay;
            case "minecraft:music.creative" -> config.creativeConfig.minSongDelay;
            case "minecraft:music.end" -> config.endConfig.minSongDelay;
            case "minecraft:music.underwater" -> config.underwaterConfig.minSongDelay;
            case "minecraft:music.game", "music.overworld.deep_dark", "music.overworld.dripstone_caves", "music.overworld.grove", "music.overworld.jagged_peaks", "music.overworld.lush_caves", "music.overworld.swamp", "music.overworld.jungle_and_forest", "music.overworld.old_growth_taiga", "music.overworld.meadow", "music.overworld.frozen_peaks", "music.overworld.snowy_slopes", "music.overworld.stony_peaks" -> config.gameConfig.minSongDelay;
            case "minecraft:music.nether.nether_wastes", "minecraft:music.nether.warped_forest", "minecraft:music.nether.soul_sand_valley", "minecraft:music.nether.crimson_forest", "minecraft:music.nether.basalt_deltas" -> config.netherConfig.minSongDelay;
            default -> musicSound.getMinDelay();
        };
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;getMaxDelay()I"))
    private int getMaxDelayMixin(MusicSound musicSound) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.mainConfig.enabled) {
            return musicSound.getMaxDelay();
        }
        if(config.mainConfig.paused) {
            return Integer.MAX_VALUE;
        }

        return switch (musicSound.getSound().getId().toString()) {
            case "minecraft:music.menu" -> config.menuConfig.maxSongDelay;
            case "minecraft:music.creative" -> config.creativeConfig.maxSongDelay;
            case "minecraft:music.end" -> config.endConfig.maxSongDelay;
            case "minecraft:music.underwater" -> config.underwaterConfig.maxSongDelay;
            case "minecraft:music.game" -> config.gameConfig.maxSongDelay;
            case "minecraft:music.nether.nether_wastes", "minecraft:music.nether.warped_forest", "minecraft:music.nether.soul_sand_valley", "minecraft:music.nether.crimson_forest", "minecraft:music.nether.basalt_deltas" -> config.netherConfig.maxSongDelay;
            default -> musicSound.getMaxDelay();
        };
    }
}
