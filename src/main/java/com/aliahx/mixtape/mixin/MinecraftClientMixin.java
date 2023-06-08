package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.Mixtape;
import com.aliahx.mixtape.MusicManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.aliahx.mixtape.Mixtape.config;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Redirect(method = "getMusicType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/MusicTracker;isPlayingType(Lnet/minecraft/sound/MusicSound;)Z"))
    private boolean isPlayingTypeMixin(MusicTracker instance, MusicSound type) {
        return false;
    }

    @Redirect(method = "openPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;pauseAll()V"))
    private void pauseAllMixin(SoundManager instance) {
        if(!(config.mainConfig.enabled && !config.mainConfig.pauseMusicWhenGamePaused)) {
            instance.pauseAll();
        }
    }

    @Redirect(method = "reset", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;stopAll()V"))
    private void stopAllMixin(SoundManager instance) {
        if (!(config.mainConfig.enabled && !config.mainConfig.stopMusicWhenLeftGame)) {
            instance.stopAll();
        }
    }

    @Inject(method = "<init>", at = @At(value="TAIL", target = "Lnet/minecraft/client/MinecraftClient;<init>(Lnet/minecraft/client/RunArgs;)V"))
    private void onInit(CallbackInfo info) {
        Mixtape.resourceManager = Mixtape.client.getResourceManager();
        Mixtape.musicManager = new MusicManager(Mixtape.resourceLoader(Mixtape.resourceManager));
        Mixtape.soundManager = Mixtape.client.getSoundManager();
        Mixtape.soundManager.registerListener(Mixtape.SoundListener);
    }
}