package com.aliahx.mixtape.toast;

import com.aliahx.mixtape.Mixtape;
import com.aliahx.mixtape.MusicManager;
import com.aliahx.mixtape.MusicManager.AlbumCover;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Objects;

import static com.aliahx.mixtape.Mixtape.config;

@Environment(EnvType.CLIENT)
public class MusicToast implements Toast {
    private static final Vector3fc FORWARD_SHIFT = new Vector3f(0.0F, 0.0F, 0.03F);
    private final long DURATION;
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
        this.DURATION = config.musicToastConfig.toastDisplayTime;
        this.ALBUMCOVER = MusicManager.getAlbumCover(ALBUM.getString());
    }

    @Override
    public Toast.Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        context.drawTexture(TEXTURE, 0, 0, 0, 32, this.getWidth(), this.getHeight());
        if(config.musicToastConfig.showAlbumCover) {
            if(config.musicToastConfig.useDiscItemAsAlbumCover && ITEM != ItemStack.EMPTY) {
                context.drawItemWithoutEntity(ITEM, 8, 8);
            } else {
                ALBUMCOVER.drawIcon(context, 6, 6);
            }
        }

        int albumCoverOffset = config.musicToastConfig.showAlbumCover ? 30 : 6;
        drawScrollableText(context, manager.getClient().textRenderer, Text.of((config.musicToastConfig.showArtistName && !Objects.equals(ARTIST.getString(), "")) ? ARTIST.getString() + " - " + NAME.getString() : NAME.getString()), albumCoverOffset, 0, 154, 20, -11534256);
        if(config.musicToastConfig.showAlbumName) {
            drawScrollableText(context, manager.getClient().textRenderer, ALBUM, albumCoverOffset, 10, 154, 30, -16777216);
        }

        return (double)(startTime) >= DURATION * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    static void drawScrollableText(DrawContext context, TextRenderer textRenderer, Text text, int left, int top, int right, int bottom, int color) {
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
            int g = (int) MathHelper.lerp(f, 0.0, l);
            MatrixStack matrices = context.getMatrices();
            int x = (int) matrices.peek().getPositionMatrix().translate(FORWARD_SHIFT).get(3, 0);
            int y = (int) matrices.peek().getPositionMatrix().translate(FORWARD_SHIFT).get(3, 1);

            context.enableScissor(x + left, y + top, x + right, y + bottom);
            context.drawText(textRenderer, text, left - g, j, color, false);
            context.disableScissor();
        } else {
            context.drawText(textRenderer, text, left, j, color, false);
        }
    }

    public static void show(ToastManager manager, MusicManager.Entry entry, ItemStack itemStack) {
        if(config.mainConfig.enabled && config.musicToastConfig.enabled) {
            if(config.musicToastConfig.useHotbarInsteadOfToast) {
                Mixtape.client.inGameHud.setRecordPlayingOverlay(Text.of((config.musicToastConfig.showArtistName && !Objects.equals(entry.getArtist(), "") ? entry.getArtist() + " - ": "") + entry.getName()));
            } else {
                manager.add(new MusicToast(Text.literal(entry.getName()), Text.literal(entry.getArtist()), Text.literal(entry.getAlbum()), itemStack));
            }
        }
    }


}
