package ua.mei.spwp.api.types;

import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import ua.mei.spwp.api.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.screens.or.*;
import ua.mei.spwp.client.screens.or.components.*;
import ua.mei.spwp.util.*;

import java.util.*;

public class Card {
    public int rowid;
    public String name;
    public Identifier texture;
    public String id;
    public String token;
    public Server server;
    public Integer balance = null;

    public Card(int rowid, String name, Identifier texture, String id, String token, Server server) {
        this.rowid = rowid;
        this.name = name;
        this.texture = texture;
        this.id = id;
        this.token = token;
        this.server = server;
    }

    public String getKey() {
        return Base64.getEncoder().encodeToString((this.id + ":" + this.token).getBytes());
    }

    public Card requestBalance() {
        this.balance = SPWorldsApi.requestBalance(this);
        return this;
    }

    public FakeSlot asSlot() {
        List<Text> tooltips = new ArrayList<>();
        tooltips.add(Text.literal(this.name).styled(style -> style.withColor(ORColorScheme.GRAY)));
        tooltips.add(Text.literal("• Баланс: ").styled(style -> style.withColor(ORColorScheme.GRAY)).append(Text.literal("загрузка...").styled(style -> style.withColor(ORColorScheme.DARK_GRAY))));
        tooltips.add(Text.literal("• Сервер: " + this.server.name()).styled(style -> style.withColor(ORColorScheme.GRAY)));

        FakeSlot slot = new FakeSlot(Registries.ITEM.get(this.texture).getDefaultStack(), tooltips);

        SPWorldsPayClient.tasks.addTask(this::requestBalance, result -> {
            tooltips.set(1, Text.literal("• Баланс: ").styled(style -> style.withColor(ORColorScheme.GRAY)).append(Text.literal((this.balance == null) ? "Ошибка!" : String.valueOf(this.balance)).styled(style -> style.withColor((this.balance == null) ? ORColorScheme.RED : ORColorScheme.GRAY))));
            slot.tooltip(tooltips);
        }, exception -> {

        });

        return slot;
    }
}
