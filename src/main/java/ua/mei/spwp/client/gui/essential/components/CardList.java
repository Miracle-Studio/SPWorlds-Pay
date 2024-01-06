package ua.mei.spwp.client.gui.essential.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.util.*;

import java.util.*;
import java.util.function.*;

public class CardList extends FlowLayout {
    public List<CardButton> cardList = new ArrayList<>();

    public Consumer<DatabaseCard> onChange;
    public Consumer<DatabaseCard> onDelete;
    public Server server;

    public CardList(Server server, Consumer<DatabaseCard> onChange, Consumer<DatabaseCard> onDelete) {
        super(Sizing.fill(100), Sizing.content(), Algorithm.VERTICAL);

        this.child(Components.box(Sizing.fill(100), Sizing.fixed(5)).color(Color.ofArgb(0x00000000)));
        this.margins(Insets.right(3));

        this.onChange = onChange;
        this.onDelete = onDelete;
        this.server = server;

        initCards();
    }

    private void initCards() {
        List<DatabaseCard> cards = switch(this.server) {
            case SP -> SPWorldsPayClient.database.getSpCards();
            case SPm -> SPWorldsPayClient.database.getSpmCards();
            case OTHER -> new ArrayList<>();
        };

        this.child(Components.box(Sizing.fill(100), Sizing.fixed(5)).color(Color.ofArgb(0x00000000)));

        for (DatabaseCard card : cards) {
            CardButton cardButton = new CardButton(card, databaseCard -> {}, delete -> { this.onDelete.accept(card); });

            if (cardList.isEmpty()) {
                this.onChange.accept(card);
                cardButton.selected = true;
            }

            cardButton.onPress(databaseCard -> {
                this.onChange.accept(card);

                for (CardButton cardBtn : cardList) {
                    cardBtn.selected = false;
                }

                cardButton.selected = true;
            });

            cardList.add(cardButton);
            this.children.add(cardButton);
        }
    }
}
