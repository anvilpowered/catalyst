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
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anvilpowered.catalyst.api.listener.ChatListener;
import org.anvilpowered.catalyst.api.listener.JoinListener;
import org.anvilpowered.catalyst.api.listener.LeaveListener;

public class BungeeListener implements Listener {

    @Inject
    private LeaveListener<ProxiedPlayer> leaveListener;

    @Inject
    private JoinListener<ProxiedPlayer> joinListener;

    @Inject
    private ChatListener<ProxiedPlayer> chatListener;

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        joinListener.onPlayerJoin(event.getPlayer(), event.getPlayer().getUniqueId(), event.getPlayer().getServer().getAddress().getHostString());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        leaveListener.onPlayerLeave(event.getPlayer(), event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCommand()) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        chatListener.onPlayerChat(player, player.getUniqueId(), event.getMessage());
        event.setCancelled(true);
    }
}
