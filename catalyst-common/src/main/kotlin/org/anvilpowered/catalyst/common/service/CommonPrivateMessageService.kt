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

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import com.google.inject.Singleton
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.registry.CatalystKeys.PRIVATE_MESSAGE_FORMAT
import org.anvilpowered.catalyst.api.service.PrivateMessageService
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Singleton
class CommonPrivateMessageService<TPlayer> @Inject constructor(
    private val userService: UserService<TPlayer, TPlayer>,
    private val registry: Registry,
) : PrivateMessageService {
    override var socialSpySet: MutableSet<UUID> = HashSet()
    override var replyMap: MutableMap<UUID, UUID> = HashMap()

    override fun sendMessage(sender: String, recipient: String, rawMessage: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val message = PrivateMessageService.Message.builder()
                .source(sender)
                .recipient(recipient)
                .rawMessage(rawMessage)
                .build()
            message.sourceMessage.sendTo(userService[sender])
            message.recipientMessage.sendTo(userService[recipient])
            socialSpy(sender, recipient, message.socialSpyMessage)
        }
    }

    override fun sendMessageFromConsole(recipient: String, rawMessage: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            LegacyComponentSerializer.legacyAmpersand().deserialize(
                registry.getOrDefault(PRIVATE_MESSAGE_FORMAT)
                    .replace("%sender%", "Console")
                    .replace("%recipient%", "Me")
                    .replace("%message%", rawMessage.trim { it <= ' ' })
            ).sendTo(userService[recipient])
        }
    }

    override fun socialSpy(source: String, recipient: String, component: Component): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
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
    }
}
