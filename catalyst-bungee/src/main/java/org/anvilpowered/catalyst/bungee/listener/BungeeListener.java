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
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.JoinEvent;
import org.anvilpowered.catalyst.api.event.LeaveEvent;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.bungee.service.BungeeCommandDispatcher;

public class BungeeListener implements Listener {

    @Inject
    private JoinEvent<ProxiedPlayer> joinEvent;

    @Inject
    private LeaveEvent<ProxiedPlayer> leaveEvent;

    @Inject
    private EventService eventService;

    @Inject
    private Registry registry;

    @Inject
    private BungeeCommandDispatcher dispatcher;

    @Inject
    private org.anvilpowered.catalyst.api.event.ChatEvent<TextComponent, ProxiedPlayer> chatEvent;

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        joinEvent.setPlayer(event.getPlayer());
        joinEvent.setPlayerUUID(event.getPlayer().getUniqueId());
        joinEvent.setHostString(event.getPlayer().getAddress().toString());
        eventService.getEventBus().post(joinEvent);
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        leaveEvent.setPlayer(event.getPlayer());
        eventService.getEventBus().post(leaveEvent);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCommand()) {
            try {
                dispatcher.execute(event.getMessage().replaceAll("/", ""), (CommandSender) event.getSender());
            } catch (Exception ignored) {
            }
            return;
        }
        if (!registry.getOrDefault(CatalystKeys.PROXY_CHAT_ENABLED)) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        chatEvent.setPlayer(player);
        chatEvent.setRawMessage(event.getMessage());
        chatEvent.setMessage(new TextComponent(event.getMessage()));
        eventService.getEventBus().post(chatEvent);
        event.setCancelled(true);
    }
}
