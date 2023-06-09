package com.aliahx.mixtape.mixin;

import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static com.aliahx.mixtape.Mixtape.config;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {
    @Shadow @Final private Map<SoundInstance, Channel.SourceManager> sources;

    @Shadow protected abstract float getAdjustedVolume(SoundInstance sound);

    @Shadow private boolean started;

    @Inject(at = @At("HEAD"), method = "updateSoundVolume", cancellable = true)
    private void updateSoundVolume(SoundCategory category, float volume, CallbackInfo ci) {
        if (this.started && category == SoundCategory.MUSIC) {
                this.sources.forEach((source, sourceManager) -> {
                    if(source.getCategory() == SoundCategory.MUSIC) {
                        float f = this.getAdjustedVolume(source) * volume;
                        sourceManager.run((sourcex) -> {
                            sourcex.setVolume(f);
                        });
                    }
                });
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "stopSounds", cancellable = true)
    public void stopSoundsMixin(CallbackInfo ci) {
        if(config.mainConfig.enabled && config.mainConfig.stopMusicWhenLeftGame) {
            ci.cancel();
        }
    }
}