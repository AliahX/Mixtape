package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.Mixtape;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.aliahx.mixtape.Mixtape.config;
import static com.aliahx.mixtape.Mixtape.paused;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin {

    @Shadow private int timeUntilNextSong;

    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "tick")
    private void tickMixin(CallbackInfo info) {
        Mixtape.debugTimeUntilNextSong = timeUntilNextSong;
        Mixtape.debugMaxTimeUntilNextSong = getMaxDelayMixin(client.getMusicType());
        Mixtape.debugNextMusicType = client.getMusicType().getSound().value().getId().toString();

        float defaultVolume = client.options.getSoundVolume(SoundCategory.MUSIC);
        client.getSoundManager().updateSoundVolume(SoundCategory.MUSIC, config.jukeboxConfig.turnDownMusic ? Mixtape.volumeScale * defaultVolume : defaultVolume);

        Mixtape.discPlaying = false;
        Mixtape.jukeboxesPlaying.forEach((blockPos, isPlaying) -> {
            if(isPlaying && (this.client.world != null && this.client.world.getBlockEntity(blockPos) != null && this.client.world.getBlockEntity(blockPos).getType() == BlockEntityType.JUKEBOX && this.client.world.isChunkLoaded(blockPos)) || config.jukeboxConfig.mono) {
                if(this.client.player != null) {
                    if (Math.sqrt(this.client.player.squaredDistanceTo(blockPos.toCenterPos())) < 64 || config.jukeboxConfig.mono) {
                        Mixtape.discPlaying = true;
                    }
                }
            }
        });

        if(Mixtape.discPlaying && Mixtape.volumeScale > 0.1f) {
            Mixtape.volumeScale -= 0.1f;
        } else if (Mixtape.discPlaying && Mixtape.volumeScale <= 0.1f) {
            Mixtape.volumeScale = 0.001f;
        } else if (!Mixtape.discPlaying && Mixtape.volumeScale < 1.0f) {
            Mixtape.volumeScale += 0.1f;
        } else {
            Mixtape.volumeScale = 1.0f;
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;getMinDelay()I"))
    private int getMinDelayMixin(MusicSound musicSound) {
        return !config.mainConfig.enabled ? musicSound.getMinDelay() : paused ? Integer.MAX_VALUE : config.mainConfig.noDelayBetweenSongs ? 0 : switch (musicSound.getSound().value().getId().toString()) {
            case "minecraft:music.menu" -> config.menuConfig.minSongDelay;
            case "minecraft:music.creative" -> config.creativeConfig.minSongDelay;
            case "minecraft:music.end" -> config.endConfig.minSongDelay;
            case "minecraft:music.under_water" -> config.underwaterConfig.minSongDelay;
            case "minecraft:music.game", "minecraft:music.overworld.deep_dark", "minecraft:music.overworld.dripstone_caves", "minecraft:music.overworld.grove", "minecraft:music.overworld.jagged_peaks", "minecraft:music.overworld.lush_caves", "minecraft:music.overworld.swamp", "minecraft:music.overworld.jungle_and_forest", "minecraft:music.overworld.old_growth_taiga", "minecraft:music.overworld.meadow", "minecraft:music.overworld.frozen_peaks", "minecraft:music.overworld.snowy_slopes", "minecraft:music.overworld.stony_peaks" -> config.gameConfig.minSongDelay;
            case "minecraft:music.nether.nether_wastes", "minecraft:music.nether.warped_forest", "minecraft:music.nether.soul_sand_valley", "minecraft:music.nether.crimson_forest", "minecraft:music.nether.basalt_deltas" -> config.netherConfig.minSongDelay;
            default -> musicSound.getMinDelay();
        };
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;getMaxDelay()I"))
    private int getMaxDelayMixin(MusicSound musicSound) {
        return !config.mainConfig.enabled ? musicSound.getMaxDelay() : paused ? Integer.MAX_VALUE : config.mainConfig.noDelayBetweenSongs ? 0 : switch (musicSound.getSound().value().getId().toString()) {
            case "minecraft:music.menu" -> config.menuConfig.maxSongDelay;
            case "minecraft:music.creative" -> config.creativeConfig.maxSongDelay;
            case "minecraft:music.end" -> config.endConfig.maxSongDelay;
            case "minecraft:music.under_water" -> config.underwaterConfig.maxSongDelay;
            case "minecraft:music.game", "minecraft:music.overworld.deep_dark", "minecraft:music.overworld.dripstone_caves", "minecraft:music.overworld.grove", "minecraft:music.overworld.jagged_peaks", "minecraft:music.overworld.lush_caves", "minecraft:music.overworld.swamp", "minecraft:music.overworld.jungle_and_forest", "minecraft:music.overworld.old_growth_taiga", "minecraft:music.overworld.meadow", "minecraft:music.overworld.frozen_peaks", "minecraft:music.overworld.snowy_slopes", "minecraft:music.overworld.stony_peaks" -> config.gameConfig.maxSongDelay;
            case "minecraft:music.nether.nether_wastes", "minecraft:music.nether.warped_forest", "minecraft:music.nether.soul_sand_valley", "minecraft:music.nether.crimson_forest", "minecraft:music.nether.basalt_deltas" -> config.netherConfig.maxSongDelay;
            default -> musicSound.getMaxDelay();
        };
    }

    @Inject(method = "stop", at = @At("HEAD"), cancellable = true)
    public void stopMixin(CallbackInfo ci) {
        if(config.mainConfig.enabled && !config.mainConfig.stopMusicWhenSwitchingDimensions) {
            ci.cancel();
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;shouldReplaceCurrentMusic()Z"))
    private boolean shouldReplaceCurrentMusicMixin(MusicSound instance) {
        if(config.mainConfig.enabled && !config.mainConfig.stopMusicWhenLeftGame && !Objects.equals(instance.getSound().value().getId().toString(), "minecraft:music.dragon")) {
            return false;
        }
        return instance.shouldReplaceCurrentMusic();
    }
}
