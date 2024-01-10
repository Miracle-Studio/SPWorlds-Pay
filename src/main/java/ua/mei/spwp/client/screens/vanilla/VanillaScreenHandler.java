package ua.mei.spwp.client.screens.vanilla;

import io.wispforest.owo.client.screens.*;
import net.fabricmc.api.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;

@Environment(EnvType.CLIENT)
public class VanillaScreenHandler extends ScreenHandler {
    public PlayerInventory inventory;

    public VanillaScreenHandler(PlayerInventory inventory) {
        super(null, 0);

        this.inventory = inventory;

        SlotGenerator.begin(this::addSlot, 8, 89).playerInventory(inventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ScreenUtils.handleSlotTransfer(this, slot, this.inventory.size());
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
