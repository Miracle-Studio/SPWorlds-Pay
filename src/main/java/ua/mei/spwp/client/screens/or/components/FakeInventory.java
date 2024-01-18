package ua.mei.spwp.client.screens.or.components;

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

        FakeGrid inventoryGrid = new FakeGrid(3, 9);
        FakeGrid hotbarGrid = new FakeGrid(1, 9);

        List<ItemStack> inventory = playerInventory.main.subList(9, playerInventory.main.size());
        for (ItemStack stack : inventory) {
            inventoryGrid.child(new FakeSlot(stack));
        }

        List<ItemStack> hotbar = playerInventory.main.subList(0, playerInventory.main.size() - 27);
        for (ItemStack stack : hotbar) {
            hotbarGrid.child(new FakeSlot(stack));
        }

        this.child(Components.label(Text.translatable("container.inventory")).color(Color.ofArgb(0xFF3F3F3F)).margins(Insets.of(3, 0, 1, 0)));
        this.child(inventoryGrid.margins(Insets.top(1)));
        this.child(hotbarGrid.margins(Insets.top(4)));
    }
}
