package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(method = "getMusicType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/MusicTracker;isPlayingType(Lnet/minecraft/sound/MusicSound;)Z"))
    private boolean isPlayingTypeMixin(MusicTracker instance, MusicSound type) {
        return false;
    }

    @Redirect(method = "openPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;pauseAll()V"))
    private void pauseAllMixin(SoundManager instance) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!(config.mainConfig.enabled && !config.mainConfig.pauseMusicWhenGamePaused)) {
            instance.pauseAll();
        }
    }
}