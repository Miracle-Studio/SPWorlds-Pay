package ua.mei.spwp.client.gui.essential.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.*;
import ua.mei.spwp.client.gui.essential.*;

public class EssentialTextBox extends FlowLayout {
    public final TextBoxComponent textBoxComponent;

    public EssentialTextBox(Sizing horizontalSizing, MutableText placeholder) {
        super(horizontalSizing, Sizing.content(), Algorithm.VERTICAL);

        textBoxComponent = Components.textBox(horizontalSizing);

        textBoxComponent.setDrawsBackground(false);
        textBoxComponent.setPlaceholder(placeholder.styled(style -> style.withColor(EssentialColorScheme.INPUT_PLACEHOLDER)));
        textBoxComponent.setEditableColor(EssentialColorScheme.INPUT_TEXT);
        textBoxComponent.margins(Insets.of(10, 9, 9, 9));
        this.surface(Surface.flat(EssentialColorScheme.BORDER));

        this.child(textBoxComponent);
    }
}
