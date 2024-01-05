package ua.mei.spwp.config;

import com.terraformersmc.modmenu.api.*;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.text.*;
import ua.mei.spwp.util.*;

public class ModMenuIntegration implements ModMenuApi {
    public static Screen createConfigScreen(Screen parent) {
        SPWorldsPayConfig config = SPWorldsPayConfig.getConfig();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("SPWorlds Pay"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Главное"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Внешний вид"))
                                .option(Option.<Theme>createBuilder()
                                        .name(Text.literal("Тема"))
                                        .binding(config.theme, () -> config.theme, value -> config.theme = value)
                                        .controller(enumOption -> EnumControllerBuilder.create(enumOption).enumClass(Theme.class).formatValue(value -> Text.literal(value.name())))
                                        .build()
                                )
                                .build()
                        ).build())
                .save(SPWorldsPayConfig.CONFIG_CLASS_HANDLER::save)
                .build()
                .generateScreen(parent);
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::createConfigScreen;
    }
}
