package gay.aliahx.mixtape.mixin;

import gay.aliahx.mixtape.toast.MusicToast;
import gay.aliahx.mixtape.Mixtape;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static gay.aliahx.mixtape.Mixtape.config;

@Mixin(ToastManager.Entry.class)
public class ToastManagerEntryMixin<T extends Toast>  {
    @Shadow @Final private T instance;

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/Toast$Visibility;playSound(Lnet/minecraft/client/sound/SoundManager;)V"))
    public void playSound(Toast.Visibility visibility, net.minecraft.client.sound.SoundManager soundManager) {
        if(!(this.instance instanceof MusicToast) || config.musicToast.toastMakesSound) {
            visibility.playSound(soundManager);
        }
    }
}