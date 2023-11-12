package ua.mei.spwp.client.gui.components;

import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.*;
import ua.mei.spwp.client.gui.*;

import java.util.*;
import java.util.function.*;

public class ServerList extends FlowLayout {
    public List<TransparentButton> tabList = new ArrayList<>();

    public Consumer<Server> onChange;

    public ServerList(Consumer<Server> onChange) {
        super(Sizing.content(), Sizing.content(), Algorithm.HORIZONTAL);
        this.onChange = onChange;
        this.gap(13);

        initTabs();
    }

    private void initTabs() {
        List<Server> servers = new ArrayList<>(List.of(Server.values()));
        servers.remove(Server.OTHER);

        for (Server server : servers) {
            TransparentButton button = new TransparentButton(Text.literal(server.name()), EssentialColorScheme.TAB_TEXT, EssentialColorScheme.HOVERED_TAB_TEXT, EssentialColorScheme.SELECTED_TAB_TEXT, btn -> {});
            button.shadow(true);

            if (tabList.isEmpty()) {
                this.onChange.accept(server);
                button.selected = true;
            }

            button.onPress((btn) -> {
                this.onChange.accept(server);

                for (TransparentButton tab : tabList) {
                    tab.selected = false;
                }

                button.selected = true;
            });

            tabList.add(button);
            this.children.add(button);
        }
    }
}
