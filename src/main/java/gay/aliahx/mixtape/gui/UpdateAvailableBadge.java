package gay.aliahx.mixtape.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Objects;

public class UpdateAvailableBadge extends TexturedButtonWidget {

    private static final Identifier AMETHYST_GEM = new Identifier("mixtape", "textures/gui/sprites/amethyst_gem.png");

    private final Text toolTipMessage;

    public UpdateAvailableBadge(int x, int y, Text toolTipMessage, PressAction pressAction) {
        super(x, y, 8, 8, new ButtonTextures(AMETHYST_GEM, AMETHYST_GEM), pressAction);
        this.toolTipMessage = toolTipMessage;
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        int u = ((Util.getMeasuringTimeMs() / (hovered ? 200L : 400L) & 1L) == 1L) ? 8 : 0;
        int v = ((Util.getMeasuringTimeMs() / (hovered ? 200L : 400L) & 2L) == 2L) ? 8 : 0;
        if(hovered) {
            setTooltip(Tooltip.of(toolTipMessage));
        }
        context.drawTexture(AMETHYST_GEM, this.getX(), this.getY(), 0, u, v, 8, 8, 16, 16);
    }
}
