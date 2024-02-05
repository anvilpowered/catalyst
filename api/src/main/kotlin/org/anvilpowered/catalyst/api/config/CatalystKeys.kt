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

@file:Suppress("MemberVisibilityCanBePrivate")

package org.anvilpowered.catalyst.api.config

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.core.config.Key
import org.anvilpowered.anvil.core.config.KeyBuilderDsl
import org.anvilpowered.anvil.core.config.KeyNamespace
import org.anvilpowered.anvil.core.config.ListKey
import org.anvilpowered.anvil.core.config.SimpleKey
import org.anvilpowered.anvil.core.config.buildingList
import org.anvilpowered.anvil.core.config.buildingMap
import org.anvilpowered.anvil.core.config.buildingSimple
import org.anvilpowered.catalyst.api.chat.placeholder.MessageFormat
import org.anvilpowered.catalyst.api.chat.placeholder.MiniMessageSerializer
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import org.anvilpowered.catalyst.api.chat.placeholder.PlayerFormat
import org.anvilpowered.catalyst.api.chat.placeholder.PrivateMessageFormat
import org.anvilpowered.catalyst.api.chat.placeholder.ProxyFormat

@Suppress("PropertyName")
class CatalystKeys(
    private val chatChannelBuilderFactory: ChatChannel.Builder.Factory,
) : KeyNamespace by KeyNamespace.create("CATALYST") {

    @KeyBuilderDsl
    private fun SimpleKey.BuilderFacet<Component, *>.miniMessageFallback(fallbackValue: Component) {
        fallback(fallbackValue)
        serializer(MiniMessageSerializer)
    }

    @KeyBuilderDsl
    private fun ListKey.BuilderFacet<Component, *>.miniMessageListFallback(fallbackValue: List<Component>) {
        fallback(fallbackValue)
        elementSerializer(MiniMessageSerializer)
    }

    @KeyBuilderDsl
    private fun <
        M : MessageFormat,
        P : MessageFormat.Placeholders<M>,
        B : MessageFormat.Builder<P, M>,
        > SimpleKey.BuilderFacet<M, *>.miniMessageFallbackFormat(
        builder: B,
        block: P.() -> Component,
    ) {
        fallback(builder.build(block))
    }

    @KeyBuilderDsl
    private fun <
        M : MessageFormat,
        P : MessageFormat.Placeholders<M>,
        B : MessageFormat.Builder<P, M>,
        > ListKey.BuilderFacet<M, *>.miniMessageListFallbackFormat(
        builder: B,
        blocks: List<P.() -> Component>,
    ) {
        fallback(blocks.map { builder.build(it) })
    }

    val DB_TYPE by Key.buildingSimple {
        fallback("postgres")
    }

    val DB_URL by Key.buildingSimple {
        fallback("jdbc:postgresql://db:5432/catalyst")
    }

    val DB_USER by Key.buildingSimple {
        fallback("catalyst")
    }

    val DB_PASSWORD by Key.buildingSimple {
        fallback("catalyst")
    }

    val CHAT_ENABLED by Key.buildingSimple {
        fallback(true)
    }

    val CHAT_FILTER_ENABLED by Key.buildingSimple {
        fallback(false)
    }

    val CHAT_FILTER_SWEARS by Key.buildingList {
        fallback(listOf("fuck", "shit", "ass"))
    }

    val CHAT_FILTER_EXCEPTIONS by Key.buildingList {
        fallback(listOf("assassin", "jkass"))
    }

    val CHAT_DM_FORMAT_SOURCE by Key.buildingSimple {
        miniMessageFallbackFormat(PrivateMessageFormat) {
            // TODO: Nice builder api with + unary operator
            Component.text().append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("me(${source.backend.name})").color(NamedTextColor.BLUE))
                .append(Component.text(" -> ").color(NamedTextColor.GOLD))
                .append(Component.text("${recipient.displayname}(${recipient.backend.name})").color(NamedTextColor.BLUE))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY)).append(Component.text(content).color(NamedTextColor.GRAY))
                .build()
        }
    }

    val CHAT_DM_FORMAT_RECIPIENT by Key.buildingSimple {
        miniMessageFallbackFormat(PrivateMessageFormat) {
            Component.text().append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("${source.displayname}(${source.backend.name})").color(NamedTextColor.BLUE))
                .append(Component.text(" -> ").color(NamedTextColor.GOLD))
                .append(Component.text("me(${recipient.backend.name})").color(NamedTextColor.BLUE))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY)).append(Component.text(content).color(NamedTextColor.GRAY))
                .build()
        }
    }

    val CHAT_DM_FORMAT_SOCIALSPY by Key.buildingSimple {
        miniMessageFallbackFormat(PrivateMessageFormat) {
            Component.text().append(Component.text("[SocialSpy] ").color(NamedTextColor.GRAY))
                .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(source.displayname).color(NamedTextColor.BLUE))
                .append(Component.text(" -> ").color(NamedTextColor.GOLD))
                .append(Component.text(recipient.displayname).color(NamedTextColor.BLUE))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY)).append(Component.text(content).color(NamedTextColor.GRAY))
                .build()
        }
    }

    val CHAT_DISCORD_ENABLED by Key.buildingSimple {
        fallback(false)
    }

    val CHAT_DISCORD_BOT_NAME by Key.buildingSimple {
        fallback("System")
    }

    val CHAT_DISCORD_BOT_AVATAR by Key.buildingSimple {
        fallback("https://crafthead.net/avatar/%uuid%")
    }

    val CHAT_DISCORD_BOT_TOKEN by Key.buildingSimple {
        fallback("bot token")
    }

    val CHAT_DISCORD_BOT_STATUS by Key.buildingSimple {
        fallback("A Minecraft Server!")
    }

    val CHAT_DISCORD_USERNAME_FORMAT by Key.buildingSimple {
        miniMessageFallbackFormat(OnlineUserFormat) {
            Component.text("[${backend.name}] $prefix$displayname$suffix")
        }
    }

    // TODO: Use ChannelFormat
    val CHAT_DISCORD_MESSAGE_FORMAT by Key.buildingSimple {
        miniMessageFallback(
            Component.text()
                .append(Component.text("%channel.name% "))
                .append(Component.text("[Discord] ", NamedTextColor.GOLD))
                .append(Component.text("%name%: "))
                .append(Component.text("%content%"))
                .color(NamedTextColor.GRAY)
                .build(),
        )
    }

    val CHAT_DISCORD_TOPIC_FORMAT by Key.buildingSimple {
        fallback("Player Count: %players%")
    }

    val CHAT_DISCORD_TOPIC_ENABLED by Key.buildingSimple {
        fallback(false)
    }

    val CHAT_DISCORD_TOPIC_REFRESHRATE by Key.buildingSimple {
        fallback(5)
    }

    val CHAT_DISCORD_TOPIC_NOPLAYERS by Key.buildingSimple {
        fallback("There are no players online!")
    }

    val CHAT_DISCORD_INVITE by Key.buildingSimple {
        fallback("https://discord.gg/8RUzuwu")
    }

    val CHAT_DISCORD_HOVER_MESSAGE by Key.buildingSimple {
        miniMessageFallback(Component.text("Click here to join our discord!"))
    }

    val CHAT_NICKNAME_PREFIX by Key.buildingSimple {
        fallback(Component.text("~"))
    }

    val CHAT_DEFAULT_CHANNEL by Key.buildingSimple {
        fallback("global")
    }

    val CHAT_CHANNELS by Key.buildingMap {
        fallback(
            mapOf(
                "global" to chatChannelBuilderFactory.build {
                    id("global")
                    name(Component.text("[Global]").color(NamedTextColor.GREEN))
                    commandAliases(listOf("g", "global"))
                    alwaysVisible(true)
                    discordChannelId("123456789")
                    passThrough(false)
                },
                "staff" to chatChannelBuilderFactory.build {
                    id("staff")
                    name(Component.text("[Staff]").color(NamedTextColor.AQUA))
                    messageFormat {
                        Component.text()
                            .append(Component.text(channel.name))
                            .append(Component.space())
                            .append(Component.text(name))
                            .append(Component.text(": "))
                            .append(Component.text(content))
                            .color(NamedTextColor.AQUA)
                            .build()
                    }
                    commandAliases(listOf("s", "staff"))
                    alwaysVisible(true)
                    discordChannelId("123456789")
                    passThrough(false)
                },
            ),
        )
    }

    val TAB_ENABLED by Key.buildingSimple {
        fallback(true)
    }

    val TAB_HEADER by Key.buildingSimple {
        miniMessageFallbackFormat(PlayerFormat) {
            Component.text("Welcome to")
        }
    }

    val TAB_FOOTER by Key.buildingSimple {
        miniMessageFallbackFormat(PlayerFormat) {
            Component.text("A Velocity Server")
        }
    }

    val TAB_FORMAT_PLAYER by Key.buildingSimple {
        miniMessageFallbackFormat(PlayerFormat) {
            Component.text("$prefix$username$suffix")
        }
    }

    val TAB_FORMAT_CUSTOM by Key.buildingList {
        miniMessageListFallbackFormat(
            PlayerFormat,
            listOf(
                {
                    Component.text().append(Component.text("Your latency").color(NamedTextColor.DARK_AQUA))
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Component.text(latency).color(NamedTextColor.YELLOW)).build()
                },
                {
                    Component.text().append(Component.text("Current Server").color(NamedTextColor.DARK_AQUA))
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Component.text(backend.name).color(NamedTextColor.YELLOW)).build()
                },
                {
                    Component.text().append(Component.text("Player Count").color(NamedTextColor.DARK_AQUA))
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Component.text(proxy.playerCount).color(NamedTextColor.YELLOW)).build()
                },
            ),
        )
    }

    val TAB_REFRESHRATE by Key.buildingSimple {
        fallback(1)
    }

    val JOIN_LISTENER_ENABLED by Key.buildingSimple {
        fallback(true)
    }

    val JOIN_MESSAGE_FIRST by Key.buildingSimple {
        miniMessageFallbackFormat(OnlineUserFormat) {
            Component.text().append(Component.text("Welcome to the server, "))
                .append(Component.text(displayname).color(NamedTextColor.AQUA)).append(Component.text("!")).color(NamedTextColor.GREEN)
                .build()
        }
    }

    val JOIN_MESSAGE_NORMAL by Key.buildingSimple {
        miniMessageFallbackFormat(OnlineUserFormat) {
            Component.text().append(Component.text(displayname).color(NamedTextColor.GOLD))
                .append(Component.text(" has joined the network").color(NamedTextColor.GRAY)).build()
        }
    }

    val LEAVE_LISTENER_ENABLED by Key.buildingSimple {
        fallback(true)
    }

    val LEAVE_MESSAGE by Key.buildingSimple {
        miniMessageFallbackFormat(OnlineUserFormat) {
            Component.text().append(Component.text(displayname).color(NamedTextColor.GOLD))
                .append(Component.text(" has left the network").color(NamedTextColor.GRAY)).build()
        }
    }

    val SERVER_WEBSITE by Key.buildingSimple {
        fallback("https://www.anvilpowered.org")
    }

    val SERVER_PING_TYPE by Key.buildingSimple {
        description("Choose one of 'players' or 'message'. You may configure the message with SERVER_PING_MESSAGE")
        fallback("players")
    }

    val SERVER_PING_MESSAGE by Key.buildingSimple {
        fallback("Change this message in the config!")
    }

    val SERVER_MOTD_MESSAGE by Key.buildingSimple {
        miniMessageFallbackFormat(ProxyFormat) {
            Component.text("A Velocity Proxy running version $version!").color(NamedTextColor.DARK_AQUA)
        }
    }

    val SERVER_MOTD_ENABLED by Key.buildingSimple {
        fallback(false)
    }

    val COMMAND_LOGGING_ENABLED by Key.buildingSimple {
        fallback(true)
    }

    // TODO: Regex?
    val COMMAND_LOGGING_FILTER by Key.buildingList {
        description("A list of root commands to log. Use * to log all commands.")
        fallback(listOf("*"))
    }

    val CATALYST_PREFIX by Key.buildingSimple {
        fallback("Catalyst")
    }

    val PERMISSION_BROADCAST by Key.buildingSimple {
        fallback("catalyst.command.broadcast")
    }

    val PERMISSION_CHANNEL_EDIT by Key.buildingSimple {
        fallback("catalyst.command.channel.edit")
    }

    val PERMISSION_CHAT_COLOR by Key.buildingSimple {
        fallback("catalyst.chat.color")
    }

    val PERMISSION_FIND by Key.buildingSimple {
        fallback("catalyst.command.find")
    }

    val PERMISSION_IGNORE_BASE by Key.buildingSimple {
        fallback("catalyst.command.ignore.base")
    }

    val PERMISSION_IGNORE_EXEMPT by Key.buildingSimple {
        fallback("catalyst.command.ignore.exempt")
    }

    val PERMISSION_INFO_BASE by Key.buildingSimple {
        fallback("catalyst.command.info.base")
    }

    val PERMISSION_INFO_IP by Key.buildingSimple {
        fallback("catalyst.command.info.ip")
    }

    val PERMISSION_INFO_CHANNEL by Key.buildingSimple {
        fallback("catalyst.command.info.channel")
    }

    val PERMISSION_KICK_BASE by Key.buildingSimple {
        fallback("catalyst.command.kick.base")
    }

    val PERMISSION_KICK_EXEMPT by Key.buildingSimple {
        fallback("catalyst.command.kick.exempt")
    }

    val PERMISSION_LANGUAGE_ADMIN by Key.buildingSimple {
        fallback("catalyst.command.language.admin")
    }

    val PERMISSION_LANGUAGE_LIST by Key.buildingSimple {
        fallback("catalyst.command.language.list")
    }

    val PERMISSION_LIST by Key.buildingSimple {
        fallback("catalyst.command.list")
    }

    val PERMISSION_MESSAGE by Key.buildingSimple {
        fallback("catalyst.command.message")
    }

    val PERMISSION_MUTE_BASE by Key.buildingSimple {
        fallback("catalyst.command.mute.base")
    }

    val PERMISSION_MUTE_EXEMPT by Key.buildingSimple {
        fallback("catalyst.command.mute.exempt")
    }

    val PERMISSION_NICKNAME_BASE by Key.buildingSimple {
        fallback("catalyst.command.nickname.base")
    }

    val PERMISSION_NICKNAME_COLOR by Key.buildingSimple {
        fallback("catalyst.command.nickname.color")
    }

    val PERMISSION_NICKNAME_MAGIC by Key.buildingSimple {
        fallback("catalyst.command.nickname.magic")
    }

    val PERMISSION_SEND by Key.buildingSimple {
        fallback("catalyst.admin.command.send")
    }

    val PERMISSION_SOCIALSPY_BASE by Key.buildingSimple {
        fallback("catalyst.admin.command.socialspy,base")
    }

    val PERMISSION_SOCIALSPY_ONJOIN by Key.buildingSimple {
        fallback("catalyst.admin.command.socialspy.onjoin")
    }

    val PERMISSION_STAFFLIST_BASE by Key.buildingSimple {
        fallback("catalyst.stafflist.base")
    }

    val PERMISSION_STAFFLIST_STAFF by Key.buildingSimple {
        fallback("catalyst.stafflist.staff")
    }

    val PERMISSION_STAFFLIST_ADMIN by Key.buildingSimple {
        fallback("catalyst.stafflist.admin")
    }

    val PERMISSION_STAFFLIST_OWNER by Key.buildingSimple {
        fallback("catalyst.stafflist.owner")
    }

    val PERMISSION_CHAT_TOGGLE by Key.buildingSimple {
        fallback("catalyst.chat.toggle")
    }

    val PERMISSION_CHANNEL_BASE by Key.buildingSimple {
        description("Permission to use the /channel command")
        fallback("catalyst.channel.base")
    }

    val PERMISSION_CHANNEL_SPYALL by Key.buildingSimple {
        description("Permission to see always see all channels")
        fallback("catalyst.channel.spyall")
    }

    val PERMISSION_CHANNEL_PREFIX by Key.buildingSimple {
        description(
            """
            Permission prefix for channels. For example, if the prefix is 'catalyst.channel.access' (the default), then the permission for
            the channel with id 'foo' would be 'catalyst.channel.access.foo'.

            Players always have permission to join the default channel (default 'global').
            """.trimIndent(),
        )
        fallback("catalyst.channel.access")
    }
}
