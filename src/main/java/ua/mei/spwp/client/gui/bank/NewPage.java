package ua.mei.spwp.client.gui.bank;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.client.gui.*;
import ua.mei.spwp.client.gui.components.*;

public class NewPage extends BaseOwoScreen<FlowLayout> {
    public int oldGuiScale;

    public NewPage(int oldGuiScale) {
        this.oldGuiScale = oldGuiScale;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    public void onDisplayed() {
        super.onDisplayed();
        MinecraftClient.getInstance().getWindow().setScaleFactor(MinecraftClient.getInstance().getWindow().calculateScaleFactor(2, MinecraftClient.getInstance().options.getForceUnicodeFont().getValue()));
    }

    @Override
    public void close() {
        super.close();
        MinecraftClient.getInstance().getWindow().setScaleFactor(MinecraftClient.getInstance().getWindow().calculateScaleFactor(oldGuiScale, MinecraftClient.getInstance().options.getForceUnicodeFont().getValue()));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.child(Containers.verticalFlow(Sizing.fill(85), Sizing.content())
                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(30))
                                .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100))
                                        .child(Components.label(Text.literal("Кошелёк")).color(Color.ofArgb(EssentialColorScheme.SCREEN_TITLE)).shadow(true).margins(Insets.of(11, 0, 13, 0)))
                                )
                                .surface(SPWorldsComponents.ESSENTIAL_NAV)
                        )
                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(70))
                                .surface(SPWorldsComponents.ESSENTIAL_PANEL)
                        )
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.flat(EssentialColorScheme.BACKGROUND));
    }
}
