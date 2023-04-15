package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.Mixtape;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Inject(at = @At("RETURN"), method = "getLeftText")
    protected void getLeftText(CallbackInfoReturnable<List<String>> info) {
        info.getReturnValue().add("[Mixtape] Music: " + Mixtape.debugCurrentMusicType);
        info.getReturnValue().add("[Mixtape] Paused: " + Mixtape.paused);
        info.getReturnValue().add("[Mixtape] Next music type: " + Mixtape.debugNextMusicType);
        info.getReturnValue().add("[Mixtape] Time until next song: " + Mixtape.debugTimeUntilNextSong + "/" + Mixtape.debugMaxTimeUntilNextSong);
        info.getReturnValue().add("[Mixtape] Is disc playing: " + Mixtape.discPlaying);
        info.getReturnValue().add("[Mixtape] Music volume scale: " + Mixtape.volumeScale);
    }
}
