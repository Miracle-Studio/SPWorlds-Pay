package ua.mei.spwp.client.screens.vanilla.components;

import io.wispforest.owo.ui.component.*;;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.item.*;

public class FakeItem extends ItemComponent {
    public FakeItem(ItemStack stack) {
        super(stack);

        this.setTooltipFromStack(true);
        this.sizing(Sizing.fixed(18));
    }

    @Override
    public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        context.drawItem(this.stack, this.x + 1, this.y + 1);
        context.drawItemInSlot(MinecraftClient.getInstance().textRenderer, this.stack, this.x + 1, this.y + 1);

        if (this.hovered) {
            context.fill(this.x + 1, this.y + 1, this.x + 17, this.y + 17, 200, 0x80FFFFFF);
        }
    }
}
