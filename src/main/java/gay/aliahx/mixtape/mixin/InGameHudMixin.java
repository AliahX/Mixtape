package gay.aliahx.mixtape.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static gay.aliahx.mixtape.Mixtape.config;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow public abstract void setCanShowChatDisabledScreen(boolean canShowChatDisabledScreen);

    @Shadow private @Nullable Text overlayMessage;
    @Shadow private int overlayRemaining;
    @Shadow private boolean overlayTinted;
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "setRecordPlayingOverlay", at = @At("HEAD"), cancellable = true)
    public void setRecordPlayingOverlayMixin(Text description, CallbackInfo ci) {
        if(config.main.enabled && description.getString().contains("m:")) {
            Text text = Text.translatable("record.nowPlaying", Text.of(description.getString().replace("m:", "")));
            this.setCanShowChatDisabledScreen(false);
            this.overlayMessage = text;
            this.overlayRemaining = config.musicToast.remainForFullSong ? Integer.MAX_VALUE : config.musicToast.toastDisplayTime / 40;
            this.overlayTinted = true;
            this.client.getNarratorManager().narrate((Text)text);
            ci.cancel();
        }
    }
}
