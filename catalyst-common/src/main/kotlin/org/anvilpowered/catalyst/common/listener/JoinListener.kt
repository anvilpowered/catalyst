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
package org.anvilpowered.catalyst.common.listener

import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.event.JoinEvent
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.BroadcastService
import org.anvilpowered.catalyst.api.service.PrivateMessageService
import org.anvilpowered.catalyst.api.service.StaffListService
import org.anvilpowered.registry.Registry
import org.slf4j.Logger

class JoinListener<TPlayer> @Inject constructor(
    private val userService: UserService<TPlayer, TPlayer>,
    private val permissionService: PermissionService,
    private val registry: Registry,
    private val privateMessageService: PrivateMessageService,
    private val staffListService: StaffListService,
    private val broadcastService: BroadcastService,
    private val logger: Logger,
    private val locationService: LocationService,
) {
    @Subscribe
    fun onPlayerJoin(event: JoinEvent<TPlayer>) {
        val player = event.player
        val playerUUID = event.playerUUID
        if (permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.SOCIALSPY_ONJOIN_PERMISSION))) {
            privateMessageService.socialSpySet().add(playerUUID)
        }
        val userName = userService.getUserName(player as TPlayer)
        val server = locationService.getServer(playerUUID)?.name ?: "null"

        staffListService.getStaffNames(
            userName,
            permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN_PERMISSION)),
            permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF_PERMISSION)),
            permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER_PERMISSION))
        )
        val joinMessage = registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
        if (registry.getOrDefault(CatalystKeys.JOIN_LISTENER_ENABLED)) {
            broadcastService.broadcast(
                LegacyComponentSerializer.legacyAmpersand().deserialize(joinMessage.replace("%player%", userName).replace("%server%", server))
            )
            logger.info(registry.getOrDefault(CatalystKeys.JOIN_MESSAGE).replace("%player%", userName).replace("%server%", server))
        }
    }
}
