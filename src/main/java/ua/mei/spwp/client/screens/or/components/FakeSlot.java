package ua.mei.spwp.client.screens.or.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.text.*;

import java.util.*;

public class FakeSlot extends ItemComponent {
    public FakeSlot(ItemStack stack) {
        super(stack);

        this.setTooltipFromStack(true);
        this.sizing(Sizing.fixed(18));
    }

    public FakeSlot(ItemStack stack, List<Text> tooltip) {
        super(stack);

        this.setTooltipFromStack(true);
        this.sizing(Sizing.fixed(18));

        this.tooltip(tooltip);
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
