package ua.mei.spwp.client.gui.bank;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
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
                .child(Containers.horizontalFlow(Sizing.fill(50), Sizing.fill(50))
                        .child(new BedrockButton(Text.literal("lox"), button -> {

                        }))
                        .surface(SPWorldsComponents.BEDROCK_WINDOW)
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.VANILLA_TRANSLUCENT);
    }
}
