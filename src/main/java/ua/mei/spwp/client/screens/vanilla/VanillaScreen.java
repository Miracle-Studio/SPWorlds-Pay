package ua.mei.spwp.client.screens.vanilla;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class VanillaScreen extends BaseOwoHandledScreen<FlowLayout, VanillaScreenHandler> {
    public VanillaScreen(PlayerInventory inventory) {
        super(new VanillaScreenHandler(inventory), inventory, Text.empty());

        this.backgroundWidth = 176;
        this.backgroundHeight = 176;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Components.texture(new Identifier("spwp", "textures/gui/vanilla_screen.png"), 0, 0, 176, 176, 256, 256));
        rootComponent.horizontalAlignment(HorizontalAlignment.CENTER);
        rootComponent.verticalAlignment(VerticalAlignment.CENTER);
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    }
}
