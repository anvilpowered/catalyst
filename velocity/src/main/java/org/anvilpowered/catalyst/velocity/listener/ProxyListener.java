package org.anvilpowered.catalyst.velocity.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.config.Channel;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.plugin.StaffListService;
import org.anvilpowered.catalyst.api.service.ChatFilter;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;
import org.anvilpowered.catalyst.velocity.event.ProxyChatEvent;
import org.anvilpowered.catalyst.velocity.event.ProxyStaffChatEvent;
import org.anvilpowered.catalyst.velocity.plugin.Catalyst;
import org.anvilpowered.catalyst.velocity.utils.LuckPermsUtils;
import org.spongepowered.api.command.CommandSource;

import java.util.List;
import java.util.Optional;

public class ProxyListener {

    @Inject
    public ChatService<TextComponent, Player, CommandSource> chatService;

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

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        Player player = event.getPlayer();
        staffListService.removeStaffNames(player.getUsername());
        luckPermsUtils.removePlayerFromCache(player);
        proxyServer.broadcast(LegacyComponentSerializer.legacy().deserialize(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE).replace("%player%", player.getUsername()), '&'));
    }

    @Subscribe
    public void staffChatEvent(ProxyStaffChatEvent event) {
        proxyServer.getAllPlayers().stream().filter(target -> target
            .hasPermission(registry.getOrDefault(CatalystKeys.STAFFCHAT)))
            .forEach(target -> target.sendMessage(pluginMessages.getStaffChatMessageFormatted(event.getSender().getUsername(), event.getMessage())));
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(registry.getOrDefault(CatalystKeys.SOCIALSPY_ONJOIN))) {
            privateMessageService.socialSpySet().add(player.getUniqueId());
        }

        luckPermsUtils.addPlayerToCache(player);
        staffListService.getStaffNames(player.getUsername(), player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN)), player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF)), player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER)));
        proxyServer.broadcast(LegacyComponentSerializer.legacy().deserialize(registry.getOrDefault(CatalystKeys.JOIN_MESSAGE).replace("%player%", player.getUsername()), '&'));
    }

    @Subscribe
    public void onChat(PlayerChatEvent e) {
        String message = e.getMessage();
        Player player = e.getPlayer();

        if (ProxyStaffChatEvent.staffChatSet.contains(player.getUniqueId())) {
            ProxyStaffChatEvent proxyStaffChatEvent = new ProxyStaffChatEvent(player, message, TextComponent.of(message));
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
                        ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), checkPlayerName(message), TextComponent.of(checkPlayerName(message)), channel.get());
                        proxyServer.getEventManager().fire(proxyChatEvent).join();

                    } else {
                        ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), checkPlayerName(message), TextComponent.of(checkPlayerName(message)), channel.get());
                        proxyServer.getEventManager().fire(proxyChatEvent).join();
                    }
                    sendMessage(e, checkPlayerName(message));
                }
            } else {
                if (e.getResult().isAllowed()) {
                    ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), message, TextComponent.of(message), channel.get());
                    proxyServer.getEventManager().fire(proxyChatEvent).join();
                    sendMessage(e, checkPlayerName(message));
                }
            }
        } else {
            throw new AssertionError("Unable to find a chat channel for " + player.getUsername() + " please report this on github.");
        }
    }

    public String checkPlayerName(String message) {
        for (Player onlinePlayer : Catalyst.getServer().getAllPlayers()) {
            if (message.contains(onlinePlayer.getUsername())) {
                message = message.replaceAll(onlinePlayer.getUsername().toUpperCase(), "&b@" + onlinePlayer.getUsername() + "&r")
                    .replaceAll(onlinePlayer.getUsername().toLowerCase(), "&b@" + onlinePlayer.getUsername() + "&r");
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
        String server = player.getCurrentServer().orElseThrow(() -> new IllegalStateException("Invalid Server!")).getServer().getServerInfo().getName();
        Optional<Channel> channel = chatService.getChannelFromId(chatService.getChannelIdForUser(player.getUniqueId()));
        String channelId = chatService.getChannelIdForUser(player.getUniqueId());
        String channelPrefix = chatService.getChannelPrefix(channelId).orElseThrow(() -> new IllegalStateException("Please specify a prefix for " + channelId));

        if (!channel.isPresent()) throw new IllegalStateException("Invalid chat channel!");

        Tristate hasColorPermission = player.getPermissionValue(registry.getOrDefault(CatalystKeys.CHAT_COLOR));

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
                chatService.sendMessageToChannel(channelId, optionalMessage.get(), p -> p.hasPermission(registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS)));
            } else {
                player.sendMessage(pluginMessages.getMuted());
            }
        });
    }
}
