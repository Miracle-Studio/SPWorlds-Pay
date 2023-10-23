package ua.mei.spwp.api.types;

import java.util.*;

public record Card(String name, String id, String token) {
    public String getKey() {
        return this.id + ":" + this.token;
    }

    public String getBase64Key() {
        return Base64.getEncoder().encodeToString(getKey().getBytes());
    }
}
