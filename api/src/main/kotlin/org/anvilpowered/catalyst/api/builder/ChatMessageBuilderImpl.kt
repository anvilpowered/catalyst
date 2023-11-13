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

package org.anvilpowered.catalyst.api.builder

import com.google.inject.Inject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.ChatMessage
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.ChatChannel
import org.anvilpowered.catalyst.api.service.ChannelService
import java.lang.IllegalStateException
import java.util.UUID
import java.util.concurrent.CompletableFuture

internal class ChatMessageBuilderImpl<TPlayer> : ChatMessage.Builder {

    @Inject
    private lateinit var channelService: ChannelService<TPlayer>

    @Inject
    private lateinit var locationService: LocationService

    @Inject
    private lateinit var memberManager: MemberManager

    @Inject
    private lateinit var userService: UserService<TPlayer, TPlayer>

    @Inject
    private lateinit var pluginMessages: PluginMessages

    var player: TPlayer? = null
    var uuid: UUID = UUID.randomUUID()
    var message: String = ""
    var prefix: String = ""
    var suffix: String = ""
    var color: String = ""
    var nameColor: String = ""
    var userName: String = ""
    var server: String = ""
    var channel: ChatChannel? = null
    var hasColorPermission: Boolean = false

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

    override fun color(color: String): ChatMessage.Builder {
        this.color = color
        return this
    }

    override fun nameColor(nameColor: String): ChatMessage.Builder {
        this.nameColor = nameColor
        return this
    }

    override fun userName(userName: String): ChatMessage.Builder {
        this.userName = userName
        return this
    }

    override fun server(server: String): ChatMessage.Builder {
        this.server = server
        return this
    }

    override fun uuid(uuid: UUID): ChatMessage.Builder {
        this.uuid = uuid
        this.player = userService.getPlayer(uuid)
        return this
    }

    override fun channel(channel: ChatChannel): ChatMessage.Builder {
        this.channel = channel
        return this
    }

    override fun hasColorPermission(hasPermission: Boolean): ChatMessage.Builder {
        this.hasColorPermission = hasPermission
        return this
    }

    fun formatMessage(
        prefix: String,
        nameColor: String,
        userName: String,
        userUUID: UUID,
        message: String,
        hasChatColorPermission: Boolean,
        suffix: String,
        serverName: String,
        channelId: String
    ): CompletableFuture<Component?> {
        val channel = channelService.fromId(channelId) ?: channelService.defaultChannel()
        ?: throw IllegalStateException("Invalid channel configuration!")
        val format = channel.format
        val hover = channel.hoverMessage
        val click = channel.click
        return memberManager.primaryComponent.getOneForUser(userUUID)
            .thenApplyAsync { member ->
                if (member == null) {
                    return@thenApplyAsync Component.text("Could not find a user matching that name!").color(NamedTextColor.RED)
                }
                if (member.isMuted) {
                    return@thenApplyAsync null
                }

                var finalName = member.userName
                finalName = if (member.nickName != "") {
                    member.nickName + "&r"
                } else {
                    "$nameColor$finalName&r"
                }
                if (hasChatColorPermission) {
                    return@thenApplyAsync Component.text()
                        .append(
                            LegacyComponentSerializer.legacyAmpersand().deserialize(
                                replacePlaceholders(
                                    message,
                                    prefix,
                                    member.userName,
                                    finalName,
                                    suffix,
                                    serverName,
                                    format
                                )
                            )
                        )
                        .hoverEvent(
                            HoverEvent.showText(
                                LegacyComponentSerializer.legacyAmpersand().deserialize(
                                    replacePlaceholders(
                                        message,
                                        prefix,
                                        member.userName,
                                        finalName,
                                        suffix,
                                        serverName,
                                        hover
                                    )
                                )
                            )
                        )
                        .clickEvent(
                            ClickEvent.suggestCommand(
                                replacePlaceholders(
                                    message,
                                    prefix,
                                    member.userName,
                                    userName,
                                    suffix,
                                    finalName,
                                    click
                                )
                            )
                        )
                        .build()
                }
                Component.text()
                    .append(
                        Component.text(
                            replacePlaceholders(
                                message,
                                prefix,
                                member.userName,
                                finalName,
                                suffix,
                                serverName,
                                format
                            )
                        )
                    )
                    .hoverEvent(
                        HoverEvent.showText(
                            Component.text(
                                replacePlaceholders(
                                    message,
                                    prefix,
                                    member.userName,
                                    finalName,
                                    suffix,
                                    serverName,
                                    hover
                                )
                            )
                        )
                    )
                    .clickEvent(
                        ClickEvent.suggestCommand(
                            replacePlaceholders(
                                message,
                                prefix,
                                member.userName,
                                userName,
                                suffix,
                                finalName,
                                click
                            )
                        )
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
        serverName: String,
        format: String
    ): String {
        return format
            .replace("%server%", locationService.getServer(rawUserName)?.name ?: "null")
            .replace("%servername%", serverName)
            .replace("%prefix%", prefix)
            .replace("%player%", userName)
            .replace("%suffix%", suffix)
            .replace("%message%", rawMessage)
    }


    override fun build(): ChatMessage {
        return formatMessage(
            prefix,
            nameColor,
            userName,
            uuid,
            message,
            hasColorPermission,
            suffix,
            server,
            channel?.id ?: ""
        ).thenApplyAsync { message ->
            if (message != null) {
                return@thenApplyAsync ChatMessage(uuid, message)
            } else {
                pluginMessages.muted.sendTo(userService.getPlayer(uuid)!!)
                return@thenApplyAsync ChatMessage(UUID.randomUUID(), Component.text(""))
            }
        }.join()
    }
}
