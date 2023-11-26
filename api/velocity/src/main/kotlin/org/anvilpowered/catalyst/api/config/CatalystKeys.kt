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

@file:Suppress("MemberVisibilityCanBePrivate")

package org.anvilpowered.catalyst.api.config

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.anvilpowered.anvil.core.config.Key
import org.anvilpowered.anvil.core.config.KeyBuilderDsl
import org.anvilpowered.anvil.core.config.KeyNamespace
import org.anvilpowered.anvil.core.config.ListKey
import org.anvilpowered.anvil.core.config.SimpleKey
import org.anvilpowered.catalyst.api.chat.placeholder.MessageFormat
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
        serializer { MiniMessage.miniMessage().serialize(it.asComponent()) }
        deserializer { MiniMessage.miniMessage().deserialize(it) }
    }

    @KeyBuilderDsl
    private fun ListKey.BuilderFacet<Component, *>.miniMessageListFallback(fallbackValue: List<Component>) {
        fallback(fallbackValue)
        elementSerializer { MiniMessage.miniMessage().serialize(it.asComponent()) }
        elementDeserializer { MiniMessage.miniMessage().deserialize(it) }
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
        serializer { MiniMessage.miniMessage().serialize(it.format) }
        deserializer { builder.build { MiniMessage.miniMessage().deserialize(it) } }
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
        elementSerializer { MiniMessage.miniMessage().serialize(it.format) }
        elementDeserializer { builder.build { MiniMessage.miniMessage().deserialize(it) } }
    }

    val DB_URL by Key.buildingSimple(TypeTokens.STRING) {
        fallback("jdbc:postgresql://db:5432/catalyst")
    }

    val DB_USER by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst")
    }

    val DB_PASSWORD by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst")
    }

    val CHAT_FILTER_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(false)
    }

    val CHAT_FILTER_SWEARS by Key.buildingList(TypeTokens.STRING) {
        fallback(listOf("fuck", "shit", "ass"))
    }

    val CHAT_FILTER_EXCEPTIONS by Key.buildingList(TypeTokens.STRING) {
        fallback(listOf("assassin", "jkass"))
    }

    val FIRST_JOIN by Key.buildingSimple(TypeTokens.ONLINE_USER_FORMAT) {
        miniMessageFallbackFormat(OnlineUserFormat) {
            Component.text()
                .append(Component.text("Welcome to the server, "))
                .append(Component.text(displayname).color(NamedTextColor.AQUA))
                .append(Component.text("!"))
                .color(NamedTextColor.GREEN)
                .build()
        }
    }

    val JOIN_MESSAGE by Key.buildingSimple(TypeTokens.ONLINE_USER_FORMAT) {
        miniMessageFallbackFormat(OnlineUserFormat) {
            Component.text()
                .append(Component.text(displayname).color(NamedTextColor.GOLD))
                .append(Component.text(" has joined the network").color(NamedTextColor.GRAY))
                .build()
        }
    }

    val JOIN_LISTENER_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val LEAVE_MESSAGE by Key.buildingSimple(TypeTokens.ONLINE_USER_FORMAT) {
        miniMessageFallbackFormat(OnlineUserFormat) {
            Component.text()
                .append(Component.text(displayname).color(NamedTextColor.GOLD))
                .append(Component.text(" has left the network").color(NamedTextColor.GRAY))
                .build()
        }
    }

    val LEAVE_LISTENER_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val PROXY_CHAT_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val PRIVATE_MESSAGE_SOURCE_FORMAT by Key.buildingSimple(TypeTokens.PRIVATE_MESSAGE_FORMAT) {
        miniMessageFallbackFormat(PrivateMessageFormat) {
            // TODO: Nice builder api with + unary operator
            Component.text()
                .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("me(${source.backend.name})").color(NamedTextColor.BLUE))
                .append(Component.text(" -> ").color(NamedTextColor.GOLD))
                .append(Component.text("${recipient.displayname}(${recipient.backend.name})").color(NamedTextColor.BLUE))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(content).color(NamedTextColor.GRAY))
                .build()
        }
    }

    val PRIVATE_MESSAGE_RECIPIENT_FORMAT by Key.buildingSimple(TypeTokens.PRIVATE_MESSAGE_FORMAT) {
        miniMessageFallbackFormat(PrivateMessageFormat) {
            Component.text()
                .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                .append(Component.text("${source.displayname}(${source.backend.name})").color(NamedTextColor.BLUE))
                .append(Component.text(" -> ").color(NamedTextColor.GOLD))
                .append(Component.text("me(${recipient.backend.name})").color(NamedTextColor.BLUE))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(content).color(NamedTextColor.GRAY))
                .build()
        }
    }

    val SOCIALSPY_MESSAGE_FORMAT by Key.buildingSimple(TypeTokens.PRIVATE_MESSAGE_FORMAT) {
        miniMessageFallbackFormat(PrivateMessageFormat) {
            Component.text()
                .append(Component.text("[SocialSpy] ").color(NamedTextColor.GRAY))
                .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(source.displayname).color(NamedTextColor.BLUE))
                .append(Component.text(" -> ").color(NamedTextColor.GOLD))
                .append(Component.text(recipient.displayname).color(NamedTextColor.BLUE))
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
                .append(Component.text(content).color(NamedTextColor.GRAY))
                .build()
        }
    }

    val TAB_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val TAB_HEADER by Key.buildingSimple(TypeTokens.PLAYER_FORMAT) {
        miniMessageFallbackFormat(PlayerFormat) {
            Component.text("Welcome to")
        }
    }

    val TAB_FOOTER by Key.buildingSimple(TypeTokens.PLAYER_FORMAT) {
        miniMessageFallbackFormat(PlayerFormat) {
            Component.text("A Velocity Server")
        }
    }

    val TAB_FORMAT by Key.buildingSimple(TypeTokens.PLAYER_FORMAT) {
        miniMessageFallbackFormat(PlayerFormat) {
            Component.text("$prefix $username $suffix")
        }
    }

    val TAB_FORMAT_CUSTOM by Key.buildingList(TypeTokens.PLAYER_FORMAT) {
        miniMessageListFallbackFormat(
            PlayerFormat,
            listOf(
                {
                    Component.text()
                        .append(Component.text("Your latency").color(NamedTextColor.DARK_AQUA))
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Component.text(latency).color(NamedTextColor.YELLOW))
                        .build()
                },
                {
                    Component.text()
                        .append(Component.text("Current Server").color(NamedTextColor.DARK_AQUA))
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Component.text(backend.name).color(NamedTextColor.YELLOW))
                        .build()
                },
                {
                    Component.text()
                        .append(Component.text("Player Count").color(NamedTextColor.DARK_AQUA))
                        .append(Component.text(": ").color(NamedTextColor.GRAY))
                        .append(Component.text(proxy.playerCount).color(NamedTextColor.YELLOW))
                        .build()
                },
            ),
        )
    }

    val TAB_UPDATE by Key.buildingSimple(TypeTokens.INTEGER) {
        fallback(1)
    }

    val CHAT_DEFAULT_CHANNEL by Key.buildingSimple(TypeTokens.STRING) {
        fallback("global")
    }

    val BAN_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.ban")
    }

    val TEMP_BAN_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.tempban")
    }

    val BAN_EXEMPT_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.ban.exempt")
    }

    val BROADCAST_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.broadcast")
    }

    val CHANNEL_EDIT_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.channel.edit")
    }

    val CHAT_COLOR_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.chat.color")
    }

    val FIND_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.find")
    }

    val INFO_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.info")
    }

    val INFO_IP_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.info.ip")
    }

    val INFO_BANNED_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.info.banned")
    }

    val INFO_CHANNEL_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.info.channel")
    }

    val KICK_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.kick")
    }

    val KICK_EXEMPT_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.kick.exempt")
    }

    val LANGUAGE_ADMIN_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.language.admin")
    }

    val LANGUAGE_LIST_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.language.list")
    }

    val LIST_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.list")
    }

    val MESSAGE_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.message")
    }

    val MUTE_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.mute")
    }

    val MUTE_EXEMPT_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.mute.exempt")
    }

    val NICKNAME_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.nickname")
    }

    val NICKNAME_COLOR_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.nickname.color")
    }

    val NICKNAME_MAGIC_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.nickname.magic")
    }

    val NICKNAME_OTHER_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.nickname.other")
    }

    val NICKNAME_PREFIX by Key.buildingSimple(TypeTokens.COMPONENT) {
        miniMessageFallback(Component.text("~"))
    }

    val SEND_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.admin.command.send")
    }

    val SOCIALSPY_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.admin.command.socialspy")
    }

    val SOCIALSPY_ONJOIN_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.admin.command.socialspy.onjoin")
    }

    val STAFFLIST_ADMIN_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.stafflist.admin")
    }

    val STAFFLIST_BASE_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.stafflist.base")
    }

    val STAFFLIST_OWNER_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.stafflist.owner")
    }

    val STAFFLIST_STAFF_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.stafflist.staff")
    }

    val TOGGLE_CHAT_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.chat.toggle")
    }

    val ALL_CHAT_CHANNELS_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.channel.all")
    }

    val CHANNEL_BASE_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.channel.")
    }

    val BOT_NAME by Key.buildingSimple(TypeTokens.STRING) {
        fallback("System")
    }

    val BOT_TOKEN by Key.buildingSimple(TypeTokens.STRING) {
        fallback("bot token")
    }

    val DISCORD_USERNAME_FORMAT by Key.buildingSimple(TypeTokens.ONLINE_USER_FORMAT) {
        miniMessageFallbackFormat(OnlineUserFormat) {
            Component.text("[${backend.name}] $prefix $displayname $suffix")
        }
    }

    // TODO: Use ChannelFormat
    val DISCORD_CHAT_FORMAT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("&6[Discord]&7 %name% : %message%")
    }

    val TOPIC_FORMAT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("Player Count: %players%")
    }

    val TOPIC_UPDATE_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(false)
    }

    val TOPIC_UPDATE_DELAY by Key.buildingSimple(TypeTokens.INTEGER) {
        fallback(5)
    }

    val TOPIC_NO_ONLINE_PLAYERS by Key.buildingSimple(TypeTokens.STRING) {
        fallback("There are no players online!")
    }

    val NOW_PLAYING_MESSAGE by Key.buildingSimple(TypeTokens.STRING) {
        fallback("A Minecraft Server!")
    }

    val AVATAR_URL by Key.buildingSimple(TypeTokens.STRING) {
        fallback("https://crafthead.net/avatar/%uuid%")
    }

    val DISCORD_URL by Key.buildingSimple(TypeTokens.STRING) {
        fallback("https://discord.gg/8RUzuwu")
    }

    val DISCORD_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(false)
    }

    val DISCORD_HOVER_MESSAGE by Key.buildingSimple(TypeTokens.COMPONENT) {
        miniMessageFallback(Component.text("Click here to join our discord!"))
    }

    val WEBSITE_URL by Key.buildingSimple(TypeTokens.STRING) {
        fallback("https://www.anvilpowered.org")
    }

    val IGNORE_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.ignore")
    }

    val IGNORE_EXEMPT_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.command.ignore.exempt")
    }

    val SERVER_PING by Key.buildingSimple(TypeTokens.STRING) {
        fallback("PLAYERS")
    }

    val SERVER_PING_MESSAGE by Key.buildingSimple(TypeTokens.STRING) {
        fallback("Change this message in the config!")
    }

    val SYNC_COMMAND_PERMISSION by Key.buildingSimple(TypeTokens.STRING) {
        fallback("catalyst.admin.command.sync")
    }

    val MOTD by Key.buildingSimple(TypeTokens.PROXY_SERVER_FORMAT) {
        miniMessageFallbackFormat(ProxyFormat) {
            Component.text("A Velocity Proxy running version $version!").color(NamedTextColor.DARK_AQUA)
        }
    }

    val MOTD_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(false)
    }

    val CHAT_CHANNELS by Key.buildingMap(TypeTokens.STRING, TypeTokens.CHAT_CHANNEL) {
        fallback(
            mapOf(
                "global" to chatChannelBuilderFactory.build {
                    id("global")
                    name(Component.text("[Global]").color(NamedTextColor.GREEN))
                    alwaysVisible(true)
                    discordChannelId("123456789")
                    passThrough(false)
                },
                "admin" to chatChannelBuilderFactory.build {
                    id("admin")
                    name(Component.text("Admin"))
                    messageFormat {
                        Component.text()
                            .append(Component.text(channel.name))
                            .append(Component.text(name))
                            .append(Component.text(": "))
                            .append(Component.text(content))
                            .color(NamedTextColor.RED)
                            .build()
                    }
                    alwaysVisible(true)
                    discordChannelId("123456789")
                    passThrough(false)
                },
            ),
        )
        valueSerializer { Json.encodeToString(it) }
        valueDeserializer { Json.decodeFromString(ChatChannel.serializer(), it) }
    }

    val VIA_VERSION_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(false)
    }

    val COMMAND_LOGGING_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    // TODO: Regex?
    val COMMAND_LOGGING_FILTER by Key.buildingList(TypeTokens.STRING) {
        fallback(listOf("*"))
    }

    val ENABLE_PER_SERVER_PERMS by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(false)
    }

    // Keys for command toggling
    val BAN_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val BROADCAST_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val CHANNEL_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val CHANNEL_COMMAND_ALIAS_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(false)
    }

    val NICKNAME_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val FIND_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val INFO_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val KICK_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val LIST_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(false)
    }

    val MESSAGE_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val SEND_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val SERVER_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val SOCIALSPY_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val MUTE_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val IGNORE_COMMAND_ENABLED by Key.buildingSimple(TypeTokens.BOOLEAN) {
        fallback(true)
    }

    val CATALYST_PREFIX by Key.buildingSimple(TypeTokens.STRING) {
        fallback("Catalyst")
    }

    // Keys for root node comments
    val ADVANCED_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val COMMANDS_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val CHAT_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val DISCORD_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val JOIN_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val LEAVE_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val MODULES_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val MOTD_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val PING_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }

    val TAB_ROOT by Key.buildingSimple(TypeTokens.STRING) {
        fallback("null")
    }
}
