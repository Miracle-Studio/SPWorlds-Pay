package ua.mei.spwp.client.screens.or;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.screens.or.components.*;

import java.util.*;

public class ORAddCardScreen extends BaseOwoScreen<FlowLayout> {
    public ClientPlayerEntity player;

    public String name;
    public String id;
    public String token;

    public ORAddCardScreen(ClientPlayerEntity player, String name, String id, String token) {
        this.player = player;

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
                .child(Components.texture(new Identifier(SPWorldsPayClient.MOD_ID, "textures/gui/or/add_card.png"), 0, 0, 176, 176, 256, 256))
                .child(Containers.verticalFlow(Sizing.fixed(162), Sizing.fixed(107))
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(18))
                                .child(new InvisibleButton(36, 18, btn -> {
                                    MinecraftClient.getInstance().setScreen(null);
                                }).tooltip(Text.literal("Отмена").styled(style -> style.withColor(ORColorScheme.RED))))
                                .child(new FakeSlot(new ItemStack(Items.DIAMOND), List.of(Text.literal(this.name))))
                                .child(new InvisibleButton(36, 18, btn -> {
                                    SPWorldsPayClient.database.addCard(this.name, this.id, this.token);
                                    MinecraftClient.getInstance().setScreen(null);
                                    this.player.sendMessage(Text.literal("\nКарта " + this.name + " успешно добавлена!\n").styled(style -> style.withColor(ORColorScheme.GREEN)));
                                }).tooltip(List.of(
                                        Text.literal("Добавить").styled(style -> style.withColor(ORColorScheme.GREEN)),
                                        Text.literal("Карта: ").formatted(Formatting.GRAY).append(Text.literal(this.name).formatted(Formatting.WHITE))
                                )))
                                .gap(18)
                                .margins(Insets.horizontal(18))
                        )
                        .child(new FakeInventory(this.player.getInventory()))
                        .margins(Insets.top(10))
                        .positioning(Positioning.relative(50, 50))
                )
        );
        rootComponent.horizontalAlignment(HorizontalAlignment.CENTER);
        rootComponent.verticalAlignment(VerticalAlignment.CENTER);
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT);
    }
}
