package ua.mei.spwp;

import net.fabricmc.api.*;
import net.fabricmc.loader.api.*;
import org.slf4j.*;

public class SPWorldsPay implements ModInitializer {
    public static final String MOD_ID = "spwp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            LOGGER.warn(" ======================================== ");
            LOGGER.warn(" ⚠⚠⚠ SPWorlds Pay only client-side!!! ⚠⚠⚠ ");
            LOGGER.warn(" ⚠⚠⚠ SPWorlds Pay только на клиент!!! ⚠⚠⚠ ");
            LOGGER.warn(" ======================================== ");
        }
    }
}
