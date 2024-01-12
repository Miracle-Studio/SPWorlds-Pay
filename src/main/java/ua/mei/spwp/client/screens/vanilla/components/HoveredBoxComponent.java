package ua.mei.spwp.client.screens.vanilla.components;

import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.core.*;

public class HoveredBoxComponent extends BoxComponent {
    public int defaultColor;
    public int hoveredColor;

    public HoveredBoxComponent(Sizing horizontalSizing, Sizing verticalSizing, int defaultColor, int hoveredColor) {
        super(horizontalSizing, verticalSizing);

        this.defaultColor = defaultColor;
        this.hoveredColor = hoveredColor;

        this.color(Color.ofArgb(this.defaultColor));
    }

    @Override
    public void update(float delta, int mouseX, int mouseY) {
        if (this.hovered) {
            this.color(Color.ofArgb(this.hoveredColor));
        } else {
            this.color(Color.ofArgb(this.defaultColor));
        }

        super.update(delta, mouseX, mouseY);
    }
}
