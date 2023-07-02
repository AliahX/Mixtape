package gay.aliahx.mixtape.mixin;

import gay.aliahx.mixtape.Mixtape;
import gay.aliahx.mixtape.MusicManager;
import gay.aliahx.mixtape.toast.MusicToast;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

import static gay.aliahx.mixtape.Mixtape.config;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Final private Map<BlockPos, SoundInstance> playingSongs;
    @Shadow @Final private MinecraftClient client;
    @Shadow protected abstract void updateEntitiesForSong(World world, BlockPos pos, boolean playing);
    @Shadow private @Nullable ClientWorld world;

    @Redirect(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;playSong(Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/util/math/BlockPos;)V"))
    private void playSongMixin(WorldRenderer instance, SoundEvent song, BlockPos songPosition) {
        if(!config.main.enabled || config.jukebox.enabled) {
            SoundInstance soundInstance = this.playingSongs.get(songPosition);
            if (soundInstance != null) {
                this.client.getSoundManager().stop(soundInstance);
                this.playingSongs.remove(songPosition);
            }

            if (song != null) {
                MusicDiscItem musicDiscItem = MusicDiscItem.bySound(song);
                if (musicDiscItem != null) {
                    if(!config.musicToast.enabled || !config.musicToast.hideJukeboxHotbarMessage) {
                        this.client.inGameHud.setRecordPlayingOverlay(musicDiscItem.getDescription());
                    }
                }

                SoundInstance soundInstance2 = PositionedSoundInstance.record(song, Vec3d.ofCenter(songPosition));
                this.playingSongs.put(songPosition, soundInstance2);
                this.client.getSoundManager().play(soundInstance2);
            }
            this.updateEntitiesForSong(this.world, songPosition, song != null);
        }

        if(song != null) {
            String songName = song.getId().toString().split("\\.")[1];
            if(Objects.equals(songName, "cat") && config.jukebox.dogReplacesCat) {
                songName = "dog";
            } else if (Objects.equals(songName, "11") && config.jukebox.elevenReplaces11) {
                songName = "eleven";
            } else if (Objects.equals(songName, "ward") && config.jukebox.droopyLikesYourFaceReplacesWard) {
                songName = "droopy_likes_your_face";
            }
            MusicManager.Entry entry = Mixtape.musicManager.getEntry(songName);
            MusicToast.show(MinecraftClient.getInstance().getToastManager(), entry, new ItemStack(MusicDiscItem.bySound(song)));
        }

    }

    @Inject(method = "updateEntitiesForSong", at = @At("HEAD"))
    private void updateEntitiesForSongMixin(World world, BlockPos pos, boolean playing, CallbackInfo ci) {
        if(client.player != null) {
            if (playing) {
                Mixtape.jukeboxesPlaying.put(pos, true);
                Mixtape.lastJukeboxes.put(pos, true);
                Mixtape.lastLastJukeboxes.put(pos, true);
            } else {
                Mixtape.jukeboxesPlaying.remove(pos);
                Mixtape.lastJukeboxes.remove(pos);
                Mixtape.lastLastJukeboxes.remove(pos);
            }
        }
    }
}
