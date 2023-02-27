package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MusicDiscItem.class)
public abstract class MusicDiscItemMixin {

    @Shadow public abstract int getComparatorOutput();

    @Inject(method = "Lnet/minecraft/item/MusicDiscItem;getDescription()Lnet/minecraft/text/MutableText;", at = @At("HEAD"), cancellable = true)
    private void getDescriptionMixin(CallbackInfoReturnable<MutableText> cir) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(this.getComparatorOutput() == 2 && config.jukeboxConfig.dogReplacesCat) {
            cir.setReturnValue(Text.literal("C418 - dog"));
        } else if(this.getComparatorOutput() == 11 && config.jukeboxConfig.elevenReplaces11) {
            cir.setReturnValue(Text.literal("C418 - eleven"));
        }
    }
}
