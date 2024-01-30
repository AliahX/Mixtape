package gay.aliahx.mixtape.mixin;

import gay.aliahx.mixtape.Mixtape;
import gay.aliahx.mixtape.MusicManager;
import gay.aliahx.mixtape.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static gay.aliahx.mixtape.Mixtape.config;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(config.main.enabled && config.main.showCurrentSong && config.main.songLocation == ModConfig.SongLocation.PAUSE_SCREEN && !Objects.equals(Mixtape.currentSong, "")) {
            String[] arr = Mixtape.currentSong.split("/");
            MusicManager.Entry song = Mixtape.musicManager.getEntry(arr[arr.length - 1]);
            String currentSongString = "♫ " + song.getArtist() + " - " + song.getName() + " ♫";
            context.drawText(textRenderer, currentSongString, (this.width / 2) - (textRenderer.getWidth(currentSongString) / 2), (this.height / 3 * 2) + 30, 0xFFFFFF, true);
        }
    }
}
