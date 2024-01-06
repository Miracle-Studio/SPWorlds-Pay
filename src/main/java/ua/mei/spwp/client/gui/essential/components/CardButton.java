package ua.mei.spwp.client.gui.essential.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import ua.mei.spwp.api.types.*;
import ua.mei.spwp.client.*;
import ua.mei.spwp.client.gui.essential.*;
import ua.mei.spwp.util.*;

import java.util.function.*;

public class CardButton extends FlowLayout {
    public DatabaseCard card;

    public int textColor;
    public int hoverColor;
    public int selectedColor;

    public boolean selected = false;

    public Consumer<DatabaseCard> onPress;

    public CardButton(DatabaseCard card, Consumer<DatabaseCard> onPress, Consumer<DatabaseCard> onDelete) {
        super(Sizing.fill(100), Sizing.content(), Algorithm.VERTICAL);

        this.card = card;

        this.child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                .child(Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                .child(Components.label(Text.literal(card.card().name())).color(Color.ofArgb(EssentialColorScheme.MODAL_TEXT)).shadow(true))
                                .child(new TransparentButton(Text.literal("\uD83D\uDDD1"), EssentialColorScheme.TAB_TEXT, EssentialColorScheme.HOVERED_TAB_TEXT, 0xFFFF3333, button -> {
                                    onDelete.accept(this.card);
                                }))
                                .gap(4)
                        )
                        .child(new CardBalanceLabel(card).color(Color.ofArgb(0xFF747474)).shadow(false))
                        .gap(4)
                        .margins(Insets.top(2))
                        .verticalAlignment(VerticalAlignment.CENTER)
                )
                .margins(Insets.both(10, 8))
        );

        this.textColor = EssentialColorScheme.BACKGROUND;
        this.hoverColor = EssentialColorScheme.BORDER;
        this.selectedColor = EssentialColorScheme.BORDER;

        this.onPress = onPress;
    }

    public void onPress(Consumer<DatabaseCard> onPress) {
        this.onPress = onPress;
    }

    @Override
    public boolean onMouseDown(double mouseX, double mouseY, int button) {
        onPress.accept(this.card);

        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        return super.onMouseDown(mouseX, mouseY, button);
    }

    @Override
    public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        super.draw(context, mouseX, mouseY, partialTicks, delta);

        if (this.selected) {
            this.surface(Surface.flat(this.selectedColor));
        } else {
            if (this.hovered) {
                this.surface(Surface.flat(this.hoverColor));
            } else {
                this.surface(Surface.flat(this.textColor));
            }
        }
    }
}
