package ua.mei.spwp.client.screens.vanilla.components;

import io.wispforest.owo.mixin.ui.access.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import ua.mei.spwp.client.*;

import java.util.function.*;

public class TexturedButton extends ButtonComponent {
    public final String texturePath;

    public TexturedButton(String texturePath, int width, int height, Consumer<ButtonComponent> onPress) {
        super(Text.literal(""), onPress);

        this.texturePath = texturePath;

        this.horizontalSizing(Sizing.fixed(width));
        this.verticalSizing(Sizing.fixed(height));
    }

    @Override
    public void drawTexture(DrawContext context, Identifier texture, int x, int y, int u, int v, int hoveredVOffset, int width, int height, int textureWidth, int textureHeight) {
        context.drawTexture(new Identifier(SPWorldsPayClient.MOD_ID, "textures/gui/vanilla/button/" + this.texturePath), x, y, 0, 0, 36, 18, 36, 18);
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        Tooltip tooltip = ((ClickableWidgetAccessor) this).owo$getTooltip();

        if (this.hovered && tooltip != null) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip.getLines(MinecraftClient.getInstance()), HoveredTooltipPositioner.INSTANCE, mouseX, mouseY);
        }
    }
}
