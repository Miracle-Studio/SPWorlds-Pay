package ua.mei.spwp.client.gui.bank;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.api.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.gui.*;
import ua.mei.spwp.client.gui.components.*;

import java.util.*;

public class SPMPage extends BaseOwoScreen<FlowLayout> {
    public Card selectedCard;
    public int selectedCardBalance = 0;
    public DatabaseCard cardForEdit;
    public Transaction transaction;

    public SPMPage(Transaction transaction) {
        this.transaction = transaction;
    }

    public SPMPage() {

    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    protected FlowLayout addCardOverlay(OverlayContainer<Component> overlay) {
        TextBoxComponent cardName = Components.textBox(Sizing.fill(100));
        cardName.margins(Insets.top(4).add(0, 6, 0, 0));
        cardName.setMaxLength(12);

        TextBoxComponent cardID = Components.textBox(Sizing.fill(100));
        cardID.margins(Insets.top(4).add(0, 6, 0, 0));
        cardID.setMaxLength(36);

        TextBoxComponent cardToken = Components.textBox(Sizing.fill(100));
        cardToken.margins(Insets.top(4).add(0, 6, 0, 0));
        cardToken.setMaxLength(32);

        FlowLayout overlayLayout = Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100));
        overlayLayout.child(Containers.verticalFlow(Sizing.fill(30), Sizing.content())
                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                        .child(Components.label(Text.translatable("gui.spwp.title.add_spm_card")).margins(Insets.top(2)))
                        .child(Components.label(Text.translatable("gui.spwp.input.card_name")).horizontalTextAlignment(HorizontalAlignment.LEFT).horizontalSizing(Sizing.fill(100)).margins(Insets.top(16)))
                        .child(cardName)
                        .child(Components.label(Text.translatable("gui.spwp.input.card_id")).horizontalTextAlignment(HorizontalAlignment.LEFT).horizontalSizing(Sizing.fill(100)))
                        .child(cardID)
                        .child(Components.label(Text.translatable("gui.spwp.input.card_token")).horizontalTextAlignment(HorizontalAlignment.LEFT).horizontalSizing(Sizing.fill(100)))
                        .child(cardToken)
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                .child(Components.button(Text.translatable("gui.spwp.button.back"), button -> {
                                    overlay.remove();
                                }).horizontalSizing(Sizing.fill(48)))
                                .child(Containers.verticalFlow(Sizing.fill(4), Sizing.content()))
                                .child(Components.button(Text.translatable("gui.spwp.button.add"), button -> {
                                    Card newCard = new Card(cardName.getText(), cardID.getText(), cardToken.getText());

                                    SPWorldsPayClient.asyncTasksService.addTask(() -> {
                                        return SPWorldsApi.getBalance(newCard);
                                    }, result -> {
                                        if ((int) result != -5298) {
                                            SPWorldsPayClient.database.addSpmCard(newCard);
                                            MinecraftClient.getInstance().setScreen(new SPMPage());
                                        } else {
                                            MinecraftClient.getInstance().setScreen(new MessageModal(Text.translatable("gui.spwp.title.error"), Text.translatable("gui.spwp.description.ddos_error")));
                                        }
                                    }, exception -> {

                                    });
                                }).horizontalSizing(Sizing.fill(48)))
                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                .verticalAlignment(VerticalAlignment.CENTER)
                        )
                        .margins(Insets.of(8))
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .verticalAlignment(VerticalAlignment.CENTER)
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.DARK_PANEL)
        );
        overlayLayout.horizontalAlignment(HorizontalAlignment.CENTER);
        overlayLayout.verticalAlignment(VerticalAlignment.CENTER);
        overlayLayout.surface(Surface.flat(0x32000000));

        return overlayLayout;
    }

    protected FlowLayout editCardsOverlay(OverlayContainer<Component> overlay) {
        TextBoxComponent cardName = Components.textBox(Sizing.fill(100));
        cardName.margins(Insets.top(4));
        cardName.setMaxLength(12);
        cardName.setPlaceholder(Text.translatable("gui.spwp.input.edit_card.placeholder"));
        cardName.horizontalSizing(Sizing.fill(100));

        ScrollDropdownComponent dropdownComponent = new ScrollDropdownComponent(Sizing.fill(100), Sizing.fixed(90), Text.translatable("gui.spwp.description.choose_card"), false);

        ButtonComponent deleteButton = Components.button(Text.translatable("gui.spwp.button.delete"), button -> {
            SPWorldsPayClient.database.deleteSpmCard(this.cardForEdit.rowId());
            MessageModal.openMessage(Text.translatable("gui.spwp.title.success"), Text.translatable("gui.spwp.description.card").append(this.cardForEdit.card().name()).append(Text.translatable("gui.spwp.description.card_deleted")));
        });
        deleteButton.horizontalSizing(Sizing.fill(100));
        deleteButton.active(false);
        deleteButton.margins(Insets.top(4));
        deleteButton.tooltip(Text.translatable("gui.spwp.description.delete_card.tooltip"));

        ButtonComponent saveButton = Components.button(Text.translatable("gui.spwp.button.save"), button -> {
            SPWorldsPayClient.database.editSpmCard(cardName.getText(), this.cardForEdit.rowId());
            MessageModal.openMessage(Text.translatable("gui.spwp.title.success"), Text.translatable("gui.spwp.description.card").append(this.cardForEdit.card().name()).append(Text.translatable("gui.spwp.description.card_edited")).append(cardName.getText() + "!"));
        });
        saveButton.horizontalSizing(Sizing.fill(100));
        saveButton.active(false);
        saveButton.margins(Insets.top(4));
        saveButton.tooltip(Text.translatable("gui.spwp.description.save_card.tooltip"));

        cardName.onChanged().subscribe((string) -> {
            if (this.cardForEdit != null) {
                saveButton.active(!string.equals(this.cardForEdit.card().name()) && !string.isBlank());
            } else {
                cardName.text("");
                saveButton.active(false);
            }
        });

        List<DatabaseCard> cards = SPWorldsPayClient.database.getSpmCards();
        for (DatabaseCard databaseCard : cards) {
            Card card = databaseCard.card();
            dropdownComponent.button(Text.literal(card.name()), button -> {
                this.cardForEdit = databaseCard;
                dropdownComponent.title(Text.literal(card.name()));
                cardName.text(card.name());
                deleteButton.active(true);
                saveButton.active(false);
            });
        }

        FlowLayout overlayLayout = Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100));
        overlayLayout.child(Containers.verticalFlow(Sizing.fill(40), Sizing.content())
                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                        .child(Containers.verticalFlow(Sizing.fill(50), Sizing.content())
                                .child(dropdownComponent)
                        )
                        .child(Containers.verticalFlow(Sizing.fill(50), Sizing.content())
                                .child(Containers.verticalFlow(Sizing.fill(90), Sizing.content())
                                        .child(Components.button(Text.translatable("gui.spwp.button.back"), button -> {
                                            overlay.remove();
                                        }).horizontalSizing(Sizing.fill(100)))
                                        .child(deleteButton)
                                        .child(saveButton)
                                        .child(Components.label(Text.translatable("gui.spwp.input.card_name")).horizontalTextAlignment(HorizontalAlignment.LEFT).horizontalSizing(Sizing.fill(100)).margins(Insets.top(8)))
                                        .child(cardName)
                                )
                                .horizontalAlignment(HorizontalAlignment.RIGHT)
                        )
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .verticalAlignment(VerticalAlignment.TOP)
                        .margins(Insets.of(8))
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.DARK_PANEL)
        );
        overlayLayout.horizontalAlignment(HorizontalAlignment.CENTER);
        overlayLayout.verticalAlignment(VerticalAlignment.CENTER);
        overlayLayout.surface(Surface.flat(0x32000000));

        return overlayLayout;
    }

    public MutableText getBalance(int balance) {
        if (balance != -5298) {
            return Text.translatable("gui.spwp.description.balance").append(String.valueOf(balance));
        } else {
            return Text.translatable("gui.spwp.description.balance_error").styled(style -> style.withFormatting(Formatting.RED).withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Text.translatable("gui.spwp.description.ddos_error"))));
        }
    }

    private void checkTransfer(ButtonComponent transferButton, TextBoxComponent cardNumber, TextBoxComponent amount) {
        if (cardNumber.getText().length() == 5 && cardNumber.getText().matches("[0-9]+")) {
            try {
                int transferAmount = Integer.parseInt(amount.getText());
                transferButton.active(transferAmount > 0 && this.selectedCardBalance >= transferAmount);
            } catch (NumberFormatException e) {
                transferButton.active(false);
            }
        } else {
            transferButton.active(false);
        }
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        ScrollDropdownComponent dropdownComponent = new ScrollDropdownComponent(Sizing.fill(100), Sizing.fill(100), this.selectedCard == null ? Text.translatable("gui.spwp.description.choose_card") : Text.literal(this.selectedCard.name()), false);

        OverlayContainer<Component> addCardOverlay = Containers.overlay(Containers.verticalFlow(Sizing.fill(0), Sizing.fill(0)));
        addCardOverlay.child(addCardOverlay(addCardOverlay));
        addCardOverlay.zIndex(100);
        addCardOverlay.closeOnClick(false);

        OverlayContainer<Component> editCardsOverlay = Containers.overlay(Containers.verticalFlow(Sizing.fill(0), Sizing.fill(0)));
        editCardsOverlay.child(editCardsOverlay(editCardsOverlay));
        editCardsOverlay.zIndex(200);
        editCardsOverlay.closeOnClick(false);

        dropdownComponent.button(Text.translatable("gui.spwp.button.add_spm_card"), button -> {
            rootComponent.child(addCardOverlay);
        });

        dropdownComponent.button(Text.translatable("gui.spwp.button.edit"), button -> {
            rootComponent.child(editCardsOverlay);
        });

        TextBoxComponent cardNumber = Components.textBox(Sizing.fill(100));
        cardNumber.margins(Insets.top(4).add(0, 6, 0, 0));
        cardNumber.setMaxLength(5);
        cardNumber.text(transaction != null ? transaction.receiver() : "");

        TextBoxComponent amount = Components.textBox(Sizing.fill(100));
        amount.margins(Insets.top(4).add(0, 6, 0, 0));
        amount.setMaxLength(6);
        amount.text(transaction != null ? String.valueOf(transaction.amount()) : "");

        TextBoxComponent comment = Components.textBox(Sizing.fill(100));
        comment.margins(Insets.top(4).add(0, 6, 0, 0));
        comment.setMaxLength(45);
        comment.text(transaction != null ? transaction.comment() : "");

        ButtonComponent transferButton = Components.button(Text.translatable("gui.spwp.button.transfer"), button -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                SPWorldsApi.transfer(this.selectedCard, new Transaction(cardNumber.getText(), Integer.parseInt(amount.getText()), comment.getText().isEmpty() ? "Нет комментария - " + player.getGameProfile().getName() : comment.getText() + " - " + player.getGameProfile().getName()));
            } else {
                SPWorldsApi.transfer(this.selectedCard, new Transaction(cardNumber.getText(), Integer.parseInt(amount.getText()), comment.getText().isEmpty() ? "Нет комментария" : comment.getText()));
            }
            MessageModal.openMessage(Text.translatable("gui.spwp.title.success"), Text.translatable("gui.spwp.description.successfully_sent").append(this.selectedCard.name() + " " + amount.getText()).append(Text.translatable("gui.spwp.description.diamonds_to_the_card")).append(cardNumber.getText()));
        });

        cardNumber.onChanged().subscribe((string) -> {
            checkTransfer(transferButton, cardNumber, amount);
        });
        amount.onChanged().subscribe((string) -> {
            checkTransfer(transferButton, cardNumber, amount);
        });

        transferButton.active(selectedCard != null);
        transferButton.horizontalSizing(Sizing.fill(100));

        List<DatabaseCard> cards = SPWorldsPayClient.database.getSpmCards();
        for (DatabaseCard databaseCard : cards) {
            Card card = databaseCard.card();
            SPWorldsPayClient.asyncTasksService.addTask(() -> {
                return SPWorldsApi.getBalance(card);
            }, result -> {
                dropdownComponent.button(Text.literal(card.name() + "\n").append(getBalance((int) result)), button -> {
                    if ((int) result != -5298) {
                        this.selectedCard = card;
                        this.selectedCardBalance = (int) result;
                        checkTransfer(transferButton, cardNumber, amount);
                        dropdownComponent.title(Text.literal(card.name() + "\n").append(getBalance((int) result)));
                    }
                });
            }, exception -> {

            });
        }

        rootComponent
                .child(new NavigationBar("spmPage").navbar)
                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(90))
                        .child(Containers.horizontalFlow(Sizing.fill(60), Sizing.fill(70))
                                .child(Containers.verticalFlow(Sizing.fill(50), Sizing.fill(100))
                                        .child(Containers.verticalFlow(Sizing.fill(90), Sizing.fill(100))
                                                .child(dropdownComponent)
                                        )
                                )
                                .child(Containers.verticalFlow(Sizing.fill(50), Sizing.fill(100))
                                        .child(Containers.verticalFlow(Sizing.fill(90), Sizing.fill(100))
                                                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                                                .child(Components.label(Text.translatable("gui.spwp.title.transfer_spm")).margins(Insets.top(2)))
                                                                .child(Components.label(Text.translatable("gui.spwp.input.transfer.card_number")).horizontalTextAlignment(HorizontalAlignment.LEFT).horizontalSizing(Sizing.fill(100)).margins(Insets.top(16)))
                                                                .child(cardNumber)
                                                                .child(Components.label(Text.translatable("gui.spwp.input.transfer.amount")).horizontalTextAlignment(HorizontalAlignment.LEFT).horizontalSizing(Sizing.fill(100)))
                                                                .child(amount)
                                                                .child(Components.label(Text.translatable("gui.spwp.input.transfer.comment")).horizontalTextAlignment(HorizontalAlignment.LEFT).horizontalSizing(Sizing.fill(100)))
                                                                .child(comment)
                                                                .child(transferButton)
                                                                .margins(Insets.of(8))
                                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                        )
                                                        .surface(Surface.DARK_PANEL)
                                                )
                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                .verticalAlignment(VerticalAlignment.TOP)
                                        )
                                )
                        )
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .verticalAlignment(VerticalAlignment.CENTER)
                )
                .surface(Surface.VANILLA_TRANSLUCENT);
    }
}
