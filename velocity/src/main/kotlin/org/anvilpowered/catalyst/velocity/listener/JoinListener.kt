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
import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.user.GameUserRepository
import org.anvilpowered.catalyst.api.user.UserRepository
import org.anvilpowered.catalyst.velocity.chat.StaffListService
import org.apache.logging.log4j.Logger

class JoinListener(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val logger: Logger,
    private val staffListService: StaffListService,
    private val luckpermsService: LuckpermsService,
    private val userRepository: UserRepository,
    private val gameUserRepository: GameUserRepository,
) {
    @Subscribe
    fun onPlayerJoin(event: LoginEvent) = runBlocking {
        val player = event.player
        val user = userRepository.initializeFromGameUser(player.uniqueId, player.username)
        val result = gameUserRepository.initialize(player.uniqueId, user.id, player.username, player.remoteAddress.hostString)

        if (result.firstJoin && registry[catalystKeys.JOIN_LISTENER_ENABLED]) {
            proxyServer.sendMessage(registry[catalystKeys.FIRST_JOIN].resolve(proxyServer, logger, luckpermsService, player))
        }

        staffListService.getStaffNames(
            player.username,
            player.hasPermission(registry[catalystKeys.STAFFLIST_ADMIN_PERMISSION]),
            player.hasPermission(registry[catalystKeys.STAFFLIST_STAFF_PERMISSION]),
            player.hasPermission(registry[catalystKeys.STAFFLIST_OWNER_PERMISSION]),
        )
        val joinMessage = registry[catalystKeys.JOIN_MESSAGE].resolve(proxyServer, logger, luckpermsService, player)
        if (registry[catalystKeys.JOIN_LISTENER_ENABLED]) {
            proxyServer.sendMessage(joinMessage)
            logger.info(PlainTextComponentSerializer.plainText().serialize(joinMessage))
        }
    }
}
