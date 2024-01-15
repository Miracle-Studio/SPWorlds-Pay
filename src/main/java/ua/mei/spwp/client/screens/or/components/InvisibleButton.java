package ua.mei.spwp.client.screens.or.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.function.*;

public class InvisibleButton extends ButtonComponent {
    public InvisibleButton(int width, int height, Consumer<ButtonComponent> onPress) {
        super(Text.literal(""), onPress);

        this.horizontalSizing(Sizing.fixed(width));
        this.verticalSizing(Sizing.fixed(height));
    }

    @Override
    public void drawTexture(DrawContext context, Identifier texture, int x, int y, int u, int v, int hoveredVOffset, int width, int height, int textureWidth, int textureHeight) {
        // Нихуя не делаем
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        Tooltip tooltip = this.getTooltip();

        if (this.hovered && tooltip != null) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip.getLines(MinecraftClient.getInstance()), HoveredTooltipPositioner.INSTANCE, mouseX, mouseY);
        }
    }
}
