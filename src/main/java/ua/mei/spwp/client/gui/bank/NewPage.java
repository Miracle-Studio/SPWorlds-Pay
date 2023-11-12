package ua.mei.spwp.client.gui.bank;

import io.wispforest.owo.ui.base.*;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.gui.*;
import ua.mei.spwp.client.gui.components.*;

public class NewPage extends BaseOwoScreen<FlowLayout> {
    public Server server = Server.SP;
    public DatabaseCard card = null;

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
        MinecraftClient.getInstance().getWindow().setScaleFactor(MinecraftClient.getInstance().getWindow().calculateScaleFactor(this.oldGuiScale, MinecraftClient.getInstance().options.getForceUnicodeFont().getValue()));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        EssentialScrollContainer cardList = new EssentialScrollContainer(ScrollContainer.ScrollDirection.VERTICAL, Sizing.fill(100), Sizing.fill(100), new CardList(this.server, card -> {
            this.card = card;
        }));

        ServerList serverList = new ServerList(server -> {
            this.card = null;
            this.server = server;

            cardList.child(new CardList(server, card -> {
                this.card = card;
            }));
        });
        serverList.padding(Insets.of(10, 10, 12, 12));

        rootComponent.child(Containers.verticalFlow(Sizing.fill(85), Sizing.content())
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(30))
                                .child(Containers.horizontalFlow(Sizing.fill(25), Sizing.fill(100))
                                        .child(Components.label(Text.literal("Кошелёк")).color(Color.ofArgb(EssentialColorScheme.SCREEN_TITLE)).shadow(true).margins(Insets.of(11, 0, 13, 0)))
                                        .surface(EssentialSurface.ESSENTIAL_NAV_LEFT)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(75), Sizing.fill(100))
                                        .child(Components.label(Text.literal("Переводы")).color(Color.ofArgb(EssentialColorScheme.SCREEN_TITLE)).shadow(true).margins(Insets.of(11, 0, 10, 0)))
                                        .surface(EssentialSurface.ESSENTIAL_NAV_RIGHT)
                                )
                        )
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(30))
                                .child(Containers.horizontalFlow(Sizing.fill(25), Sizing.fill(100))
                                        .child(serverList)
                                        .surface(EssentialSurface.ESSENTIAL_PANEL_LEFT)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(75), Sizing.fill(100))
                                        .surface(EssentialSurface.ESSENTIAL_PANEL_RIGHT_TOP)
                                )
                        )
                        .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.fill(65))
                                .child(Containers.horizontalFlow(Sizing.fill(25), Sizing.fill(100))
                                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100))
                                                .child(cardList)
                                                .margins(Insets.of(0, 3, 3, 0))
                                        )
                                        .surface(EssentialSurface.ESSENTIAL_PANEL_LEFT)
                                )
                                .child(Containers.horizontalFlow(Sizing.fill(75), Sizing.fill(100))
                                        .child(Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100))
                                                .margins(Insets.of(0, 33, 0, 3))
                                        )
                                        .surface(EssentialSurface.ESSENTIAL_PANEL_RIGHT)
                                )
                        )
                )
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.flat(EssentialColorScheme.BACKGROUND));
    }
}
