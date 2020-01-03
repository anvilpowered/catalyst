package rocks.milspecsg.msessentials.modules.tab;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.misc.LuckpermsHook;
import rocks.milspecsg.msessentials.misc.PluginMessages;

public class TabBuilder {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;


    public TextComponent formatPlayerTab(String raw, Player player) {
        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", LuckpermsHook.getPrefix(player));
        raw = raw.replace("%suffix%", LuckpermsHook.getSuffix(player));

        raw = raw.replace("%server%", player.getCurrentServer().get().getServerInfo().getName());

        return pluginMessages.legacyColor(raw);
    }

    public TextComponent formatTab(String raw, Player player) {
        raw = raw.replace("%username%", player.getUsername())
                .replace("%prefix%", LuckpermsHook.getPrefix(player))
                .replace("%suffix%", LuckpermsHook.getSuffix(player))
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
