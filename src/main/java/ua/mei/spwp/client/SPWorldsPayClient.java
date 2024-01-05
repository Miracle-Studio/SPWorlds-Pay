package ua.mei.spwp.client;

import com.google.gson.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.keybinding.v1.*;
import net.fabricmc.fabric.api.client.message.v1.*;
import net.fabricmc.fabric.api.event.player.*;
import net.kyori.adventure.text.serializer.gson.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.client.option.*;
import net.minecraft.client.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import org.lwjgl.glfw.*;
import org.slf4j.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.gui.essential.*;
import ua.mei.spwp.client.gui.old.*;
import ua.mei.spwp.config.*;
import ua.mei.spwp.util.*;

import java.util.concurrent.*;

public class SPWorldsPayClient implements ClientModInitializer {
    public static final DatabaseWrapper database = new DatabaseWrapper();
    public static final AsyncTasksService asyncTasksService = new AsyncTasksService(Executors.newCachedThreadPool());
    public static final String MOD_ID = "spwp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static KeyBinding bankGuiKeyBinding;

    @Override
    public void onInitializeClient() {
        SPWorldsPayConfig.load();

        bankGuiKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.spwp.open_wallet_screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "SPWorlds Pay"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                asyncTasksService.updateTasks();
            } catch (Exception e) {
                e.printStackTrace();
            }

            asyncTasksService.removeDone();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (bankGuiKeyBinding.wasPressed()) {
                Server server = SPMath.server();

                if (server != Server.OTHER) {
                    if (SPWorldsPayConfig.getConfig().theme == Theme.Essential) {
                        MinecraftClient.getInstance().setScreen(new NewPage(server));
                    } else {
                        if (server == Server.SP) {
                            MinecraftClient.getInstance().setScreen(new SPPage());
                        } else {
                            MinecraftClient.getInstance().setScreen(new SPMPage());
                        }
                    }
                }
            }
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient()) {
                return ActionResult.PASS;
            }

            BlockState block = world.getBlockState(hitResult.getBlockPos());

            if (block.getBlock() instanceof SignBlock || block.getBlock() instanceof WallSignBlock || block.getBlock() instanceof HangingSignBlock || block.getBlock() instanceof WallHangingSignBlock) {
                SignBlockEntity signBlockEntity = (SignBlockEntity) world.getBlockEntity(hitResult.getBlockPos());
                if (signBlockEntity != null) {
                    ClientPlayerEntity playerEntity = MinecraftClient.getInstance().player;
                    if (playerEntity != null) {
                        if (!playerEntity.input.sneaking) {
                            if (tryParseSignPayment(signBlockEntity, player)) {
                                return ActionResult.FAIL;
                            }
                        }
                    }
                }
            }

            return ActionResult.PASS;
        });

        ClientReceiveMessageEvents.GAME.register(((message, overlay) -> {
            if (SPMath.server() != Server.OTHER) {
                Gson gson = new Gson();
                JsonObject jsonMessage = gson.fromJson(GsonComponentSerializer.gson().serialize(message.asComponent()), JsonObject.class);
                String stringMessage = message.getString();

                if (stringMessage.startsWith("[") && (!stringMessage.startsWith("[СП]") || !stringMessage.startsWith("[СПм]")) && stringMessage.endsWith("] Управление картой [Копир. токен] [Копир. айди]")) {
                    try {
                        String cardName = jsonMessage.get("text").getAsString().replace("[", "").replace("] ", "");
                        String cardId = jsonMessage.get("extra").getAsJsonArray().get(0).getAsJsonObject().get("extra").getAsJsonArray().get(1).getAsJsonObject().get("clickEvent").getAsJsonObject().get("value").getAsString();
                        String cardToken = jsonMessage.get("extra").getAsJsonArray().get(0).getAsJsonObject().get("extra").getAsJsonArray().get(0).getAsJsonObject().get("clickEvent").getAsJsonObject().get("value").getAsString();

                        Card newCard = new Card(cardName, cardId, cardToken);

                        if (SPWorldsPayConfig.getConfig().theme == Theme.Essential) {
                            MinecraftClient.getInstance().setScreen(new EssentialAddCardModal(newCard));
                        } else {
                            MinecraftClient.getInstance().setScreen(new OldAddCardModal(newCard));
                        }
                    } catch (NullPointerException ignored) {
                        // Если игроки додумаются каким-то способом обойти проверку
                        // на реальное сообщение с токеном и айди, то майнкрафт не крашило
                    }
                }
            }
        }));
    }

    private boolean tryParseSignPayment(SignBlockEntity signBlockEntity, PlayerEntity player) {
        if (!signBlockEntity.getTextFacing(player).getMessage(0, false).getString().startsWith("#SPWP")) {
            return false;
        }

        Server server = SPMath.server();

        if (server == Server.OTHER) {
            return false;
        }

        try {
            String cardNumber = signBlockEntity.getTextFacing(player).getMessage(1, false).getString();
            int amount = Integer.parseInt(signBlockEntity.getTextFacing(player).getMessage(2, false).getString());
            String comment = signBlockEntity.getTextFacing(player).getMessage(3, false).getString();

            if (amount > 0 && cardNumber.matches("[0-9]+") && cardNumber.length() == 5) {
                Transaction transaction = new Transaction(cardNumber, amount, comment);

                if (server == Server.SP) {
                    MinecraftClient.getInstance().setScreen(new SPPage(transaction));
                } else {
                    MinecraftClient.getInstance().setScreen(new SPMPage(transaction));
                }

                return true;
            }

            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
