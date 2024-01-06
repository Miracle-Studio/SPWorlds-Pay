package ua.mei.spwp.client.gui.essential.components;

import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.*;
import ua.mei.spwp.client.gui.essential.*;
import ua.mei.spwp.util.*;

import java.util.*;
import java.util.function.*;

public class ServerList extends FlowLayout {
    public List<TransparentButton> tabList = new ArrayList<>();

    public Consumer<Server> onChange;
    public Server server;

    public ServerList(Server server, Consumer<Server> onChange) {
        super(Sizing.content(), Sizing.content(), Algorithm.HORIZONTAL);
        this.onChange = onChange;
        this.server = server;
        this.gap(13);

        initTabs();
    }

    private void initTabs() {
        List<Server> servers = new ArrayList<>(List.of(Server.values()));
        servers.remove(Server.OTHER);

        for (Server server : servers) {
            TransparentButton button = new TransparentButton(Text.translatable("gui.spwp.server." + server.name().toLowerCase()), EssentialColorScheme.TAB_TEXT, EssentialColorScheme.HOVERED_TAB_TEXT, EssentialColorScheme.SELECTED_TAB_TEXT, btn -> {});
            button.shadow(true);

            if (server == this.server) {
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
