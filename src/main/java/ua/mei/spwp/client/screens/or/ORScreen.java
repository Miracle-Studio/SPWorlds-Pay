package ua.mei.spwp.client.screens.or;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.network.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.screens.or.components.*;

public class ORScreen extends BaseOwoScreen<FlowLayout> {
    public ClientPlayerEntity player;

    public ORScreen(ClientPlayerEntity player) {
        this.player = player;
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
        rootComponent.child(Containers.verticalFlow(Sizing.fixed(176), Sizing.fixed(176))
                .child(Components.texture(new Identifier(SPWorldsPayClient.MOD_ID, "textures/gui/or/main.png"), 0, 0, 176, 176, 256, 256))
                .child(Containers.verticalFlow(Sizing.fixed(162), Sizing.fixed(161))
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(36))
                                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        )
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        .child(new FakeInventory(this.player.getInventory()))
                        .positioning(Positioning.relative(50, 50))
                )
        );
        rootComponent.horizontalAlignment(HorizontalAlignment.CENTER);
        rootComponent.verticalAlignment(VerticalAlignment.CENTER);
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    }
}
