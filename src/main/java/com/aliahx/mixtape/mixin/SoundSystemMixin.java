package com.aliahx.mixtape.mixin;

import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundListener;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {
    @Shadow @Final private Map<SoundInstance, Channel.SourceManager> sources;

    @Shadow protected abstract float getAdjustedVolume(SoundInstance sound);

    @Shadow @Final private SoundListener listener;

    @Shadow private boolean started;

    @Inject(at = @At("HEAD"), method = "updateSoundVolume", cancellable = true)
    private void updateSoundVolume(SoundCategory category, float volume, CallbackInfo ci) {
        if (this.started) {
            if (category == SoundCategory.MASTER) {
                this.listener.setVolume(volume);
            } else {
                this.sources.forEach((source, sourceManager) -> {
                    float f = this.getAdjustedVolume(source);
                    if(source.getCategory() == category) {
                        f *= volume;
                    }
                    float finalF = f;
                    sourceManager.run((sourcex) -> {
                        if (finalF <= 0.0F) {
                            sourcex.stop();
                        } else {
                            sourcex.setVolume(finalF);
                        }

                    });
                });
            }
        }
        ci.cancel();
    }
}
