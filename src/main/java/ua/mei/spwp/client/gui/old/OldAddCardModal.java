package ua.mei.spwp.client.gui.old;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.util.*;

public class OldAddCardModal extends BaseOwoScreen<FlowLayout> {
    public Card newCard;

    public OldAddCardModal(Card newCard) {
        this.newCard = newCard;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(Components.label(Text.translatable("gui.spwp.title.add_card"))
                                .shadow(true)
                                .horizontalSizing(Sizing.content())
                                .margins(Insets.bottom(10))
                        )
                        .child(Components.label(Text.translatable("gui.spwp.description.want_add_card").append(this.newCard.name() + "?"))
                                .color(Color.ofRgb(Colors.GRAY))
                                .horizontalSizing(Sizing.content())
                                .margins(Insets.bottom(10))
                        )
                        .child(Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                .child(Components.button(Text.translatable("gui.spwp.button.yes"), button -> {
                                    Server server = SPMath.server();

                                    if (server == Server.SP) {
                                        SPWorldsPayClient.database.addSpCard(this.newCard);
                                        OldMessageModal.openMessage(Text.translatable("gui.spwp.title.card_added"), Text.translatable("gui.spwp.description.card").append(this.newCard.name()).append(Text.translatable("gui.spwp.description.successfully_added")));
                                    } else if (server == Server.SPm) {
                                        SPWorldsPayClient.database.addSpmCard(this.newCard);
                                        OldMessageModal.openMessage(Text.translatable("gui.spwp.title.card_added"), Text.translatable("gui.spwp.description.card").append(this.newCard.name()).append(Text.translatable("gui.spwp.description.successfully_added")));
                                    }
                                }).horizontalSizing(Sizing.fixed(120)))
                                .child(Components.box(Sizing.fixed(1), Sizing.fixed(14))
                                        .color(Color.ofArgb(0))
                                        .margins(Insets.horizontal(5))
                                )
                                .child(Components.button(Text.translatable("gui.spwp.button.no"), button -> {
                                    MinecraftClient.getInstance().setScreen(null);
                                }).horizontalSizing(Sizing.fixed(120)))
                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                .verticalAlignment(VerticalAlignment.CENTER)
                        )
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .padding(Insets.of(15))

                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.VANILLA_TRANSLUCENT);
    }
}
