package ua.mei.spwp.client.screens.vanilla;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.screens.vanilla.components.*;

import java.util.*;

public class VanillaAddCardScreen extends BaseOwoScreen<FlowLayout> {
    public PlayerInventory playerInventory;

    public String name;
    public String id;
    public String token;

    public VanillaAddCardScreen(PlayerInventory playerInventory, String name, String id, String token) {
        this.playerInventory = playerInventory;

        this.name = name;
        this.id = id;
        this.token = token;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Containers.verticalFlow(Sizing.fixed(176), Sizing.fixed(131))
                .child(Components.texture(new Identifier(SPWorldsPayClient.MOD_ID, "textures/gui/vanilla/add_card.png"), 0, 0, 176, 176, 256, 256))
                .child(Containers.verticalFlow(Sizing.fixed(162), Sizing.fixed(107))
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18))
                                .child(new InvisibleButton(36, 18, btn -> {

                                }).tooltip(Text.literal("Отмена").styled(style -> style.withColor(0xD92625))))
                                .child(new FakeSlot(new ItemStack(Items.DIAMOND), List.of(Text.literal(this.name))))
                                .child(new InvisibleButton(36, 18, btn -> {

                                }).tooltip(List.of(
                                        Text.literal("Добавить").styled(style -> style.withColor(0x83CA16)),
                                        Text.literal("Карта: ").formatted(Formatting.GRAY).append(Text.literal(this.name).formatted(Formatting.WHITE))
                                )))
                                .gap(18)
                                .margins(Insets.horizontal(18))
                        )
                        .child(new FakeInventory(this.playerInventory))
                        .margins(Insets.top(10))
                        .positioning(Positioning.relative(50, 50))
                )
        );
        rootComponent.horizontalAlignment(HorizontalAlignment.CENTER);
        rootComponent.verticalAlignment(VerticalAlignment.CENTER);
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    }
}
