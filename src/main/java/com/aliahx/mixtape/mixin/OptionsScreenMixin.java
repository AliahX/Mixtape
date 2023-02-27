package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Shadow @Final private GameOptions settings;
    private static final Identifier MIXTAPE_ICON_TEXTURE = new Identifier("mixtape:textures/gui/mixtape_button.png");

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initMixin(CallbackInfo ci) {
        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 159, this.height / 6 + 42, 20, 20, 0, 0, 20, MIXTAPE_ICON_TEXTURE, 20, 40, (button) -> {
            MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(ModConfig.class, this).get());
        }));
    }
//
//    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/ClickableWidget;)Lnet/minecraft/client/gui/widget/ClickableWidget;", ordinal = 3), index = 0)
//    private ClickableWidget createSoundsButton(ClickableWidget widget) {
//        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
//        if(config.mainConfig.mixtapeButtonLocation == ModConfig.MixtapeButtonLocation.BESIDE_MUSIC_BUTTON) {
//            return widget;
//        }
//        ButtonWidget soundsButton = ButtonWidget.builder(Text.translatable("options.sounds"), (button) -> {
//            Supplier<Screen> screenSupplier = () -> {
//                return new SoundOptionsScreen(this, this.settings);
//            };
//            this.client.setScreen((Screen) screenSupplier.get());
//        }).build();
//        soundsButton.setWidth(130);
//        AxisGridWidget axisGridWidget = new AxisGridWidget(150, 0, AxisGridWidget.DisplayAxis.HORIZONTAL);
//        axisGridWidget.add(soundsButton);
////        axisGridWidget.add(mixtapeButton);
//        axisGridWidget.recalculateDimensions();
//        return axisGridWidget;
//    }
}
