package ua.mei.spwp.client.gui.essential.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;

import java.util.function.*;

public class TransparentButton extends LabelComponent {
    public Color textColor;
    public Color hoverColor;
    public Color selectedColor;

    public boolean selected = false;

    public Consumer<TransparentButton> onPress;

    public TransparentButton(Text message, int textColor, int hoverColor, int selectedColor, Consumer<TransparentButton> onPress) {
        super(message);

        this.textColor = Color.ofArgb(textColor);
        this.hoverColor = Color.ofArgb(hoverColor);
        this.selectedColor = Color.ofArgb(selectedColor);

        this.onPress = onPress;
    }

    public void onPress(Consumer<TransparentButton> onPress) {
        this.onPress = onPress;
    }

    @Override
    public boolean onMouseDown(double mouseX, double mouseY, int button) {
        onPress.accept(this);
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        return super.onMouseDown(mouseX, mouseY, button);
    }

    @Override
    public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        super.draw(context, mouseX, mouseY, partialTicks, delta);

        if (this.selected) {
            this.color(this.selectedColor);
        } else {
            if (this.hovered) {
                this.color(this.hoverColor);
            } else {
                this.color(this.textColor);
            }
        }
    }
}
