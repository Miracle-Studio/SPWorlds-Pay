package ua.mei.spwp.client.gui.essential;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.api.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.gui.essential.components.*;
import ua.mei.spwp.util.*;

public class NewPage extends BaseOwoScreen<FlowLayout> {
    public Server server;
    public DatabaseCard card = null;

    public int oldGuiScale;
    public boolean closing = false;

    public Transaction transaction;

    public NewPage(Server server) {
        this.oldGuiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();
        this.server = server;
    }

    public NewPage(Server server, Transaction transaction) {
        this.oldGuiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();
        this.server = server;
        this.transaction = transaction;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    public void onDisplayed() {
        MinecraftClient.getInstance().options.getGuiScale().setValue(2);
        MinecraftClient.getInstance().onResolutionChanged();
    }

    @Override
    public void close() {
        this.closing = true;

        MinecraftClient.getInstance().options.getGuiScale().setValue(this.oldGuiScale);
        MinecraftClient.getInstance().onResolutionChanged();

        super.close();
    }

    private boolean checkValues(EssentialTextBox number, EssentialTextBox amount) {
        if (number.textBoxComponent.getText().matches("[0-9]+") && number.textBoxComponent.getText().length() == 5 && amount.textBoxComponent.getText().matches("[0-9]+") && !amount.textBoxComponent.getText().isEmpty() && this.card != null) {
            int amountToSend = Integer.parseInt(amount.textBoxComponent.getText());

            return amountToSend > 0;
        }

        return false;
    }

    public FlowLayout generateOverlay(OverlayContainer<Component> overlay) {
        ParentComponent content = Containers.verticalFlow(Sizing.fill(20), Sizing.content())
                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                        .child(Components.label(Text.translatable("modal.spwp.delete_card.description").append(this.card.card().name() + "?"))
                                .color(Color.ofArgb(EssentialColorScheme.MODAL_TEXT))
                                .horizontalTextAlignment(HorizontalAlignment.CENTER)
                                .shadow(true)
                                .horizontalSizing(Sizing.fill(100))
                        )
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                .child(new EssentialButton(Text.translatable("gui.spwp.button.no"), button -> {
                                    overlay.remove();
                                }).horizontalSizing(Sizing.fill(47)))
                                .child(new EssentialRedButton(Text.translatable("gui.spwp.button.delete"), button -> {
                                    Server server = SPMath.server();

                                    if (server != Server.OTHER) {
                                        if (server == Server.SP) {
                                            SPWorldsPayClient.database.deleteSpCard(this.card.rowId());
                                        } else {
                                            SPWorldsPayClient.database.deleteSpmCard(this.card.rowId());
                                        }

                                        MinecraftClient.getInstance().setScreen(new NewPage(server));
                                    }
                                }).horizontalSizing(Sizing.fill(47)))
                                .gap(8)
                                .horizontalAlignment(HorizontalAlignment.CENTER)
                        )
                        .gap(18)
                        .margins(Insets.of(17))
                ).surface(Surface.flat(EssentialColorScheme.BACKGROUND).and(Surface.outline(EssentialColorScheme.MODAL_OUTLINE)));

        FlowLayout layout = Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100));

        layout.child(content).surface(Surface.flat(0x33000000)).horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER);

        return layout;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        OverlayContainer<Component> overlay = Containers.overlay(Containers.verticalFlow(Sizing.fill(0), Sizing.fixed(0)));
        overlay.zIndex(200);
        overlay.closeOnClick(false);

        EssentialScrollContainer cardList = new EssentialScrollContainer(ScrollContainer.ScrollDirection.VERTICAL, Sizing.fill(100), Sizing.fill(100), new CardList(this.server, card -> {
            this.card = card;
        }, delete -> {
            overlay.child(generateOverlay(overlay));
            rootComponent.child(overlay);
        }));

        ServerList serverList = new ServerList(this.server, server -> {
            this.card = null;
            this.server = server;

            cardList.child(new CardList(server, card -> {
                this.card = card;
            }, delete -> {
                overlay.child(generateOverlay(overlay));
                rootComponent.child(overlay);
            }));
        });
        serverList.padding(Insets.of(10, 10, 12, 12));

        EssentialTextBox number = new EssentialTextBox(Sizing.fill(100), Text.translatable("gui.spwp.input.transfer.card_number"));
        number.textBoxComponent.setMaxLength(5);
        number.textBoxComponent.text(this.transaction == null ? "" : String.valueOf(this.transaction.receiver()));
        EssentialTextBox amount = new EssentialTextBox(Sizing.fill(100), Text.translatable("gui.spwp.input.transfer.amount"));
        amount.textBoxComponent.setMaxLength(6);
        amount.textBoxComponent.text(this.transaction == null ? "" : String.valueOf(this.transaction.amount()));
        EssentialTextBox comment = new EssentialTextBox(Sizing.fill(100), Text.translatable("gui.spwp.input.transfer.comment"));
        comment.textBoxComponent.setMaxLength(45);
        comment.textBoxComponent.text(this.transaction == null ? "" : this.transaction.comment());

        EssentialFlatButton transferButton = new EssentialFlatButton(Text.translatable("gui.spwp.button.transfer"), button -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            if (player != null) {
                SPWorldsApi.transfer(this.card.card(), new Transaction(number.textBoxComponent.getText(), Integer.parseInt(amount.textBoxComponent.getText()), comment.textBoxComponent.getText().isEmpty() ? "Нет комментария - " + player.getGameProfile().getName() : comment.textBoxComponent.getText() + " - " + player.getGameProfile().getName()));
            } else {
                SPWorldsApi.transfer(this.card.card(), new Transaction(number.textBoxComponent.getText(), Integer.parseInt(amount.textBoxComponent.getText()), comment.textBoxComponent.getText().isEmpty() ? "Нет комментария" : comment.textBoxComponent.getText()));
            }

            EssentialMessageModal.openMessage(Text.translatable("gui.spwp.title.success"), Text.translatable("gui.spwp.description.successfully_sent").append(this.card.card().name() + " " + amount.textBoxComponent.getText()).append(Text.translatable("gui.spwp.description.diamonds_to_the_card")).append(number.textBoxComponent.getText()));
        });
        transferButton.active(checkValues(number, amount));
        transferButton.horizontalSizing(Sizing.fill(100));
        transferButton.verticalSizing(Sizing.fixed(18));

        number.textBoxComponent.onChanged().subscribe(string -> {
            transferButton.active(checkValues(number, amount));
        });
        amount.textBoxComponent.onChanged().subscribe(string -> {
            transferButton.active(checkValues(number, amount));
        });

        rootComponent.child(Containers.verticalFlow(Sizing.fill(85), Sizing.content())
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(30))
                                .child(Containers.horizontalFlow(Sizing.fill(25), Sizing.fill(100))
                                        .child(Components.label(Text.translatable("gui.spwp.title.cards")).color(Color.ofArgb(EssentialColorScheme.SCREEN_TITLE)).shadow(true).margins(Insets.of(11, 0, 13, 0)))
                                        .surface(EssentialSurface.ESSENTIAL_NAV_LEFT)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(75), Sizing.fill(100))
                                        .child(Components.label(Text.translatable("gui.spwp.title.transfer")).color(Color.ofArgb(EssentialColorScheme.SCREEN_TITLE)).shadow(true).margins(Insets.of(11, 0, 10, 0)))
                                        .surface(EssentialSurface.ESSENTIAL_NAV_RIGHT)
                                )
                        )
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(30))
                                .child(Containers.horizontalFlow(Sizing.fill(25), Sizing.fill(100))
                                        .child(serverList)
                                        .surface(EssentialSurface.ESSENTIAL_PANEL_LEFT)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(75), Sizing.fill(100))
                                        .surface(EssentialSurface.ESSENTIAL_PANEL_RIGHT_TOP)
                                )
                        )
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fill(65))
                                .child(Containers.horizontalFlow(Sizing.fill(25), Sizing.fill(100))
                                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100))
                                                .child(cardList)
                                                .margins(Insets.of(0, 3, 3, 0))
                                        )
                                        .surface(EssentialSurface.ESSENTIAL_PANEL_LEFT)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(75), Sizing.fill(100))
                                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100))
                                                .child(Containers.verticalFlow(Sizing.fill(50), Sizing.content())
                                                        .child(number)
                                                        .child(amount)
                                                        .child(comment)
                                                        .child(transferButton)
                                                        .gap(7)
                                                )
                                                .margins(Insets.of(0, 33, 0, 3))
                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                .verticalAlignment(VerticalAlignment.CENTER)
                                        )
                                        .surface(EssentialSurface.ESSENTIAL_PANEL_RIGHT)
                                )
                        )
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.flat(EssentialColorScheme.BACKGROUND));
    }
}
