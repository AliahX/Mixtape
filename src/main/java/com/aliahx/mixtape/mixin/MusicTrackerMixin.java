package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.Mixtape;
import com.aliahx.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.aliahx.mixtape.Mixtape.paused;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {

    @Shadow private int timeUntilNextSong;

    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;getMinDelay()I"))
    private int getMinDelayMixin(MusicSound musicSound) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.mainConfig.enabled) {
            return musicSound.getMinDelay();
        }
        if(paused) {
            return Integer.MAX_VALUE;
        }
        return switch (musicSound.getSound().getId().toString()) {
            case "minecraft:music.menu" -> config.menuConfig.minSongDelay;
            case "minecraft:music.creative" -> config.creativeConfig.minSongDelay;
            case "minecraft:music.end" -> config.endConfig.minSongDelay;
            case "minecraft:music.under_water" -> config.underwaterConfig.minSongDelay;
            case "minecraft:music.game", "minecraft:music.overworld.deep_dark", "minecraft:music.overworld.dripstone_caves", "minecraft:music.overworld.grove", "minecraft:music.overworld.jagged_peaks", "minecraft:music.overworld.lush_caves", "minecraft:music.overworld.swamp", "minecraft:music.overworld.jungle_and_forest", "minecraft:music.overworld.old_growth_taiga", "minecraft:music.overworld.meadow", "minecraft:music.overworld.frozen_peaks", "minecraft:music.overworld.snowy_slopes", "minecraft:music.overworld.stony_peaks" -> config.gameConfig.minSongDelay;
            case "minecraft:music.nether.nether_wastes", "minecraft:music.nether.warped_forest", "minecraft:music.nether.soul_sand_valley", "minecraft:music.nether.crimson_forest", "minecraft:music.nether.basalt_deltas" -> config.netherConfig.minSongDelay;
            default -> musicSound.getMinDelay();
        };
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void tick(CallbackInfo info) {
        Mixtape.debugTimeUntilNextSong = this.timeUntilNextSong;
        Mixtape.debugMaxTimeUntilNextSong = this.getMaxDelayMixin(this.client.getMusicType());
        Mixtape.debugNextMusicType = this.client.getMusicType().getSound().getId().toString();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;getMaxDelay()I"))
    private int getMaxDelayMixin(MusicSound musicSound) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.mainConfig.enabled) {
            return musicSound.getMaxDelay();
        }
        if(paused) {
            return Integer.MAX_VALUE;
        }

        return switch (musicSound.getSound().getId().toString()) {
            case "minecraft:music.menu" -> config.menuConfig.maxSongDelay;
            case "minecraft:music.creative" -> config.creativeConfig.maxSongDelay;
            case "minecraft:music.end" -> config.endConfig.maxSongDelay;
            case "minecraft:music.under_water" -> config.underwaterConfig.maxSongDelay;
            case "minecraft:music.game", "minecraft:music.overworld.deep_dark", "minecraft:music.overworld.dripstone_caves", "minecraft:music.overworld.grove", "minecraft:music.overworld.jagged_peaks", "minecraft:music.overworld.lush_caves", "minecraft:music.overworld.swamp", "minecraft:music.overworld.jungle_and_forest", "minecraft:music.overworld.old_growth_taiga", "minecraft:music.overworld.meadow", "minecraft:music.overworld.frozen_peaks", "minecraft:music.overworld.snowy_slopes", "minecraft:music.overworld.stony_peaks" -> config.gameConfig.maxSongDelay;
            case "minecraft:music.nether.nether_wastes", "minecraft:music.nether.warped_forest", "minecraft:music.nether.soul_sand_valley", "minecraft:music.nether.crimson_forest", "minecraft:music.nether.basalt_deltas" -> config.netherConfig.maxSongDelay;
            default -> musicSound.getMaxDelay();
        };
    }
}
