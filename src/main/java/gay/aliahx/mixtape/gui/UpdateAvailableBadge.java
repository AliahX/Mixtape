package gay.aliahx.mixtape.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Objects;

public class UpdateAvailableBadge extends TexturedButtonWidget {

    private static final Identifier AMETHYST_GEM = new Identifier("mixtape", "textures/gui/gems/amethyst_gem.png");
    private static final Identifier COAL_GEM = new Identifier("mixtape", "textures/gui/gems/coal_gem.png");
    private static final Identifier COPPER_GEM = new Identifier("mixtape", "textures/gui/gems/copper_gem.png");
    private static final Identifier DIAMOND_GEM = new Identifier("mixtape", "textures/gui/gems/diamond_gem.png");
    private static final Identifier EMERALD_GEM = new Identifier("mixtape", "textures/gui/gems/emerald_gem.png");
    private static final Identifier GOLD_GEM = new Identifier("mixtape", "textures/gui/gems/gold_gem.png");
    private static final Identifier IRON_GEM = new Identifier("mixtape", "textures/gui/gems/iron_gem.png");
    private static final Identifier LAPIS_GEM = new Identifier("mixtape", "textures/gui/gems/lapis_gem.png");
    private static final Identifier NETHERITE_GEM = new Identifier("mixtape", "textures/gui/gems/netherite_gem.png");
    private static final Identifier REDSTONE_GEM = new Identifier("mixtape", "textures/gui/gems/redstone_gem.png");
    private static final Identifier QUARTZ_GEM = new Identifier("mixtape", "textures/gui/gems/ruby_gem.png");
    private static final Identifier ENDER_GEM = new Identifier("mixtape", "textures/gui/gems/ruby_gem.png");
    private static int u = 0;
    private static int v = 0;
    private final Text toolTipMessage;

    public UpdateAvailableBadge(int x, int y, String gemType, Text toolTipMessage, PressAction pressAction) {
        super(x, y, 8, 8, u, v, Objects.equals(gemType, "amethyst") ? AMETHYST_GEM : Objects.equals(gemType, "coal") ? COAL_GEM : Objects.equals(gemType, "copper") ? COPPER_GEM : Objects.equals(gemType, "emerald") ? EMERALD_GEM : Objects.equals(gemType, "gold") ? GOLD_GEM : Objects.equals(gemType, "iron") ? IRON_GEM : Objects.equals(gemType, "lapis") ? LAPIS_GEM : Objects.equals(gemType, "netherite") ? NETHERITE_GEM : Objects.equals(gemType, "redstone") ? REDSTONE_GEM : Objects.equals(gemType, "quartz") ? QUARTZ_GEM : Objects.equals(gemType, "ender") ? ENDER_GEM : DIAMOND_GEM, pressAction);
        this.toolTipMessage = toolTipMessage;
    }

    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        u = ((Util.getMeasuringTimeMs() / (hovered ? 200L : 400L) & 1L) == 1L) ? 8 : 0;
        v = ((Util.getMeasuringTimeMs() / (hovered ? 200L : 400L) & 2L) == 2L) ? 8 : 0;
        if(hovered) {
            setTooltip(Tooltip.of(toolTipMessage));
        }
        this.drawTexture(context, AMETHYST_GEM, this.getX(), this.getY(), u, v, 0, 8, 8, 16, 16);
    }
}
