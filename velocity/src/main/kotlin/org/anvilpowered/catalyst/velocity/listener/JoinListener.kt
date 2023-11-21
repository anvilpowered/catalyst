/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.catalyst.velocity.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.db.RepositoryScope
import org.anvilpowered.catalyst.velocity.chat.StaffListService

context(ProxyServerScope, Registry.Scope, LoggerScope, StaffListService.Scope, LuckpermsService.Scope, RepositoryScope)
class JoinListener {
    @Subscribe
    fun onPlayerJoin(event: LoginEvent) = runBlocking {
        val player = event.player
        val user = userRepository.initializeFromGameUser(player.uniqueId, player.username)
        val result = gameUserRepository.initialize(player.uniqueId, user.id, player.username, player.remoteAddress.hostString)

        if (result.firstJoin && registry[CatalystKeys.JOIN_LISTENER_ENABLED]) {
            proxyServer.sendMessage(registry[CatalystKeys.FIRST_JOIN].resolvePlaceholders(player))
        }

        staffListService.getStaffNames(
            player.username,
            player.hasPermission(registry[CatalystKeys.STAFFLIST_ADMIN_PERMISSION]),
            player.hasPermission(registry[CatalystKeys.STAFFLIST_STAFF_PERMISSION]),
            player.hasPermission(registry[CatalystKeys.STAFFLIST_OWNER_PERMISSION]),
        )
        val joinMessage = registry[CatalystKeys.JOIN_MESSAGE].resolvePlaceholders(player)
        if (registry[CatalystKeys.JOIN_LISTENER_ENABLED]) {
            proxyServer.sendMessage(joinMessage)
            logger.info(PlainTextComponentSerializer.plainText().serialize(joinMessage))
        }
    }
}
