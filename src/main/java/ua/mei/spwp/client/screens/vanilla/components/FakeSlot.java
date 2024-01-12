package ua.mei.spwp.client.screens.vanilla.components;

import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.item.*;

public class FakeSlot extends FlowLayout {
    public FakeSlot(ItemStack itemStack) {
        super(Sizing.fixed(18), Sizing.fixed(18), Algorithm.VERTICAL);

        this.child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                .child(new FakeItem(itemStack))
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
        );
        this.horizontalAlignment(HorizontalAlignment.CENTER);
        this.verticalAlignment(VerticalAlignment.CENTER);
    }
}
