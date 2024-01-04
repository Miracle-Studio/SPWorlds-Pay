package ua.mei.spwp.config;

import mrnavastar.sqlib.database.*;
import net.fabricmc.loader.api.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;

import java.sql.*;
import java.util.*;

public class DatabaseWrapper {
    public SQLiteDatabase database;

    public DatabaseWrapper() {
        database = new SQLiteDatabase(SPWorldsPayClient.MOD_ID, "spwp-cards", FabricLoader.getInstance().getConfigDir().toString());
        database.executeCommand("CREATE TABLE IF NOT EXISTS spCards (rowId INTEGER NOT NULL PRIMARY KEY, name TEXT, id TEXT, token TEXT)", false);
        database.executeCommand("CREATE TABLE IF NOT EXISTS spmCards (rowId INTEGER NOT NULL PRIMARY KEY, name TEXT, id TEXT, token TEXT)", false);
    }

    public List<DatabaseCard> getSpCards() {
        try {
            PreparedStatement statement = database.executeCommand("SELECT * FROM spCards ORDER BY rowId", false);
            ResultSet result = statement.getResultSet();
            List<DatabaseCard> cardList = new ArrayList<>();

            while (result.next()) {
                cardList.add(new DatabaseCard(result.getInt("rowId"), new Card(result.getString("name"), result.getString("id"), result.getString("token"))));
            }

            statement.close();

            return cardList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DatabaseCard> getSpmCards() {
        try {
            PreparedStatement statement = database.executeCommand("SELECT * FROM spmCards ORDER BY rowId", false);
            ResultSet result = statement.getResultSet();
            List<DatabaseCard> cardList = new ArrayList<>();

            while (result.next()) {
                cardList.add(new DatabaseCard(result.getInt("rowId"), new Card(result.getString("name"), result.getString("id"), result.getString("token"))));
            }

            statement.close();

            return cardList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSpCard(Card card) {
        database.executeCommand("INSERT INTO spCards (name, id, token) VALUES (?, ?, ?)", true, card.name(), card.id(), card.token());
    }

    public void addSpmCard(Card card) {
        database.executeCommand("INSERT INTO spmCards (name, id, token) VALUES (?, ?, ?)", true, card.name(), card.id(), card.token());
    }

    public DatabaseCard getSpCard(int index) {
        try {
            PreparedStatement statement = database.executeCommand("SELECT * FROM spCards WHERE rowId = ?", false, index);
            ResultSet result = statement.getResultSet();
            DatabaseCard card = null;

            while (result.next()) {
                card = new DatabaseCard(result.getInt("rowId"), new Card(result.getString("name"), result.getString("id"), result.getString("token")));
            }

            statement.close();

            return card;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseCard getSpmCard(int index) {
        try {
            PreparedStatement statement = database.executeCommand("SELECT * FROM spmCards WHERE rowId = ?", false, index);
            ResultSet result = statement.getResultSet();
            DatabaseCard card = null;

            while (result.next()) {
                card = new DatabaseCard(result.getInt("rowId"), new Card(result.getString("name"), result.getString("id"), result.getString("token")));
            }

            statement.close();

            return card;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editSpCard(String newName, int index) {
        database.executeCommand("UPDATE spCards SET name = ? WHERE rowId = ?", true, newName, index);
    }

    public void editSpmCard(String newName, int index) {
        database.executeCommand("UPDATE spmCards SET name = ? WHERE rowId = ?", true, newName, index);
    }

    public void deleteSpCard(int index) {
        database.executeCommand("DELETE FROM spCards WHERE rowId = ?", true, index);
    }

    public void deleteSpmCard(int index) {
        database.executeCommand("DELETE FROM spmCards WHERE rowId = ?", true, index);
    }
}
