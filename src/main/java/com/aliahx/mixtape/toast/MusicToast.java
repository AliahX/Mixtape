package com.aliahx.mixtape.toast;

import com.aliahx.mixtape.Mixtape;
import com.aliahx.mixtape.MusicManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Objects;

import static com.aliahx.mixtape.Mixtape.config;
import static net.minecraft.client.gui.DrawableHelper.*;

@Environment(EnvType.CLIENT)
public class MusicToast implements Toast {
    public static final Identifier ALBUM_COVERS = new Identifier(Mixtape.MOD_ID, "textures/gui/album_covers.png");
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
        this.ALBUMCOVER = switch(ALBUM.getString()) {
            case "Minecraft - Volume Alpha" -> AlbumCover.ALPHA;
            case "Minecraft - Volume Beta" -> AlbumCover.BETA;
            case "Minecraft: The Wild Update" -> AlbumCover.THE_WILD_UPDATE;
            case "Minecraft: Caves & Cliffs" -> AlbumCover.CAVES_AND_CLIFFS;
            case "Minecraft: Nether Update" -> AlbumCover.NETHER_UPDATE;
            case "Axolotl - Single" -> AlbumCover.AXOLOTL;
            case "Dragon Fish - Single" -> AlbumCover.DRAGON_FISH;
            case "Shuniji - Single" -> AlbumCover.SHUNIJI;
            default -> AlbumCover._0x10c;
        };
    }

    @Override
    public Toast.Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        DrawableHelper.drawTexture(matrices, 0, 0, 0, 32, this.getWidth(), this.getHeight());
        if(config.musicToastConfig.showAlbumCover) {
            if(config.musicToastConfig.useDiscItemAsAlbumCover && ITEM != ItemStack.EMPTY) {
                manager.getClient().getItemRenderer().renderInGui(matrices, ITEM, 8, 8);
            } else {
                ALBUMCOVER.drawIcon(matrices, 6, 6);
            }
        }

        int albumCoverOffset = config.musicToastConfig.showAlbumCover ? 30 : 6;
        drawScrollableText(matrices, manager.getClient().textRenderer, Text.of(config.musicToastConfig.showArtistName ? ARTIST.getString() + " - " + NAME.getString() : NAME.getString()), albumCoverOffset, 0, 154, 20);
        if(config.musicToastConfig.showAlbumName) {drawScrollableText(matrices, manager.getClient().textRenderer, ALBUM, albumCoverOffset, 10, 154, 30);}

        return (double)(startTime) >= DURATION * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    static void drawScrollableText(MatrixStack matrices, TextRenderer textRenderer, Text text, int left, int top, int right, int bottom) {
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
            int x = (int) matrices.peek().getPositionMatrix().translate(FORWARD_SHIFT).get(3, 0);
            int y = (int) matrices.peek().getPositionMatrix().translate(FORWARD_SHIFT).get(3, 1);

            enableScissor(x + left, y + top, x + right, y + bottom);
            textRenderer.draw(matrices, text, left - g, j, -11534256);
            disableScissor();
        } else {
            textRenderer.draw(matrices, text, left, j, -11534256);
        }
    }

    public static void show(ToastManager manager, MusicManager.Entry entry, ItemStack itemStack) {
        if(config.mainConfig.enabled && config.musicToastConfig.enabled) {
            if(config.musicToastConfig.useHotbarInsteadOfToast) {
                Mixtape.client.inGameHud.setRecordPlayingOverlay(Text.of((config.musicToastConfig.showArtistName ? entry.getArtist() + " - ": "") + entry.getName()));
            } else {
                manager.add(new MusicToast(Text.literal(entry.getName()), Text.literal(entry.getArtist()), Text.literal(entry.getAlbum()), itemStack));
            }
        }
    }

    public enum AlbumCover {
        ALPHA(0, 0),
        BETA(1, 0),
        AXOLOTL(2, 0),
        DRAGON_FISH(3, 0),
        SHUNIJI(4, 0),
        NETHER_UPDATE(0, 1),
        CAVES_AND_CLIFFS(1, 1),
        THE_WILD_UPDATE(2, 1),
        _0x10c(3, 1);

        private final int x;
        private final int y;

        AlbumCover(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void drawIcon(MatrixStack matrices, int x, int y) {
            RenderSystem.setShaderTexture(0, ALBUM_COVERS);
            DrawableHelper.drawTexture(matrices, x, y, 0, this.x * 20, this.y * 20, 20, 20, 100, 100);
        }
    }
}
