package com.unjust1ce.mixtape.mixin;

import com.unjust1ce.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "getLeftText")
    protected void getLeftText(CallbackInfoReturnable<List<String>> info) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        info.getReturnValue().add("[Mixtape] Music: " + config.mainConfig.currentMusicType);
        info.getReturnValue().add("[Mixtape] Time until next song: " + config.mainConfig.timeUntilNextSong);
    }
}
