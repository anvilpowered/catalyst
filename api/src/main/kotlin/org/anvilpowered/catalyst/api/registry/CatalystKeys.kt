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

import com.google.common.reflect.TypeToken
import org.anvilpowered.anvil.api.registry.Key
import org.anvilpowered.anvil.api.registry.Keys
import org.anvilpowered.anvil.api.registry.TypeTokens
import java.io.File
import java.util.LinkedList

object CatalystKeys {

  private val LIST_STRING: TypeToken<MutableList<String>> = object : TypeToken<MutableList<String>>() {}

  val CHAT_FILTER_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("CHAT_FILTER_ENABLED")
    .fallback(false)
    .build()
  val CHAT_FILTER_SWEARS = Key.builder(LIST_STRING)
    .name("CHAT_FILTER_SWEARS")
    .fallback(mutableListOf("fuck", "shit", "ass"))
    .build()
  val CHAT_FILTER_EXCEPTIONS = Key.builder(LIST_STRING)
    .name("CHAT_FILTER_EXCEPTIONS")
    .fallback(mutableListOf("assassin", "jkass"))
    .build()
  val FIRST_JOIN = Key.builder(TypeTokens.STRING)
    .name("FIRST_JOIN")
    .fallback("Welcome to the server, %player%")
    .build()
  val JOIN_MESSAGE = Key.builder(TypeTokens.STRING)
    .name("JOIN_MESSAGE")
    .fallback("%player% has joined the proxy")
    .build()
  val JOIN_LISTENER_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("JOIN_LISTENER_ENABLED")
    .fallback(true)
    .build()
  val LEAVE_MESSAGE = Key.builder(TypeTokens.STRING)
    .name("LEAVE_MESSAGE")
    .fallback("%player% has left the proxy")
    .build()
  val LEAVE_LISTENER_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("LEAVE_LISTENER_ENABLED")
    .fallback(true)
    .build()
  val PROXY_CHAT_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("PROXY_CHAT_ENABLED")
    .fallback(true)
    .build()
  val PRIVATE_MESSAGE_FORMAT = Key.builder(TypeTokens.STRING)
    .name("PRIVATE_MESSAGE_FORMAT")
    .fallback("&8[&9%sender%&6 -> &9%recipient%&8] &7%message%")
    .build()
  val TAB_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("TAB_ENABLED")
    .fallback(true)
    .build()
  val TAB_HEADER = Key.builder(TypeTokens.STRING)
    .name("TAB_HEADER")
    .fallback("Welcome to")
    .build()
  val TAB_FOOTER = Key.builder(TypeTokens.STRING)
    .name("TAB_FOOTER")
    .fallback("A Velocity Server")
    .build()
  val TAB_FORMAT = Key.builder(TypeTokens.STRING)
    .name("TAB_FORMAT")
    .fallback("%prefix% %player% %suffix%")
    .build()
  val TAB_FORMAT_CUSTOM = Key.builder(LIST_STRING)
    .name("TAB_FORMAT_CUSTOM")
    .fallback(
      mutableListOf(
        "&3Your Ping : &e%ping%",
        "&3Current Server : &e%server%",
        "&3Player Count : &e%playercount%"
      )
    )
    .build()
  val TAB_UPDATE = Key.builder(TypeTokens.INTEGER)
    .name("TAB_UPDATE")
    .fallback(1)
    .build()
  val TAB_ORDER = Key.builder(TypeTokens.STRING)
    .name("TAB_ORDER")
    .fallback("a-z")
    .build()
  val TAB_GROUP_ORDER = Key.builder(LIST_STRING)
    .name("TAB_GROUP_ORDER")
    .fallback(mutableListOf("admin", "mod", "player"))
    .build()
  val CHAT_DEFAULT_CHANNEL = Key.builder(TypeTokens.STRING)
    .name("CHAT_DEFAULT_CHANNEL")
    .fallback("global")
    .build()
  val BAN_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("BAN_PERMISSION")
    .fallback("catalyst.command.ban")
    .sensitive()
    .build()
  val TEMP_BAN_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("TEMP_BAN_PERMISSION")
    .fallback("catalyst.command.tempban")
    .sensitive()
    .build()
  val BAN_EXEMPT_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("BAN_EXEMPT_PERMISSION")
    .fallback("catalyst.command.ban.exempt")
    .sensitive()
    .build()
  val BROADCAST_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("BROADCAST_PERMISSION")
    .fallback("catalyst.command.broadcast")
    .sensitive()
    .build()
  val CHANNEL_EDIT_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("CHANNEL_EDIT_PERMISSION")
    .fallback("catalyst.command.channel.edit")
    .build()
  val CHAT_COLOR_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("CHAT_COLOR_PERMISSION")
    .fallback("catalyst.chat.color")
    .sensitive()
    .build()
  val FIND_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("FIND_PERMISSION")
    .fallback("catalyst.command.find")
    .sensitive()
    .build()
  val INFO_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("INFO_PERMISSION")
    .fallback("catalyst.command.info")
    .sensitive()
    .build()
  val INFO_IP_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("INFO_IP_PERMISSION")
    .fallback("catalyst.command.info.ip")
    .sensitive()
    .build()
  val INFO_BANNED_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("INFO_BANNED_PERMISSION")
    .fallback("catalyst.command.info.banned")
    .sensitive()
    .build()
  val INFO_CHANNEL_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("INFO_CHANNEL_PERMISSION")
    .fallback("catalyst.command.info.channel")
    .sensitive()
    .build()
  val KICK_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("KICK_PERMISSION")
    .fallback("catalyst.command.kick")
    .sensitive()
    .build()
  val KICK_EXEMPT_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("KICK_EXEMPT_PERMISSION")
    .fallback("catalyst.command.kick.exempt")
    .sensitive()
    .build()
  val LANGUAGE_ADMIN_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("LANGUAGE_ADMIN_PERMISSION")
    .fallback("catalyst.command.language.admin")
    .sensitive()
    .build()
  val LANGUAGE_LIST_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("LANGUAGE_LIST_PERMISSION")
    .fallback("catalyst.command.language.list")
    .sensitive()
    .build()
  val LIST_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("LIST_PERMISSION")
    .fallback("catalyst.command.list")
    .sensitive()
    .build()
  val MESSAGE_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("MESSAGE_PERMISSION")
    .fallback("catalyst.command.message")
    .sensitive()
    .build()
  val MUTE_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("MUTE_PERMISSION")
    .fallback("catalyst.command.mute")
    .sensitive()
    .build()
  val MUTE_EXEMPT_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("MUTE_EXEMPT_PERMISSION")
    .fallback("catalyst.command.mute.exempt")
    .sensitive()
    .build()
  val NICKNAME_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("NICKNAME_PERMISSION")
    .fallback("catalyst.command.nickname")
    .sensitive()
    .build()
  val NICKNAME_COLOR_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("NICKNAME_COLOR_PERMISSION")
    .fallback("catalyst.command.nickname.color")
    .sensitive()
    .build()
  val NICKNAME_MAGIC_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("NICKNAME_MAGIC_PERMISSION")
    .fallback("catalyst.command.nickname.magic")
    .sensitive()
    .build()
  val NICKNAME_OTHER_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("NICKNAME_OTHER_PERMISSION")
    .fallback("catalyst.command.nickname.other")
    .sensitive()
    .build()
  val NICKNAME_PREFIX = Key.builder(TypeTokens.STRING)
    .name("NICKNAME_PREFIX")
    .fallback("~")
    .build()
  val SEND_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("SEND_PERMISSION")
    .fallback("catalyst.admin.command.send")
    .sensitive()
    .build()
  val SOCIALSPY_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("SOCIALSPY_PERMISSION")
    .fallback("catalyst.admin.command.socialspy")
    .sensitive()
    .build()
  val SOCIALSPY_ONJOIN_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("SOCIALSPY_ONJOIN_PERMISSION")
    .fallback("catalyst.admin.command.socialspy.onjoin")
    .sensitive()
    .build()
  val STAFFLIST_ADMIN_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("STAFFLIST_ADMIN_PERMISSION")
    .fallback("catalyst.stafflist.admin")
    .sensitive()
    .build()
  val STAFFLIST_BASE_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("STAFFLIST_BASE_PERMISSION")
    .fallback("catalyst.stafflist.base")
    .sensitive()
    .build()
  val STAFFLIST_OWNER_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("STAFFLIST_OWNER_PERMISSION")
    .fallback("catalyst.stafflist.owner")
    .sensitive()
    .build()
  val STAFFLIST_STAFF_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("STAFFLIST_STAFF_PERMISSION")
    .fallback("catalyst.stafflist.staff")
    .sensitive()
    .build()
  val TOGGLE_CHAT_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("TOGGLE_CHAT_PERMISSION")
    .fallback("catalyst.chat.toggle")
    .sensitive()
    .build()
  val ALL_CHAT_CHANNELS_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("ALL_CHAT_CHANNELS_PERMISSION")
    .fallback("catalyst.channel.all")
    .sensitive()
    .build()
  val CHANNEL_BASE_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("CHANNEL_BASE_PERMISSION")
    .fallback("catalyst.channel.")
    .sensitive()
    .build()
  val BOT_NAME = Key.builder(TypeTokens.STRING)
    .name("BOT_NAME")
    .fallback("System")
    .build()
  val BOT_TOKEN = Key.builder(TypeTokens.STRING)
    .name("BOT_TOKEN")
    .fallback("bot token")
    .sensitive()
    .build()
  val DISCORD_PLAYER_CHAT_FORMAT = Key.builder(TypeTokens.STRING)
    .name("DISCORD_PLAYER_CHAT_FORMAT")
    .fallback("[%server%] %prefix% %player% %suffix%")
    .build()
  val DISCORD_JOIN_FORMAT = Key.builder(TypeTokens.STRING)
    .name("DISCORD_JOIN_FORMAT")
    .fallback("%player% has joined the game.")
    .build()
  val DISCORD_LEAVE_FORMAT = Key.builder(TypeTokens.STRING)
    .name("DISCORD_LEAVE_FORMAT")
    .fallback("%player% has left the game.")
    .build()
  val DISCORD_CHAT_FORMAT = Key.builder(TypeTokens.STRING)
    .name("DISCORD_CHAT_FORMAT")
    .fallback("&6[Discord]&7 %name% : %message%")
    .build()
  val TOPIC_FORMAT = Key.builder(TypeTokens.STRING)
    .name("TOPIC_FORMAT")
    .fallback("Player Count: %players%")
    .build()
  val TOPIC_UPDATE_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("TOPIC_UPDATE_ENABLED")
    .fallback(false)
    .build()
  val TOPIC_UPDATE_DELAY = Key.builder(TypeTokens.INTEGER)
    .name("TOPIC_UPDATE_DELAY")
    .fallback(5)
    .build()
  val TOPIC_NO_ONLINE_PLAYERS = Key.builder(TypeTokens.STRING)
    .name("TOPIC_NO_ONLINE_PLAYERS")
    .fallback("There are no players online!")
    .build()
  val NOW_PLAYING_MESSAGE = Key.builder(TypeTokens.STRING)
    .name("NOW_PLAYING_MESSAGE")
    .fallback("A Minecraft Server!")
    .build()
  val WEBHOOK_URL = Key.builder(TypeTokens.STRING)
    .name("WEBHOOK_URL")
    .fallback("https://crafatar.com/avatars/%uuid%?default=MHF_Alex")
    .build()
  val DISCORD_URL = Key.builder(TypeTokens.STRING)
    .name("DISCORD_URL")
    .fallback("https://discord.gg/hZpQ5Sg")
    .build()
  val DISCORD_ENABLE = Key.builder(TypeTokens.BOOLEAN)
    .name("DISCORD_ENABLE")
    .fallback(false)
    .build()
  val DISCORD_HOVER_MESSAGE = Key.builder(TypeTokens.STRING)
    .name("DISCORD_HOVER_MESSAGE")
    .fallback("Click here to join our discord!")
    .build()
  val WEBSITE_URL = Key.builder(TypeTokens.STRING)
    .name("WEBSITE_URL")
    .fallback("https://www.anvilpowered.org")
    .build()
  val IGNORE_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("IGNORE_PERMISSION")
    .fallback("catalyst.command.ignore")
    .sensitive()
    .build()
  val IGNORE_EXEMPT_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("IGNORE_EXEMPT_PERMISSION")
    .fallback("catalyst.command.ignore.exempt")
    .sensitive()
    .build()
  val SERVER_PING = Key.builder(TypeTokens.STRING)
    .name("SERVER_PING")
    .fallback("PLAYERS")
    .build()
  val SERVER_PING_MESSAGE = Key.builder(TypeTokens.STRING)
    .name("SERVER_PING_MESSAGE")
    .fallback("Change this message in the config!")
    .build()
  val SYNC_COMMAND_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("SYNC_COMMNAD_PERMISSION")
    .fallback("catalyst.admin.command.sync")
    .sensitive()
    .build()
  val MOTD = Key.builder(TypeTokens.STRING)
    .name("MOTD")
    .fallback("A Velocity Proxy!")
    .build()
  val MOTD_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("MOTD_ENABLED")
    .fallback(false)
    .build()
  val ADVANCED_SERVER_INFO_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("ADVANCED_SERVER_INFO_ENABLED")
    .fallback(false)
    .build()
  private val emojiMap = mutableMapOf<String, Char>()
  var emojiChar = '\uac00'
  val EMOJI_ENABLE = Key.builder(TypeTokens.BOOLEAN)
    .name("EMOJI_ENABLE")
    .fallback(false)
    .build()
  val EMOJI_MAP = Key.builder(object : TypeToken<Map<String, Char>>() {})
    .name("EMOJI_MAP")
    .fallback(emojiMap)
    .sensitive()
    .build()
  val EMOJI_PERMISSION = Key.builder(TypeTokens.STRING)
    .name("EMOJI_PERMISSION")
    .fallback("catalyst.chat.emoji.base")
    .sensitive()
    .build()
  var chatChannels = mutableListOf<ChatChannel>()
  val CHAT_CHANNELS = Key.builder(object : TypeToken<MutableList<ChatChannel>>() {})
    .name("CHAT_CHANNELS")
    .fallback(chatChannels)
    .build()
  var advancedServerInfo: MutableList<AdvancedServerInfo> = LinkedList()
  val ADVANCED_SERVER_INFO = Key.builder(object : TypeToken<MutableList<AdvancedServerInfo>>() {})
    .name("ADVANCED_SERVER_INFO")
    .fallback(advancedServerInfo)
    .build()
  val VIA_VERSION_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("VIA_VERSION_ENABLED")
    .fallback(false)
    .build()
  val COMMAND_LOGGING_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("COMMAND_LOGGING_ENABLED")
    .fallback(true)
    .build()
  val COMMAND_LOGGING_FILTER = Key.builder(LIST_STRING)
    .name("COMMAND_LOGGING_FILTER")
    .fallback(mutableListOf("*"))
    .build()
  val ENABLE_PER_SERVER_PERMS = Key.builder(TypeTokens.BOOLEAN)
    .name("ENABLE_PER_SERVER_PERMS")
    .fallback(false)
    .build()

  //Keys for command toggling
  val BAN_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("BAN_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val BROADCAST_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("BROADCAST_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val CHANNEL_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("CHANNEL_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val CHANNEL_COMMAND_ALIAS_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("CHANNEL_COMMAND_ALIAS_ENABLED")
    .fallback(false)
    .build()
  val NICKNAME_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("NICKNAME_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val FIND_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("FIND_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val INFO_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("INFO_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val KICK_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("KICK_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val LIST_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("LIST_COMMAND_ENABLED")
    .fallback(false)
    .build()
  val MESSAGE_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("MESSAGE_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val SEND_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("SEND_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val SERVER_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("SERVER_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val SOCIALSPY_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("SOCIALSPY_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val MUTE_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("MUTE_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val IGNORE_COMMAND_ENABLED = Key.builder(TypeTokens.BOOLEAN)
    .name("IGNORE_COMMAND_ENABLED")
    .fallback(true)
    .build()
  val CATALYST_PREFIX = Key.builder(TypeTokens.STRING)
    .name("CATALYST_PREFIX")
    .fallback("Catalyst")
    .build()

  //Keys for root node comments
  val ADVANCED_ROOT = Key.builder(TypeTokens.STRING)
    .name("ADVANCED_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val COMMANDS_ROOT = Key.builder(TypeTokens.STRING)
    .name("COMMANDS_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val CHAT_ROOT = Key.builder(TypeTokens.STRING)
    .name("CHAT_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val DISCORD_ROOT = Key.builder(TypeTokens.STRING)
    .name("DISCORD_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val JOIN_ROOT = Key.builder(TypeTokens.STRING)
    .name("JOIN_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val LEAVE_ROOT = Key.builder(TypeTokens.STRING)
    .name("LEAVE_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val MODULES_ROOT = Key.builder(TypeTokens.STRING)
    .name("MODULES_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val MOTD_ROOT = Key.builder(TypeTokens.STRING)
    .name("MOTD_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val PING_ROOT = Key.builder(TypeTokens.STRING)
    .name("PING_ROOT")
    .fallback(null)
    .sensitive()
    .build()
  val TAB_ROOT = Key.builder(TypeTokens.STRING)
    .name("TAB_ROOT")
    .fallback(null)
    .sensitive()
    .build()

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

    val example = AdvancedServerInfo()
    example.hostName = "lobby.hostname.com"
    example.motd = "&cA Velocity Proxy"
    example.prefix = "Lobby"
    example.port = 30066
    example.modded = false
    advancedServerInfo.add(example)

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
      .register(TAB_ORDER)
      .register(TAB_GROUP_ORDER)
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
      .register(ADVANCED_SERVER_INFO_ENABLED)
      .register(ADVANCED_SERVER_INFO)
      .register(EMOJI_ENABLE)
      .register(EMOJI_MAP)
      .register(EMOJI_PERMISSION)
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

    emojiMap[":100:"] = emojiChar++
    emojiMap[":1234:"] = emojiChar++
    emojiMap[":grinning:"] = emojiChar++
    emojiMap[":grimacing:"] = emojiChar++
    emojiMap[":grin:"] = emojiChar++
    emojiMap[":joy:"] = emojiChar++
    emojiMap[":rofl:"] = emojiChar++
    emojiMap[":smiley:"] = emojiChar++
    emojiMap[":smile:"] = emojiChar++
    emojiMap[":sweat_smile:"] = emojiChar++
    emojiMap[":laughing:"] = emojiChar++
    emojiMap[":innocent:"] = emojiChar++
    emojiMap[":wink:"] = emojiChar++
    emojiMap[":blush:"] = emojiChar++
    emojiMap[":slightly_smiling_face:"] = emojiChar++
    emojiMap[":upside_down_face:"] = emojiChar++
    emojiMap[":relaxed:"] = emojiChar++
    emojiMap[":yum:"] = emojiChar++
    emojiMap[":relieved:"] = emojiChar++
    emojiMap[":heart_eyes:"] = emojiChar++
    emojiMap[":kissing_heart:"] = emojiChar++
    emojiMap[":kissing:"] = emojiChar++
    emojiMap[":kissing_smiling_eyes:"] = emojiChar++
    emojiMap[":kissing_closed_eyes:"] = emojiChar++
    emojiMap[":stuck_out_tongue_winking_eye:"] = emojiChar++
    emojiMap[":stuck_out_tongue_closed_eyes:"] = emojiChar++
    emojiMap[":stuck_out_tongue:"] = emojiChar++
    emojiMap[":money_mouth_face:"] = emojiChar++
    emojiMap[":nerd_face:"] = emojiChar++
    emojiMap[":sunglasses:"] = emojiChar++
    emojiMap[":clown_face:"] = emojiChar++
    emojiMap[":cowboy_hat_face:"] = emojiChar++
    emojiMap[":hugs:"] = emojiChar++
    emojiMap[":smirk:"] = emojiChar++
    emojiMap[":no_mouth:"] = emojiChar++
    emojiMap[":neutral_face:"] = emojiChar++
    emojiMap[":expressionless:"] = emojiChar++
    emojiMap[":unamused:"] = emojiChar++
    emojiMap[":roll_eyes:"] = emojiChar++
    emojiMap[":thinking:"] = emojiChar++
    emojiMap[":lying_face:"] = emojiChar++
    emojiMap[":flushed:"] = emojiChar++
    emojiMap[":disappointed:"] = emojiChar++
    emojiMap[":worried:"] = emojiChar++
    emojiMap[":angry:"] = emojiChar++
    emojiMap[":rage:"] = emojiChar++
    emojiMap[":pensive:"] = emojiChar++
    emojiMap[":confused:"] = emojiChar++
    emojiMap[":slightly_frowning_face:"] = emojiChar++
    emojiMap[":frowning_face:"] = emojiChar++
    emojiMap[":persevere:"] = emojiChar++
    emojiMap[":confounded:"] = emojiChar++
    emojiMap[":tired_face:"] = emojiChar++
    emojiMap[":weary:"] = emojiChar++
    emojiMap[":triumph:"] = emojiChar++
    emojiMap[":open_mouth:"] = emojiChar++
    emojiMap[":scream:"] = emojiChar++
    emojiMap[":fearful:"] = emojiChar++
    emojiMap[":cold_sweat:"] = emojiChar++
    emojiMap[":hushed:"] = emojiChar++
    emojiMap[":frowning:"] = emojiChar++
    emojiMap[":anguished:"] = emojiChar++
    emojiMap[":cry:"] = emojiChar++
    emojiMap[":disappointed_relieved:"] = emojiChar++
    emojiMap[":drooling_face:"] = emojiChar++
    emojiMap[":sleepy:"] = emojiChar++
    emojiMap[":sweat:"] = emojiChar++
    emojiMap[":sob:"] = emojiChar++
    emojiMap[":dizzy_face:"] = emojiChar++
    emojiMap[":astonished:"] = emojiChar++
    emojiMap[":zipper_mouth_face:"] = emojiChar++
    emojiMap[":nauseated_face:"] = emojiChar++
    emojiMap[":sneezing_face:"] = emojiChar++
    emojiMap[":mask:"] = emojiChar++
    emojiMap[":face_with_thermometer:"] = emojiChar++
    emojiMap[":face_with_head_bandage:"] = emojiChar++
    emojiMap[":sleeping:"] = emojiChar++
    emojiMap[":zzz:"] = emojiChar++
    emojiMap[":poop:"] = emojiChar++
    emojiMap[":smiling_imp:"] = emojiChar++
    emojiMap[":imp:"] = emojiChar++
    emojiMap[":japanese_ogre:"] = emojiChar++
    emojiMap[":japanese_goblin:"] = emojiChar++
    emojiMap[":skull:"] = emojiChar++
    emojiMap[":ghost:"] = emojiChar++
    emojiMap[":alien:"] = emojiChar++
    emojiMap[":robot:"] = emojiChar++
    emojiMap[":smiley_cat:"] = emojiChar++
    emojiMap[":smile_cat:"] = emojiChar++
    emojiMap[":joy_cat:"] = emojiChar++
    emojiMap[":heart_eyes_cat:"] = emojiChar++
    emojiMap[":smirk_cat:"] = emojiChar++
    emojiMap[":kissing_cat:"] = emojiChar++
    emojiMap[":scream_cat:"] = emojiChar++
    emojiMap[":crying_cat_face:"] = emojiChar++
    emojiMap[":pouting_cat:"] = emojiChar++
    emojiMap[":raised_hands:"] = emojiChar++
    emojiMap[":clap:"] = emojiChar++
    emojiMap[":wave:"] = emojiChar++
    emojiMap[":call_me_hand:"] = emojiChar++
    emojiMap[":+1:"] = emojiChar++
    emojiMap[":-1:"] = emojiChar++
    emojiMap[":facepunch:"] = emojiChar++
    emojiMap[":fist:"] = emojiChar++
    emojiMap[":fist_left:"] = emojiChar++
    emojiMap[":fist_right:"] = emojiChar++
    emojiMap[":v:"] = emojiChar++
    emojiMap[":ok_hand:"] = emojiChar++
    emojiMap[":raised_hand:"] = emojiChar++
    emojiMap[":raised_back_of_hand:"] = emojiChar++
    emojiMap[":open_hands:"] = emojiChar++
    emojiMap[":muscle:"] = emojiChar++
    emojiMap[":pray:"] = emojiChar++
    emojiMap[":handshake:"] = emojiChar++
    emojiMap[":point_up:"] = emojiChar++
    emojiMap[":point_up_2:"] = emojiChar++
    emojiMap[":point_down:"] = emojiChar++
    emojiMap[":point_left:"] = emojiChar++
    emojiMap[":point_right:"] = emojiChar++
    emojiMap[":fu:"] = emojiChar++
    emojiMap[":raised_hand_with_fingers_splayed:"] = emojiChar++
    emojiMap[":metal:"] = emojiChar++
    emojiMap[":crossed_fingers:"] = emojiChar++
    emojiMap[":vulcan_salute:"] = emojiChar++
    emojiMap[":writing_hand:"] = emojiChar++
    emojiMap[":selfie:"] = emojiChar++
    emojiMap[":lips:"] = emojiChar++
    emojiMap[":tongue:"] = emojiChar++
    emojiMap[":ear:"] = emojiChar++
    emojiMap[":nose:"] = emojiChar++
    emojiMap[":eye:"] = emojiChar++
    emojiMap[":eyes:"] = emojiChar++
    emojiMap[":womans_clothes:"] = emojiChar++
    emojiMap[":tshirt:"] = emojiChar++
    emojiMap[":jeans:"] = emojiChar++
    emojiMap[":necktie:"] = emojiChar++
    emojiMap[":dress:"] = emojiChar++
    emojiMap[":bikini:"] = emojiChar++
    emojiMap[":kimono:"] = emojiChar++
    emojiMap[":lipstick:"] = emojiChar++
    emojiMap[":kiss:"] = emojiChar++
    emojiMap[":footprints:"] = emojiChar++
    emojiMap[":high_heel:"] = emojiChar++
    emojiMap[":sandal:"] = emojiChar++
    emojiMap[":boot:"] = emojiChar++
    emojiMap[":mans_shoe:"] = emojiChar++
    emojiMap[":athletic_shoe:"] = emojiChar++
    emojiMap[":womans_hat:"] = emojiChar++
    emojiMap[":tophat:"] = emojiChar++
    emojiMap[":rescue_worker_helmet:"] = emojiChar++
    emojiMap[":mortar_board:"] = emojiChar++
    emojiMap[":crown:"] = emojiChar++
    emojiMap[":school_satchel:"] = emojiChar++
    emojiMap[":pouch:"] = emojiChar++
    emojiMap[":purse:"] = emojiChar++
    emojiMap[":handbag:"] = emojiChar++
    emojiMap[":briefcase:"] = emojiChar++
    emojiMap[":eyeglasses:"] = emojiChar++
    emojiMap[":dark_sunglasses:"] = emojiChar++
    emojiMap[":ring:"] = emojiChar++
    emojiMap[":closed_umbrella:"] = emojiChar++
    emojiMap[":dog:"] = emojiChar++
    emojiMap[":cat:"] = emojiChar++
    emojiMap[":mouse:"] = emojiChar++
    emojiMap[":hamster:"] = emojiChar++
    emojiMap[":rabbit:"] = emojiChar++
    emojiMap[":fox_face:"] = emojiChar++
    emojiMap[":bear:"] = emojiChar++
    emojiMap[":panda_face:"] = emojiChar++
    emojiMap[":koala:"] = emojiChar++
    emojiMap[":tiger:"] = emojiChar++
    emojiMap[":lion:"] = emojiChar++
    emojiMap[":cow:"] = emojiChar++
    emojiMap[":pig:"] = emojiChar++
    emojiMap[":pig_nose:"] = emojiChar++
    emojiMap[":frog:"] = emojiChar++
    emojiMap[":squid:"] = emojiChar++
    emojiMap[":octopus:"] = emojiChar++
    emojiMap[":shrimp:"] = emojiChar++
    emojiMap[":monkey_face:"] = emojiChar++
    emojiMap[":gorilla:"] = emojiChar++
    emojiMap[":see_no_evil:"] = emojiChar++
    emojiMap[":hear_no_evil:"] = emojiChar++
    emojiMap[":speak_no_evil:"] = emojiChar++
    emojiMap[":monkey:"] = emojiChar++
    emojiMap[":chicken:"] = emojiChar++
    emojiMap[":penguin:"] = emojiChar++
    emojiMap[":bird:"] = emojiChar++
    emojiMap[":baby_chick:"] = emojiChar++
    emojiMap[":hatching_chick:"] = emojiChar++
    emojiMap[":hatched_chick:"] = emojiChar++
    emojiMap[":duck:"] = emojiChar++
    emojiMap[":eagle:"] = emojiChar++
    emojiMap[":owl:"] = emojiChar++
    emojiMap[":bat:"] = emojiChar++
    emojiMap[":wolf:"] = emojiChar++
    emojiMap[":boar:"] = emojiChar++
    emojiMap[":horse:"] = emojiChar++
    emojiMap[":unicorn:"] = emojiChar++
    emojiMap[":honeybee:"] = emojiChar++
    emojiMap[":bug:"] = emojiChar++
    emojiMap[":butterfly:"] = emojiChar++
    emojiMap[":snail:"] = emojiChar++
    emojiMap[":beetle:"] = emojiChar++
    emojiMap[":ant:"] = emojiChar++
    emojiMap[":spider:"] = emojiChar++
    emojiMap[":scorpion:"] = emojiChar++
    emojiMap[":crab:"] = emojiChar++
    emojiMap[":snake:"] = emojiChar++
    emojiMap[":lizard:"] = emojiChar++
    emojiMap[":turtle:"] = emojiChar++
    emojiMap[":tropical_fish:"] = emojiChar++
    emojiMap[":fish:"] = emojiChar++
    emojiMap[":blowfish:"] = emojiChar++
    emojiMap[":dolphin:"] = emojiChar++
    emojiMap[":shark:"] = emojiChar++
    emojiMap[":whale:"] = emojiChar++
    emojiMap[":whale2:"] = emojiChar++
    emojiMap[":crocodile:"] = emojiChar++
    emojiMap[":leopard:"] = emojiChar++
    emojiMap[":tiger2:"] = emojiChar++
    emojiMap[":water_buffalo:"] = emojiChar++
    emojiMap[":ox:"] = emojiChar++
    emojiMap[":cow2:"] = emojiChar++
    emojiMap[":deer:"] = emojiChar++
    emojiMap[":dromedary_camel:"] = emojiChar++
    emojiMap[":camel:"] = emojiChar++
    emojiMap[":elephant:"] = emojiChar++
    emojiMap[":rhinoceros:"] = emojiChar++
    emojiMap[":goat:"] = emojiChar++
    emojiMap[":ram:"] = emojiChar++
    emojiMap[":sheep:"] = emojiChar++
    emojiMap[":racehorse:"] = emojiChar++
    emojiMap[":pig2:"] = emojiChar++
    emojiMap[":rat:"] = emojiChar++
    emojiMap[":mouse2:"] = emojiChar++
    emojiMap[":rooster:"] = emojiChar++
    emojiMap[":turkey:"] = emojiChar++
    emojiMap[":dove:"] = emojiChar++
    emojiMap[":dog2:"] = emojiChar++
    emojiMap[":poodle:"] = emojiChar++
    emojiMap[":cat2:"] = emojiChar++
    emojiMap[":rabbit2:"] = emojiChar++
    emojiMap[":chipmunk:"] = emojiChar++
    emojiMap[":paw_prints:"] = emojiChar++
    emojiMap[":dragon:"] = emojiChar++
    emojiMap[":dragon_face:"] = emojiChar++
    emojiMap[":cactus:"] = emojiChar++
    emojiMap[":christmas_tree:"] = emojiChar++
    emojiMap[":evergreen_tree:"] = emojiChar++
    emojiMap[":deciduous_tree:"] = emojiChar++
    emojiMap[":palm_tree:"] = emojiChar++
    emojiMap[":seedling:"] = emojiChar++
    emojiMap[":herb:"] = emojiChar++
    emojiMap[":shamrock:"] = emojiChar++
    emojiMap[":four_leaf_clover:"] = emojiChar++
    emojiMap[":bamboo:"] = emojiChar++
    emojiMap[":tanabata_tree:"] = emojiChar++
    emojiMap[":leaves:"] = emojiChar++
    emojiMap[":fallen_leaf:"] = emojiChar++
    emojiMap[":maple_leaf:"] = emojiChar++
    emojiMap[":ear_of_rice:"] = emojiChar++
    emojiMap[":hibiscus:"] = emojiChar++
    emojiMap[":sunflower:"] = emojiChar++
    emojiMap[":rose:"] = emojiChar++
    emojiMap[":wilted_flower:"] = emojiChar++
    emojiMap[":tulip:"] = emojiChar++
    emojiMap[":blossom:"] = emojiChar++
    emojiMap[":cherry_blossom:"] = emojiChar++
    emojiMap[":bouquet:"] = emojiChar++
    emojiMap[":mushroom:"] = emojiChar++
    emojiMap[":chestnut:"] = emojiChar++
    emojiMap[":jack_o_lantern:"] = emojiChar++
    emojiMap[":shell:"] = emojiChar++
    emojiMap[":spider_web:"] = emojiChar++
    emojiMap[":earth_americas:"] = emojiChar++
    emojiMap[":earth_africa:"] = emojiChar++
    emojiMap[":earth_asia:"] = emojiChar++
    emojiMap[":full_moon:"] = emojiChar++
    emojiMap[":waning_gibbous_moon:"] = emojiChar++
    emojiMap[":last_quarter_moon:"] = emojiChar++
    emojiMap[":waning_crescent_moon:"] = emojiChar++
    emojiMap[":new_moon:"] = emojiChar++
    emojiMap[":waxing_crescent_moon:"] = emojiChar++
    emojiMap[":first_quarter_moon:"] = emojiChar++
    emojiMap[":waxing_gibbous_moon:"] = emojiChar++
    emojiMap[":new_moon_with_face:"] = emojiChar++
    emojiMap[":full_moon_with_face:"] = emojiChar++
    emojiMap[":first_quarter_moon_with_face:"] = emojiChar++
    emojiMap[":last_quarter_moon_with_face:"] = emojiChar++
    emojiMap[":sun_with_face:"] = emojiChar++
    emojiMap[":crescent_moon:"] = emojiChar++
    emojiMap[":star:"] = emojiChar++
    emojiMap[":star2:"] = emojiChar++
    emojiMap[":dizzy:"] = emojiChar++
    emojiMap[":sparkles:"] = emojiChar++
    emojiMap[":comet:"] = emojiChar++
    emojiMap[":sunny:"] = emojiChar++
    emojiMap[":sun_behind_small_cloud:"] = emojiChar++
    emojiMap[":partly_sunny:"] = emojiChar++
    emojiMap[":sun_behind_large_cloud:"] = emojiChar++
    emojiMap[":sun_behind_rain_cloud:"] = emojiChar++
    emojiMap[":cloud:"] = emojiChar++
    emojiMap[":cloud_with_rain:"] = emojiChar++
    emojiMap[":cloud_with_lightning_and_rain:"] = emojiChar++
    emojiMap[":cloud_with_lightning:"] = emojiChar++
    emojiMap[":zap:"] = emojiChar++
    emojiMap[":fire:"] = emojiChar++
    emojiMap[":boom:"] = emojiChar++
    emojiMap[":snowflake:"] = emojiChar++
    emojiMap[":cloud_with_snow:"] = emojiChar++
    emojiMap[":snowman:"] = emojiChar++
    emojiMap[":snowman_with_snow:"] = emojiChar++
    emojiMap[":wind_face:"] = emojiChar++
    emojiMap[":dash:"] = emojiChar++
    emojiMap[":tornado:"] = emojiChar++
    emojiMap[":fog:"] = emojiChar++
    emojiMap[":open_umbrella:"] = emojiChar++
    emojiMap[":umbrella:"] = emojiChar++
    emojiMap[":droplet:"] = emojiChar++
    emojiMap[":sweat_drops:"] = emojiChar++
    emojiMap[":ocean:"] = emojiChar++
    emojiMap[":green_apple:"] = emojiChar++
    emojiMap[":apple:"] = emojiChar++
    emojiMap[":pear:"] = emojiChar++
    emojiMap[":tangerine:"] = emojiChar++
    emojiMap[":lemon:"] = emojiChar++
    emojiMap[":banana:"] = emojiChar++
    emojiMap[":watermelon:"] = emojiChar++
    emojiMap[":grapes:"] = emojiChar++
    emojiMap[":strawberry:"] = emojiChar++
    emojiMap[":melon:"] = emojiChar++
    emojiMap[":cherries:"] = emojiChar++
    emojiMap[":peach:"] = emojiChar++
    emojiMap[":pineapple:"] = emojiChar++
    emojiMap[":kiwi_fruit:"] = emojiChar++
    emojiMap[":avocado:"] = emojiChar++
    emojiMap[":tomato:"] = emojiChar++
    emojiMap[":eggplant:"] = emojiChar++
    emojiMap[":cucumber:"] = emojiChar++
    emojiMap[":carrot:"] = emojiChar++
    emojiMap[":hot_pepper:"] = emojiChar++
    emojiMap[":potato:"] = emojiChar++
    emojiMap[":corn:"] = emojiChar++
    emojiMap[":sweet_potato:"] = emojiChar++
    emojiMap[":peanuts:"] = emojiChar++
    emojiMap[":honey_pot:"] = emojiChar++
    emojiMap[":croissant:"] = emojiChar++
    emojiMap[":bread:"] = emojiChar++
    emojiMap[":baguette_bread:"] = emojiChar++
    emojiMap[":cheese:"] = emojiChar++
    emojiMap[":egg:"] = emojiChar++
    emojiMap[":bacon:"] = emojiChar++
    emojiMap[":pancakes:"] = emojiChar++
    emojiMap[":poultry_leg:"] = emojiChar++
    emojiMap[":meat_on_bone:"] = emojiChar++
    emojiMap[":fried_shrimp:"] = emojiChar++
    emojiMap[":fried_egg:"] = emojiChar++
    emojiMap[":hamburger:"] = emojiChar++
    emojiMap[":fries:"] = emojiChar++
    emojiMap[":stuffed_flatbread:"] = emojiChar++
    emojiMap[":hotdog:"] = emojiChar++
    emojiMap[":pizza:"] = emojiChar++
    emojiMap[":spaghetti:"] = emojiChar++
    emojiMap[":taco:"] = emojiChar++
    emojiMap[":burrito:"] = emojiChar++
    emojiMap[":green_salad:"] = emojiChar++
    emojiMap[":shallow_pan_of_food:"] = emojiChar++
    emojiMap[":ramen:"] = emojiChar++
    emojiMap[":stew:"] = emojiChar++
    emojiMap[":fish_cake:"] = emojiChar++
    emojiMap[":sushi:"] = emojiChar++
    emojiMap[":bento:"] = emojiChar++
    emojiMap[":curry:"] = emojiChar++
    emojiMap[":rice_ball:"] = emojiChar++
    emojiMap[":rice:"] = emojiChar++
    emojiMap[":rice_cracker:"] = emojiChar++
    emojiMap[":oden:"] = emojiChar++
    emojiMap[":dango:"] = emojiChar++
    emojiMap[":shaved_ice:"] = emojiChar++
    emojiMap[":ice_cream:"] = emojiChar++
    emojiMap[":icecream:"] = emojiChar++
    emojiMap[":cake:"] = emojiChar++
    emojiMap[":birthday:"] = emojiChar++
    emojiMap[":custard:"] = emojiChar++
    emojiMap[":candy:"] = emojiChar++
    emojiMap[":lollipop:"] = emojiChar++
    emojiMap[":chocolate_bar:"] = emojiChar++
    emojiMap[":popcorn:"] = emojiChar++
    emojiMap[":doughnut:"] = emojiChar++
    emojiMap[":cookie:"] = emojiChar++
    emojiMap[":milk_glass:"] = emojiChar++
    emojiMap[":beer:"] = emojiChar++
    emojiMap[":beers:"] = emojiChar++
    emojiMap[":clinking_glasses:"] = emojiChar++
    emojiMap[":wine_glass:"] = emojiChar++
    emojiMap[":tumbler_glass:"] = emojiChar++
    emojiMap[":cocktail:"] = emojiChar++
    emojiMap[":tropical_drink:"] = emojiChar++
    emojiMap[":champagne:"] = emojiChar++
    emojiMap[":sake:"] = emojiChar++
    emojiMap[":tea:"] = emojiChar++
    emojiMap[":coffee:"] = emojiChar++
    emojiMap[":crazy_face:"] = emojiChar++
    emojiMap[":face_with_monocle:"] = emojiChar++
    emojiMap[":face_with_raised_eyebrow:"] = emojiChar++
    emojiMap[":shushing_face:"] = emojiChar++
    emojiMap[":face_with_hand_over_mouth:"] = emojiChar++
    emojiMap[":face_with_symbols_over_mouth:"] = emojiChar++
    emojiMap[":star_struck:"] = emojiChar++
    emojiMap[":exploding_head:"] = emojiChar++
    emojiMap[":face_vomiting:"] = emojiChar++
    emojiMap[":soccer:"] = emojiChar++
    emojiMap[":basketball:"] = emojiChar++
    emojiMap[":football:"] = emojiChar++
    emojiMap[":baseball:"] = emojiChar++
    emojiMap[":tennis:"] = emojiChar++
    emojiMap[":volleyball:"] = emojiChar++
    emojiMap[":rugby_football:"] = emojiChar++
    emojiMap[":8ball:"] = emojiChar++
    emojiMap[":golf:"] = emojiChar++
    emojiMap[":ping_pong:"] = emojiChar++
    emojiMap[":badminton:"] = emojiChar++
    emojiMap[":goal_net:"] = emojiChar++
    emojiMap[":ice_hockey:"] = emojiChar++
    emojiMap[":field_hockey:"] = emojiChar++
    emojiMap[":cricket:"] = emojiChar++
    emojiMap[":ice_skate:"] = emojiChar++
    emojiMap[":bow_and_arrow:"] = emojiChar++
    emojiMap[":boxing_glove:"] = emojiChar++
    emojiMap[":martial_arts_uniform:"] = emojiChar++
    emojiMap[":trophy:"] = emojiChar++
    emojiMap[":medal_sports:"] = emojiChar++
    emojiMap[":medal_military:"] = emojiChar++
    emojiMap[":1st_place_medal:"] = emojiChar++
    emojiMap[":2nd_place_medal:"] = emojiChar++
    emojiMap[":3rd_place_medal:"] = emojiChar++
    emojiMap[":reminder_ribbon:"] = emojiChar++
    emojiMap[":rosette:"] = emojiChar++
    emojiMap[":ticket:"] = emojiChar++
    emojiMap[":tickets:"] = emojiChar++
    emojiMap[":performing_arts:"] = emojiChar++
    emojiMap[":art:"] = emojiChar++
    emojiMap[":circus_tent:"] = emojiChar++
    emojiMap[":microphone:"] = emojiChar++
    emojiMap[":headphones:"] = emojiChar++
    emojiMap[":musical_score:"] = emojiChar++
    emojiMap[":musical_keyboard:"] = emojiChar++
    emojiMap[":drum:"] = emojiChar++
    emojiMap[":saxophone:"] = emojiChar++
    emojiMap[":trumpet:"] = emojiChar++
    emojiMap[":guitar:"] = emojiChar++
    emojiMap[":violin:"] = emojiChar++
    emojiMap[":clapper:"] = emojiChar++
    emojiMap[":video_game:"] = emojiChar++
    emojiMap[":space_invader:"] = emojiChar++
    emojiMap[":dart:"] = emojiChar++
    emojiMap[":game_die:"] = emojiChar++
    emojiMap[":slot_machine:"] = emojiChar++
    emojiMap[":bowling:"] = emojiChar++
    emojiMap[":heart:"] = emojiChar++
    emojiMap[":broken_heart:"] = emojiChar++
    emojiMap[":ok:"] = emojiChar++
    emojiMap[":up:"] = emojiChar++
    emojiMap[":cool:"] = emojiChar++
    emojiMap[":new:"] = emojiChar++
    emojiMap[":free:"] = emojiChar++
    emojiMap[":zero:"] = emojiChar++
    emojiMap[":one:"] = emojiChar++
    emojiMap[":two:"] = emojiChar++
    emojiMap[":three:"] = emojiChar++
    emojiMap[":four:"] = emojiChar++
    emojiMap[":five:"] = emojiChar++
    emojiMap[":six:"] = emojiChar++
    emojiMap[":seven:"] = emojiChar++
    emojiMap[":eight:"] = emojiChar++
    emojiMap[":nine:"] = emojiChar++
    emojiMap[":keycap_ten:"] = emojiChar++
    emojiMap[":asterisk:"] = emojiChar++
    emojiMap[":rotating_light:"] = emojiChar++
    emojiMap[":airplane:"] = emojiChar++
    emojiMap[":rocket:"] = emojiChar++
    emojiMap[":artificial_satellite:"] = emojiChar++
    emojiMap[":anchor:"] = emojiChar++
    emojiMap[":construction:"] = emojiChar++
    emojiMap[":vertical_traffic_light:"] = emojiChar++
    emojiMap[":traffic_light:"] = emojiChar++
    emojiMap[":checkered_flag:"] = emojiChar++
    emojiMap[":tokyo_tower:"] = emojiChar++
    emojiMap[":fountain:"] = emojiChar++
    emojiMap[":mountain:"] = emojiChar++
    emojiMap[":mountain_snow:"] = emojiChar++
    emojiMap[":mount_fuji:"] = emojiChar++
    emojiMap[":volcano:"] = emojiChar++
    emojiMap[":tent:"] = emojiChar++
    emojiMap[":railway_track:"] = emojiChar++
    emojiMap[":sunrise:"] = emojiChar++
    emojiMap[":sunrise_over_mountains:"] = emojiChar++
    emojiMap[":desert:"] = emojiChar++
    emojiMap[":beach_umbrella:"] = emojiChar++
    emojiMap[":desert_island:"] = emojiChar++
    emojiMap[":cityscape:"] = emojiChar++
    emojiMap[":night_with_stars:"] = emojiChar++
    emojiMap[":bridge_at_night:"] = emojiChar++
    emojiMap[":city_sunrise:"] = emojiChar++
    emojiMap[":city_sunset:"] = emojiChar++
    emojiMap[":watch:"] = emojiChar++
    emojiMap[":phone:"] = emojiChar++
    emojiMap[":computer:"] = emojiChar++
    emojiMap[":keyboard:"] = emojiChar++
    emojiMap[":desktop_computer:"] = emojiChar++
    emojiMap[":printer:"] = emojiChar++
    emojiMap[":mc_planks:"] = emojiChar++
    emojiMap[":mc_podzol:"] = emojiChar++
    emojiMap[":mc_grass:"] = emojiChar++
    emojiMap[":mc_snow_grass:"] = emojiChar++
    emojiMap[":mc_dirt:"] = emojiChar++
    emojiMap[":mc_mycellium:"] = emojiChar++
    emojiMap[":mc_red_sand:"] = emojiChar++
    emojiMap[":mc_sand:"] = emojiChar++
    emojiMap[":mc_soulsand:"] = emojiChar++
    emojiMap[":mc_netherrack:"] = emojiChar++
    emojiMap[":mc_netherbrick:"] = emojiChar++
    emojiMap[":mc_quartz_ore:"] = emojiChar++
    emojiMap[":mc_ender_portal:"] = emojiChar++
    emojiMap.put(":mc_ghast:", emojiChar++)
    emojiMap.put(":mc_zombie_pigman:", emojiChar++)
    emojiMap.put(":mc_enderman:", emojiChar++)
    emojiMap.put(":mc_magma_cube:", emojiChar++)
    emojiMap.put(":mc_slime:", emojiChar++)
    emojiMap.put(":mc_spider:", emojiChar++)
    emojiMap.put(":mc_chicken:", emojiChar++)
    emojiMap.put(":mc_pig:", emojiChar++)
    emojiMap.put(":mc_sheep:", emojiChar++)
    emojiMap.put(":mc_cow:", emojiChar++)
    emojiMap.put(":mc_squid:", emojiChar++)
    emojiMap.put(":mc_villager:", emojiChar++)
    emojiMap.put(":mc_iron_golem:", emojiChar++)
    emojiMap.put(":mc_mooshroom:", emojiChar++)
    emojiMap.put(":mc_ocelot:", emojiChar++)
    emojiMap.put(":mc_wither:", emojiChar++)
    emojiMap.put(":mc_wooden_sword:", emojiChar++);
    emojiMap.put(":mc_wooden_axe:", emojiChar++);
    emojiMap.put(":mc_wooden_pickaxe:", emojiChar++);
    emojiMap.put(":mc_wooden_shovel:", emojiChar++);
    emojiMap.put(":mc_wooden_hoe:", emojiChar++);
    emojiMap.put(":mc_bow:", emojiChar++);
    emojiMap.put(":mc_arrow:", emojiChar++);
    emojiMap.put(":mc_fishing_rod:", emojiChar++);
    emojiMap[":mc_shears:"] = emojiChar++
    emojiMap.put(":mc_ice:", emojiChar++);
    emojiMap.put(":mc_water:", emojiChar++);
    emojiMap.put(":mc_lava:", emojiChar++);
    emojiMap.put(":mc_emerald_block:", emojiChar++);
    emojiMap.put(":mc_diamond_block:", emojiChar++);
    emojiMap.put(":mc_gold_block:", emojiChar++);
    emojiMap.put(":mc_iron_block:", emojiChar++);
    emojiMap.put(":mc_emerald_ore:", emojiChar++);
    emojiMap.put(":mc_redstone_ore:", emojiChar++);
    emojiMap.put(":mc_diamond_ore:", emojiChar++);
    emojiMap.put(":mc_iron_ore:", emojiChar++);
    emojiMap.put(":mc_gold_ore:", emojiChar++);
    emojiMap.put(":mc_cobblestone:", emojiChar++);
    emojiMap.put(":mc_stone:", emojiChar++);
    emojiMap.put(":mc_stone_brick:", emojiChar++);
    emojiMap.put(":mc_bedrock:", emojiChar++);
    emojiMap.put(":mc_brick:", emojiChar++);
    emojiMap.put(":mc_quartz:", emojiChar++);
    emojiMap.put(":mc_redstone_lamp:", emojiChar++);
    emojiMap.put(":mc_piston:", emojiChar++);
    emojiMap.put(":mc_sticky_piston:", emojiChar++);
    emojiMap.put(":mc_dropper:", emojiChar++);
    emojiMap.put(":mc_redstone_block:", emojiChar++);
    emojiMap.put(":mc_tnt:", emojiChar++);
    emojiMap.put(":mc_jukebox:", emojiChar++);
    emojiMap.put(":mc_sponge:", emojiChar++);
    emojiMap.put(":mc_prismarine:", emojiChar++);
    emojiMap.put(":mc_command_block:", emojiChar++);
    emojiMap.put(":mc_hay:", emojiChar++);
    emojiMap.put(":mc_glowstone:", emojiChar++);
    emojiMap.put(":mc_gravel:", emojiChar++);
    emojiMap.put(":mc_lapis_lazuli_block:", emojiChar++);
    emojiMap.put(":mc_lapis_lazuli_ore:", emojiChar++);
    emojiMap.put(":mc_black_wool:", emojiChar++);
    emojiMap.put(":mc_blue_wool:", emojiChar++);
    emojiMap.put(":mc_brown_wool:", emojiChar++);
    emojiMap.put(":mc_cyan_wool:", emojiChar++);
    emojiMap.put(":mc_gray_wool:", emojiChar++);
    emojiMap.put(":mc_green_wool:", emojiChar++);
    emojiMap.put(":mc_light_blue_wool:", emojiChar++);
    emojiMap.put(":mc_lime_wool:", emojiChar++);
    emojiMap.put(":mc_magenta_wool:", emojiChar++);
    emojiMap.put(":mc_orange_wool:", emojiChar++);
    emojiMap.put(":mc_pink_wool:", emojiChar++);
    emojiMap.put(":mc_purple_wool:", emojiChar++);
    emojiMap.put(":mc_red_wool:", emojiChar++);
    emojiMap.put(":mc_light_gray_wool:", emojiChar++);
    emojiMap.put(":mc_white_wool:", emojiChar++);
    emojiMap.put(":mc_yellow_wool:", emojiChar++);
    emojiMap.put(":mc_snow_block:", emojiChar++);
    emojiMap.put(":mc_slime_block:", emojiChar++);
    emojiMap.put(":mc_obsidian:", emojiChar++);
    emojiMap.put(":mc_red_sandstone:", emojiChar++);
    emojiMap.put(":mc_portal:", emojiChar++);
    emojiMap.put(":mc_endstone:", emojiChar++);
    emojiMap.put(":mc_coal_ore:", emojiChar++);
    emojiMap.put(":mc_coal_block:", emojiChar++);
    emojiMap.put(":mc_beacon:", emojiChar++);
    emojiMap.put(":mc_bookshelf:", emojiChar++);
    emojiMap.put(":mc_chest:", emojiChar++);
    emojiMap.put(":mc_enderchest:", emojiChar++);
    emojiMap.put(":mc_cave_spider:", emojiChar++);
    emojiMap.put(":mc_blaze:", emojiChar++);
    emojiMap.put(":mc_flint_and_steel:", emojiChar++)

    val output = File("")
  }
}
