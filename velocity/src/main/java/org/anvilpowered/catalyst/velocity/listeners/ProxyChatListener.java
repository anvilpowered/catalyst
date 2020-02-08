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

package org.anvilpowered.catalyst.velocity.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.anvilpowered.catalyst.velocity.events.ProxyChatEvent;
import org.anvilpowered.catalyst.velocity.events.ProxyStaffChatEvent;
import org.anvilpowered.catalyst.velocity.plugin.Catalyst;
import org.anvilpowered.catalyst.velocity.utils.LuckPermsUtils;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.chat.ChatService;
import org.anvilpowered.catalyst.api.data.config.Channel;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.velocity.chatutils.ChatFilter;

import java.util.List;
import java.util.Optional;

public class ProxyChatListener {

    @Inject
    public ChatService<TextComponent> chatService;

    @Inject
    public ChatFilter chatFilter;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Registry registry;

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

        List<String> swearList = chatFilter.isSwear(message);
        if (swearList != null) {
            if (e.getResult().isAllowed()) {
                if (!player.hasPermission(registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                    for (String swear : swearList) {
                        message = message.replace(swear, "****");
                    }
                    ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), checkPlayerName(message), TextComponent.of(checkPlayerName(message)));
                    proxyServer.getEventManager().fire(proxyChatEvent).join();
                    sendMessage(e, checkPlayerName(message));

                } else {
                    ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), checkPlayerName(message), TextComponent.of(checkPlayerName(message)));
                    proxyServer.getEventManager().fire(proxyChatEvent).join();
                    sendMessage(e, checkPlayerName(message));
                }
            }
        } else {
            if (e.getResult().isAllowed()) {
                ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), message, TextComponent.of(message));
                proxyServer.getEventManager().fire(proxyChatEvent).join();
                sendMessage(e, checkPlayerName(message));
            }
        }
    }

    public String checkPlayerName(String message) {
        for (Player onlinePlayer : Catalyst.getServer().getAllPlayers()) {
            if (message.toLowerCase().equalsIgnoreCase(onlinePlayer.getUsername())) {
                message = message.replaceAll(onlinePlayer.getUsername().toLowerCase(), "&b@" + onlinePlayer.getUsername() + "&r");
            }
        }
        return message;
    }

    public void sendMessage(PlayerChatEvent e, String message) {
        //Set the result to denied to broadcast our own message
        e.setResult(PlayerChatEvent.ChatResult.denied());
        Player player = e.getPlayer();
        //Grab all the information we will need from luckperms
        //The only supported permissions plugin to date
        String prefix = LuckPermsUtils.getPrefix(player);
        String chatColor = LuckPermsUtils.getChatColor(player);
        String nameColor = LuckPermsUtils.getNameColor(player);
        String suffix = LuckPermsUtils.getSuffix(player);
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
            for (Player p : proxyServer.getAllPlayers()) {
                if (p.hasPermission(registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS)) || p.hasPermission(registry.getOrDefault(CatalystKeys.CHANNEL_BASE) + channelId)) {
                    p.sendMessage(optionalMessage);
                } else
                    if (chatService.getChannelIdForUser(p.getUniqueId()).equals(channelId)) {
                        chatService.sendMessageToChannel(channelId, optionalMessage);
                    }
                }
        });
    }
}
