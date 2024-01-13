package ua.mei.spwp.api;

import com.google.gson.*;
import ua.mei.spwp.api.types.*;

import java.net.*;
import java.net.http.*;

public class SPWorldsApi {
    private static final String API_URL = "https://spworlds.ru/api/public/";
    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static Card requestBalance(DatabaseCard card) {
        JsonObject json = request(card, "card", "GET", null);

        return (json != null && json.has("balance")) ? new Card(card.rowid(), card.name(), card.texture(), card.id(), card.token(), json.get("balance").getAsInt()) : null;
    }

    public static JsonObject request(DatabaseCard card, String endpoint, String method, String requestBody) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + endpoint))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Authorization", "Bearer " + card.getKey())
                    .method(method, (requestBody == null) ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(requestBody));

            HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }
}
