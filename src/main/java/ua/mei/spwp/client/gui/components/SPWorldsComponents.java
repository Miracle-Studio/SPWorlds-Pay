package ua.mei.spwp.client.gui.components;

import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.util.*;
import net.minecraft.util.*;

public class SPWorldsComponents {
    public static Surface BEDROCK_WINDOW = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/bedrock_window"), context, component);
    };
    public static Surface BEDROCK_BAR = (context, component) -> {
        NinePatchTexture.draw(new Identifier("spwp", "panel/bedrock_bar"), context, component);
    };
}
