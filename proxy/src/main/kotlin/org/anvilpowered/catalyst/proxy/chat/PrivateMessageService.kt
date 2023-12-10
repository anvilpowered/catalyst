/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
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

package org.anvilpowered.catalyst.proxy.chat

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.chat.PrivateMessage
import org.anvilpowered.catalyst.api.chat.placeholder.PrivateMessageFormat
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.user.MinecraftUser
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.apache.logging.log4j.Logger
import java.util.UUID

class PrivateMessageService(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val logger: Logger,
    private val luckpermsService: LuckpermsService,
    private val minecraftUserRepository: MinecraftUserRepository,
    private val privateMessageFormatResolver: PrivateMessageFormat.Resolver,
) {
    private var replyMap = mutableMapOf<UUID, UUID>()

    suspend fun sendMessage(source: Player, recipient: Player, content: Component) {
        val sourceUser = minecraftUserRepository.getById(source.uniqueId)?.let { MinecraftUser.Online(it, source) }
            ?: throw IllegalStateException("User ${source.username} with id ${source.uniqueId} is not in the database!")

        val recipientUser = minecraftUserRepository.getById(recipient.uniqueId)?.let { MinecraftUser.Online(it, recipient) }
            ?: throw IllegalStateException("User ${recipient.username} with id ${recipient.uniqueId} is not in the database!")

        val message = PrivateMessage(sourceUser, recipientUser, content)

        source.sendMessage(privateMessageFormatResolver.resolve(registry[catalystKeys.PRIVATE_MESSAGE_SOURCE_FORMAT], message))
        recipient.sendMessage(privateMessageFormatResolver.resolve(registry[catalystKeys.PRIVATE_MESSAGE_RECIPIENT_FORMAT], message))
        replyMap[recipient.uniqueId] = source.uniqueId
        replyMap[source.uniqueId] = recipient.uniqueId
        socialSpy(message)
    }

    suspend fun reply(source: Player, content: Component) {
        val recipientId = replyMap[source.uniqueId]
        if (recipientId == null) {
            source.sendMessage(Component.text("No person to reply to.").color(NamedTextColor.RED))
            return
        }

        val recipient = proxyServer.getPlayer(recipientId).orElse(null)
        if (recipient == null) {
            source.sendMessage(Component.text("The person you last replied to is offline.").color(NamedTextColor.RED))
            return
        }

        sendMessage(source, recipient, content)
    }

    // TODO: Send PM from console

    private suspend fun socialSpy(message: PrivateMessage) {
        val socialSpyMessage = privateMessageFormatResolver.resolve(registry[catalystKeys.SOCIALSPY_MESSAGE_FORMAT], message)
        proxyServer.allPlayers
            .filter { it.hasPermission(registry[catalystKeys.SOCIALSPY_PERMISSION]) }
            .filter { it.uniqueId != message.source.player.uniqueId && it.uniqueId != message.recipient.player.uniqueId }
            .forEach { it.sendMessage(socialSpyMessage) }
    }
}
