/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.bungee.listener;

import com.google.inject.Inject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.api.event.JoinEvent;
import org.anvilpowered.catalyst.api.event.LeaveEvent;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.bungee.service.BungeeCommandDispatcher;

public class BungeeListener implements Listener {

    @Inject
    private ChatService<TextComponent, ProxiedPlayer, CommandSender> chatService;

    @Inject
    private EventService eventService;

    @Inject
    private Registry registry;

    @Inject
    private BungeeCommandDispatcher dispatcher;

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        eventService.post(
            new JoinEvent<>(e.getPlayer(), e.getPlayer().getAddress().toString(), e.getPlayer().getUniqueId())
        );
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent e) {
        eventService.post(new LeaveEvent<>(e.getPlayer()));
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        if (e.isCommand()) {
            try {
                dispatcher.execute(e.getMessage().replaceAll("/", ""), (CommandSender) e.getSender());
            } catch (Exception ignored) {
            }
            return;
        }
        if (!registry.getOrDefault(CatalystKeys.INSTANCE.getPROXY_CHAT_ENABLED())) {
            return;
        }
        if (chatService.isDisabledForUser((ProxiedPlayer) e.getSender())) {
            return;
        }
        eventService.post(
            new org.anvilpowered.catalyst.api.event.ChatEvent<>(
                (ProxiedPlayer) e.getSender(),
                e.getMessage(),
                new TextComponent(e.getMessage())
            ));
        e.setCancelled(true);
    }
}
