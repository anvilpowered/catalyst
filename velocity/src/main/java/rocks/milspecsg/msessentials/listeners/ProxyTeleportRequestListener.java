package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import rocks.milspecsg.msessentials.events.ProxyTeleportRequestEvent;
import rocks.milspecsg.msessentials.misc.PluginMessages;

public class ProxyTeleportRequestListener {

    @Inject
    private PluginMessages pluginMessages;

    @Subscribe
    public void onTeleportRequest (ProxyTeleportRequestEvent event) {
        event.getTargetPlayer().sendMessage(pluginMessages.teleportRequestRecieved(event.getSourcePlayer().getUsername()));
        event.getSourcePlayer().sendMessage(pluginMessages.teleportRequestSent(event.getTargetPlayer().getUsername()));

        //Fire plugin message events
    }
}
