package ua.mei.spwp.client;

import com.google.gson.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.keybinding.v1.*;
import net.fabricmc.fabric.api.client.message.v1.*;
import net.kyori.adventure.text.serializer.gson.*;
import net.minecraft.client.*;
import net.minecraft.client.option.*;
import org.lwjgl.glfw.*;
import ua.mei.spwp.client.screens.or.*;
import ua.mei.spwp.util.*;

import java.util.concurrent.*;

public class SPWorldsPayClient implements ClientModInitializer {
    public static final String MOD_ID = "spwp";
    public static final AsyncTasksService tasks = new AsyncTasksService(Executors.newCachedThreadPool());
    public static SPWorldsPayDatabase database;

    private static KeyBinding openScreenKeyBinding;

    @Override
    public void onInitializeClient() {
        database = new SPWorldsPayDatabase();
        openScreenKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Ахуеть от кринжа", GLFW.GLFW_KEY_Z, "SPWorlds Pay"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                tasks.updateTasks();
            } catch (Exception e) {
                e.printStackTrace();
            }

            tasks.removeDone();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openScreenKeyBinding.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new ORScreen(client.player));
                }
            }
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            String messageContent = message.getString();

            if (messageContent.startsWith("[") && !(messageContent.contains("[СП]") || messageContent.contains("[СПм]")) && messageContent.endsWith("] Управление картой [Копир. токен] [Копир. айди]")) {
                Server server = Server.getServer();

                if (server != null) {
                    Gson gson = new Gson();
                    JsonObject jsonMessage = gson.fromJson(GsonComponentSerializer.gson().serialize(message.asComponent()), JsonObject.class);

                    try {
                        String cardName = jsonMessage.get("text").getAsString().replace("[", "").replace("] ", "");
                        String cardId = jsonMessage.get("extra").getAsJsonArray().get(0).getAsJsonObject().get("extra").getAsJsonArray().get(1).getAsJsonObject().get("clickEvent").getAsJsonObject().get("value").getAsString();
                        String cardToken = jsonMessage.get("extra").getAsJsonArray().get(0).getAsJsonObject().get("extra").getAsJsonArray().get(0).getAsJsonObject().get("clickEvent").getAsJsonObject().get("value").getAsString();

                        MinecraftClient client = MinecraftClient.getInstance();

                        if (client.player != null) {
                            client.setScreen(new ORAddCardScreen(client.player, cardName, cardId, cardToken));
                        }
                    } catch (NullPointerException ignored) {
                        // Что-бы не крашило
                    }
                }
            }
        });
    }
}
