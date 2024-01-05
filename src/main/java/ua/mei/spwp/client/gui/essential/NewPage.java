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
import ua.mei.spwp.client.gui.essential.components.*;
import ua.mei.spwp.util.*;

public class NewPage extends BaseOwoScreen<FlowLayout> {
    public Server server;
    public DatabaseCard card = null;

    public int oldGuiScale;
    public boolean closing = false;

    public NewPage(Server server) {
        this.oldGuiScale = MinecraftClient.getInstance().options.getGuiScale().getValue();
        this.server = server;
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

    @Override
    protected void build(FlowLayout rootComponent) {
        EssentialScrollContainer cardList = new EssentialScrollContainer(ScrollContainer.ScrollDirection.VERTICAL, Sizing.fill(100), Sizing.fill(100), new CardList(this.server, card -> {
            this.card = card;
        }));

        ServerList serverList = new ServerList(this.server, server -> {
            this.card = null;
            this.server = server;

            cardList.child(new CardList(server, card -> {
                this.card = card;
            }));
        });
        serverList.padding(Insets.of(10, 10, 12, 12));

        EssentialTextBox number = new EssentialTextBox(Sizing.fill(100), Text.literal("Введите номер"));
        number.textBoxComponent.setMaxLength(5);
        EssentialTextBox amount = new EssentialTextBox(Sizing.fill(100), Text.literal("Введите сумму"));
        amount.textBoxComponent.setMaxLength(6);
        EssentialTextBox comment = new EssentialTextBox(Sizing.fill(100), Text.literal("Введите комментарий"));
        comment.textBoxComponent.setMaxLength(45);

        EssentialFlatButton transferButton = new EssentialFlatButton(Text.literal("Перевести"), button -> {
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
                                        .child(Components.label(Text.literal("Кошелёк")).color(Color.ofArgb(EssentialColorScheme.SCREEN_TITLE)).shadow(true).margins(Insets.of(11, 0, 13, 0)))
                                        .surface(EssentialSurface.ESSENTIAL_NAV_LEFT)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(75), Sizing.fill(100))
                                        .child(Components.label(Text.literal("Переводы")).color(Color.ofArgb(EssentialColorScheme.SCREEN_TITLE)).shadow(true).margins(Insets.of(11, 0, 10, 0)))
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
