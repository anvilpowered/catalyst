/*
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.bungee.listener;

import com.google.inject.Inject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.data.config.Channel;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.ChatFilter;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;
import org.anvilpowered.catalyst.api.service.StaffListService;
import org.anvilpowered.catalyst.bungee.event.ProxyChatEvent;
import org.anvilpowered.catalyst.bungee.event.ProxyStaffChatEvent;
import org.anvilpowered.catalyst.bungee.plugin.CatalystBungee;
import org.anvilpowered.catalyst.bungee.utils.LuckPermsUtils;

import java.util.List;
import java.util.Optional;

public class BungeeListener implements Listener {

    @Inject
    private ChatService<TextComponent, ProxiedPlayer, CommandSender> chatService;

    @Inject
    private StaffListService<TextComponent> staffListService;

    @Inject
    private Registry registry;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private PrivateMessageService<TextComponent> privateMessageService;

    @Inject
    private ChatFilter chatFilter;

    @Inject
    private java.util.logging.Logger logger;

    @Inject
    private LuckPermsUtils luckPermsUtils;

    @Inject
    private TextService<TextComponent, CommandSender> textService;

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission(registry.getOrDefault(CatalystKeys.SOCIALSPY_ONJOIN))) {
            privateMessageService.socialSpySet().add(player.getUniqueId());
        }

        staffListService.getStaffNames(
            player.getDisplayName(),
            player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN)),
            player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF)),
            player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER)));

        CatalystBungee.plugin.getProxy().broadcast(registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
            .replace("%player%", player.getDisplayName()));
        logger.info(registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
            .replace("%player%", player.getDisplayName()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        staffListService.removeStaffNames(player.getDisplayName());
        CatalystBungee.plugin.getProxy().broadcast(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE)
            .replace("%player%", player.getDisplayName()));
        logger.info(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE)
            .replace("%player%", player.getDisplayName()));
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        String message = event.getMessage();
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (message.startsWith("/")) {
            return;
        }

        if (ProxyStaffChatEvent.staffChatSet.contains(player.getUniqueId())) {
            ProxyStaffChatEvent proxyStaffChatEvent = new ProxyStaffChatEvent(
                player,
                message,
                textService.deserialize(checkPlayerName(message))
            );
            CatalystBungee.plugin.getProxy().getPluginManager().callEvent(proxyStaffChatEvent);
            event.setCancelled(true);
            return;
        }

        Optional<Channel> channel = chatService.getChannelFromId(chatService.getChannelIdForUser(player.getUniqueId()));

        List<String> swearList = chatFilter.isSwear(message);
        if (channel.isPresent()) {
            if (swearList != null) {
                if (!event.isCancelled()) {
                    if (!player.hasPermission(registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                        for (String swear : swearList) {
                            message = message.replace(swear, "****");
                        }
                        ProxyChatEvent proxyChatEvent = new ProxyChatEvent(
                            player,
                            checkPlayerName(message),
                            textService.deserialize(checkPlayerName(message)),
                            channel.get()
                        );
                        CatalystBungee.plugin.getProxy().getPluginManager().callEvent(proxyChatEvent);
                    } else {
                        ProxyChatEvent proxyChatEvent = new ProxyChatEvent(
                            player,
                            checkPlayerName(message),
                            textService.deserialize(checkPlayerName(message)),
                            channel.get()
                        );
                        CatalystBungee.plugin.getProxy().getPluginManager().callEvent(proxyChatEvent);
                    }
                    sendMessage(event, checkPlayerName(message));
                }
            } else {
                if (!event.isCancelled()) {
                    ProxyChatEvent proxyChatEvent = new ProxyChatEvent(
                        player,
                        checkPlayerName(message),
                        textService.deserialize(checkPlayerName(message)),
                        channel.get()
                    );
                    CatalystBungee.plugin.getProxy().getPluginManager().callEvent(proxyChatEvent);
                    sendMessage(event, checkPlayerName(message));
                }
            }
        } else {
            throw new AssertionError(
                "Unable to find a chat channel for " + player.getDisplayName() +
                    " please report this on github"
            );
        }
    }

    public String checkPlayerName(String message) {
        for (ProxiedPlayer player : CatalystBungee.plugin.getProxy().getPlayers()) {
            if (message.contains(player.getDisplayName())) {
                message = message.replaceAll(
                    player.getDisplayName().toUpperCase(),
                    "&b@" + player.getDisplayName() + "&r")
                    .replaceAll(player.getDisplayName().toLowerCase(),
                        "&b@" + player.getDisplayName() + "&r");
            }
        }
        return message;
    }

    public void sendMessage(ChatEvent event, String message) {
        event.setCancelled(true);

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        String prefix = luckPermsUtils.getPrefix(player);
        String chatColor = luckPermsUtils.getChatColor(player);
        String nameColor = luckPermsUtils.getNameColor(player);
        String suffix = luckPermsUtils.getSuffix(player);
        String server = player.getServer().getInfo().getName();
        String channelId = chatService.getChannelIdForUser(player.getUniqueId());
        Optional<Channel> channel = chatService.getChannelFromId(channelId);
        String channelPrefix = chatService.getChannelPrefix(channelId).orElseThrow(() ->
            new IllegalStateException("Please specify a prefix for " + channelId));

        if (!channel.isPresent()) throw new IllegalStateException("Invalid chat channel!");


        boolean hasColorPermission = player.hasPermission(registry.getOrDefault(CatalystKeys.CHAT_COLOR));
        chatService.formatMessage(
            prefix,
            nameColor,
            player.getDisplayName(),
            chatColor + message,
            hasColorPermission,
            suffix,
            server,
            channelId,
            channelPrefix
        ).thenAcceptAsync(optionalMessage -> {
            if (optionalMessage.isPresent()) {
                chatService.sendMessageToChannel(channelId, optionalMessage.get(), player.getUniqueId(), p ->
                    p.hasPermission(registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS)));
            } else {
                player.sendMessage(pluginMessages.getMuted());
            }
        });
    }


}
