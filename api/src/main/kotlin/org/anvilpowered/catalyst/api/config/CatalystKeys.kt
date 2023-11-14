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

@file:Suppress("MemberVisibilityCanBePrivate")

package org.anvilpowered.catalyst.api.config

import org.anvilpowered.anvil.core.config.Key
import org.anvilpowered.anvil.core.config.KeyNamespace

object CatalystKeys : KeyNamespace by KeyNamespace.create("CATALYST") {

    val CHAT_FILTER_ENABLED by Key.building {
        fallback(false)
    }
    val CHAT_FILTER_SWEARS by Key.building {
        fallback(mutableListOf("fuck", "shit", "ass"))
    }
    val CHAT_FILTER_EXCEPTIONS by Key.building {
        fallback(mutableListOf("assassin", "jkass"))
    }
    val FIRST_JOIN by Key.building {
        fallback("Welcome to the server, %player%")
    }
    val JOIN_MESSAGE by Key.building {
        fallback("%player% has joined the proxy")
    }
    val JOIN_LISTENER_ENABLED by Key.building {
        fallback(true)
    }
    val LEAVE_MESSAGE by Key.building {
        fallback("%player% has left the proxy")
    }
    val LEAVE_LISTENER_ENABLED by Key.building {
        fallback(true)
    }
    val PROXY_CHAT_ENABLED by Key.building {
        fallback(true)
    }
    val PRIVATE_MESSAGE_FORMAT by Key.building {
        fallback("&8[&9%sender%&6 -> &9%recipient%&8] &7%message%")
    }
    val TAB_ENABLED by Key.building {
        fallback(true)
    }
    val TAB_HEADER by Key.building {
        fallback("Welcome to")
    }
    val TAB_FOOTER by Key.building {
        fallback("A Velocity Server")
    }
    val TAB_FORMAT by Key.building {
        fallback("%prefix% %player% %suffix%")
    }
    val TAB_FORMAT_CUSTOM by Key.building {
        fallback(
            mutableListOf(
                "&3Your Ping : &e%ping%",
                "&3Current Server : &e%server%",
                "&3Player Count : &e%playercount%",
            ),
        )
    }
    val TAB_UPDATE by Key.building {
        fallback(1)
    }
    val CHAT_DEFAULT_CHANNEL by Key.building {
        fallback("global")
    }
    val BAN_PERMISSION by Key.building {
        fallback("catalyst.command.ban")
    }
    val TEMP_BAN_PERMISSION by Key.building {
        fallback("catalyst.command.tempban")
    }
    val BAN_EXEMPT_PERMISSION by Key.building {
        fallback("catalyst.command.ban.exempt")
    }
    val BROADCAST_PERMISSION by Key.building {
        fallback("catalyst.command.broadcast")
    }
    val CHANNEL_EDIT_PERMISSION by Key.building {
        fallback("catalyst.command.channel.edit")
    }
    val CHAT_COLOR_PERMISSION by Key.building {
        fallback("catalyst.chat.color")
    }
    val FIND_PERMISSION by Key.building {
        fallback("catalyst.command.find")
    }
    val INFO_PERMISSION by Key.building {
        fallback("catalyst.command.info")
    }
    val INFO_IP_PERMISSION by Key.building {
        fallback("catalyst.command.info.ip")
    }
    val INFO_BANNED_PERMISSION by Key.building {
        fallback("catalyst.command.info.banned")
    }
    val INFO_CHANNEL_PERMISSION by Key.building {
        fallback("catalyst.command.info.channel")
    }
    val KICK_PERMISSION by Key.building {
        fallback("catalyst.command.kick")
    }
    val KICK_EXEMPT_PERMISSION by Key.building {
        fallback("catalyst.command.kick.exempt")
    }
    val LANGUAGE_ADMIN_PERMISSION by Key.building {
        fallback("catalyst.command.language.admin")
    }
    val LANGUAGE_LIST_PERMISSION by Key.building {
        fallback("catalyst.command.language.list")
    }
    val LIST_PERMISSION by Key.building {
        fallback("catalyst.command.list")
    }
    val MESSAGE_PERMISSION by Key.building {
        fallback("catalyst.command.message")
    }
    val MUTE_PERMISSION by Key.building {
        fallback("catalyst.command.mute")
    }
    val MUTE_EXEMPT_PERMISSION by Key.building {
        fallback("catalyst.command.mute.exempt")
    }
    val NICKNAME_PERMISSION by Key.building {
        fallback("catalyst.command.nickname")
    }
    val NICKNAME_COLOR_PERMISSION by Key.building {
        fallback("catalyst.command.nickname.color")
    }
    val NICKNAME_MAGIC_PERMISSION by Key.building {
        fallback("catalyst.command.nickname.magic")
    }

    val NICKNAME_OTHER_PERMISSION by Key.building {
        fallback("catalyst.command.nickname.other")
    }

    val NICKNAME_PREFIX by Key.building {
        fallback("~")
    }

    val SEND_PERMISSION by Key.building {
        fallback("catalyst.admin.command.send")
    }

    val SOCIALSPY_PERMISSION by Key.building {
        fallback("catalyst.admin.command.socialspy")
    }
    val SOCIALSPY_ONJOIN_PERMISSION by Key.building {
        fallback("catalyst.admin.command.socialspy.onjoin")
    }
    val STAFFLIST_ADMIN_PERMISSION by Key.building {
        fallback("catalyst.stafflist.admin")
    }
    val STAFFLIST_BASE_PERMISSION by Key.building {
        fallback("catalyst.stafflist.base")
    }
    val STAFFLIST_OWNER_PERMISSION by Key.building {
        fallback("catalyst.stafflist.owner")
    }
    val STAFFLIST_STAFF_PERMISSION by Key.building {
        fallback("catalyst.stafflist.staff")
    }
    val TOGGLE_CHAT_PERMISSION by Key.building {
        fallback("catalyst.chat.toggle")
    }
    val ALL_CHAT_CHANNELS_PERMISSION by Key.building {
        fallback("catalyst.channel.all")
    }
    val CHANNEL_BASE_PERMISSION by Key.building {
        fallback("catalyst.channel.")
    }
    val BOT_NAME by Key.building {
        fallback("System")
    }
    val BOT_TOKEN by Key.building {
        fallback("bot token")
    }
    val DISCORD_PLAYER_CHAT_FORMAT by Key.building {
        fallback("[%server%] %prefix% %player% %suffix%")
    }
    val DISCORD_JOIN_FORMAT by Key.building {
        fallback("%player% has joined the game.")
    }
    val DISCORD_LEAVE_FORMAT by Key.building {
        fallback("%player% has left the game.")
    }
    val DISCORD_CHAT_FORMAT by Key.building {
        fallback("&6[Discord]&7 %name% : %message%")
    }
    val TOPIC_FORMAT by Key.building {
        fallback("Player Count: %players%")
    }

    val TOPIC_UPDATE_ENABLED by Key.building {
        fallback(false)
    }

    val TOPIC_UPDATE_DELAY by Key.building {
        fallback(5)
    }

    val TOPIC_NO_ONLINE_PLAYERS by Key.building {
        fallback("There are no players online!")
    }

    val NOW_PLAYING_MESSAGE by Key.building {
        fallback("A Minecraft Server!")
    }
    val WEBHOOK_URL by Key.building {
        fallback("https://crafatar.com/avatars/%uuid%?default=MHF_Alex")
    }

    val DISCORD_URL by Key.building {
        fallback("https://discord.gg/hZpQ5Sg")
    }

    val DISCORD_ENABLE by Key.building {
        fallback(false)
    }

    val DISCORD_HOVER_MESSAGE by Key.building {
        fallback("Click here to join our discord!")
    }

    val WEBSITE_URL by Key.building {
        fallback("https://www.anvilpowered.org")
    }

    val IGNORE_PERMISSION by Key.building {
        fallback("catalyst.command.ignore")
    }

    val IGNORE_EXEMPT_PERMISSION by Key.building {
        fallback("catalyst.command.ignore.exempt")
    }

    val SERVER_PING by Key.building {
        fallback("PLAYERS")
    }

    val SERVER_PING_MESSAGE by Key.building {
        fallback("Change this message in the config!")
    }

    val SYNC_COMMAND_PERMISSION by Key.building {
        fallback("catalyst.admin.command.sync")
    }

    val MOTD by Key.building {
        fallback("A Velocity Proxy!")
    }

    val MOTD_ENABLED by Key.building {
        fallback(false)
    }

    var chatChannels = mutableListOf<ChatChannel>()

    val CHAT_CHANNELS by Key.building {
        fallback(chatChannels)
    }

    val VIA_VERSION_ENABLED by Key.building {
        fallback(false)
    }

    val COMMAND_LOGGING_ENABLED by Key.building {
        fallback(true)
    }

    val COMMAND_LOGGING_FILTER by Key.building {
        fallback(mutableListOf("*"))
    }

    val ENABLE_PER_SERVER_PERMS by Key.building {
        fallback(false)
    }

    //Keys for command toggling
    val BAN_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val BROADCAST_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val CHANNEL_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val CHANNEL_COMMAND_ALIAS_ENABLED by Key.building {
        fallback(false)

    }
    val NICKNAME_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val FIND_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val INFO_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val KICK_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val LIST_COMMAND_ENABLED by Key.building {
        fallback(false)
    }

    val MESSAGE_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val SEND_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val SERVER_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val SOCIALSPY_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val MUTE_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val IGNORE_COMMAND_ENABLED by Key.building {
        fallback(true)
    }

    val CATALYST_PREFIX by Key.building {
        fallback("Catalyst")
    }

    //Keys for root node comments
    val ADVANCED_ROOT by Key.building {
        fallback("null")
    }

    val COMMANDS_ROOT by Key.building {
        fallback("null")
    }

    val CHAT_ROOT by Key.building {
        fallback("null")
    }

    val DISCORD_ROOT by Key.building<String> {
        fallback("null")
    }

    val JOIN_ROOT by Key.building<String> {
        fallback("null")
    }

    val LEAVE_ROOT by Key.building<String> {
        fallback("null")
    }

    val MODULES_ROOT by Key.building<String> {
        fallback("null")
    }

    val MOTD_ROOT by Key.building<String> {
        fallback("null")
    }

    val PING_ROOT by Key.building<String> {
        fallback("null")
    }

    val TAB_ROOT by Key.building<String> {
        fallback("null")
    }

    init {
        val global = ChatChannel.build {
            id("global")
            alwaysVisible(true)
            click("/msg %player%")
            discordChannel("123456789")
            format("[Global] %player% : %message%")
            hoverMessage("Click here to message %player%")
            passThrough(false)
            servers(mutableListOf("lobby", "minigames"))
        }
        chatChannels.add(global)

        val admin = ChatChannel.build {
            id("admin")
            alwaysVisible(true)
            click("/msg %player%")
            discordChannel("123456789")
            format("[Admin Chat] %player% : %message%")
            hoverMessage("Click here to message %player%")
            passThrough(false)
            servers(mutableListOf("lobby", "minigames"))
        }
        chatChannels.add(admin)
    }
}
