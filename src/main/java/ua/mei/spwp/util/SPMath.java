package ua.mei.spwp.util;

import net.minecraft.client.*;
import net.minecraft.client.network.*;
import ua.mei.spwp.client.gui.*;

public class SPMath {
    public static Server server() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();

            if (server != null) {
                switch (server.address) {
                    case "sp.spworlds.ru" -> {
                        return Server.SP;
                    }
                    case "spm.spworlds.ru" -> {
                        return Server.SPm;
                    }
                    case "pl.spworlds.ru" -> {
                        return Server.PoopLand;
                    }
                }
            }
        }

        return Server.OTHER;
    }
}
