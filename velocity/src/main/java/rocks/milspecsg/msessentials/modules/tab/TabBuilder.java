package rocks.milspecsg.msessentials.modules.tab;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.modules.utils.LuckPermsUtils;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;

public class TabBuilder {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;


    public TextComponent formatPlayerTab(String raw, Player player) {
        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", LuckPermsUtils.getPrefix(player));
        raw = raw.replace("%suffix%", LuckPermsUtils.getSuffix(player));

        raw = raw.replace("%server%", player.getCurrentServer().get().getServerInfo().getName());

        return pluginMessages.legacyColor(raw);
    }

    public TextComponent formatTab(String raw, Player player) {
        raw = raw.replace("%username%", player.getUsername())
                .replace("%prefix%", LuckPermsUtils.getPrefix(player))
                .replace("%suffix%", LuckPermsUtils.getSuffix(player))
                .replace("%server%", player.getCurrentServer().get().getServerInfo().getName())
                .replace("%ping%", String.valueOf(player.getPing()))
                .replace("%playercount%", String.valueOf(proxyServer.getPlayerCount()))
                .replace("%balance%", getBalance(player));

        return pluginMessages.legacyColor(raw);
    }

    private String getBalance(Player player) {
        return "null";
    }
}
