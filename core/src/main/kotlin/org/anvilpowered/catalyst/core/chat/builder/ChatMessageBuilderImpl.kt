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

package org.anvilpowered.catalyst.core.chat.builder

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.config.ChatChannel
import org.anvilpowered.catalyst.api.user.GameUser
import org.anvilpowered.catalyst.core.PluginMessages
import org.anvilpowered.catalyst.core.chat.ChatMessage
import java.util.UUID

context(PlayerService.Scope)
internal class ChatMessageBuilderImpl : ChatMessage.Builder {

    private var user: GameUser? = null
    private var player: Player? = null
    private var channel: ChatChannel? = null
    private var message: Component? = null
    private var prefix: Component? = null
    private var suffix: Component? = null
    private var messageFormatOverride: Component? = null
    private var nameFormatOverride: Component? = null
    private var serverName: String = ""

    override fun user(user: GameUser): ChatMessage.Builder {
        player = requireNotNull(playerService[user.id]) { "User ${user.username} with id ${user.id} is not on the server!" }
        this.user = user
        return this
    }

    override fun channel(channel: ChatChannel): ChatMessage.Builder {
        this.channel = channel
        return this
    }

    override fun message(message: Component): ChatMessage.Builder {
        this.message = message
        return this
    }

    override fun prefix(prefix: Component): ChatMessage.Builder {
        this.prefix = prefix
        return this
    }

    override fun suffix(suffix: Component): ChatMessage.Builder {
        this.suffix = suffix
        return this
    }

    override fun messageFormatOverride(format: Component?): ChatMessage.Builder {
        this.messageFormatOverride = format
        return this
    }

    override fun nameFormatOverride(format: Component?): ChatMessage.Builder {
        this.nameFormatOverride = format
        return this
    }

    override fun server(server: String): ChatMessage.Builder {
        this.serverName = server
        return this
    }

    private fun formatMessage(): Component? {
        val user = requireNotNull(user) { "User is null" }
        val player = requireNotNull(player) { "Player is null" }
        val channel = requireNotNull(channel) { "Channel is null" }
        val message = requireNotNull(message) { "Message is null" }
        val prefix = requireNotNull(prefix) { "Prefix is null" }
        val suffix = requireNotNull(suffix) { "Prefix is null" }

        // TODO: Check muted
        if (false) {
            return null
        }

        val processedDisplayName: Component = channel.nameFormat.replaceText {
            it.matchLiteral("%name%")
            val nickname = user.nickname
            if (nickname.isNullOrEmpty()) {
                it.replacement(player.displayname)
            } else {
                it.replacement(nickname)
            }
        }

        return Component.text()
            .append(channel.messageFormat.replacePlaceholders(message, prefix, suffix, processedDisplayName, serverName))
            .hoverEvent(
                HoverEvent.showText(channel.hoverFormat.replacePlaceholders(message, prefix, suffix, processedDisplayName, serverName)),
            ).clickEvent(
                ClickEvent.suggestCommand(
                    channel.clickFormat.replacePlaceholders(message, prefix, suffix, processedDisplayName, serverName),
                ),
            )
            .build()
    }

    private fun Component.replacePlaceholders(
        message: Component,
        prefix: Component,
        suffix: Component,
        displayName: Component,
        serverName: String,
    ): Component {
        return replaceText {
            it.matchLiteral("%server%")
            it.replacement(serverName)
        }.replaceText {
            it.matchLiteral("%prefix%")
            it.replacement(prefix)
        }.replaceText {
            it.matchLiteral("%suffix%")
            it.replacement(suffix)
        }.replaceText {
            it.matchLiteral("%user%")
            it.replacement(displayName)
        }.replaceText {
            it.matchLiteral("%message%")
            it.replacement(message)
        }
    }

    private fun String.replacePlaceholders(
        message: Component,
        prefix: Component,
        suffix: Component,
        displayName: Component,
        serverName: String,
    ): String {
        // TODO: Keep colors?
        fun Component.toPlain(): String = PlainTextComponentSerializer.plainText().serialize(this)
        return replace("%server%", serverName)
            .replace("%prefix%", prefix.toPlain())
            .replace("%suffix%", suffix.toPlain())
            .replace("%user%", displayName.toPlain())
            .replace("%message%", message.toPlain())
    }

    override fun build(): ChatMessage {
        val user = requireNotNull(user) { "User is null" }
        val player = requireNotNull(player) { "Player is null" }
        val message = formatMessage()

        return if (message == null) {
            player.sendMessage(PluginMessages.muted)
            ChatMessage(UUID.randomUUID(), Component.text(""))
        } else {
            ChatMessage(user.id, message)
        }
    }
}
