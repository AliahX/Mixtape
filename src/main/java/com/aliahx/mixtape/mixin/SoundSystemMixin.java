package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.sound.*;
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
                    float f = this.getAdjustedVolume(source) * (source.getCategory() == category ? volume : 1.0F);
                    sourceManager.run((sourcex) -> {
                        if (f <= 0.0F) {
                            sourcex.stop();
                        } else {
                            sourcex.setVolume(f);
                        }
                    });
                });
            }
        }
        ci.cancel();
    }


    @Inject(at = @At("HEAD"), method = "stopAll", cancellable = true)
    public void stopAllMixin(CallbackInfo ci) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if (config.mainConfig.enabled && !(config.mainConfig.stopMusicWhenLeftGame || config.mainConfig.stopMusicWhenSwitchingDimensions)) {
            ci.cancel();
        }
    }
}