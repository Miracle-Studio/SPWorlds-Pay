package ua.mei.spwp.client.screens.vanilla;

import io.wispforest.owo.client.screens.*;
import net.fabricmc.api.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;

@Environment(EnvType.CLIENT)
public class VanillaScreenHandler extends ScreenHandler {
    public PlayerInventory playerInventory;

    public VanillaScreenHandler(PlayerInventory playerInventory) {
        super(null, 0);

        this.playerInventory = playerInventory;

        SlotGenerator.begin(this::addSlot, 8, 94).playerInventory(this.playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ScreenUtils.handleSlotTransfer(this, slot, this.playerInventory.size());
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.playerInventory.canPlayerUse(player);
    }
}
