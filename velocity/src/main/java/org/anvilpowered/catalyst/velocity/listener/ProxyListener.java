package org.anvilpowered.catalyst.velocity.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.text.serializer.plain.PlainComponentSerializer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.data.config.Channel;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.ChatFilter;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;
import org.anvilpowered.catalyst.api.service.StaffListService;
import org.anvilpowered.catalyst.api.service.TabService;
import org.anvilpowered.catalyst.velocity.event.ProxyChatEvent;
import org.anvilpowered.catalyst.velocity.event.ProxyStaffChatEvent;
import org.anvilpowered.catalyst.velocity.plugin.CatalystVelocity;
import org.anvilpowered.catalyst.velocity.utils.LuckPermsUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ProxyListener {

    @Inject
    private ChatService<TextComponent, Player, CommandSource> chatService;

    @Inject
    private StaffListService<TextComponent> staffListService;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Registry registry;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private PrivateMessageService<TextComponent> privateMessageService;

    @Inject
    private LuckPermsUtils luckPermsUtils;

    @Inject
    private ChatFilter chatFilter;

    @Inject
    private TabService<TextComponent> tabService;

    @Inject
    private Logger logger;

    @Inject
    private TextService<TextComponent, CommandSource> textService;

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        Player player = event.getPlayer();
        staffListService.removeStaffNames(player.getUsername());
        luckPermsUtils.removePlayerFromCache(player);
        proxyServer.broadcast(LegacyComponentSerializer.legacy().deserialize(
            registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE)
                .replace("%player%", player.getUsername()),
            '&'));
        logger.info(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE).replace("%player%", player.getUsername()).replaceAll("&", ""));
    }

    @Subscribe
    public void staffChatEvent(ProxyStaffChatEvent event) {
        proxyServer.getAllPlayers().stream().filter(target -> target
            .hasPermission(registry.getOrDefault(CatalystKeys.STAFFCHAT)))
            .forEach(target -> target.sendMessage(pluginMessages.getStaffChatMessageFormatted(
                event.getSender().getUsername(),
                event.getMessage())));
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(registry.getOrDefault(CatalystKeys.SOCIALSPY_ONJOIN))) {
            privateMessageService.socialSpySet().add(player.getUniqueId());
        }

        luckPermsUtils.addPlayerToCache(player);
        staffListService.getStaffNames(
            player.getUsername(),
            player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN)),
            player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF)),
            player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER)));
        proxyServer.broadcast(LegacyComponentSerializer.legacy().deserialize(registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
            .replace("%player%", player.getUsername()), '&'));
        logger.info(registry.getOrDefault(CatalystKeys.JOIN_MESSAGE).replace("%player%", player.getUsername()).replaceAll("&", ""));
    }

    @Subscribe
    public void onChat(PlayerChatEvent e) {
        String message = e.getMessage();
        Player player = e.getPlayer();

        if (ProxyStaffChatEvent.staffChatSet.contains(player.getUniqueId())) {
            ProxyStaffChatEvent proxyStaffChatEvent = new ProxyStaffChatEvent(
                player,
                message,
                TextComponent.of(message));
            proxyServer.getEventManager().fire(proxyStaffChatEvent).join();
            e.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        Optional<Channel> channel = chatService.getChannelFromId(chatService.getChannelIdForUser(player.getUniqueId()));

        List<String> swearList = chatFilter.isSwear(message);
        if (channel.isPresent()) {
            if (swearList != null) {
                if (e.getResult().isAllowed()) {
                    if (!player.hasPermission(registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                        for (String swear : swearList) {
                            message = message.replace(swear, "****");
                        }
                        ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(),
                            checkPlayerName(message),
                            TextComponent.of(checkPlayerName(message)),
                            channel.get());
                        proxyServer.getEventManager().fire(proxyChatEvent).join();

                    } else {
                        ProxyChatEvent proxyChatEvent = new ProxyChatEvent(
                            e.getPlayer(),
                            checkPlayerName(message),
                            TextComponent.of(
                                checkPlayerName(message)),
                            channel.get());
                        proxyServer.getEventManager().fire(proxyChatEvent).join();
                    }
                    sendMessage(e, checkPlayerName(message));
                }
            } else {
                if (e.getResult().isAllowed()) {
                    ProxyChatEvent proxyChatEvent = new ProxyChatEvent(
                        e.getPlayer(), message, TextComponent.of(message), channel.get());
                    proxyServer.getEventManager().fire(proxyChatEvent).join();
                    sendMessage(e, checkPlayerName(message));
                }
            }
        } else {
            throw new AssertionError(
                "Unable to find a chat channel for " + player.getUsername() +
                    " please report this on github.");
        }
    }

    public String checkPlayerName(String message) {
        for (Player onlinePlayer : CatalystVelocity.getServer().getAllPlayers()) {
            if (message.contains(onlinePlayer.getUsername())) {
                message = message.replaceAll(
                    onlinePlayer.getUsername().toUpperCase(),
                    "&b@" + onlinePlayer.getUsername() + "&r")
                    .replaceAll(onlinePlayer.getUsername().toLowerCase(),
                        "&b@" + onlinePlayer.getUsername() + "&r");
            }
        }
        return message;
    }

    public void sendMessage(PlayerChatEvent e, String message) {

        e.setResult(PlayerChatEvent.ChatResult.denied());

        Player player = e.getPlayer();

        String prefix = luckPermsUtils.getPrefix(player);
        String chatColor = luckPermsUtils.getChatColor(player);
        String nameColor = luckPermsUtils.getNameColor(player);
        String suffix = luckPermsUtils.getSuffix(player);
        String server = player.getCurrentServer().orElseThrow(() ->
            new IllegalStateException("Invalid Server!")).getServer().getServerInfo().getName();
        Optional<Channel> channel = chatService.getChannelFromId(
            chatService.getChannelIdForUser(player.getUniqueId()));
        String channelId = chatService.getChannelIdForUser(player.getUniqueId());
        String channelPrefix = chatService.getChannelPrefix(channelId).orElseThrow(() ->
            new IllegalStateException("Please specify a prefix for " + channelId));

        if (!channel.isPresent()) throw new IllegalStateException("Invalid chat channel!");

        Tristate hasColorPermission = player.getPermissionValue(
            registry.getOrDefault(CatalystKeys.CHAT_COLOR));

        chatService.formatMessage(
            prefix,
            nameColor,
            player.getUsername(),
            chatColor + message,
            hasColorPermission.asBoolean(),
            suffix,
            server,
            channelId,
            channelPrefix
        ).thenAcceptAsync(optionalMessage -> {
            if (optionalMessage.isPresent()) {
                logger.info(channelId + " : " + PlainComponentSerializer.INSTANCE.serialize(optionalMessage.get()));
                chatService.sendMessageToChannel(channelId, optionalMessage.get(), player.getUniqueId(), p ->
                    p.hasPermission(registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS)));
            } else {
                player.sendMessage(pluginMessages.getMuted());
            }
        });
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
