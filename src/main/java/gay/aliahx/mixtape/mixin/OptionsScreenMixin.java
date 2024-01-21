package gay.aliahx.mixtape.mixin;

import gay.aliahx.mixtape.Mixtape;
import gay.aliahx.mixtape.MusicManager;
import gay.aliahx.mixtape.config.ModConfig;
import gay.aliahx.mixtape.config.YACLImplementation;
import gay.aliahx.mixtape.gui.UpdateAvailableBadge;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static gay.aliahx.mixtape.Mixtape.config;


@Environment(EnvType.CLIENT)
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Unique
    private static final Identifier MIXTAPE_ICON_TEXTURE = new Identifier("mixtape:textures/gui/mixtape_button.png");

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initMixin(CallbackInfo ci) {
        boolean needsUpdate = false;
        String version = "";
        try {
            String json = IOUtils.toString(new URL("https://api.github.com/repos/aliahx/mixtape/releases/latest"), StandardCharsets.UTF_8);
            version = json.split("\"tag_name\":\"")[1].split("\"")[0];
            if(!version.equals(Mixtape.MOD_VERSION)) needsUpdate = true;
        } catch (Exception ignored) {}

        if(needsUpdate && !config.main.hideUpdateBadge) {
            String latestVersion = version;
            this.addDrawableChild(new UpdateAvailableBadge(this.width / 2 + 156, this.height / 6 + 39, "amethyst", Text.translatable("mixtape.update.available"), (button) -> {
                MinecraftClient.getInstance().setScreen(new ConfirmScreen((result) -> {
                    if (result) {
                        Util.getOperatingSystem().open(URI.create("https://modrinth.com/mod/mixtape/versions"));
                    }
                    MinecraftClient.getInstance().setScreen(this);
                }, Text.translatable("mixtape.update", latestVersion), Text.translatable("mixtape.update.message"), ScreenTexts.YES, ScreenTexts.NO));
            }));
        }
        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 159, this.height / 6 + 42, 20, 20, 0, 0, 20, MIXTAPE_ICON_TEXTURE, 20, 40, (button) -> {
            MinecraftClient.getInstance().setScreen(YACLImplementation.generateConfigScreen(this));
        }));
        if(needsUpdate && !config.main.hideUpdateBadge) {
            this.addDrawableChild(new UpdateAvailableBadge(this.width / 2 + 156, this.height / 6 + 39, "amethyst", Text.of("Mixtape Requires an update"), (button) -> {}));
        }

        //the reason the badge is drawn twice is that it has to be drawn before the mixtape button
        // you are able to click on the mixtape button through the badge, but then it must be drawn
        // a second time otherwise it renders behind the mixtape button :P
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(config.main.enabled && config.main.showCurrentSong && config.main.songLocation == ModConfig.SongLocation.OPTIONS_SCREEN) {
            String[] arr = Mixtape.currentSong.split("/");
            MusicManager.Entry song = Mixtape.musicManager.getEntry(arr[arr.length - 1]);

            String currentSongString = "♫ " + song.getArtist() + " - " + song.getName() + " ♫";
            context.drawText(textRenderer, currentSongString, (this.width / 2) - (textRenderer.getWidth(currentSongString) / 2), this.height / 6 + 30, 0xFFFFFF, true);
        }
    }
}
