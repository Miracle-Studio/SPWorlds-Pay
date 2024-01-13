package ua.mei.spwp.api.types;

import net.minecraft.util.*;

import java.util.*;

public record DatabaseCard(int rowid, String name, Identifier texture, String id, String token) {
    public String getKey() {
        return Base64.getEncoder().encodeToString((this.id + ":" + this.token).getBytes());
    }
}
