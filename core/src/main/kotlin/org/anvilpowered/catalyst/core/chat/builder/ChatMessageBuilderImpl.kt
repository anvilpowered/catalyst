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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
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
    private var message: String = ""
    private var prefix: String = ""
    private var suffix: String = ""
    private var messageColor: String = ""
    private var nameColor: String = ""
    private var server: String = ""
    private var channel: ChatChannel? = null
    private var hasColorPermission: Boolean = false

    override fun user(user: GameUser): ChatMessage.Builder {
        player = requireNotNull(playerService[user.id]) { "User ${user.username} with id ${user.id} is not on the server!" }
        this.user = user
        return this
    }

    override fun channel(channel: ChatChannel): ChatMessage.Builder {
        this.channel = channel
        return this
    }

    override fun message(message: String): ChatMessage.Builder {
        this.message = message
        return this
    }

    override fun prefix(prefix: String): ChatMessage.Builder {
        this.prefix = prefix
        return this
    }

    override fun suffix(suffix: String): ChatMessage.Builder {
        this.suffix = suffix
        return this
    }

    override fun messageColor(color: String): ChatMessage.Builder {
        this.messageColor = color
        return this
    }

    override fun nameColor(nameColor: String): ChatMessage.Builder {
        this.nameColor = nameColor
        return this
    }

    override fun server(server: String): ChatMessage.Builder {
        this.server = server
        return this
    }

    override fun hasColorPermission(hasPermission: Boolean): ChatMessage.Builder {
        this.hasColorPermission = hasPermission
        return this
    }

    private fun formatMessage(): Component? {
        val user = requireNotNull(user) { "User is null" }
        val channel = requireNotNull(channel) { "Channel is null" }

        val format = channel.format
        val hover = channel.hoverMessage
        val click = channel.click

        // TODO: Check muted
        if (false) {
            return null
        }

        val finalName = if (user.nickname.isNullOrEmpty()) {
            "$nameColor${user.username}&r"
        } else {
            user.nickname + "&r"
        }

        // TODO: Clean this up
        return if (hasColorPermission) {
            Component.text()
                .append(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(
                        replacePlaceholders(
                            message,
                            prefix,
                            user.username,
                            finalName,
                            suffix,
                            server,
                            format,
                        ),
                    ),
                )
                .hoverEvent(
                    HoverEvent.showText(
                        LegacyComponentSerializer.legacyAmpersand().deserialize(
                            replacePlaceholders(
                                message,
                                prefix,
                                user.username,
                                finalName,
                                suffix,
                                server,
                                hover,
                            ),
                        ),
                    ),
                )
                .clickEvent(
                    ClickEvent.suggestCommand(
                        replacePlaceholders(
                            message,
                            prefix,
                            user.username,
                            finalName,
                            suffix,
                            finalName,
                            click,
                        ),
                    ),
                )
                .build()
        } else {
            Component.text()
                .append(
                    Component.text(
                        replacePlaceholders(
                            message,
                            prefix,
                            user.username,
                            finalName,
                            suffix,
                            server,
                            format,
                        ),
                    ),
                )
                .hoverEvent(
                    HoverEvent.showText(
                        Component.text(
                            replacePlaceholders(
                                message,
                                prefix,
                                user.username,
                                finalName,
                                suffix,
                                server,
                                hover,
                            ),
                        ),
                    ),
                )
                .clickEvent(
                    ClickEvent.suggestCommand(
                        replacePlaceholders(
                            message,
                            prefix,
                            user.username,
                            finalName,
                            suffix,
                            finalName,
                            click,
                        ),
                    ),
                )
                .build()
        }
    }

    private fun replacePlaceholders(
        rawMessage: String,
        prefix: String,
        rawUserName: String,
        userName: String,
        suffix: String,
        server: String,
        format: String,
    ): String {
        // TODO: rawUserName?
        return format
            .replace("%server%", server)
            .replace("%prefix%", prefix)
            .replace("%player%", userName)
            .replace("%suffix%", suffix)
            .replace("%message%", rawMessage)
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
