package ua.mei.spwp.client;

import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.keybinding.v1.*;
import net.minecraft.client.option.*;
import org.lwjgl.glfw.*;
import ua.mei.spwp.client.screens.vanilla.*;

public class SPWorldsPayClient implements ClientModInitializer {
    public static final String MOD_ID = "spwp";

    private static KeyBinding openScreenKeyBinding;

    @Override
    public void onInitializeClient() {
        openScreenKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("Ахуеть от кринжа", GLFW.GLFW_KEY_Z, "SPWorlds Pay"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openScreenKeyBinding.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new VanillaScreen(client.player.getInventory()));
                }
            }
        });
    }
}
