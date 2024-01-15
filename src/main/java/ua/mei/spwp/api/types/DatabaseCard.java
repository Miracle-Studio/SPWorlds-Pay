package ua.mei.spwp.api.types;

import net.minecraft.client.gui.tooltip.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import ua.mei.spwp.api.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.screens.or.*;
import ua.mei.spwp.client.screens.or.components.*;

import java.util.*;

public record DatabaseCard(int rowid, String name, Identifier texture, String id, String token) {
    public String getKey() {
        return Base64.getEncoder().encodeToString((this.id + ":" + this.token).getBytes());
    }

    public Card card() {
        return SPWorldsApi.requestBalance(this);
    }

    public FakeSlot asSlot() {
        List<Text> tooltips = new ArrayList<>();
        tooltips.add(Text.literal(this.name).styled(style -> style.withColor(ORColorScheme.GRAY)));
        tooltips.add(Text.literal("• Баланс: загрузка...").styled(style -> style.withColor(ORColorScheme.DARK_GRAY)));

        FakeSlot slot = new FakeSlot(Registries.ITEM.get(this.texture).getDefaultStack(), tooltips);

        SPWorldsPayClient.tasks.addTask(this::card, result -> {
            Card card = (Card) result;

            tooltips.set(1, Text.literal("• Баланс: " + card.balance()).styled(style -> style.withColor(ORColorScheme.GRAY)));
            slot.tooltip(tooltips);
            slot.notifyParent();
        }, exception -> {

        });

        return slot;
    }
}
