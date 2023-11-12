package ua.mei.spwp.client.gui.components;

import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import ua.mei.spwp.mixin.*;

public class EssentialScrollContainer extends ScrollContainer<Component> {
    public EssentialScrollContainer(ScrollDirection direction, Sizing horizontalSizing, Sizing verticalSizing, Component child) {
        super(direction, horizontalSizing, verticalSizing, child);

        this.scrollbar(ScrollContainer.Scrollbar.flat(Color.ofArgb(0xFF5C5C5C)));
        ((ScrollContainerAccessor) this).setScrollbaring(true);
        this.scrollbarThiccness(3);
    }

    @Override
    public boolean onMouseUp(double mouseX, double mouseY, int button) {
        return true;
    }
}
