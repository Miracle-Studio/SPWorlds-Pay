package ua.mei.spwp.client.screens.vanilla.components;

import io.wispforest.owo.ui.component.*;;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import ua.mei.spwp.mixin.*;

public class FakeItem extends ItemComponent {
    public FakeItem(ItemStack stack) {
        super(stack);

        this.showOverlay(true);
        this.setTooltipFromStack(true);
    }

    @Override
    public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        super.draw(context, mouseX, mouseY, partialTicks, delta);

        if (this.parent() != null) {
            if (((BaseComponentAccessor) this.parent()).getHovered() || this.hovered) {
                context.fill(this.x, this.y, this.x + 16, this.y + 16, 200, 0x80FFFFFF);
            }
        }
    }

    @Override
    public void drawTooltip(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        if (this.parent() != null) {
            if ((((BaseComponentAccessor) this.parent()).getHovered() || this.hovered) && this.tooltip() != null) {
                context.drawTooltip(MinecraftClient.getInstance().textRenderer, mouseX, mouseY, this.tooltip());
            }
        }
    }
}
