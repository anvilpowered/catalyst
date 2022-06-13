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
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.registry.CatalystKeys.PRIVATE_MESSAGE_FORMAT
import org.anvilpowered.catalyst.api.service.PrivateMessageService
import org.anvilpowered.anvil.api.registry.Registry
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Singleton
class CommonPrivateMessageService<TPlayer> @Inject constructor(
    private val userService: UserService<TPlayer, TPlayer>,
    private val registry: Registry,
) : PrivateMessageService {
    private val socialSpySet: Set<UUID> = HashSet()
    private val replyMap: Map<UUID, UUID> = HashMap()
    private lateinit var source: String
    private lateinit var recipient: String
    private lateinit var rawMessage: String

    override fun socialSpySet(): Set<UUID> = socialSpySet
    override fun replyMap(): Map<UUID, UUID> = replyMap
    override fun getSource(): String = source
    override fun getRawMessage(): String = rawMessage
    override fun getRecipient(): String = recipient

    override fun setSource(sourceUserName: String) {
        source = sourceUserName
    }

    override fun setRecipient(recipient: String) {
        this.recipient = recipient
    }

    override fun setRawMessage(rawMessage: String) {
        this.rawMessage = rawMessage
    }

    override fun formatMessage(sender: String, recipient: String, rawMessage: String): Component {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(
            registry.getOrDefault(PRIVATE_MESSAGE_FORMAT)
                .replace("%sender%", sender)
                .replace("%recipient%", recipient)
                .replace("%message%", rawMessage.trim { it <= ' ' })
        )
    }

    override fun sendMessage(sender: String, recipient: String, rawMessage: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            formatMessage("Me", recipient, rawMessage).sendTo(userService[sender])
            formatMessage(sender, "Me", rawMessage).sendTo(userService[recipient])
            socialSpy(sender, recipient, rawMessage)
        }
    }

    override fun sendMessageFromConsole(recipient: String, rawMessage: String, console: Class<*>?): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            formatMessage("Console", "Me", rawMessage).sendTo(userService[recipient])
        }
    }

    override fun socialSpy(sender: String, recipient: String, rawMessage: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            userService.onlinePlayers().forEach {
                if (socialSpySet.isEmpty() && !socialSpySet.contains(userService.getUUID(it))) {
                    return@forEach
                }
                if (userService.getUserName(it).equals(sender, ignoreCase = true)
                    || userService.getUserName(it).equals(recipient, ignoreCase = true)
                ) {
                    return@forEach
                }
                formatSocialSpyMessage(sender, recipient, rawMessage).sendTo(it)
            }
        }
    }

    override fun formatSocialSpyMessage(sender: String, recipient: String, rawMessage: String): Component {
        return Component.text()
            .append(Component.text("[SocialSpy] ").color(NamedTextColor.GRAY))
            .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
            .append(Component.text(sender).color(NamedTextColor.BLUE))
            .append(Component.text(" -> ").color(NamedTextColor.GOLD))
            .append(Component.text(recipient).color(NamedTextColor.BLUE))
            .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
            .append(Component.text(rawMessage.trim { it <= ' ' }).color(NamedTextColor.GRAY))
            .build()
    }
}
