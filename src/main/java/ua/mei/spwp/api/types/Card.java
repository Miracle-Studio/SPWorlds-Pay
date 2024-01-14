package ua.mei.spwp.api.types;

import net.minecraft.util.*;

public record Card(int rowid, String name, Identifier texture, String id, String token, int balance) {
    public DatabaseCard database() {
        return new DatabaseCard(this.rowid, this.name, this.texture, this.id, this.token);
    }
}
