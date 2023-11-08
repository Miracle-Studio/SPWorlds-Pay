package ua.mei.spwp.client.gui;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.client.gui.components.*;

public class MessageScreen extends BaseOwoScreen<FlowLayout> {
    public MutableText title;
    public MutableText message;

    public MessageScreen(MutableText title, MutableText message) {
        this.title = title;
        this.message = message;
    }

    public static void openMessage(MutableText title, MutableText message) {
        MinecraftClient.getInstance().setScreen(new MessageScreen(title, message));
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Containers.verticalFlow(Sizing.fill(35), Sizing.content())
                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                        .child(Components.label(this.title)
                                                .color(Color.ofArgb(EssentialColorScheme.MODAL_TEXT))
                                                .horizontalTextAlignment(HorizontalAlignment.CENTER)
                                                .shadow(true)
                                                .horizontalSizing(Sizing.fill(100))
                                        )
                                        .child(Components.label(this.message)
                                                .color(Color.ofArgb(EssentialColorScheme.MODAL_TEXT))
                                                .horizontalTextAlignment(HorizontalAlignment.CENTER)
                                                .shadow(true)
                                                .horizontalSizing(Sizing.fill(100))
                                        )
                                        .gap(13)
                                )
                                .child(new EssentialButton(Text.translatable("gui.spwp.button.ok"), button -> {
                                    MinecraftClient.getInstance().setScreen(null);
                                }).horizontalSizing(Sizing.fill(100)))
                                .gap(18)
                                .margins(Insets.of(17))
                        )
                        .surface(Surface.flat(EssentialColorScheme.BACKGROUND).and(Surface.outline(EssentialColorScheme.MODAL_OUTLINE)))
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.VANILLA_TRANSLUCENT);
    }
}
