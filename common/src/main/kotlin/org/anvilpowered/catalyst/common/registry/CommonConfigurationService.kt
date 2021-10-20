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
package org.anvilpowered.catalyst.common.registry

import com.google.inject.Inject
import com.google.inject.Singleton
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.anvilpowered.anvil.api.registry.Keys
import org.anvilpowered.anvil.base.registry.BaseConfigurationService
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import java.util.HashMap
import java.util.function.Function
import java.util.function.Predicate

@Singleton
class CommonConfigurationService @Inject constructor(
  configLoader: ConfigurationLoader<CommentedConfigurationNode>
) : BaseConfigurationService(configLoader) {

  init {
    setDefault(Keys.BASE_SCAN_PACKAGE, "org.anvilpowered.catalyst.common.model")
    setName(CatalystKeys.CHAT_FILTER_SWEARS, "chat.filter.swears")
    setName(CatalystKeys.CHAT_FILTER_EXCEPTIONS, "chat.filter.exceptions")
    setName(CatalystKeys.CHAT_FILTER_ENABLED, "chat.filter.enabled")
    setName(CatalystKeys.FIRST_JOIN, "join.firstJoin")
    setName(CatalystKeys.JOIN_MESSAGE, "join.message")
    setName(CatalystKeys.JOIN_LISTENER_ENABLED, "modules.listener.join")
    setName(CatalystKeys.LEAVE_MESSAGE, "leave.message")
    setName(CatalystKeys.LEAVE_LISTENER_ENABLED, "modules.listener.leave")
    setName(CatalystKeys.NICKNAME_PREFIX, "chat.format.nicknamePrefix")
    setName(CatalystKeys.PRIVATE_MESSAGE_FORMAT, "chat.format.privateMessage")
    setName(CatalystKeys.PROXY_CHAT_ENABLED, "modules.chat")
    setName(CatalystKeys.TAB_ENABLED, "modules.tab")
    setName(CatalystKeys.TAB_FORMAT, "tab.format.player")
    setName(CatalystKeys.TAB_HEADER, "tab.format.header")
    setName(CatalystKeys.TAB_FOOTER, "tab.format.footer")
    setName(CatalystKeys.TAB_FORMAT_CUSTOM, "tab.format.custom")
    setName(CatalystKeys.TAB_UPDATE, "tab.updateDelay")
    setName(CatalystKeys.CHAT_CHANNELS, "chat.channels")
    setName(CatalystKeys.CHAT_DEFAULT_CHANNEL, "chat.channelDefault")
    setName(CatalystKeys.BOT_TOKEN, "discord.bot.token")
    setName(CatalystKeys.BOT_NAME, "discord.bot.name")
    setName(CatalystKeys.DISCORD_PLAYER_CHAT_FORMAT, "discord.format.proxy")
    setName(CatalystKeys.DISCORD_JOIN_FORMAT, "discord.format.join")
    setName(CatalystKeys.DISCORD_LEAVE_FORMAT, "discord.format.leave")
    setName(CatalystKeys.DISCORD_CHAT_FORMAT, "discord.format.discord")
    setName(CatalystKeys.TOPIC_FORMAT, "discord.topic.format")
    setName(CatalystKeys.TOPIC_UPDATE_ENABLED, "discord.topic.enabled")
    setName(CatalystKeys.TOPIC_UPDATE_DELAY, "discord.topic.updateInterval")
    setName(CatalystKeys.TOPIC_NO_ONLINE_PLAYERS, "discord.topic.noPlayers")
    setName(CatalystKeys.NOW_PLAYING_MESSAGE, "discord.format.playing")
    setName(CatalystKeys.DISCORD_HOVER_MESSAGE, "discord.format.hover")
    setName(CatalystKeys.WEBHOOK_URL, "discord.url.webhook")
    setName(CatalystKeys.DISCORD_URL, "discord.url.invite")
    setName(CatalystKeys.DISCORD_ENABLE, "modules.discord")
    setName(CatalystKeys.SERVER_PING, "ping.mode")
    setName(CatalystKeys.SERVER_PING_MESSAGE, "ping.message")
    setName(CatalystKeys.MOTD, "motd.message")
    setName(CatalystKeys.EMOJI_ENABLE, "chat.emoji.enabled")
    setName(CatalystKeys.VIA_VERSION_ENABLED, "advanced.viaversion")
    setName(CatalystKeys.BAN_COMMAND_ENABLED, "commands.ban")
    setName(CatalystKeys.BROADCAST_COMMAND_ENABLED, "commands.broadcast")
    setName(CatalystKeys.CHANNEL_COMMAND_ENABLED, "commands.channel.base")
    setName(CatalystKeys.CHANNEL_COMMAND_ALIAS_ENABLED, "commands.channel.alias")
    setName(CatalystKeys.NICKNAME_COMMAND_ENABLED, "commands.nickname")
    setName(CatalystKeys.FIND_COMMAND_ENABLED, "commands.find")
    setName(CatalystKeys.INFO_COMMAND_ENABLED, "commands.info")
    setName(CatalystKeys.KICK_COMMAND_ENABLED, "commands.kick")
    setName(CatalystKeys.LIST_COMMAND_ENABLED, "commands.list")
    setName(CatalystKeys.MESSAGE_COMMAND_ENABLED, "commands.message")
    setName(CatalystKeys.SEND_COMMAND_ENABLED, "commands.send")
    setName(CatalystKeys.SERVER_COMMAND_ENABLED, "commands.server.enabled")
    setName(CatalystKeys.SOCIALSPY_COMMAND_ENABLED, "commands.socialspy")
    setName(CatalystKeys.MUTE_COMMAND_ENABLED, "commands.mute")
    setName(CatalystKeys.IGNORE_COMMAND_ENABLED, "commands.ignore")
    setName(CatalystKeys.CATALYST_PREFIX, "advanced.prefix")
    setName(CatalystKeys.ADVANCED_ROOT, "advanced")
    setName(CatalystKeys.COMMANDS_ROOT, "commands")
    setName(CatalystKeys.CHAT_ROOT, "chat")
    setName(CatalystKeys.DISCORD_ROOT, "discord")
    setName(CatalystKeys.JOIN_ROOT, "join")
    setName(CatalystKeys.LEAVE_ROOT, "leave")
    setName(CatalystKeys.MODULES_ROOT, "modules")
    setName(CatalystKeys.MOTD_ROOT, "motd")
    setName(CatalystKeys.PING_ROOT, "ping")
    setName(CatalystKeys.TAB_ROOT, "tab")
    setName(CatalystKeys.COMMAND_LOGGING_ENABLED, "modules.logging.command.enabled")
    setName(CatalystKeys.COMMAND_LOGGING_FILTER, "modules.logging.command.filter")
    setName(CatalystKeys.ENABLE_PER_SERVER_PERMS, "commands.server.permissions")
    setName(CatalystKeys.MOTD_ENABLED, "modules.motd")
    setDescription(CatalystKeys.CHAT_FILTER_SWEARS, "\nList of words you would like filtered out of chat.")
    setDescription(
      CatalystKeys.CHAT_FILTER_EXCEPTIONS,
      "\nList of words that are caught by the swear detection, but shouldn't be. (ex. A player name that contains 'ass'"
    )
    setDescription(CatalystKeys.CHAT_FILTER_ENABLED, "\nToggle the chat filter. (true = enabled)")
    setDescription(CatalystKeys.FIRST_JOIN, "\nFormat for the message that is displayed when a player joins the proxy for the first time")
    setDescription(CatalystKeys.JOIN_MESSAGE, "\nFormat for the message that is displayed when a player joins the proxy")
    setDescription(CatalystKeys.JOIN_LISTENER_ENABLED, "\nToggle join messages")
    setDescription(CatalystKeys.LEAVE_MESSAGE, "\nFormat for the message that is displayed when a player leaves the proxy")
    setDescription(CatalystKeys.LEAVE_LISTENER_ENABLED, "\nToggle leave messages")
    setDescription(CatalystKeys.NICKNAME_PREFIX, "\nPrefix applied to nicknames")
    setDescription(CatalystKeys.PRIVATE_MESSAGE_FORMAT, "\nFormat for private messages")
    setDescription(CatalystKeys.PROXY_CHAT_ENABLED, "\nToggle proxy-wide chat handling. (true = enabled)")
    setDescription(CatalystKeys.TAB_ENABLED, "\nToggle the proxy-wide tab handling. (true = enabled)")
    setDescription(CatalystKeys.TAB_HEADER, "\nFormat for the tab header")
    setDescription(CatalystKeys.TAB_FOOTER, "\nFormat for the tab footer")
    setDescription(CatalystKeys.TAB_FORMAT, "\nFormat for how each player is displayed in the tab")
    setDescription(CatalystKeys.TAB_FORMAT_CUSTOM, "\nFormat for extra information that can be displayed in the tab.")
    setDescription(CatalystKeys.TAB_UPDATE, "\nTime setting for how often the tab updates in seconds")
    setDescription(CatalystKeys.CHAT_CHANNELS, "\nChat Channels")
    setDescription(CatalystKeys.CHAT_DEFAULT_CHANNEL, "\nDefault chat channel")
    setDescription(CatalystKeys.BOT_TOKEN, "\nToken for the discord bot")
    setDescription(CatalystKeys.DISCORD_PLAYER_CHAT_FORMAT, "\nFormat of the message to be sent to discord from in-game")
    setDescription(CatalystKeys.DISCORD_JOIN_FORMAT, "\nMessage to be sent to the discord each time a player joins.")
    setDescription(CatalystKeys.DISCORD_LEAVE_FORMAT, "\nMessage to be sent to the discord each time a player leaves.")
    setDescription(CatalystKeys.DISCORD_CHAT_FORMAT, "\nFormat of the message being sent from discord to in-game")
    setDescription(CatalystKeys.TOPIC_FORMAT, "\nFormat of the main channel topic")
    setDescription(
      CatalystKeys.TOPIC_UPDATE_DELAY,
      "\nHow often you would like the topic to be updated in minutes. Setting this below " +
        "the default will result in constant RateLimit issues. If you experience issues " +
        "with 5 minutes, set this to 10 or higher."
    )
    setDescription(CatalystKeys.TOPIC_NO_ONLINE_PLAYERS, "\nMessage to be shown when there are no players online")
    setDescription(CatalystKeys.NOW_PLAYING_MESSAGE, "\nThe message being shown as the \"now playing\" for the discord bot.")
    setDescription(CatalystKeys.DISCORD_HOVER_MESSAGE, "\nThe message being shown when a player hovers over a message sent from discord")
    setDescription(CatalystKeys.WEBHOOK_URL, "\nURL that gets the player avatar when sending messages to discord")
    setDescription(CatalystKeys.DISCORD_URL, "\nDiscord invite link")
    setDescription(CatalystKeys.DISCORD_ENABLE, "\nToggle the discord bot (false = disabled).")
    setDescription(CatalystKeys.SERVER_PING, "\nSpecify what you would like to be shown in the server list, either PLAYERS or MESSAGE")
    setDescription(
      CatalystKeys.SERVER_PING_MESSAGE,
      """
        This message is shown when players hover over the player count.
        This message will only appear when the mode is set to MESSAGE
        """.trimIndent()
    )
    setDescription(CatalystKeys.MOTD, "\nDefault MOTD that catalyst uses if the advanced server information is disabled.")
    setDescription(
      CatalystKeys.EMOJI_ENABLE,
      """
        Toggle emojis in chat
        Enabling emojis means that you will have to have your players download a select texture pack, found on the github page.
        (true = enabled)
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.VIA_VERSION_ENABLED,
      """
        If you are running servers with ViaVersion, this will disable the version checking done in the /server command.
        (true = enabled)
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.BAN_COMMAND_ENABLED,
      """
        Toggle ban command handling 
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.BAN_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.BROADCAST_COMMAND_ENABLED,
      """
        Toggle broadcast command handling
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.BROADCAST_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(CatalystKeys.CHANNEL_COMMAND_ENABLED, "\nToggle channel command handling \n(true = enabled | false = disabled)")
    setDescription(
      CatalystKeys.CHANNEL_COMMAND_ALIAS_ENABLED,
      """
        Toggle channel alias command handling
        When enabled, channel ID's will also be a command in-game to quickly switch channels
        For example, a chat channel with the id "admin" will have the command "/admin" in-game
        (true = enabled | false = disabled)
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.NICKNAME_COMMAND_ENABLED,
      """
        Toggle nickname command handling 
        (true = enabled | false = disabled)
        Base Permission : ${CatalystKeys.NICKNAME_PERMISSION.fallbackValue} 
        Color Permission : ${CatalystKeys.NICKNAME_COLOR_PERMISSION.fallbackValue}
        Magic Permission : ${CatalystKeys.NICKNAME_MAGIC_PERMISSION.fallbackValue} 
        Other permission : ${CatalystKeys.NICKNAME_OTHER_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.FIND_COMMAND_ENABLED,
      """
        Toggle find command handling
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.FIND_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.INFO_COMMAND_ENABLED,
      """
        Toggle info command handling 
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.INFO_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.KICK_COMMAND_ENABLED,
      """
        Toggle kick command handling
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.KICK_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.LIST_COMMAND_ENABLED,
      """
        Toggle list command handling 
        (true = enabled | false = disabled)
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.MESSAGE_COMMAND_ENABLED,
      """
        Toggle message command handling 
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.MESSAGE_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.SEND_COMMAND_ENABLED,
      """
        Toggle send command handling
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.SEND_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.SERVER_COMMAND_ENABLED,
      """
        Toggle server command handling
        (true = enabled | false = disabled)
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.SOCIALSPY_COMMAND_ENABLED,
      """
        Toggle SocialSpy command handling
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.SOCIALSPY_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.MUTE_COMMAND_ENABLED,
      """
        Toggle mute command handling
        (true = enabled | false = disabled)
        Permission : ${CatalystKeys.MUTE_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.IGNORE_COMMAND_ENABLED,
      """
        Toggle ignore command handling
        (true = enabled | false = disabled)
        Permission: ${CatalystKeys.IGNORE_PERMISSION.fallbackValue}
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.COMMAND_LOGGING_ENABLED,
      """
        Toggle command logging
        (true = enabled | false = disabled)
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.COMMAND_LOGGING_FILTER,
      """
        Filter commands that you want logged to the console.
        By default, catalyst will log all commands.
        """.trimIndent()
    )
    setDescription(
      CatalystKeys.ENABLE_PER_SERVER_PERMS,
      """
        Enforces the following permission check
        catalyst.server.<name> when a player uses the /server command.
        For example, if the target server is called lobby, catalyst will test
        the permission catalyst.server.lobby
        """.trimIndent()
    )
    setDescription(CatalystKeys.MOTD_ENABLED, "Toggle MOTD handling \nBy default, Catalyst will not control the MOTD")
    setDescription(CatalystKeys.CATALYST_PREFIX, "\nPrefix to be used when displaying messages to players when commands are executed.")
    setDescription(
      CatalystKeys.ADVANCED_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Advanced                         |\n" +
        " |------------------------------------------------------------| ")
    )
    setDescription(
      CatalystKeys.COMMANDS_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Commands                         |\n" +
        " |------------------------------------------------------------| ")
    )
    setDescription(
      CatalystKeys.CHAT_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Chat                             |\n" +
        " |------------------------------------------------------------| ")
    )
    setDescription(
      CatalystKeys.DISCORD_ROOT,
      (" |------------------------------------------------------------|\n"
        + " |                           Discord                          |\n"
        + " |------------------------------------------------------------|")
    )
    setDescription(
      CatalystKeys.JOIN_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Join                             |\n" +
        " |------------------------------------------------------------| ")
    )
    setDescription(
      CatalystKeys.LEAVE_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Leave                            |\n" +
        " |------------------------------------------------------------| ")
    )
    setDescription(
      CatalystKeys.MODULES_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Modules                          |\n" +
        " |------------------------------------------------------------| ")
    )
    setDescription(
      CatalystKeys.MOTD_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Motd                             |\n" +
        " |------------------------------------------------------------| ")
    )
    setDescription(
      CatalystKeys.PING_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Ping                             |\n" +
        " |------------------------------------------------------------| ")
    )
    setDescription(
      CatalystKeys.TAB_ROOT,
      (" |------------------------------------------------------------|\n" +
        " |                           Tab                              |\n" +
        " |------------------------------------------------------------| ")
    )
    val pingMessageMap: MutableMap<Predicate<String>, Function<String, String>> = HashMap()
    pingMessageMap[Predicate { message: String -> message.contains("&") }] =
      Function { message: String ->
        logger.warn("Replacing all '&' with '\u00a7' in the config for the hover message.")
        message.replace("&".toRegex(), "\u00a7")
      }
    setVerification(CatalystKeys.SERVER_PING_MESSAGE, pingMessageMap)
  }
}
