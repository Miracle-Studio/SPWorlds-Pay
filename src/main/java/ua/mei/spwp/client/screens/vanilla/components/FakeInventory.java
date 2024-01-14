package ua.mei.spwp.client.screens.vanilla.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;

import java.util.*;

public class FakeInventory extends FlowLayout {
    public FakeInventory(PlayerInventory playerInventory) {
        super(Sizing.fixed(162), Sizing.fixed(89), Algorithm.VERTICAL);

        GridLayout inventoryLayout = Containers.grid(Sizing.fixed(162), Sizing.fixed(54), 3, 9);
        FlowLayout hotbarLayout = Containers.horizontalFlow(Sizing.fixed(162), Sizing.fixed(18));

        List<ItemStack> inventory = playerInventory.main.subList(9, playerInventory.main.size());
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                inventoryLayout.child(new FakeSlot(inventory.get((row * 9) + col)), row, col);
            }
        }

        List<ItemStack> hotbar = playerInventory.main.subList(0, playerInventory.main.size() - 27);
        for (ItemStack stack : hotbar) {
            hotbarLayout.child(new FakeSlot(stack));
        }

        this.child(Components.label(Text.translatable("container.inventory")).color(Color.ofArgb(0xFF3F3F3F)).margins(Insets.of(3, 0, 1, 0)));
        this.child(inventoryLayout.margins(Insets.top(1)));
        this.child(hotbarLayout.margins(Insets.top(4)));
    }
}
