package ua.mei.spwp.client.gui.components;

import io.wispforest.owo.mixin.ui.access.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.text.*;

import java.util.function.*;

public class TransparentButton extends ButtonComponent {
    public TransparentButton(Text message, Consumer<ButtonComponent> onPress) {
        super(message, onPress);
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        int DEFAULT_COLOR = 0x00FFFFFF;
        int HIGHLIGHT_COLOR = 0x25000000;

        Insets margins = this.margins().get();
        context.fill(this.getX() - margins.left(), this.getY() - margins.top(), this.getX() + this.width + margins.right(), this.getY() + this.height + margins.bottom(), this.hovered ? HIGHLIGHT_COLOR : DEFAULT_COLOR);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int color = this.active ? 0xffffff : 0xa0a0a0;

        if (this.textShadow) {
            context.drawCenteredTextWithShadow(textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, color);
        } else {
            context.drawText(textRenderer, this.getMessage(), (int) (this.getX() + this.width / 2f - textRenderer.getWidth(this.getMessage()) / 2f), (int) (this.getY() + (this.height - 8) / 2f), color, false);
        }

        Tooltip tooltip = ((ClickableWidgetAccessor) this).owo$getTooltip();

        if (this.hovered && tooltip != null) {
            context.drawTooltip(textRenderer, tooltip.getLines(MinecraftClient.getInstance()), HoveredTooltipPositioner.INSTANCE, mouseX, mouseY);
        }
    }
}
