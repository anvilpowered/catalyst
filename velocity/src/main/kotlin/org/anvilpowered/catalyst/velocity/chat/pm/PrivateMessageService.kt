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

package org.anvilpowered.catalyst.velocity.chat.pm

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.chat.ChatMessage
import org.anvilpowered.catalyst.api.config.CatalystKeys
import java.util.UUID

context(PlayerService.Scope, Registry.Scope)
class PrivateMessageService {
    var socialSpySet: MutableSet<UUID> = HashSet()
    var replyMap: MutableMap<UUID, UUID> = HashMap()

    suspend fun sendMessage(source: Player, recipient: Player, message: Component) {
        val message = ChatMessage.builder()
            .source(source)
            .recipient(recipient)
            .message(rawMessage)
            .build()
        val sourcePlayer playerService[source]
        message.source.sendTo(userService[source])
        message.recipient.sendTo(userService[recipient])
        socialSpy(source, recipient, message.message)
    }

    suspend fun sendMessageFromConsole(recipient: Player, message: Component) {
        LegacyComponentSerializer.legacyAmpersand().deserialize(
            registry[CatalystKeys.PRIVATE_MESSAGE_FORMAT]
                .replace("%sender%", "Console")
                .replace("%recipient%", "Me")
                .replace("%message%", rawMessage.trim { it <= ' ' })
        ).sendTo(userService[recipient])
    }

    suspend fun socialSpy(source: Player, recipient: Player, message: Component) {
        userService.onlinePlayers().forEach {
            if (socialSpySet.isEmpty() && !socialSpySet.contains(userService.getUUID(it))) {
                return@forEach
            }
            if (userService.getUserName(it).equals(source, ignoreCase = true)
                || userService.getUserName(it).equals(recipient, ignoreCase = true)
            ) {
                return@forEach
            }
            component.sendTo(it)
        }
    }

    interface Scope {
        val privateMessageService: PrivateMessageService
    }
}
