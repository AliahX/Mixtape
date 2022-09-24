package com.unjust1ce.mixtape.mixin;

import com.unjust1ce.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Final private Map<BlockPos, SoundInstance> playingSongs;

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void updateEntitiesForSong(World world, BlockPos pos, boolean playing);

    @Shadow private @Nullable ClientWorld world;

    @Redirect(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;playSong(Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/util/math/BlockPos;)V"))
    private void playSongMixin(WorldRenderer instance, SoundEvent song, BlockPos songPosition) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.mainConfig.enabled || (config.jukeboxConfig.enabled)) {
            SoundInstance soundInstance = (SoundInstance) this.playingSongs.get(songPosition);
            if (soundInstance != null) {
                this.client.getSoundManager().stop(soundInstance);
                this.playingSongs.remove(songPosition);
            }

            if (song != null) {
                MusicDiscItem musicDiscItem = MusicDiscItem.bySound(song);
                if (musicDiscItem != null) {
                    this.client.inGameHud.setRecordPlayingOverlay(musicDiscItem.getDescription());
                }

                SoundInstance soundInstance2 = PositionedSoundInstance.record(song, (double) songPosition.getX(), (double) songPosition.getY(), (double) songPosition.getZ());
                this.playingSongs.put(songPosition, soundInstance2);
                this.client.getSoundManager().play(soundInstance2);
            }
            this.updateEntitiesForSong(this.world, songPosition, song != null);
        }

    }
}
