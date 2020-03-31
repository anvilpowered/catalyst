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
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.listener.ChatListener;
import org.anvilpowered.catalyst.api.listener.JoinListener;
import org.anvilpowered.catalyst.api.listener.LeaveListener;
import org.anvilpowered.catalyst.api.service.TabService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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


    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        leaveListener.onPlayerLeave(event.getPlayer(), event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        joinListener.onPlayerJoin(event.getPlayer(), event.getPlayer().getUniqueId());
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
            Double balance = Double.parseDouble(packet[1]);
            tabService.setBalance(userName, balance);
        }
    }

    @Subscribe
    public void onServerListPing(ProxyPingEvent proxyPingEvent) {

        ServerPing serverPing = proxyPingEvent.getPing();
        ServerPing.Builder builder = ServerPing.builder();

        if (proxyServer.getConfiguration().isAnnounceForge()) {
            try {
                for (String server : proxyServer.getConfiguration().getAttemptConnectionOrder()) {
                    Optional<RegisteredServer> registeredServer = proxyServer.getServer(server);
                    if (!registeredServer.isPresent()) continue;
                    ServerPing ping = registeredServer.get().ping().get();
                    if (ping == null) continue;
                    serverPing = ping;
                    ping.getModinfo().ifPresent(builder::mods);
                }
            } catch (InterruptedException | ExecutionException e) {
                proxyPingEvent.setPing(ServerPing.builder()
                    .description(TextComponent.of("The server is offline", TextColor.RED))
                    .build());
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
        }

        builder.onlinePlayers(proxyServer.getPlayerCount());
        builder.description(proxyServer.getConfiguration().getMotdComponent());
        builder.version(serverPing.getVersion());
        builder.maximumPlayers(proxyServer.getConfiguration().getShowMaxPlayers());

        proxyPingEvent.setPing(builder.build());
    }
}
