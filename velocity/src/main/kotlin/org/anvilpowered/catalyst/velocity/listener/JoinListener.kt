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
import com.velocitypowered.api.event.connection.PostLoginEvent
import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.user.MinecraftUser
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.anvilpowered.catalyst.velocity.chat.StaffListService
import org.anvilpowered.catalyst.velocity.discord.WebhookSender
import org.apache.logging.log4j.Logger

class JoinListener(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val logger: Logger,
    private val channelService: ChannelService,
    private val webhookSender: WebhookSender,
    private val staffListService: StaffListService,
    private val minecraftUserRepository: MinecraftUserRepository,
    private val onlineUserFormatResolver: OnlineUserFormat.Resolver,
) {
    @Subscribe
    fun onPlayerJoin(event: PostLoginEvent) = runBlocking {
        val player = event.player
        val result = minecraftUserRepository.put(
            MinecraftUser.CreateDto(
                id = player.uniqueId,
                username = player.username,
                ipAddress = player.remoteAddress.hostString,
            ),
        )

        val user = MinecraftUser.Online(result.entity, player)

        if (result.created && registry[catalystKeys.JOIN_LISTENER_ENABLED]) {
            proxyServer.sendMessage(onlineUserFormatResolver.resolve(registry[catalystKeys.FIRST_JOIN], user))
        }

        staffListService.getStaffNames(
            player.username,
            player.hasPermission(registry[catalystKeys.STAFFLIST_ADMIN_PERMISSION]),
            player.hasPermission(registry[catalystKeys.STAFFLIST_STAFF_PERMISSION]),
            player.hasPermission(registry[catalystKeys.STAFFLIST_OWNER_PERMISSION]),
        )
        val joinMessage = onlineUserFormatResolver.resolve(registry[catalystKeys.JOIN_MESSAGE], user)
        if (registry[catalystKeys.JOIN_LISTENER_ENABLED]) {
            proxyServer.sendMessage(joinMessage)
            logger.info(PlainTextComponentSerializer.plainText().serialize(joinMessage))
        }
        if (registry[catalystKeys.DISCORD_ENABLED]) {
            val discordChannel = channelService.getForPlayer(user.player.uniqueId)
            webhookSender.sendSpecialMessage(user, discordChannel.discordChannelId, catalystKeys.JOIN_MESSAGE)
        }
    }
}
