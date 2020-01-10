package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

public class PingEventListener {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private ConfigurationService configService;

    @Subscribe
    public void onProxyPingEvent(ProxyPingEvent event) {
        ServerPing.Builder builder = ServerPing.builder();
        ServerPing serverPing = event.getPing();

        int playerCount = proxyServer.getPlayerCount();
        serverPing.getFavicon().ifPresent(builder::favicon);
        builder.version(serverPing.getVersion());
        builder.onlinePlayers(playerCount);
        builder.description(TextComponent.of(configService.getConfigString(ConfigKeys.MOTD)));
        builder.maximumPlayers(proxyServer.getConfiguration().getShowMaxPlayers());

        event.setPing(builder.build());
    }
}
