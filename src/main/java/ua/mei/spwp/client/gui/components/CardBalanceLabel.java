package ua.mei.spwp.client.gui.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.*;
import ua.mei.spwp.api.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;

public class CardBalanceLabel extends LabelComponent {
    public CardBalanceLabel(DatabaseCard card) {
        super(Text.translatable("gui.spwp.description.balance").append("0"));

        SPWorldsPayClient.asyncTasksService.addTask(() -> {
            return SPWorldsApi.getBalance(card.card());
        }, result -> {
            SPWorldsPayClient.LOGGER.info(String.valueOf((int) result));

            if ((int) result != -5298) {
                this.text = Text.translatable("gui.spwp.description.balance").append(String.valueOf((int) result));
            } else {
                this.text = Text.translatable("gui.spwp.description.balance_error");
                this.tooltip(Text.translatable("gui.spwp.description.ddos_error"));
                this.color(Color.ofArgb(0xFFFF3333));
            }

            this.notifyParentIfMounted();
        }, exception -> {

        });
    }
}
