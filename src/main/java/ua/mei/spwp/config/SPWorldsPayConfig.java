package ua.mei.spwp.config;

import com.google.gson.*;
import dev.isxander.yacl3.config.v2.api.*;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.serializer.*;
import net.fabricmc.loader.api.*;
import net.minecraft.util.*;
import ua.mei.spwp.util.*;

public class SPWorldsPayConfig {
    public static ConfigClassHandler<SPWorldsPayConfig> CONFIG_CLASS_HANDLER = ConfigClassHandler.createBuilder(SPWorldsPayConfig.class)
            .id(new Identifier("spwp", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("spwp-config.json"))
                    .setJson5(true)
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .build()
            ).build();

    @SerialEntry
    public Theme theme = Theme.Essential;

    public static SPWorldsPayConfig getConfig() {
        return CONFIG_CLASS_HANDLER.instance();
    }

    public static void load() {
        CONFIG_CLASS_HANDLER.load();
    }
}
