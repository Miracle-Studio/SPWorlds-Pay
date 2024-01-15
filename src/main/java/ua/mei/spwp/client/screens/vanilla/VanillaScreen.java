package ua.mei.spwp.client.screens.vanilla;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.screens.vanilla.components.*;

@Environment(EnvType.CLIENT)
public class VanillaScreen extends AbstractInventoryScreen<VanillaScreenHandler> {
    public PlayerInventory playerInventory;

    public VanillaScreen(PlayerInventory playerInventory) {
        super(new VanillaScreenHandler(playerInventory), playerInventory, Text.empty());

        this.playerInventory = playerInventory;

        this.backgroundWidth = 176;
        this.backgroundHeight = 176;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(new Identifier(SPWorldsPayClient.MOD_ID, "textures/gui/vanilla/main.png"), this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    /* @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Containers.verticalFlow(Sizing.fixed(176), Sizing.fixed(176))
                .child(Components.texture(new Identifier(SPWorldsPayClient.MOD_ID, "textures/gui/vanilla/main.png"), 0, 0, 176, 176, 256, 256))
                .child(Containers.verticalFlow(Sizing.fixed(162), Sizing.fixed(161))
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(36))
                                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        )
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        .child(new FakeInventory(this.playerInventory))
                        .positioning(Positioning.relative(50, 50))
                )
        );
        rootComponent.horizontalAlignment(HorizontalAlignment.CENTER);
        rootComponent.verticalAlignment(VerticalAlignment.CENTER);
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    } */
}
