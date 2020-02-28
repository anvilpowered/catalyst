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
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.chat.PrivateMessageService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.StaffListService;
import org.anvilpowered.catalyst.velocity.utils.LuckPermsUtils;

public class ProxyJoinListener {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Registry registry;

    @Inject
    private PrivateMessageService<TextComponent> privateMessageService;

    @Inject
    private StaffListService<TextComponent> staffListService;

    @Inject
    private LuckPermsUtils luckPermsUtils;

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
}
