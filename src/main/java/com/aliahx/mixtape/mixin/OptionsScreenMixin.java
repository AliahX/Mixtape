package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Environment(EnvType.CLIENT)
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    private static final Identifier MIXTAPE_ICON_TEXTURE = new Identifier("mixtape:textures/gui/mixtape_button.png");

    protected OptionsScreenMixin(Text title) {
        super(title);
    }
    ClickableWidget data;

    @Inject(at = @At("RETURN"), method = "init")
    private void addCustomButton(CallbackInfo ci) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        int additionalWidth = 0;
        for (ClickableWidget button : Screens.getButtons((Screen) (Object) this)) {
            if (Objects.equals(button.getMessage().toString(), "translation{key='options.sounds', args=[]}")) {
                if(config.mainConfig.mixtapeButtonLocation == ModConfig.MixtapeButtonLocation.IN_MUSIC_BUTTON) {
                    button.setWidth(126);
                } else {
                    additionalWidth = 24;
                }
                data = button;
            }
        }

        this.addDrawableChild(new TexturedButtonWidget(data.getX() + 130 + additionalWidth, this.height / 6 + 48 - 6, 20, 20, 0, 0, 20, MIXTAPE_ICON_TEXTURE, 20, 40, (button) -> {
            MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(ModConfig.class, this).get());
        }));
    }
}
