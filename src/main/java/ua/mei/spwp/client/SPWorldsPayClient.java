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
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.network.*;
import net.minecraft.client.option.*;
import net.minecraft.client.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.lwjgl.glfw.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.gui.*;
import ua.mei.spwp.client.gui.MessageScreen;
import ua.mei.spwp.client.gui.bank.*;
import ua.mei.spwp.config.*;
import ua.mei.spwp.util.*;

import java.awt.*;
import java.util.concurrent.*;

public class SPWorldsPayClient implements ClientModInitializer {
    // public static final ua.mei.spwp.config.SPWorldsPayConfig config = ua.mei.spwp.config.SPWorldsPayConfig.createAndLoad();
    public static final DatabaseWrapper database = new DatabaseWrapper();
    public static final AsyncTasksService asyncTasksService = new AsyncTasksService(Executors.newCachedThreadPool());
    private static KeyBinding bankGuiKeyBinding;

    @Override
    public void onInitializeClient() {
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
                if (MinecraftClient.getInstance().getCurrentServerEntry() != null) {
                    if (MinecraftClient.getInstance().getCurrentServerEntry().address.equals("sp.spworlds.ru")) {
                        MinecraftClient.getInstance().setScreen(new SPPage());
                    } else if (MinecraftClient.getInstance().getCurrentServerEntry().address.equals("spm.spworlds.ru")) {
                        MinecraftClient.getInstance().setScreen(new SPMPage());
                    } else {
                        MessageScreen.openMessage(Text.translatable("gui.spwp.title.error"), Text.translatable("gui.spwp.description.join_to_server"));
                    }
                } else {
                    MessageScreen.openMessage(Text.translatable("gui.spwp.title.error"), Text.translatable("gui.spwp.description.join_to_server"));
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
                            } else {
                                return ActionResult.PASS;
                            }
                        }
                    }
                }
            }

            return ActionResult.PASS;
        });

        ClientReceiveMessageEvents.GAME.register(((message, overlay) -> {
            if (MinecraftClient.getInstance().getCurrentServerEntry() != null) {
                if (MinecraftClient.getInstance().getCurrentServerEntry().address.equals("sp.spworlds.ru") || MinecraftClient.getInstance().getCurrentServerEntry().address.equals("spm.spworlds.ru")) {
                    Gson gson = new Gson();
                    JsonObject jsonMessage = gson.fromJson(GsonComponentSerializer.gson().serialize(message.asComponent()), JsonObject.class);
                    String stringMessage = message.getString();

                    if (stringMessage.startsWith("[") && (!stringMessage.contains("[СП]") || !stringMessage.contains("[СПм]")) && stringMessage.endsWith("] Управление картой [Копир. токен] [Копир. айди]")) {
                        String cardName = jsonMessage.get("text").getAsString().replace("[", "").replace("] ", "");
                        String cardId = jsonMessage.get("extra").getAsJsonArray().get(0).getAsJsonObject().get("extra").getAsJsonArray().get(1).getAsJsonObject().get("clickEvent").getAsJsonObject().get("value").getAsString();
                        String cardToken = jsonMessage.get("extra").getAsJsonArray().get(0).getAsJsonObject().get("extra").getAsJsonArray().get(0).getAsJsonObject().get("clickEvent").getAsJsonObject().get("value").getAsString();

                        Card newCard = new Card(cardName, cardId, cardToken);
                        MinecraftClient.getInstance().setScreen(new AddCardScreen(newCard));
                    }
                }
            }
        }));
    }

    private boolean tryParseSignPayment(SignBlockEntity signBlockEntity, PlayerEntity player) {
        if (!signBlockEntity.getTextFacing(player).getMessage(0, false).getString().startsWith("#SPWP")) {
            return false;
        }
        if (MinecraftClient.getInstance().getCurrentServerEntry() == null) {
            return false;
        }
        if (!MinecraftClient.getInstance().getCurrentServerEntry().address.equals("sp.spworlds.ru") || !MinecraftClient.getInstance().getCurrentServerEntry().address.equals("spm.spworlds.ru")) {
            return false;
        }

        try {
            String cardNumber = signBlockEntity.getTextFacing(player).getMessage(1, false).getString();
            int amount = Integer.parseInt(signBlockEntity.getTextFacing(player).getMessage(2, false).getString());
            String comment = signBlockEntity.getTextFacing(player).getMessage(3, false).getString();

            Transaction transaction = new Transaction(cardNumber, amount, comment);

            if (MinecraftClient.getInstance().getCurrentServerEntry().address.equals("sp.spworlds.ru")) {
                MinecraftClient.getInstance().setScreen(new SPPage(transaction));
            } else if (MinecraftClient.getInstance().getCurrentServerEntry().address.equals("spm.spworlds.ru")) {
                MinecraftClient.getInstance().setScreen(new SPMPage(transaction));
            }

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
