package ua.mei.spwp.client.gui.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.loader.api.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.gui.bank.*;

import java.util.*;

public class NavigationBar {
    private static final int GRADIENT_START = 0xFF000000;
    private static final int GRADIENT_END = 0x00000000;
    public Component navbar;

    public NavigationBar(String currentScreen) {
        List<Component> buttons = new ArrayList<>();

        ButtonComponent spButton = Components.button(Text.translatable("gui.spwp.button.sp"), button -> {
            MinecraftClient.getInstance().setScreen(new SPPage());
        }).active(!currentScreen.equals("spPage"));

        ButtonComponent spmButton = Components.button(Text.translatable("gui.spwp.button.spm"), button -> {
            MinecraftClient.getInstance().setScreen(new SPMPage());
        }).active(!currentScreen.equals("spmPage"));
        spmButton.margins(Insets.left(14));

        buttons.add(spButton);
        buttons.add(spmButton);


        navbar = Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                .child(
                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                .child(
                                        Containers.horizontalFlow(Sizing.fill(25), Sizing.content())
                                                .child(
                                                        Components.label(Text.literal("SPWorlds Pay " + FabricLoader.getInstance().getModContainer(SPWorldsPayClient.MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString()))
                                                                .shadow(true)
                                                )
                                                .horizontalAlignment(HorizontalAlignment.LEFT)
                                                .verticalAlignment(VerticalAlignment.CENTER)
                                )
                                .child(
                                        Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
                                                .children(buttons)
                                                .margins(Insets.vertical(8))
                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                .verticalAlignment(VerticalAlignment.CENTER)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(25), Sizing.content()))
                                .margins(Insets.horizontal(14))
                                .verticalAlignment(VerticalAlignment.CENTER)
                )
                .child(shadow())
                .allowOverflow(true)
                .surface(Surface.OPTIONS_BACKGROUND)
                .verticalAlignment(VerticalAlignment.CENTER);
    }

    public static Component shadow() {
        FlowLayout shadow = Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(5));

        shadow.surface((drawContext, component) -> {
            drawContext.drawGradientRect(component.x(), component.y(), component.width(), component.height(), GRADIENT_START, GRADIENT_START, GRADIENT_END, GRADIENT_END);
        });

        shadow.positioning(Positioning.across(0, 100));
        shadow.zIndex(0);

        return shadow;
    }
}
