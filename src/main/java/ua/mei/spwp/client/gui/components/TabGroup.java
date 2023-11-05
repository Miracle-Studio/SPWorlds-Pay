package ua.mei.spwp.client.gui.components;

import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.*;
import ua.mei.spwp.client.gui.*;

import java.util.*;
import java.util.function.*;

public class TabGroup extends FlowLayout {
    public List<TransparentButton> tabList = new ArrayList<>();

    public Consumer<Integer> onChange;

    public TabGroup(Consumer<Integer> onChange) {
        super(Sizing.content(), Sizing.content(), Algorithm.HORIZONTAL);
        this.onChange = onChange;
        this.gap(13);
    }

    public TabGroup addTab(MutableText text) {
        TransparentButton button = new TransparentButton(text, EssentialColorScheme.TAB_TEXT, EssentialColorScheme.HOVERED_TAB_TEXT, EssentialColorScheme.SELECTED_TAB_TEXT, btn -> {});
        button.shadow(true);

        if (tabList.isEmpty()) {
            button.selected = true;
        }

        button.onPress((btn) -> {
            this.onChange.accept(tabList.indexOf(button));

            for (TransparentButton tab : tabList) {
                tab.selected = false;
            }

            button.selected = true;
        });

        tabList.add(button);
        this.children.add(button);

        return this;
    }
}
