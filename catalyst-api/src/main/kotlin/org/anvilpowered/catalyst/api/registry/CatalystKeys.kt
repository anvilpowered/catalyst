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

package org.anvilpowered.catalyst.api.registry

import org.anvilpowered.registry.key.Key
import org.anvilpowered.registry.key.Keys
import java.io.File

object CatalystKeys {

    val CHAT_FILTER_ENABLED = Key.build {
        name("CHAT_FILTER_ENABLED")
        fallback(false)
    }
    val CHAT_FILTER_SWEARS = Key.build {
        name("CHAT_FILTER_SWEARS")
        fallback(mutableListOf("fuck", "shit", "ass"))
    }
    val CHAT_FILTER_EXCEPTIONS = Key.build {
        name("CHAT_FILTER_EXCEPTIONS")
        fallback(mutableListOf("assassin", "jkass"))
    }
    val FIRST_JOIN = Key.build {
        name("FIRST_JOIN")
        fallback("Welcome to the server, %player%")
    }
    val JOIN_MESSAGE = Key.build {
        name("JOIN_MESSAGE")
        fallback("%player% has joined the proxy")
    }
    val JOIN_LISTENER_ENABLED = Key.build {
        name("JOIN_LISTENER_ENABLED")
        fallback(true)
    }
    val LEAVE_MESSAGE = Key.build {
        name("LEAVE_MESSAGE")
        fallback("%player% has left the proxy")
    }
    val LEAVE_LISTENER_ENABLED = Key.build {
        name("LEAVE_LISTENER_ENABLED")
        fallback(true)
    }
    val PROXY_CHAT_ENABLED = Key.build {
        name("PROXY_CHAT_ENABLED")
        fallback(true)
    }
    val PRIVATE_MESSAGE_FORMAT = Key.build {
        name("PRIVATE_MESSAGE_FORMAT")
        fallback("&8[&9%sender%&6 -> &9%recipient%&8] &7%message%")
    }
    val TAB_ENABLED = Key.build {
        name("TAB_ENABLED")
        fallback(true)
    }
    val TAB_HEADER = Key.build {
        name("TAB_HEADER")
        fallback("Welcome to")
    }
    val TAB_FOOTER = Key.build {
        name("TAB_FOOTER")
        fallback("A Velocity Server")
    }
    val TAB_FORMAT = Key.build {
        name("TAB_FORMAT")
        fallback("%prefix% %player% %suffix%")
    }
    val TAB_FORMAT_CUSTOM = Key.build {
        name("TAB_FORMAT_CUSTOM")
        fallback(
            mutableListOf(
                "&3Your Ping : &e%ping%",
                "&3Current Server : &e%server%",
                "&3Player Count : &e%playercount%"
            )
        )
    }
    val TAB_UPDATE = Key.build {
        name("TAB_UPDATE")
        fallback(1)
    }
    val CHAT_DEFAULT_CHANNEL = Key.build {
        name("CHAT_DEFAULT_CHANNEL")
        fallback("global")
    }
    val BAN_PERMISSION = Key.build {
        name("BAN_PERMISSION")
        fallback("catalyst.command.ban")
        sensitive()
    }
    val TEMP_BAN_PERMISSION = Key.build {
        name("TEMP_BAN_PERMISSION")
        fallback("catalyst.command.tempban")
        sensitive()
    }
    val BAN_EXEMPT_PERMISSION = Key.build {
        name("BAN_EXEMPT_PERMISSION")
        fallback("catalyst.command.ban.exempt")
        sensitive()
    }
    val BROADCAST_PERMISSION = Key.build {
        name("BROADCAST_PERMISSION")
        fallback("catalyst.command.broadcast")
        sensitive()
    }
    val CHANNEL_EDIT_PERMISSION = Key.build {
        name("CHANNEL_EDIT_PERMISSION")
        fallback("catalyst.command.channel.edit")
    }
    val CHAT_COLOR_PERMISSION = Key.build {
        name("CHAT_COLOR_PERMISSION")
        fallback("catalyst.chat.color")
        sensitive()
    }
    val FIND_PERMISSION = Key.build {
        name("FIND_PERMISSION")
        fallback("catalyst.command.find")
        sensitive()
    }
    val INFO_PERMISSION = Key.build {
        name("INFO_PERMISSION")
        fallback("catalyst.command.info")
        sensitive()
    }
    val INFO_IP_PERMISSION = Key.build {
        name("INFO_IP_PERMISSION")
        fallback("catalyst.command.info.ip")
        sensitive()
    }
    val INFO_BANNED_PERMISSION = Key.build {
        name("INFO_BANNED_PERMISSION")
        fallback("catalyst.command.info.banned")
        sensitive()
    }
    val INFO_CHANNEL_PERMISSION = Key.build {
        name("INFO_CHANNEL_PERMISSION")
        fallback("catalyst.command.info.channel")
        sensitive()
    }
    val KICK_PERMISSION = Key.build {
        name("KICK_PERMISSION")
        fallback("catalyst.command.kick")
        sensitive()
    }
    val KICK_EXEMPT_PERMISSION = Key.build {
        name("KICK_EXEMPT_PERMISSION")
        fallback("catalyst.command.kick.exempt")
        sensitive()
    }
    val LANGUAGE_ADMIN_PERMISSION = Key.build {
        name("LANGUAGE_ADMIN_PERMISSION")
        fallback("catalyst.command.language.admin")
        sensitive()
    }
    val LANGUAGE_LIST_PERMISSION = Key.build {
        name("LANGUAGE_LIST_PERMISSION")
        fallback("catalyst.command.language.list")
        sensitive()
    }
    val LIST_PERMISSION = Key.build {
        name("LIST_PERMISSION")
        fallback("catalyst.command.list")
        sensitive()
    }
    val MESSAGE_PERMISSION = Key.build {
        name("MESSAGE_PERMISSION")
        fallback("catalyst.command.message")
        sensitive()
    }
    val MUTE_PERMISSION = Key.build {
        name("MUTE_PERMISSION")
        fallback("catalyst.command.mute")
        sensitive()
    }
    val MUTE_EXEMPT_PERMISSION = Key.build {
        name("MUTE_EXEMPT_PERMISSION")
        fallback("catalyst.command.mute.exempt")
        sensitive()
    }
    val NICKNAME_PERMISSION = Key.build {
        name("NICKNAME_PERMISSION")
        fallback("catalyst.command.nickname")
        sensitive()
    }
    val NICKNAME_COLOR_PERMISSION = Key.build {
        name("NICKNAME_COLOR_PERMISSION")
        fallback("catalyst.command.nickname.color")
        sensitive()
    }
    val NICKNAME_MAGIC_PERMISSION = Key.build {
        name("NICKNAME_MAGIC_PERMISSION")
        fallback("catalyst.command.nickname.magic")
        sensitive()
    }

    val NICKNAME_OTHER_PERMISSION = Key.build {
        name("NICKNAME_OTHER_PERMISSION")
        fallback("catalyst.command.nickname.other")
        sensitive()
    }

    val NICKNAME_PREFIX = Key.build {
        name("NICKNAME_PREFIX")
        fallback("~")
    }

    val SEND_PERMISSION = Key.build {
        name("SEND_PERMISSION")
        fallback("catalyst.admin.command.send")
        sensitive()
    }

    val SOCIALSPY_PERMISSION = Key.build {
        name("SOCIALSPY_PERMISSION")
        fallback("catalyst.admin.command.socialspy")
        sensitive()
    }

    val SOCIALSPY_ONJOIN_PERMISSION = Key.build {
        name("SOCIALSPY_ONJOIN_PERMISSION")
        fallback("catalyst.admin.command.socialspy.onjoin")
        sensitive()
    }

    val STAFFLIST_ADMIN_PERMISSION = Key.build {
        name("STAFFLIST_ADMIN_PERMISSION")
        fallback("catalyst.stafflist.admin")
        sensitive()
    }

    val STAFFLIST_BASE_PERMISSION = Key.build {
        name("STAFFLIST_BASE_PERMISSION")
        fallback("catalyst.stafflist.base")
        sensitive()
    }

    val STAFFLIST_OWNER_PERMISSION = Key.build {
        name("STAFFLIST_OWNER_PERMISSION")
        fallback("catalyst.stafflist.owner")
        sensitive()
    }

    val STAFFLIST_STAFF_PERMISSION = Key.build {
        name("STAFFLIST_STAFF_PERMISSION")
        fallback("catalyst.stafflist.staff")
        sensitive()
    }

    val TOGGLE_CHAT_PERMISSION = Key.build {
        name("TOGGLE_CHAT_PERMISSION")
        fallback("catalyst.chat.toggle")
        sensitive()
    }

    val ALL_CHAT_CHANNELS_PERMISSION = Key.build {
        name("ALL_CHAT_CHANNELS_PERMISSION")
        fallback("catalyst.channel.all")
        sensitive()
    }

    val CHANNEL_BASE_PERMISSION = Key.build {
        name("CHANNEL_BASE_PERMISSION")
        fallback("catalyst.channel.")
        sensitive()
    }

    val BOT_NAME = Key.build {
        name("BOT_NAME")
        fallback("System")
    }

    val BOT_TOKEN = Key.build {
        name("BOT_TOKEN")
        fallback("bot token")
        sensitive()
    }

    val DISCORD_PLAYER_CHAT_FORMAT = Key.build {
        name("DISCORD_PLAYER_CHAT_FORMAT")
        fallback("[%server%] %prefix% %player% %suffix%")
    }

    val DISCORD_JOIN_FORMAT = Key.build {
        name("DISCORD_JOIN_FORMAT")
        fallback("%player% has joined the game.")
    }

    val DISCORD_LEAVE_FORMAT = Key.build {
        name("DISCORD_LEAVE_FORMAT")
        fallback("%player% has left the game.")
    }

    val DISCORD_CHAT_FORMAT = Key.build {
        name("DISCORD_CHAT_FORMAT")
        fallback("&6[Discord]&7 %name% : %message%")
    }

    val TOPIC_FORMAT = Key.build {
        name("TOPIC_FORMAT")
        fallback("Player Count: %players%")
    }

    val TOPIC_UPDATE_ENABLED = Key.build {
        name("TOPIC_UPDATE_ENABLED")
        fallback(false)
    }

    val TOPIC_UPDATE_DELAY = Key.build {
        name("TOPIC_UPDATE_DELAY")
        fallback(5)
    }

    val TOPIC_NO_ONLINE_PLAYERS = Key.build {
        name("TOPIC_NO_ONLINE_PLAYERS")
        fallback("There are no players online!")
    }

    val NOW_PLAYING_MESSAGE = Key.build {
        name("NOW_PLAYING_MESSAGE")
        fallback("A Minecraft Server!")
    }
    val WEBHOOK_URL = Key.build {
        name("WEBHOOK_URL")
        fallback("https://crafatar.com/avatars/%uuid%?default=MHF_Alex")
    }

    val DISCORD_URL = Key.build {
        name("DISCORD_URL")
        fallback("https://discord.gg/hZpQ5Sg")
    }

    val DISCORD_ENABLE = Key.build {
        name("DISCORD_ENABLE")
        fallback(false)
    }

    val DISCORD_HOVER_MESSAGE = Key.build {
        name("DISCORD_HOVER_MESSAGE")
        fallback("Click here to join our discord!")
    }

    val WEBSITE_URL = Key.build {
        name("WEBSITE_URL")
        fallback("https://www.anvilpowered.org")
    }

    val IGNORE_PERMISSION = Key.build {
        name("IGNORE_PERMISSION")
        fallback("catalyst.command.ignore")
        sensitive()
    }

    val IGNORE_EXEMPT_PERMISSION = Key.build {
        name("IGNORE_EXEMPT_PERMISSION")
        fallback("catalyst.command.ignore.exempt")
        sensitive()
    }

    val SERVER_PING = Key.build {
        name("SERVER_PING")
        fallback("PLAYERS")
    }

    val SERVER_PING_MESSAGE = Key.build {
        name("SERVER_PING_MESSAGE")
        fallback("Change this message in the config!")
    }

    val SYNC_COMMAND_PERMISSION = Key.build {
        name("SYNC_COMMNAD_PERMISSION")
        fallback("catalyst.admin.command.sync")
        sensitive()
    }

    val MOTD = Key.build {
        name("MOTD")
        fallback("A Velocity Proxy!")
    }

    val MOTD_ENABLED = Key.build {
        name("MOTD_ENABLED")
        fallback(false)
    }

    var chatChannels = mutableListOf<ChatChannel>()
    val CHAT_CHANNELS = Key.build {
        name("CHAT_CHANNELS")
        fallback(chatChannels)
    }

    val VIA_VERSION_ENABLED = Key.build {
        name("VIA_VERSION_ENABLED")
        fallback(false)
    }

    val COMMAND_LOGGING_ENABLED = Key.build {
        name("COMMAND_LOGGING_ENABLED")
        fallback(true)
    }

    val COMMAND_LOGGING_FILTER = Key.build {
        name("COMMAND_LOGGING_FILTER")
        fallback(mutableListOf("*"))
    }

    val ENABLE_PER_SERVER_PERMS = Key.build {
        name("ENABLE_PER_SERVER_PERMS")
        fallback(false)
    }


    //Keys for command toggling
    val BAN_COMMAND_ENABLED = Key.build {
        name("BAN_COMMAND_ENABLED")
        fallback(true)
    }

    val BROADCAST_COMMAND_ENABLED = Key.build {
        name("BROADCAST_COMMAND_ENABLED")
        fallback(true)
    }

    val CHANNEL_COMMAND_ENABLED = Key.build {
        name("CHANNEL_COMMAND_ENABLED")
        fallback(true)
    }

    val CHANNEL_COMMAND_ALIAS_ENABLED = Key.build {
        name("CHANNEL_COMMAND_ALIAS_ENABLED")
        fallback(false)

    }
    val NICKNAME_COMMAND_ENABLED = Key.build {
        name("NICKNAME_COMMAND_ENABLED")
        fallback(true)
    }

    val FIND_COMMAND_ENABLED = Key.build {
        name("FIND_COMMAND_ENABLED")
        fallback(true)
    }

    val INFO_COMMAND_ENABLED = Key.build {
        name("INFO_COMMAND_ENABLED")
        fallback(true)
    }

    val KICK_COMMAND_ENABLED = Key.build {
        name("KICK_COMMAND_ENABLED")
        fallback(true)
    }

    val LIST_COMMAND_ENABLED = Key.build {
        name("LIST_COMMAND_ENABLED")
        fallback(false)
    }

    val MESSAGE_COMMAND_ENABLED = Key.build {
        name("MESSAGE_COMMAND_ENABLED")
        fallback(true)
    }

    val SEND_COMMAND_ENABLED = Key.build {
        name("SEND_COMMAND_ENABLED")
        fallback(true)
    }

    val SERVER_COMMAND_ENABLED = Key.build {
        name("SERVER_COMMAND_ENABLED")
        fallback(true)
    }

    val SOCIALSPY_COMMAND_ENABLED = Key.build {
        name("SOCIALSPY_COMMAND_ENABLED")
        fallback(true)
    }

    val MUTE_COMMAND_ENABLED = Key.build {
        name("MUTE_COMMAND_ENABLED")
        fallback(true)
    }

    val IGNORE_COMMAND_ENABLED = Key.build {
        name("IGNORE_COMMAND_ENABLED")
        fallback(true)
    }

    val CATALYST_PREFIX = Key.build {
        name("CATALYST_PREFIX")
        fallback("Catalyst")
    }


    //Keys for root node comments
    val ADVANCED_ROOT = Key.build {
        name("ADVANCED_ROOT")
        fallback("null")
        sensitive()
    }

    val COMMANDS_ROOT = Key.build {
        name("COMMANDS_ROOT")
        fallback("null")
        sensitive()
    }

    val CHAT_ROOT = Key.build {
        name("CHAT_ROOT")
        fallback("null")
        sensitive()
    }

    val DISCORD_ROOT = Key.build<String> {
        name("DISCORD_ROOT")
        fallback("null")
        sensitive()
    }

    val JOIN_ROOT = Key.build<String> {
        name("JOIN_ROOT")
        fallback("null")
        sensitive()
    }

    val LEAVE_ROOT = Key.build<String> {
        name("LEAVE_ROOT")
        fallback("null")
        sensitive()
    }

    val MODULES_ROOT = Key.build<String> {
        name("MODULES_ROOT")
        fallback("null")
        sensitive()
    }

    val MOTD_ROOT = Key.build<String> {
        name("MOTD_ROOT")
        fallback("null")
        sensitive()
    }

    val PING_ROOT = Key.build<String> {
        name("PING_ROOT")
        fallback("null")
        sensitive()
    }

    val TAB_ROOT = Key.build<String> {
        name("TAB_ROOT")
        fallback("null")
        sensitive()
    }


    init {
        val global = ChatChannel()
        global.alwaysVisible = true;
        global.click = "/msg %player%";
        global.discordChannel = "123456789";
        global.format = "[Global] %player% : %message%";
        global.hoverMessage = "Click here to message %player%";
        global.id = "global"
        global.passthrough = false
        global.servers = mutableListOf("main", "wild")
        chatChannels.add(global)

        val admin = ChatChannel()
        admin.alwaysVisible = true
        admin.click = "/msg %player%"
        admin.discordChannel = "123456789"
        admin.format = "[Admin Chat] %player% : %message%"
        admin.hoverMessage = "Click here to message %player%"
        admin.passthrough = false
        admin.id = "admin"
        admin.servers = mutableListOf("main", "wild")
        chatChannels.add(admin)

        Keys.startRegistration("catalyst")
            .register(CHAT_FILTER_ENABLED)
            .register(CHAT_FILTER_EXCEPTIONS)
            .register(CHAT_FILTER_SWEARS)
            .register(FIRST_JOIN)
            .register(JOIN_MESSAGE)
            .register(LEAVE_MESSAGE)
            .register(PROXY_CHAT_ENABLED)
            .register(TAB_ENABLED)
            .register(TAB_HEADER)
            .register(TAB_FOOTER)
            .register(TAB_FORMAT)
            .register(TAB_FORMAT_CUSTOM)
            .register(TAB_UPDATE)
            .register(CHAT_CHANNELS)
            .register(CHAT_DEFAULT_CHANNEL)
            .register(BAN_PERMISSION)
            .register(TEMP_BAN_PERMISSION)
            .register(BAN_EXEMPT_PERMISSION)
            .register(BROADCAST_PERMISSION)
            .register(CHANNEL_EDIT_PERMISSION)
            .register(CHAT_COLOR_PERMISSION)
            .register(FIND_PERMISSION)
            .register(INFO_PERMISSION)
            .register(INFO_IP_PERMISSION)
            .register(INFO_BANNED_PERMISSION)
            .register(INFO_CHANNEL_PERMISSION)
            .register(KICK_PERMISSION)
            .register(KICK_EXEMPT_PERMISSION)
            .register(LANGUAGE_ADMIN_PERMISSION)
            .register(LANGUAGE_LIST_PERMISSION)
            .register(LIST_PERMISSION)
            .register(MESSAGE_PERMISSION)
            .register(MUTE_PERMISSION)
            .register(MUTE_EXEMPT_PERMISSION)
            .register(NICKNAME_PERMISSION)
            .register(NICKNAME_COLOR_PERMISSION)
            .register(NICKNAME_MAGIC_PERMISSION)
            .register(NICKNAME_OTHER_PERMISSION)
            .register(NICKNAME_PREFIX)
            .register(SEND_PERMISSION)
            .register(SOCIALSPY_PERMISSION)
            .register(SOCIALSPY_ONJOIN_PERMISSION)
            .register(STAFFLIST_ADMIN_PERMISSION)
            .register(STAFFLIST_BASE_PERMISSION)
            .register(STAFFLIST_OWNER_PERMISSION)
            .register(STAFFLIST_STAFF_PERMISSION)
            .register(TOGGLE_CHAT_PERMISSION)
            .register(ALL_CHAT_CHANNELS_PERMISSION)
            .register(CHANNEL_BASE_PERMISSION)
            .register(BOT_NAME)
            .register(BOT_TOKEN)
            .register(DISCORD_PLAYER_CHAT_FORMAT)
            .register(DISCORD_JOIN_FORMAT)
            .register(DISCORD_LEAVE_FORMAT)
            .register(DISCORD_CHAT_FORMAT)
            .register(TOPIC_FORMAT)
            .register(TOPIC_UPDATE_ENABLED)
            .register(TOPIC_UPDATE_DELAY)
            .register(NOW_PLAYING_MESSAGE)
            .register(WEBHOOK_URL)
            .register(DISCORD_URL)
            .register(DISCORD_ENABLE)
            .register(DISCORD_HOVER_MESSAGE)
            .register(WEBSITE_URL)
            .register(IGNORE_PERMISSION)
            .register(IGNORE_EXEMPT_PERMISSION)
            .register(SERVER_PING)
            .register(SERVER_PING_MESSAGE)
            .register(SYNC_COMMAND_PERMISSION)
            .register(MOTD)
            .register(MOTD_ENABLED)
            .register(VIA_VERSION_ENABLED)
            .register(BAN_COMMAND_ENABLED)
            .register(BROADCAST_COMMAND_ENABLED)
            .register(CHANNEL_COMMAND_ENABLED)
            .register(CHANNEL_COMMAND_ALIAS_ENABLED)
            .register(NICKNAME_COMMAND_ENABLED)
            .register(FIND_COMMAND_ENABLED)
            .register(INFO_COMMAND_ENABLED)
            .register(KICK_COMMAND_ENABLED)
            .register(LIST_COMMAND_ENABLED)
            .register(MESSAGE_COMMAND_ENABLED)
            .register(SEND_COMMAND_ENABLED)
            .register(SERVER_COMMAND_ENABLED)
            .register(SOCIALSPY_COMMAND_ENABLED)
            .register(MUTE_COMMAND_ENABLED)
            .register(IGNORE_COMMAND_ENABLED)
            .register(CATALYST_PREFIX)
            .register(ADVANCED_ROOT)
            .register(CHAT_ROOT)
            .register(DISCORD_ROOT)
            .register(JOIN_ROOT)
            .register(LEAVE_ROOT)
            .register(MODULES_ROOT)
            .register(MOTD_ROOT)
            .register(PING_ROOT)
            .register(TAB_ROOT)
            .register(COMMAND_LOGGING_ENABLED)
            .register(COMMAND_LOGGING_FILTER)
            .register(ENABLE_PER_SERVER_PERMS)
    }
}
