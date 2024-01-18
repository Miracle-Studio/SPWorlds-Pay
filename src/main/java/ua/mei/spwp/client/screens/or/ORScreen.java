package ua.mei.spwp.client.screens.or;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.network.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.screens.or.components.*;

import java.util.*;

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
        FakeGrid cardGrid = new FakeGrid(2, 9);

        List<Card> cardList = SPWorldsPayClient.database.getCards();

        for (Card card : cardList) {
            cardGrid.child(card.asSlot());
        }

        rootComponent.child(Containers.verticalFlow(Sizing.fixed(176), Sizing.fixed(176))
                .child(Components.texture(new Identifier(SPWorldsPayClient.MOD_ID, "textures/gui/or/main.png"), 0, 0, 176, 176, 256, 256))
                .child(Containers.verticalFlow(Sizing.fixed(162), Sizing.fixed(161))
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18)))
                        .child(cardGrid)
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
