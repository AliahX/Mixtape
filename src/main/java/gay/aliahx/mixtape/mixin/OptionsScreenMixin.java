package gay.aliahx.mixtape.mixin;

import gay.aliahx.mixtape.config.YACLImplementation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    private static final Identifier MIXTAPE_ICON_TEXTURE_FOCUSED = new Identifier(
            "mixtape:mixtape_button_focused");
    private static final Identifier MIXTAPE_ICON_TEXTURE_UNFOCUSED = new Identifier(
            "mixtape:mixtape_button_unfocused");

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initMixin(CallbackInfo ci) {
        this.addDrawableChild(new TexturedButtonWidget(
                this.width / 2 + 159, this.height / 6 + 42, 20, 20,
                new ButtonTextures(MIXTAPE_ICON_TEXTURE_UNFOCUSED, MIXTAPE_ICON_TEXTURE_FOCUSED), (button) -> {
                    MinecraftClient.getInstance().setScreen(YACLImplementation.generateConfigScreen(this));
                }));
    }
}
