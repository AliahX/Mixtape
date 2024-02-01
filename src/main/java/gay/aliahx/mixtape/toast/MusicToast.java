package gay.aliahx.mixtape.toast;

import gay.aliahx.mixtape.Mixtape;
import gay.aliahx.mixtape.MusicManager;
import gay.aliahx.mixtape.MusicManager.AlbumCover;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Objects;

import static gay.aliahx.mixtape.Mixtape.config;

@Environment(EnvType.CLIENT)
public class MusicToast implements Toast {
    private static final Vector3fc FORWARD_SHIFT = new Vector3f(0.0F, 0.0F, 0.03F);
    private static final Identifier TEXTURE = new Identifier("toast/recipe");
    private final Text NAME;
    private final Text ARTIST;
    private final Text ALBUM;
    private final ItemStack ITEM;
    private final AlbumCover ALBUMCOVER;

    public MusicToast(Text NAME, Text ARTIST, Text ALBUM, ItemStack ITEM) {
        this.NAME = NAME;
        this.ARTIST = ARTIST;
        this.ALBUM = ALBUM;
        this.ITEM = ITEM;
        this.ALBUMCOVER = MusicManager.getAlbumCover(ALBUM.getString());
    }

    @Override
    public Toast.Visibility draw(DrawContext context, ToastManager manager, long startTime) {

        String currentSongName = "";
        if(!Objects.equals(Mixtape.currentSong, "")) {
            String[] arr = Mixtape.currentSong.split("/");
            currentSongName = Mixtape.musicManager.getEntry(arr[arr.length - 1]).getName();
        }

        context.drawGuiTexture(TEXTURE, 0, 0, 0, this.getWidth(), this.getHeight());
        if (config.musicToast.showAlbumCover) {
            if (config.musicToast.useDiscItemAsAlbumCover && ITEM != ItemStack.EMPTY) {
                context.drawItemWithoutEntity(ITEM, 8, 8);
            } else {
                ALBUMCOVER.drawIcon(context, 6, 6);
            }
        }

        int albumCoverOffset = config.musicToast.showAlbumCover ? 30 : 6;
        drawScrollableText(context, manager.getClient().textRenderer,
                Text.of((config.musicToast.showArtistName && !Objects.equals(ARTIST.getString(), ""))
                        ? ARTIST.getString() + " - " + NAME.getString()
                        : NAME.getString()),
                albumCoverOffset, 0, 154, 20, -11534256);
        if (config.musicToast.showAlbumName) {
            drawScrollableText(context, manager.getClient().textRenderer, ALBUM, albumCoverOffset, 10, 154, 30,
                    -16777216);
        }

        int toastDisplayTime = 0;
        if(currentSongName.equals(NAME.getString()) || ITEM != ItemStack.EMPTY) {
            if(config.musicToast.remainForFullSong) {
                toastDisplayTime = Integer.MAX_VALUE;
            } else {
                toastDisplayTime = config.musicToast.toastDisplayTime;
            }
        }
        return (double)(startTime) >= toastDisplayTime * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    static void drawScrollableText(DrawContext context, TextRenderer textRenderer, Text text, int left, int top,
            int right, int bottom, int color) {
        int i = textRenderer.getWidth(text);
        int var10000 = top + bottom;
        Objects.requireNonNull(textRenderer);
        int j = (var10000 - 9) / 2 + 1;
        int k = right - left;
        if (i > k) {
            double l = i - k;
            double d = (double) Util.getMeasuringTimeMs() / 1000.0;
            double e = Math.max(l * 0.5, 3.0);
            double f = Math.sin(1.5707963267948966 * Math.cos(6.283185307179586 * d / e)) / 2.0 + 0.5;
            Matrix4f matrices = context.getMatrices().peek().getPositionMatrix().translate(FORWARD_SHIFT);
            int x = (int) matrices.get(3, 0);
            int y = (int) matrices.get(3, 1);

            context.enableScissor(x + left, y + top, x + right, y + bottom);
            context.drawText(textRenderer, text, left - (int) MathHelper.lerp(f, 0.0, l), j, color, false);
            context.disableScissor();
        } else {
            context.drawText(textRenderer, text, left, j, color, false);
        }
    }

    public static void show(ToastManager manager, MusicManager.Entry entry, ItemStack itemStack) {
        if (config.main.enabled && config.musicToast.enabled) {
            if (config.musicToast.useHotbarInsteadOfToast) {
                Mixtape.client.inGameHud.setRecordPlayingOverlay(
                        Text.of("m:" + (config.musicToast.showArtistName && !Objects.equals(entry.getArtist(), "")
                                ? entry.getArtist() + " - "
                                : "") + entry.getName()));
            } else {
                manager.add(new MusicToast(Text.literal(entry.getName()), Text.literal(entry.getArtist()),
                        Text.literal(entry.getAlbum()), itemStack));
            }
        }
    }
}
