package ua.mei.spwp.client.gui.essential.components;

import com.mojang.blaze3d.systems.*;
import io.wispforest.owo.mixin.ui.access.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.util.*;
import net.minecraft.client.*;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.function.*;

public class EssentialFlatButton extends ButtonComponent {
    public static final Identifier ACTIVE_TEXTURE = new Identifier("spwp", "essential_flat_button/active");
    public static final Identifier HOVERED_TEXTURE = new Identifier("spwp", "essential_flat_button/hovered");
    public static final Identifier DISABLED_TEXTURE = new Identifier("spwp", "essential_flat_button/disabled");

    public EssentialFlatButton(Text message, Consumer<ButtonComponent> onPress) {
        super(message, onPress);
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        Identifier texture = ACTIVE_TEXTURE;

        if (!this.active) {
            texture = DISABLED_TEXTURE;
        } else if (this.isHovered()) {
            texture = HOVERED_TEXTURE;
        }

        NinePatchTexture.draw(texture, (OwoUIDrawContext) context, this.getX(), this.getY(), this.width, this.height);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int color = this.active ? 0xE2E2E2 : 0xBFBFBF;

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
