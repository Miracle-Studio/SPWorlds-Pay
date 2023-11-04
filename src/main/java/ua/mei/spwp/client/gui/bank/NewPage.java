package ua.mei.spwp.client.gui.bank;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.client.gui.components.*;

public class NewPage extends BaseOwoScreen<FlowLayout> {
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .child(Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(Containers.verticalFlow(Sizing.fill(50), Sizing.fixed(21))
                                .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fill(100))
                                        .child(new TransparentButton(Text.literal("x"), button -> {
                                            MinecraftClient.getInstance().setScreen(null);
                                        }).textShadow(false).horizontalSizing(Sizing.fixed(17)).verticalSizing(Sizing.fixed(17)))
                                        .margins(Insets.of(2))
                                        .horizontalAlignment(HorizontalAlignment.RIGHT)
                                )
                                .surface(SPWorldsComponents.BEDROCK_BAR)
                        )
                        .child(Containers.verticalFlow(Sizing.fill(50), Sizing.fill(50))
                                .surface(SPWorldsComponents.BEDROCK_WINDOW)
                        )
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.VANILLA_TRANSLUCENT);
    }
}
