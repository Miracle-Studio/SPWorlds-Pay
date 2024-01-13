package ua.mei.spwp.client;

import me.mrnavastar.sqlib.*;
import me.mrnavastar.sqlib.database.*;
import me.mrnavastar.sqlib.sql.*;
import net.fabricmc.loader.api.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.util.*;

import java.util.*;
import java.util.stream.*;

public class SPWorldsPayDatabase {
    private final Database database;
    private final Table spCards;
    private final Table spmCards;

    public SPWorldsPayDatabase() {
        this.database = new SQLiteDatabase("spwp-cards", FabricLoader.getInstance().getConfigDir().toString());

        this.spCards = this.database.createTable(SPWorldsPayClient.MOD_ID, "spCards")
                .setAutoIncrement()
                .addColumn("name", SQLDataType.STRING)
                .addColumn("texture", SQLDataType.IDENTIFIER)
                .addColumn("id", SQLDataType.STRING)
                .addColumn("token", SQLDataType.STRING)
                .finish();
        this.spmCards = this.database.createTable(SPWorldsPayClient.MOD_ID, "spmCards")
                .setAutoIncrement()
                .addColumn("name", SQLDataType.STRING)
                .addColumn("texture", SQLDataType.IDENTIFIER)
                .addColumn("id", SQLDataType.STRING)
                .addColumn("token", SQLDataType.STRING)
                .finish();
    }

    public List<DatabaseCard> getCards(Server server) {
        List<DataContainer> dataContainers = switch(server) {
            case SP -> spCards.getDataContainers();
            case SPm -> spmCards.getDataContainers();
            case OTHER -> Collections.emptyList();
        };

        return dataContainers.stream()
                .map(data -> new DatabaseCard(data.getIdAsInt(), data.getString("name"), data.getIdentifier("texture"), data.getString("id"), data.getString("token")))
                .collect(Collectors.toList());
    }
    public List<DatabaseCard> getCards() {
        Server server = Server.getServer();

        return (server != Server.OTHER) ? getCards(server) : Collections.emptyList();
    }
}
