package ua.mei.spwp.client.gui;

import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.util.*;
import net.minecraft.util.*;

public class EssentialSurface {
    public static Surface ESSENTIAL_NAV_LEFT = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/essential_nav_left"), context, component);
    };

    public static Surface ESSENTIAL_NAV_RIGHT = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/essential_nav_right"), context, component);
    };

    public static Surface ESSENTIAL_PANEL_LEFT = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/essential_panel_left"), context, component);
    };

    public static Surface ESSENTIAL_PANEL_RIGHT = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/essential_panel_right"), context, component);
    };

    public static Surface ESSENTIAL_PANEL_RIGHT_TOP = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/essential_panel_right_top"), context, component);
    };
}
