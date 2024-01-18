package ua.mei.spwp.client;

import me.mrnavastar.sqlib.*;
import me.mrnavastar.sqlib.database.*;
import me.mrnavastar.sqlib.sql.*;
import net.fabricmc.loader.api.*;
import net.minecraft.util.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.util.*;

import java.util.*;
import java.util.stream.*;

public class SPWorldsPayDatabase {
    private final Table spCards;
    private final Table spmCards;

    public SPWorldsPayDatabase() {
        Database database = new SQLiteDatabase("spwp-cards", FabricLoader.getInstance().getConfigDir().toString());

        this.spCards = database.createTable(SPWorldsPayClient.MOD_ID, "spCards")
                .setAutoIncrement()
                .addColumn("name", SQLDataType.STRING)
                .addColumn("texture", SQLDataType.IDENTIFIER)
                .addColumn("cardId", SQLDataType.STRING)
                .addColumn("token", SQLDataType.STRING)
                .finish();
        this.spmCards = database.createTable(SPWorldsPayClient.MOD_ID, "spmCards")
                .setAutoIncrement()
                .addColumn("name", SQLDataType.STRING)
                .addColumn("texture", SQLDataType.IDENTIFIER)
                .addColumn("cardId", SQLDataType.STRING)
                .addColumn("token", SQLDataType.STRING)
                .finish();
    }

    public List<Card> getCards(Server server) {
        List<DataContainer> dataContainers = switch(server) {
            case SP -> spCards.getDataContainers();
            case SPm -> spmCards.getDataContainers();
        };

        return dataContainers.stream()
                .map(data -> new Card(data.getIdAsInt(), data.getString("name"), data.getIdentifier("texture"), data.getString("cardId"), data.getString("token")))
                .collect(Collectors.toList());
    }
    public List<Card> getCards() {
        Server server = Server.getServer();

        return (server != null) ? getCards(server) : Collections.emptyList();
    }

    public Card getCard(Server server, int id) {
        DataContainer data = switch(server) {
            case SP -> spCards.getOrCreateDataContainer(id);
            case SPm -> spmCards.getOrCreateDataContainer(id);
        };

        return (data != null) ? new Card(data.getIdAsInt(), data.getString("name"), data.getIdentifier("texture"), data.getString("cardId"), data.getString("token")) : null;
    }

    public Card getCard(int id) {
        Server server = Server.getServer();

        return (server != null) ? getCard(server, id) : null;
    }

    public void addCard(Server server, String name, Identifier texture, String id, String token) {
        switch(server) {
            case SP -> spCards.beginTransaction();
            case SPm -> spmCards.beginTransaction();
        }

        DataContainer data = switch(server) {
            case SP -> spCards.createDataContainerAutoID();
            case SPm -> spmCards.createDataContainerAutoID();
        };
        data.put("name", name);
        data.put("texture", texture);
        data.put("cardId", id);
        data.put("token", token);

        switch(server) {
            case SP -> spCards.endTransaction();
            case SPm -> spmCards.endTransaction();
        }
    }

    public void addCard(String name, String id, String token) {
        Server server = Server.getServer();

        if (server != null) {
            addCard(server, name, new Identifier("minecraft", "diamond"), id, token);
        }
    }
}
