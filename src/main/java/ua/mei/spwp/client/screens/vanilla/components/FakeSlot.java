package ua.mei.spwp.client.screens.vanilla.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.item.*;

public class FakeSlot extends FlowLayout {
    public ItemStack itemStack;

    public FakeSlot(ItemStack itemStack) {
        super(Sizing.fixed(18), Sizing.fixed(18), Algorithm.VERTICAL);

        this.itemStack = itemStack;

        BoxComponent box = new HoveredBoxComponent(Sizing.fixed(16), Sizing.fixed(16), 0x00FFFFFF, 0x80FFFFFF).fill(true);
        box.positioning(Positioning.relative(50, 50));
        box.zIndex(200);

        this.child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                .child(Components.item(itemStack).setTooltipFromStack(true).showOverlay(true))
                .child(box)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
        );
        this.horizontalAlignment(HorizontalAlignment.CENTER);
        this.verticalAlignment(VerticalAlignment.CENTER);
    }
}
