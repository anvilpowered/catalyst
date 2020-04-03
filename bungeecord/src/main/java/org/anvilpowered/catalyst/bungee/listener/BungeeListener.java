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
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
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
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;
import org.anvilpowered.catalyst.api.service.StaffListService;
import org.anvilpowered.catalyst.bungee.event.ProxyChatEvent;
import org.anvilpowered.catalyst.bungee.event.ProxyStaffChatEvent;
import org.anvilpowered.catalyst.bungee.plugin.CatalystBungee;

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
    private LuckpermsService<ProxiedPlayer> luckPermsUtils;

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

        ProxyServer.getInstance().broadcast(
            textService.deserialize(registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
                .replace("%player%", player.getDisplayName())));
        CatalystBungee.plugin.getLogger().info(
            registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
                .replace("%player%", player.getDisplayName()));
        luckPermsUtils.addPlayerToCache(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        staffListService.removeStaffNames(player.getDisplayName());
        CatalystBungee.plugin.getProxy().broadcast(
            textService.deserialize(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE)
                .replace("%player%", player.getDisplayName())));
        CatalystBungee.plugin.getLogger().info(
            registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE)
                .replace("%player%", player.getDisplayName()));
        luckPermsUtils.removePlayerFromCache(player);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        String message = event.getMessage();
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (message.startsWith("/")) {
            return;
        }
        message = chatService.checkPlayerName(message);

        if (ProxyStaffChatEvent.staffChatSet.contains(player.getUniqueId())) {
            ProxyStaffChatEvent proxyStaffChatEvent = new ProxyStaffChatEvent(
                player,
                message,
                textService.deserialize(message)
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
                            message,
                            textService.deserialize(message),
                            channel.get()
                        );
                        CatalystBungee.plugin.getProxy().getPluginManager().callEvent(proxyChatEvent);
                    } else {
                        ProxyChatEvent proxyChatEvent = new ProxyChatEvent(
                            player,
                            message,
                            textService.deserialize(message),
                            channel.get()
                        );
                        CatalystBungee.plugin.getProxy().getPluginManager().callEvent(proxyChatEvent);
                    }
                    chatService.sendChatMessage(player, message);
                }
            } else {
                ProxyChatEvent proxyChatEvent = new ProxyChatEvent(
                    player,
                    message,
                    textService.deserialize(message),
                    channel.get()
                );
                CatalystBungee.plugin.getProxy().getPluginManager().callEvent(proxyChatEvent);
                chatService.sendChatMessage(player, message);
                event.setCancelled(true);
            }
        } else {
            throw new AssertionError(
                "Unable to find a chat channel for " + player.getDisplayName() +
                    " please report this on github"
            );
        }
    }
}
