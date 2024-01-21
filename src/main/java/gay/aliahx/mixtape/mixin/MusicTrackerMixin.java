package gay.aliahx.mixtape.mixin;

import gay.aliahx.mixtape.Mixtape;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static gay.aliahx.mixtape.Mixtape.config;
import static gay.aliahx.mixtape.Mixtape.paused;

@Mixin(MusicTracker.class)
public abstract class MusicTrackerMixin {

    @Shadow private int timeUntilNextSong;
    @Shadow @Final private MinecraftClient client;
    @Shadow @Nullable private SoundInstance current;

    @Inject(at = @At("RETURN"), method = "tick")
    private void tickMixin(CallbackInfo info) {
        Mixtape.debugTimeUntilNextSong = timeUntilNextSong;
        Mixtape.debugMaxTimeUntilNextSong = getMaxDelayMixin(client.getMusicType());
        Mixtape.debugNextMusicType = client.getMusicType().getSound().value().getId().toString();
        if(current == null) {
            if(!Mixtape.currentSong.equals("")) {
                Mixtape.currentSong = "";
                //song has finished
            }

            if(Mixtape.startSong) {
                Mixtape.startSong = false;
                timeUntilNextSong = 13;
            }
        }

        float defaultVolume = client.options.getSoundVolume(SoundCategory.MUSIC);
        if(config.main.enabled) {
            client.getSoundManager().updateSoundVolume(SoundCategory.MUSIC, config.jukebox.turnDownMusic ? Mixtape.volumeScale * defaultVolume : defaultVolume);
        }

        Mixtape.discPlaying = false;
        Mixtape.jukeboxes.forEach((blockPos, isPlaying) -> {
            if(isPlaying && (this.client.world != null && this.client.world.getBlockEntity(blockPos) != null && this.client.world.getBlockEntity(blockPos).getType() == BlockEntityType.JUKEBOX && this.client.world.isChunkLoaded(blockPos))) {
                if(this.client.player != null) {
                    if (Math.sqrt(this.client.player.squaredDistanceTo(blockPos.toCenterPos())) < 64) {
                        Mixtape.discPlaying = true;
                    }
                }
            }
        });

        boolean scaleMusic = Mixtape.discPlaying;
        if(scaleMusic && Mixtape.volumeScale > 0.1f) {
            Mixtape.volumeScale -= 0.1f;
        } else if (scaleMusic && Mixtape.volumeScale <= 0.1f) {
            Mixtape.volumeScale = 0.001f;
        } else if (!scaleMusic && Mixtape.volumeScale < 1.0f) {
            Mixtape.volumeScale += 0.1f;
        } else {
            Mixtape.volumeScale = 1.0f;
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;getMinDelay()I"))
    private int getMinDelayMixin(MusicSound musicSound) {
        return !config.main.enabled ? musicSound.getMinDelay() : paused ? Integer.MAX_VALUE : config.main.noDelayBetweenSongs ? 13 : switch (musicSound.getSound().value().getId().toString()) {
            case "minecraft:music.menu" -> config.menu.minSongDelay;
            case "minecraft:music.creative" -> config.creative.minSongDelay;
            case "minecraft:music.end" -> config.end.minSongDelay;
            case "minecraft:music.under_water" -> config.underwater.minSongDelay;
            case "minecraft:music.game" -> config.game.minSongDelay;
            default -> {
                if(musicSound.getSound().value().getId().toString().contains("overworld")) {
                    yield config.game.minSongDelay;
                } else if (musicSound.getSound().value().getId().toString().contains("nether")) {
                    yield config.nether.minSongDelay;
                }
                yield musicSound.getMinDelay();
            }
        };
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;getMaxDelay()I"))
    private int getMaxDelayMixin(MusicSound musicSound) {
        return !config.main.enabled ? musicSound.getMaxDelay() : paused ? Integer.MAX_VALUE : config.main.noDelayBetweenSongs ? 13 : switch (musicSound.getSound().value().getId().toString()) {
            case "minecraft:music.menu" -> config.menu.maxSongDelay;
            case "minecraft:music.creative" -> config.creative.maxSongDelay;
            case "minecraft:music.end" -> config.end.maxSongDelay;
            case "minecraft:music.under_water" -> config.underwater.maxSongDelay;
            case "minecraft:music.game" -> config.game.maxSongDelay;
            default -> {
                if(musicSound.getSound().value().getId().toString().contains("overworld")) {
                    yield config.game.maxSongDelay;
                } else if (musicSound.getSound().value().getId().toString().contains("nether")) {
                    yield config.nether.maxSongDelay;
                }
                yield musicSound.getMinDelay();
            }
        };
    }

    @Inject(method = "stop*", at = @At("HEAD"), cancellable = true)
    public void stopMixin(CallbackInfo ci) {
        if(config.main.enabled && !config.main.stopMusicWhenSwitchingDimensions) {
            ci.cancel();
        }
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/sound/MusicSound;shouldReplaceCurrentMusic()Z"))
    private boolean shouldReplaceCurrentMusicMixin(MusicSound instance) {
        if(config.main.enabled && !config.main.stopMusicWhenLeftGame && !Objects.equals(instance.getSound().value().getId().toString(), "minecraft:music.dragon")) {
            return false;
        }
        return instance.shouldReplaceCurrentMusic();
    }
}
