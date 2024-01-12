package ua.mei.spwp.client.screens.vanilla;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.screens.vanilla.components.*;

import java.util.*;

public class VanillaScreen extends BaseOwoScreen<FlowLayout> {
    public PlayerInventory inventory;

    public VanillaScreen(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        List<ItemStack> hotbar = this.inventory.main.subList(0, this.inventory.main.size() - 27);
        List<ItemStack> inventory = this.inventory.main.subList(9, this.inventory.main.size());

        FlowLayout hotbarLayout = Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18));

        for (ItemStack stack : hotbar) {
            hotbarLayout.child(new FakeSlot(stack));
        }

        GridLayout inventoryLayout = Containers.grid(Sizing.fill(100), Sizing.fixed(54), 3, 9);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                inventoryLayout.child(new FakeSlot(inventory.get((row * 9) + col)), row, col);
            }
        }

        rootComponent.child(Containers.verticalFlow(Sizing.fixed(176), Sizing.fixed(176))
                .child(Components.texture(new Identifier(SPWorldsPayClient.MOD_ID, "textures/gui/vanilla_screen.png"), 0, 0, 176, 176, 256, 256))
                .child(Containers.verticalFlow(Sizing.fixed(162), Sizing.fixed(161))
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(36))
                                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        )
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(13))
                                .child(Components.label(Text.translatable("container.inventory")).shadow(false).color(Color.ofArgb(0xFF3F3F3F)).margins(Insets.of(1, 0, 1, 0)))
                                .verticalAlignment(VerticalAlignment.CENTER)
                        )
                        .child(inventoryLayout)
                        .child(hotbarLayout.margins(Insets.top(4)))
                        .positioning(Positioning.relative(50, 50))
                )
        );
        rootComponent.horizontalAlignment(HorizontalAlignment.CENTER);
        rootComponent.verticalAlignment(VerticalAlignment.CENTER);
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    }
}
