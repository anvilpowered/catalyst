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

package org.anvilpowered.catalyst.velocity.chat

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.chat.PrivateMessage
import org.anvilpowered.catalyst.api.config.CatalystKeys
import java.util.UUID

context(ProxyServerScope, Registry.Scope, LoggerScope, LuckpermsService.Scope)
class PrivateMessageService {
    private var replyMap = mutableMapOf<UUID, UUID>()

    suspend fun sendMessage(source: Player, recipient: Player, content: Component) {
        val message = PrivateMessage(source, recipient, content)
        source.sendMessage(registry[CatalystKeys.PRIVATE_MESSAGE_SOURCE_FORMAT].resolvePlaceholders(message))
        recipient.sendMessage(registry[CatalystKeys.PRIVATE_MESSAGE_RECIPIENT_FORMAT].resolvePlaceholders(message))
        replyMap[recipient.uniqueId] = source.uniqueId
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
        val socialSpyMessage = registry[CatalystKeys.SOCIALSPY_MESSAGE_FORMAT].resolvePlaceholders(message)
        proxyServer.allPlayers
            .filter { it.hasPermission(registry[CatalystKeys.SOCIALSPY_PERMISSION]) }
            .filter { it != message.source && it != message.recipient }
            .forEach { it.sendMessage(socialSpyMessage) }
    }

    interface Scope {
        val privateMessageService: PrivateMessageService
    }
}
