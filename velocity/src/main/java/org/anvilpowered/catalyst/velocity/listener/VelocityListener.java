package org.anvilpowered.catalyst.velocity.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.ModInfo;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.config.AdvancedServerInfo;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.listener.ChatListener;
import org.anvilpowered.catalyst.api.listener.JoinListener;
import org.anvilpowered.catalyst.api.listener.LeaveListener;
import org.anvilpowered.catalyst.api.service.LoggerService;
import org.anvilpowered.catalyst.api.service.TabService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class VelocityListener {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Registry registry;

    @Inject
    private TabService<TextComponent> tabService;

    @Inject
    private ChatListener<Player> chatListener;

    @Inject
    private JoinListener<Player> joinListener;

    @Inject
    private LeaveListener<Player> leaveListener;

    @Inject
    private LoggerService<TextComponent> loggerService;

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        leaveListener.onPlayerLeave(event.getPlayer(), event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        if (event.getPlayer().getVirtualHost().isPresent()) {
            if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
                AtomicBoolean hostNameExists = new AtomicBoolean(false);
                for (AdvancedServerInfo serverInfo : registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO)) {
                    if (serverInfo.hostName.equalsIgnoreCase(event.getPlayer().getVirtualHost().get().getHostString())) {
                        hostNameExists.set(true);
                    }
                }
                if (!hostNameExists.get()) {
                    event.getPlayer().disconnect(LegacyComponentSerializer.legacy().deserialize("&4Please re-connect using the correct IP!", '&'));
                }
            }
            joinListener.onPlayerJoin(event.getPlayer(), event.getPlayer().getUniqueId(), event.getPlayer().getVirtualHost().get().getHostString());
        }
    }

    @Subscribe
    public void onChat(PlayerChatEvent e) {
        e.setResult(PlayerChatEvent.ChatResult.denied());
        chatListener.onPlayerChat(e.getPlayer(), e.getPlayer().getUniqueId(), e.getMessage());
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(new LegacyChannelIdentifier("GlobalTab"))) {
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }

        ByteArrayDataInput in = event.dataAsDataStream();
        String subChannel = in.readUTF();

        if (subChannel.endsWith("Balance")) {
            String[] packet = in.readUTF().split(":");
            String userName = packet[0];
            double balance = Double.parseDouble(packet[1]);
            tabService.setBalance(userName, balance);
        }
    }


    @Subscribe
    public void onServerListPing(ProxyPingEvent proxyPingEvent) {
        ServerPing serverPing = proxyPingEvent.getPing();
        ServerPing.Builder builder = ServerPing.builder();
        ModInfo modInfo = null;
        String playerProvidedHost;
        AtomicBoolean hostNameExists = new AtomicBoolean(false);

        if (proxyPingEvent.getConnection().getVirtualHost().isPresent()) {
            playerProvidedHost = proxyPingEvent.getConnection().getVirtualHost().get().getHostString();
        } else {
            loggerService.warn("Unable to get the virtual host from the player, please report this on github!");
            return;
        }

        List<AdvancedServerInfo> advancedServerInfoList = registry.get(CatalystKeys.ADVANCED_SERVER_INFO).orElseThrow(() -> new IllegalArgumentException("Invalid server configuration!"));
        boolean useCatalyst = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED);

        builder.description(LegacyComponentSerializer.legacy().deserialize(registry.getOrDefault(CatalystKeys.MOTD), '&'));
        if (useCatalyst) {
            advancedServerInfoList.forEach(advancedServerInfo -> {
                if (playerProvidedHost.equals(advancedServerInfo.hostName)) {
                    hostNameExists.set(true);
                    builder.description(LegacyComponentSerializer.legacy().deserialize(advancedServerInfo.motd, '&'));
                }
            });
            if (!hostNameExists.get()) {
                builder.description(LegacyComponentSerializer.legacy().deserialize("&4Using the direct IP to connect has been disabled!", '&'));
            }
        }

        if (proxyServer.getConfiguration().isAnnounceForge()) {
            if (useCatalyst) {
                for (AdvancedServerInfo advancedServerInfo : advancedServerInfoList) {
                    if (playerProvidedHost.equalsIgnoreCase(advancedServerInfo.hostName)) {
                        for (RegisteredServer pServer : proxyServer.getAllServers()) {
                            serverPing = pServer.ping().join();
                            if (advancedServerInfo.port == pServer.getServerInfo().getAddress().getPort()) {
                                if (serverPing.getModinfo().isPresent()) {
                                    modInfo = serverPing.getModinfo().get();
                                }
                            }
                        }
                    }
                }
            } else {
                for (String server : proxyServer.getConfiguration().getAttemptConnectionOrder()) {
                    Optional<RegisteredServer> registeredServer = proxyServer.getServer(server);
                    if (!registeredServer.isPresent()) return;
                    ServerPing ping = registeredServer.get().ping().join();
                    if (ping == null) continue;
                    if (ping.getModinfo().isPresent()) {
                        modInfo = ping.getModinfo().get();
                    }
                }
            }
            if (modInfo != null) {
                builder.mods(modInfo);
            } else {
                loggerService.warn("Please disable announceForge if you do not have a forge server running on your proxy." +
                    "\n If you are utilizing the advanced server info, please ensure that the port specified matches the target server port");
            }
        }

        if (registry.getOrDefault(CatalystKeys.SERVER_PING).equalsIgnoreCase("players")) {
            if (proxyServer.getPlayerCount() > 0) {
                ServerPing.SamplePlayer[] samplePlayers = new ServerPing.SamplePlayer[proxyServer.getPlayerCount()];
                List<Player> proxiedPlayers = new ArrayList<>(proxyServer.getAllPlayers());
                for (int i = 0; i < proxyServer.getPlayerCount(); i++) {
                    samplePlayers[i] = new ServerPing.SamplePlayer(proxiedPlayers.get(i).getUsername(), UUID.randomUUID());
                }
                builder.samplePlayers(samplePlayers);
            }
        } else if (registry.getOrDefault(CatalystKeys.SERVER_PING).equalsIgnoreCase("MESSAGE")) {
            builder.samplePlayers(new ServerPing.SamplePlayer(registry.getOrDefault(CatalystKeys.SERVER_PING_MESSAGE), UUID.randomUUID()));
        }

        if (serverPing.getFavicon().isPresent()) {
            builder.favicon(serverPing.getFavicon().get());
        } else if (proxyServer.getConfiguration().getFavicon().isPresent()) {
            builder.favicon(proxyServer.getConfiguration().getFavicon().get());
        }

        builder.onlinePlayers(proxyServer.getPlayerCount());
        builder.version(serverPing.getVersion());
        builder.maximumPlayers(proxyServer.getConfiguration().getShowMaxPlayers());
        proxyPingEvent.setPing(builder.build());
    }
}