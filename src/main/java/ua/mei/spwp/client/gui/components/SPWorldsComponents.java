package ua.mei.spwp.client.gui.components;

import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.util.*;
import net.minecraft.util.*;

public class SPWorldsComponents {
    public static Surface ESSENTIAL_NAV = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/essential_nav"), context, component);
    };

    public static Surface ESSENTIAL_PANEL = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/essential_panel"), context, component);
    };
}
