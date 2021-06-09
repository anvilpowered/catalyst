/*
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.catalyst.common.command

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.inject.Inject
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import org.anvilpowered.anvil.api.command.CommandNode
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.CommandSuggestionType
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo
import java.util.HashMap
import java.util.Locale
import java.util.Objects
import java.util.function.Function
import java.util.function.Predicate

abstract class CommonCommandNode<TString, TPlayer : TCommandSource, TCommandSource> protected constructor(
  protected var registry: Registry,
  protected var playerClass: Class<*>,
  protected var consoleClass: Class<*>
) : CommandNode<TCommandSource> {

  @Inject
  private lateinit var banCommand: BanCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var tempBanCommand: TempBanCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var unBanCommand: UnBanCommand<TString, TCommandSource>

  @Inject
  private lateinit var broadcastCommand: BroadcastCommand<TString, TCommandSource>

  @Inject
  private lateinit var channelCommand: ChannelCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var deleteNickCommand: DeleteNicknameCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var nickCommand: NickNameCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var findCommand: FindCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var infoCommand: InfoCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var kickCommand: KickCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var messageCommand: MessageCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var replyCommand: ReplyCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var muteCommand: MuteCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var tempMuteCommand: TempMuteCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var toggleProxyChat: ToggleProxyChat<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var unMuteCommand: UnMuteCommand<TString, TCommandSource>

  @Inject
  private lateinit var socialSpyCommand: SocialSpyCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var exceptionCommand: ExceptionCommand<TString, TCommandSource>

  @Inject
  private lateinit var swearCommand: SwearCommand<TString, TCommandSource>

  @Inject
  private lateinit var staffListCommand: StaffListCommand<TString, TCommandSource>

  @Inject
  private lateinit var sendCommand: SendCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var serverCommand: ServerCommand<TString, TPlayer, TCommandSource>

  @Inject
  private lateinit var ignoreCommand: IgnoreCommand<TString, TPlayer, TCommandSource>

  private var alreadyLoaded: Boolean = false
  protected var commands = mutableMapOf<MutableList<String>, LiteralCommandNode<TCommandSource>>()
  protected var suggestionType = mutableMapOf<LiteralCommandNode<TCommandSource>, Map<Int, CommandSuggestionType>>()

  @Inject
  protected lateinit var permissionService: PermissionService

  @Inject
  private lateinit var textService: TextService<TString, TCommandSource>

  @Inject
  private lateinit var pluginMessages: PluginMessages<TString>

  @Inject
  private lateinit var userService: UserService<TPlayer, TPlayer>

  @Inject
  protected lateinit var locationService: LocationService

  @Inject
  protected lateinit var advancedServerInfo: AdvancedServerInfoService

  protected abstract fun loadCommands()

  private fun loadNodes() {
    val ban = LiteralArgumentBuilder
      .literal<TCommandSource>("ban")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.BAN_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.banCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.string())
        .suggests(suggestPlayers())
        .executes { banCommand.withoutReason(it) }
        .then(
          RequiredArgumentBuilder.argument<TCommandSource, String>("reason", StringArgumentType.greedyString())
            .executes { banCommand.withReason(it) }
            .build()
        ).build()
      ).build()
    val tempBan = LiteralArgumentBuilder
      .literal<TCommandSource>("tempban")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.TEMP_BAN_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.tempBanCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.string())
        .executes { sendNotEnoughArgs(it, pluginMessages.tempBanCommandUsage()) }
        .then(RequiredArgumentBuilder.argument<TCommandSource, String>("duration", StringArgumentType.string())
          .executes { tempBanCommand.withoutReason(it) }
          .then(
            RequiredArgumentBuilder.argument<TCommandSource, String>("reason", StringArgumentType.greedyString())
              .executes { tempBanCommand.withReason(it) }
              .build()
          ).build()
        ).build()
      ).build()
    val unban = LiteralArgumentBuilder.literal<TCommandSource>("unban")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.BAN_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.unbanCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.string())
        .executes { unBanCommand.unban(it) }
        .build()
      ).build()
    val broadcast = LiteralArgumentBuilder
      .literal<TCommandSource>("broadcast")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.BROADCAST_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.broadcastCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("message", StringArgumentType.greedyString())
        .executes { broadcastCommand.execute(it) }
        .build()
      ).build()
    val channel = LiteralArgumentBuilder
      .literal<TCommandSource>("channel")
      .executes { channelCommand.list(it, playerClass) }
      .then(LiteralArgumentBuilder.literal<TCommandSource>("set")
        .executes {
          textService.send(pluginMessages.notEnoughArgs, it.source)
          1
        }
        .then(
          RequiredArgumentBuilder.argument<TCommandSource, String>("channel", StringArgumentType.greedyString())
            .executes { channelCommand.set(it, playerClass) }
            .suggests(suggestChannels())
            .build()
        )
      )
      .then(LiteralArgumentBuilder.literal<TCommandSource>("info")
        .executes {
          textService.send(pluginMessages.notEnoughArgs, it.source)
          1
        }
        .then(RequiredArgumentBuilder.argument<TCommandSource, String>("channel", StringArgumentType.greedyString())
          .executes { channelCommand.info(it) }
          .suggests(suggestChannels())
          .build()
        )
      )
      .then(LiteralArgumentBuilder.literal<TCommandSource>("edit")
        .executes { ctx: CommandContext<TCommandSource> ->
          textService.send(pluginMessages.notEnoughArgs, ctx.source)
          1
        }
        .requires { it.hasPermission("catalyst.admin.channel") }
        .then(LiteralArgumentBuilder.literal<TCommandSource>("abort")
          .executes { channelCommand.abortEdit(it) }
          .build()
        ).then(LiteralArgumentBuilder.literal<TCommandSource>("start")
          .executes { sendNotEnoughArgs(it, pluginMessages.channelEditStartUsage()) }
          .then(RequiredArgumentBuilder.argument<TCommandSource, String>("channel", StringArgumentType.greedyString())
            .executes { channelCommand.startEdit(it) }
            .suggests(suggestChannels())
            .build()
          ).build()
        ).then(LiteralArgumentBuilder.literal<TCommandSource>("property")
          .executes { sendNotEnoughArgs(it, pluginMessages.channelEditPropertyUsage()) }
          .then(RequiredArgumentBuilder.argument<TCommandSource, String>("name", StringArgumentType.word())
            .executes { sendNotEnoughArgs(it, pluginMessages.channelEditPropertyUsage()) }
            .suggests { _: CommandContext<TCommandSource>, builder: SuggestionsBuilder ->
              builder.suggest("format")
              builder.suggest("id")
              builder.suggest("visibility")
              builder.suggest("discord")
              builder.suggest("hovermessage")
              builder.suggest("hover")
              builder.suggest("click")
              builder.suggest("onclick")
              builder.suggest("server")
              builder.buildFuture()
            }
            .then(RequiredArgumentBuilder.argument<TCommandSource, String>("value", StringArgumentType.string())
              .executes { channelCommand.editProperty(it) }
              .build()
            ).build()
          ).build()
        ).then(LiteralArgumentBuilder.literal<TCommandSource>("commit")
          .executes { channelCommand.commit(it) }
          .build()
        ).build()
      ).build()
    val delNick = LiteralArgumentBuilder.literal<TCommandSource>("delnick")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.NICKNAME_PERMISSION)) }
      .executes { deleteNickCommand.execute(it, playerClass) }
      .then(LiteralArgumentBuilder.literal<TCommandSource>("other")
        .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.NICKNAME_OTHER_PERMISSION)) }
        .executes { sendNotEnoughArgs(it, pluginMessages.deleteNickOtherCommandUsage()) }
        .then(
          RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.string())
            .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.NICKNAME_OTHER_PERMISSION)) }
            .suggests(suggestPlayers())
            .executes { deleteNickCommand.executeOther(it) }
            .build()
        ).build()
      ).build()
    val nickName = LiteralArgumentBuilder.literal<TCommandSource>("nick")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.NICKNAME_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.nickNameCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("nickname", StringArgumentType.greedyString())
        .executes { nickCommand.execute(it, playerClass) }
        .build()
      )
      .then(LiteralArgumentBuilder.literal<TCommandSource>("other")
        .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.NICKNAME_OTHER_PERMISSION)) }
        .executes { sendNotEnoughArgs(it, pluginMessages.nickNameCommandUsage()) }
        .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.word())
          .suggests(suggestPlayers())
          .executes { sendNotEnoughArgs(it, pluginMessages.nickNameCommandUsage()) }
          .then(RequiredArgumentBuilder.argument<TCommandSource, String>("targetnick", StringArgumentType.word())
            .executes { nickCommand.executeOther(it) }
          ).build()
        ).build()
      ).build()
    val find = LiteralArgumentBuilder.literal<TCommandSource>("find")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.FIND_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.findCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.string())
        .suggests(suggestPlayers())
        .executes { findCommand.execute(it) }
        .build()
      ).build()
    val info = LiteralArgumentBuilder.literal<TCommandSource>("info")
      .executes { sendNotEnoughArgs(it, pluginMessages.infoCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.word())
        .suggests(suggestPlayers())
        .executes { infoCommand.execute(it) }
        .build()
      ).build()
    val kick = LiteralArgumentBuilder.literal<TCommandSource>("kick")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.KICK_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.kickCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.word())
        .suggests(suggestPlayers())
        .executes { kickCommand.withoutReason(it) }
        .then(RequiredArgumentBuilder.argument<TCommandSource, String>("reason", StringArgumentType.greedyString())
          .executes { kickCommand.execute(it) }
          .build()
        ).build()
      ).build()
    val message = LiteralArgumentBuilder.literal<TCommandSource>("msg")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.MESSAGE_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.messageCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.word())
        .suggests(suggestPlayers())
        .executes { sendNotEnoughArgs(it, pluginMessages.messageCommandUsage()) }
        .then(
          RequiredArgumentBuilder.argument<TCommandSource, String>("message", StringArgumentType.greedyString())
            .executes { messageCommand.execute(it, consoleClass) }
            .build()
        ).build()
      ).build()
    val reply = LiteralArgumentBuilder.literal<TCommandSource>("reply")
      .requires { source: TCommandSource -> permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.MESSAGE_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.replyCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("message", StringArgumentType.greedyString())
        .executes { replyCommand.execute(it) }
        .build()
      ).build()
    val mute = LiteralArgumentBuilder.literal<TCommandSource>("mute")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.MUTE_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.muteCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.word())
        .suggests(suggestPlayers())
        .executes { muteCommand.withoutReason(it) }
        .then(RequiredArgumentBuilder.argument<TCommandSource, String>("reason", StringArgumentType.greedyString())
          .executes { muteCommand.execute(it) }
          .build()
        ).build()
      ).build()
    val tempMute = LiteralArgumentBuilder.literal<TCommandSource>("tempmute")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.MUTE_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.tempBanCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.word())
        .suggests(suggestPlayers())
        .executes { sendNotEnoughArgs(it, pluginMessages.tempMuteCommandUsage()) }
        .then(RequiredArgumentBuilder.argument<TCommandSource, String>("duration", StringArgumentType.word())
          .executes { context: CommandContext<TCommandSource> -> tempMuteCommand.withoutReason(context) }
          .then(RequiredArgumentBuilder.argument<TCommandSource, String>("reason", StringArgumentType.greedyString())
            .executes { tempMuteCommand.withReason(it) }
            .build()
          ).build()
        ).build()
      ).build()
    val toggleChat = LiteralArgumentBuilder.literal<TCommandSource>("togglechat")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.TOGGLE_CHAT_PERMISSION)) }
      .executes { toggleProxyChat.execute(it, playerClass) }
      .build()
    val unmute = LiteralArgumentBuilder.literal<TCommandSource>("unmute")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.MUTE_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.unMuteCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.word())
        .suggests(suggestPlayers())
        .executes { unMuteCommand.execute(it) }
        .build()
      ).build()
    val socialSpy = LiteralArgumentBuilder.literal<TCommandSource>("socialspy")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.SOCIALSPY_PERMISSION)) }
      .executes { socialSpyCommand.execute(it, playerClass) }
      .build()
    val exception = LiteralArgumentBuilder.literal<TCommandSource>("exception")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.LANGUAGE_LIST_PERMISSION)) }
      .executes { exceptionCommand.execute(it) }
      .build()
    val swear = LiteralArgumentBuilder.literal<TCommandSource>("swear")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.LANGUAGE_LIST_PERMISSION)) }
      .executes { swearCommand.execute(it) }
      .build()
    val staffList = LiteralArgumentBuilder.literal<TCommandSource>("stafflist")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_BASE_PERMISSION)) }
      .executes { staffListCommand.execute(it) }
      .build()
    val send = LiteralArgumentBuilder.literal<TCommandSource>("send")
      .requires { it.hasPermission(registry.getOrDefault(CatalystKeys.SEND_PERMISSION)) }
      .executes { sendNotEnoughArgs(it, pluginMessages.sendCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("player", StringArgumentType.word())
        .suggests(suggestPlayers())
        .executes { sendNotEnoughArgs(it, pluginMessages.sendCommandUsage()) }
        .then(RequiredArgumentBuilder.argument<TCommandSource, String>("server", StringArgumentType.word())
          .suggests(suggestAllServers())
          .executes { sendCommand.execute(it) }
          .build()
        ).build()
      ).build()
    val server = LiteralArgumentBuilder.literal<TCommandSource>("server")
      .executes { serverCommand.sendServers(it) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("server", StringArgumentType.word())
        .suggests(suggestServers())
        .executes { serverCommand.execute(it) }
        .build()
      ).build()
    val ignore = LiteralArgumentBuilder.literal<TCommandSource>("ignore")
      .executes { sendNotEnoughArgs(it, pluginMessages.ignoreCommandUsage()) }
      .then(RequiredArgumentBuilder.argument<TCommandSource, String>("target", StringArgumentType.word())
        .suggests(suggestPlayers())
        .executes { ignoreCommand.execute(it, playerClass) }
        .build()
      ).build()

    if (registry.getOrDefault(CatalystKeys.BAN_COMMAND_ENABLED)) {
      commands[ImmutableList.of("tempban", "ctempban")] = tempBan
      commands[ImmutableList.of("ban", "cban")] = ban
      commands[ImmutableList.of("unban", "cunban")] = unban
      suggestionType[tempBan] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
      suggestionType[ban] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
      suggestionType[unban] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
    }
    if (registry.getOrDefault(CatalystKeys.BROADCAST_COMMAND_ENABLED)) {
      commands[ImmutableList.of("broadcast", "cbroadcast")] = broadcast
    }
    if (registry.getOrDefault(CatalystKeys.CHANNEL_COMMAND_ENABLED)) {
      commands[ImmutableList.of("channel", "chatchannel")] = channel
    }
    if (registry.getOrDefault(CatalystKeys.NICKNAME_COMMAND_ENABLED)) {
      commands[ImmutableList.of("delnick", "deletenick")] = delNick
      commands[ImmutableList.of("nick", "nickname")] = nickName
    }
    if (registry.getOrDefault(CatalystKeys.FIND_COMMAND_ENABLED)) {
      commands[ImmutableList.of("find", "cfind")] = find
      suggestionType[find] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
    }
    if (registry.getOrDefault(CatalystKeys.INFO_COMMAND_ENABLED)) {
      commands[ImmutableList.of("info", "cinfo")] = info
      suggestionType[info] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
    }
    if (registry.getOrDefault(CatalystKeys.KICK_COMMAND_ENABLED)) {
      commands[ImmutableList.of("kick", "ckick")] = kick
      suggestionType[kick] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
    }
    if (registry.getOrDefault(CatalystKeys.MESSAGE_COMMAND_ENABLED)) {
      commands[ImmutableList.of("msg", "m", "w", "t", "whisper", "tell")] = message
      commands[ImmutableList.of("reply", "r")] = reply
      suggestionType[message] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
    }
    if (registry.getOrDefault(CatalystKeys.MUTE_COMMAND_ENABLED)) {
      commands[ImmutableList.of("mute", "cmute")] = mute
      commands[ImmutableList.of("tempmute", "ctempmute")] = tempMute
      commands[ImmutableList.of("unmute", "cunmute")] = unmute
      suggestionType[mute] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
      suggestionType[tempMute] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
      suggestionType[unmute] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
    }
    if (registry.getOrDefault(CatalystKeys.SOCIALSPY_COMMAND_ENABLED)) {
      commands[ImmutableList.of("socialspy", "ss")] = socialSpy
    }
    if (registry.getOrDefault(CatalystKeys.CHAT_FILTER_ENABLED)) {
      commands[ImmutableList.of("exception")] = exception
      commands[ImmutableList.of("swear")] = swear
    }
    if (registry.getOrDefault(CatalystKeys.SEND_COMMAND_ENABLED)) {
      commands[ImmutableList.of("send", "csend")] = send
      suggestionType[send] = ImmutableMap.of(1, CommandSuggestionType.PLAYER, 2, CommandSuggestionType.SERVER)
    }
    if (registry.getOrDefault(CatalystKeys.SERVER_COMMAND_ENABLED)) {
      commands[ImmutableList.of("server", "cserver")] = server
      suggestionType[server] = ImmutableMap.of(1, CommandSuggestionType.SERVER)
    }
    if (registry.getOrDefault(CatalystKeys.IGNORE_COMMAND_ENABLED)) {
      commands[ImmutableList.of("ignore", "cignore")] = ignore
      suggestionType[ignore] = ImmutableMap.of(1, CommandSuggestionType.PLAYER)
    }
    commands[ImmutableList.of("stafflist")] = staffList
    commands[ImmutableList.of("toggleproxychat")] = toggleChat
  }

  private fun suggestPlayers(): SuggestionProvider<TCommandSource> {
    return SuggestionProvider { context: CommandContext<TCommandSource>, builder: SuggestionsBuilder ->
      val input = context.input.substring(context.input.indexOf(' ') + 1).toLowerCase(Locale.ROOT)
      for (player in userService.onlinePlayers) {
        val userName = userService.getUserName(player)
        if (userName.toLowerCase(Locale.ROOT).startsWith(input)
          || userName.toLowerCase(Locale.ROOT) == input
        ) {
          builder.suggest(userService.getUserName(player)) { "target" }
        }
      }
      builder.buildFuture()
    }
  }

  private fun suggestServers(): SuggestionProvider<TCommandSource> =
    SuggestionProvider { context: CommandContext<TCommandSource>, builder: SuggestionsBuilder ->
      val source = context.source
      if (!playerClass.isAssignableFrom(source!!::class.java)) {
        return@SuggestionProvider builder.buildFuture()
      }
      if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
        if (source.javaClass.isAssignableFrom(playerClass)) {
          val prefix = advancedServerInfo.getPrefixForPlayer(userService.getUserName(source as TPlayer))
          for (server in locationService.servers) {
            if (server.name.startsWith(prefix)) {
              builder.suggest(server.name.replace(prefix, "")) { "server" }
            }
          }
        }
        return@SuggestionProvider builder.buildFuture()
      }
      for (server in locationService.servers) {
        builder.suggest(server.name) { "server" }
      }
      builder.buildFuture()
    }

  private fun suggestAllServers(): SuggestionProvider<TCommandSource> =
    SuggestionProvider { _: CommandContext<TCommandSource>, builder: SuggestionsBuilder ->
      for (server in locationService.servers) {
        builder.suggest(server.name) { "server" }
      }
      builder.buildFuture()
    }

  private fun suggestChannels(): SuggestionProvider<TCommandSource> {
    return SuggestionProvider { context: CommandContext<TCommandSource>, builder: SuggestionsBuilder ->
      for (channel in registry.getOrDefault(CatalystKeys.CHAT_CHANNELS)) {
        if (context.source.hasPermission(registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION).toString() + channel.id)) {
          builder.suggest(channel.id)
        }
      }
      builder.buildFuture()
    }
  }

  override fun getDescriptions(): Map<List<String>, Function<TCommandSource, String>> = Objects.requireNonNull(mapOf(), ERROR_MESSAGE)
  override fun getPermissions(): Map<List<String>, Predicate<TCommandSource>> = Objects.requireNonNull(mapOf(), ERROR_MESSAGE)
  override fun getUsages(): Map<List<String>, Function<TCommandSource, String>> = Objects.requireNonNull(mapOf(), ERROR_MESSAGE)
  override fun getName(): String = CatalystPluginInfo.id

  companion object {
    private const val ERROR_MESSAGE = "Catalyst commands have not been loaded yet"
  }

  init {
    registry.whenLoaded {
      if (alreadyLoaded) return@whenLoaded
      loadNodes()
      loadCommands()
      alreadyLoaded = true
    }.register()

    alreadyLoaded = false
    suggestionType = HashMap()
  }

  private fun sendNotEnoughArgs(ctx: CommandContext<TCommandSource>, usage: TString): Int {
    textService.builder()
      .append(pluginMessages.notEnoughArgs)
      .append("\n$").append(usage)
      .sendTo(ctx.source)
    return 1
  }

  private fun TCommandSource.hasPermission(permission: String): Boolean =
    permissionService.hasPermission(this, permission)
}
