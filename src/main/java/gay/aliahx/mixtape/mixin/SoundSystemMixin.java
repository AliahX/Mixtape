package gay.aliahx.mixtape.mixin;

import gay.aliahx.mixtape.Mixtape;
import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static gay.aliahx.mixtape.Mixtape.config;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {
    @Shadow @Final private Map<SoundInstance, Channel.SourceManager> sources;

    @Shadow protected abstract float getAdjustedVolume(SoundInstance sound);
    @Shadow private boolean started;
    @Shadow @Final private SoundListener listener;

    @Redirect(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;getX()D"))
    private double getXMixin(SoundInstance instance) {
        if(!config.main.enabled) return instance.getX();
        return instance.getCategory() == SoundCategory.RECORDS && config.jukebox.mono ? 0 : instance.getX();
    }

    @Redirect(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;getY()D"))
    private double getYMixin(SoundInstance instance) {
        if(!config.main.enabled) return instance.getY();
        return instance.getCategory() == SoundCategory.RECORDS && config.jukebox.mono ? 0 : instance.getY();
    }

    @Redirect(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;getZ()D"))
    private double getZMixin(SoundInstance instance) {
        if(!config.main.enabled) return instance.getZ();
        return instance.getCategory() == SoundCategory.RECORDS && config.jukebox.mono ? 0 : instance.getZ();
    }

    @Inject(at = @At("HEAD"), method = "updateSoundVolume", cancellable = true)
    private void updateSoundVolume(SoundCategory category, float volume, CallbackInfo ci) {
        if(config.main.enabled) {
            this.sources.forEach((source, sourceManager) -> {
                if (source.getCategory() == SoundCategory.RECORDS) {
                    double distanceToPlayer = Math.sqrt(listener.getPos().squaredDistanceTo(source.getX(), source.getY(), source.getZ()));
                    sourceManager.run((sourcex) -> {
                        float newVolume = (float) (config.jukebox.distance - distanceToPlayer) / config.jukebox.distance;
                        sourcex.setVolume(newVolume < 0 ? 0 : newVolume);
                    });
                }
            });

            if (this.started && category == SoundCategory.MUSIC) {
                this.sources.forEach((source, sourceManager) -> {
                    if (source.getCategory() == SoundCategory.MUSIC) {
                        float f = this.getAdjustedVolume(source) * volume;
                        sourceManager.run((sourcex) -> {
                            sourcex.setVolume(f);
                        });
                    }
                });
                ci.cancel();
            }
        }
    }
}