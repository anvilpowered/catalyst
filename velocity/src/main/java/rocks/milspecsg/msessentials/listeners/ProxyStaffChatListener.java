package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import rocks.milspecsg.msessentials.events.ProxyStaffChatEvent;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

public class ProxyStaffChatListener {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages pluginmessages;

    @Subscribe
    public void staffChatEvent(ProxyStaffChatEvent event) {
        proxyServer.getAllPlayers().stream().filter(target -> target
                .hasPermission(PluginPermissions.STAFFCHAT))
                .forEach(target -> target.sendMessage(pluginmessages.staffChatMessageFormatted(event.getMessage(), event.getSender().getUsername())));
    }
}
