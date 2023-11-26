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
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.anvilpowered.catalyst.api.user.getOnlineUser
import org.anvilpowered.catalyst.velocity.chat.StaffListService
import org.apache.logging.log4j.Logger

class LeaveListener(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val logger: Logger,
    private val staffListService: StaffListService,
    private val minecraftUserRepository: MinecraftUserRepository,
    private val onlineUserFormatResolver: OnlineUserFormat.Resolver,
) {
    @Subscribe
    fun onPlayerLeave(event: DisconnectEvent) = runBlocking {
        logger.info("Player ${event.player.username} left the server.")
        if (event.loginStatus == DisconnectEvent.LoginStatus.PRE_SERVER_JOIN) {
            return@runBlocking
        }
        val player = event.player
        val user = minecraftUserRepository.getOnlineUser(player)
        staffListService.removeStaffNames(player.username)
        val leaveMessage = onlineUserFormatResolver.resolve(registry[catalystKeys.LEAVE_MESSAGE], user)

        if (registry[catalystKeys.LEAVE_LISTENER_ENABLED]) {
            logger.info("Sending leave message to all players...")
            proxyServer.sendMessage(leaveMessage)
            logger.info(PlainTextComponentSerializer.plainText().serialize(leaveMessage))
        }
    }
}
