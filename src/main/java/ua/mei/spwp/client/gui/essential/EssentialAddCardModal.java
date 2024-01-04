package ua.mei.spwp.client.gui.essential;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.gui.essential.components.*;
import ua.mei.spwp.util.*;

public class EssentialAddCardModal extends BaseOwoScreen<FlowLayout> {
    public Card newCard;

    public EssentialAddCardModal(Card newCard) {
        this.newCard = newCard;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Containers.verticalFlow(Sizing.fill(35), Sizing.content())
                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                        .child(Components.label(Text.translatable("gui.spwp.title.add_card"))
                                                .color(Color.ofArgb(EssentialColorScheme.MODAL_TEXT))
                                                .horizontalTextAlignment(HorizontalAlignment.CENTER)
                                                .shadow(true)
                                                .horizontalSizing(Sizing.fill(100))
                                        )
                                        .child(Components.label(Text.translatable("gui.spwp.description.want_add_card").append("\n" + this.newCard.name() + "?"))
                                                .color(Color.ofArgb(EssentialColorScheme.MODAL_TEXT))
                                                .horizontalTextAlignment(HorizontalAlignment.CENTER)
                                                .shadow(true)
                                                .horizontalSizing(Sizing.fill(100))
                                        )
                                        .gap(13)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                        .child(new EssentialButton(Text.translatable("gui.spwp.button.no"), button -> {
                                            MinecraftClient.getInstance().setScreen(null);
                                        }).horizontalSizing(Sizing.fill(47)))
                                        .child(new EssentialBlueButton(Text.translatable("gui.spwp.button.yes"), button -> {
                                            switch (SPMath.server()) {
                                                case SP -> SPWorldsPayClient.database.addSpCard(this.newCard);
                                                case SPm -> SPWorldsPayClient.database.addSpmCard(this.newCard);
                                            }
                                            MinecraftClient.getInstance().setScreen(null);
                                        }).horizontalSizing(Sizing.fill(47)))
                                        .gap(8)
                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                                )
                                .gap(18)
                                .margins(Insets.of(17))
                        )
                        .surface(Surface.flat(EssentialColorScheme.BACKGROUND).and(Surface.outline(EssentialColorScheme.MODAL_OUTLINE)))
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.VANILLA_TRANSLUCENT);
    }
}
