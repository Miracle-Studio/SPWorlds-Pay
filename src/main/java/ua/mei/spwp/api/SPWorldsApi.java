package ua.mei.spwp.api;

import com.google.gson.*;
import net.minecraft.text.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.gui.*;

import java.net.*;
import java.net.http.*;

public class SPWorldsApi {
    public static final String API_URL = "https://spworlds.ru/api/public/";
    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static int getBalance(Card card) {
        JsonObject json = request(card, "card", "GET", null);
        if (json != null && json.get("balance") != null) {
            return json.get("balance").getAsInt();
        } else {
            return -5298;
        }
    }

    public static void transfer(Card card, Transaction transaction) {
        String json = gson.toJson(transaction);

        request(card, "transactions", "POST", json);
    }

    public static JsonObject request(Card card, String endpoint, String method, String requestBody) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + endpoint))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Authorization", "Bearer " + card.getBase64Key())
                    .method(method, requestBody == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(requestBody));

            HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), JsonObject.class);
        } catch (Exception e) {
            MessageScreen.openMessage(Text.translatable("gui.spwp.title.error"), Text.literal(e.getMessage()));
            return null;
        }
    }
}
