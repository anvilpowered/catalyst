/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import com.google.inject.Singleton
import org.anvilpowered.anvil.api.misc.Named
import org.anvilpowered.anvil.api.model.coremember.CoreMember
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.registry.ChatChannel
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.EmojiService
import org.anvilpowered.catalyst.api.service.LuckpermsService
import org.slf4j.Logger
import java.util.Optional
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

@Singleton
class CommonChatService<TUser, TPlayer, TString, TCommandSource> @Inject constructor(
  private val serverService: AdvancedServerInfoService,
  private val emojiService: EmojiService,
  private val logger: Logger,
  private val locationService: LocationService,
  private val luckpermsService: LuckpermsService,
  private val memberManager: MemberManager<TString>,
  private val permissionService: PermissionService,
  private val pluginMessages: PluginMessages<TString>,
  private val registry: Registry,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TUser, TPlayer>
) : ChatService<TString, TPlayer, TCommandSource> {

  var channelMap = mutableMapOf<UUID, String>()
  var ignoreMap = mutableMapOf<UUID, MutableList<UUID>>()
  var disabledList = mutableListOf<UUID>()

  override fun switchChannel(userUUID: UUID, channelId: String) {
    channelMap[userUUID] = channelId
  }

  override fun getChannelIdForUser(userUUID: UUID): String {
    return channelMap[userUUID] ?: registry.getOrDefault(CatalystKeys.CHAT_DEFAULT_CHANNEL)
  }

  override fun getChannelFromId(channelId: String): Optional<ChatChannel> {
    return registry.get<List<ChatChannel>>(CatalystKeys.CHAT_CHANNELS).flatMap {
      it.stream()
        .filter { c: ChatChannel -> c.id == channelId }
        .findAny()
    }
  }

  override fun getChannelUserCount(channelId: String): Int {
    return userService.onlinePlayers
      .stream()
      .filter { getChannelIdForUser(userService.getUUID(it as TUser)) == channelId }
      .count().toInt()
  }

  override fun getUsersInChannel(channelId: String): List<TPlayer>? {
    return userService.onlinePlayers
      .stream()
      .filter { getChannelIdForUser(userService.getUUID(it as TUser)) == channelId }
      .collect(Collectors.toList())
  }

  override fun sendMessageToChannel(channelId: String, message: TString, senderUUID: UUID): CompletableFuture<Void?>? {
    return CompletableFuture.runAsync {
      userService.onlinePlayers.forEach {
        if (permissionService.hasPermission(it, registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION))
          || getChannelIdForUser(userService.getUUID(it as TUser)) == channelId
        ) {
          if (senderUUID != userService.getUUID(it as TUser)) {
            if (!isIgnored(userService.getUUID(it as TUser), senderUUID)) {
              textService.send(message, it as TCommandSource, senderUUID)
            }
          } else {
            textService.send(message, it as TCommandSource, senderUUID)
          }
        }
      }
    }
  }

  override fun sendGlobalMessage(player: TPlayer, message: TString): CompletableFuture<Void> {
    return CompletableFuture.runAsync {
      userService.onlinePlayers.forEach { textService.send(message, it as TCommandSource, userService.getUUID(player as TUser)) }
    }
  }

  override fun formatMessage(
    prefix: String,
    nameColor: String,
    userName: String,
    userUUID: UUID,
    message: String,
    hasChatColorPermission: Boolean,
    suffix: String,
    serverName: String,
    channelId: String
  ): CompletableFuture<Optional<TString>> {
    val channel = getChannelFromId(channelId)
    var format = registry.getOrDefault(CatalystKeys.PROXY_CHAT_FORMAT_MESSAGE)
    var hover = registry.getOrDefault(CatalystKeys.PROXY_CHAT_FORMAT_HOVER)
    var click = registry.getOrDefault(CatalystKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND)
    if (channel.isPresent) {
      format = channel.get().format
      hover = channel.get().hoverMessage
      click = channel.get().click
    }
    return memberManager.primaryComponent.getOneForUser(userUUID)
      .thenApplyAsync { optionalMember ->
        if (!optionalMember.isPresent) {
          return@thenApplyAsync Optional.ofNullable(textService.fail("Couldn't find a user matching that name!"))
        }
        val optionalCoreMember: CoreMember<*> = optionalMember.get()
        if (optionalMember.get().isMuted) {
          return@thenApplyAsync Optional.empty()
        }

        var finalName = optionalMember.get().userName
        finalName = if (optionalCoreMember.nickName != null) {
          optionalMember.get().nickName + "&r"
        } else {
          "$nameColor$finalName&r"
        }
        Optional.ofNullable(
          textService
            .builder()
            .append(
              textService.deserialize(
                replacePlaceholders(
                  message,
                  prefix,
                  optionalMember.get().userName,
                  finalName,
                  hasChatColorPermission,
                  suffix,
                  serverName,
                  format
                )
              )
            )
            .onHoverShowText(
              textService.deserialize(
                replacePlaceholders(
                  message,
                  prefix,
                  optionalMember.get().userName,
                  finalName,
                  hasChatColorPermission,
                  suffix,
                  serverName,
                  hover
                )
              )
            )
            .onClickSuggestCommand(
              replacePlaceholders(
                message,
                prefix,
                optionalMember.get().userName,
                userName,
                hasChatColorPermission,
                suffix,
                finalName,
                click
              )
            )
            .build()
        )
      }
  }

  private fun replacePlaceholders(
    rawMessage: String,
    prefix: String,
    rawUserName: String,
    userName: String,
    hasChatColorPermission: Boolean,
    suffix: String,
    serverName: String,
    format: String
  ): String {
    var server = locationService.getServer(rawUserName).map { obj: Named -> obj.name }.orElse("null")
    if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
      server = serverService.getPrefixForPlayer(rawUserName)
    }
    return format
      .replace("%server%", server)
      .replace("%servername%", serverName)
      .replace("%prefix%", prefix)
      .replace("%player%", userName)
      .replace("%suffix%", suffix)
      .replace("%message%", if (hasChatColorPermission) rawMessage else textService.toPlain(rawMessage))
  }

  override fun getPlayerList(): List<TString> {
    val playerList: MutableList<String> = ArrayList()
    userService.onlinePlayers.forEach{ playerList.add(userService.getUserName(it as TUser)) }
    val tempList: MutableList<TString> = ArrayList()
    var builder = StringBuilder()
    for (s in playerList) {
      if (builder.length + s.length < 50) {
        builder.append(" ").append(s)
      } else {
        tempList.add(textService.of(builder.toString()))
        builder = StringBuilder(s)
      }
    }
    return tempList
  }

  override fun sendList(commandSource: TCommandSource) {
    textService.paginationBuilder()
      .header(textService.builder().green().append("Online Players").build())
      .padding(textService.of("-"))
      .contents(playerList)
      .build()
      .sendTo(commandSource)
  }

  override fun ignore(playerUUID: UUID, targetPlayerUUID: UUID): TString {
    val targetPlayer = userService.getPlayer(targetPlayerUUID)
    if (!targetPlayer.isPresent) {
      return textService.fail("An error occurred.")
    }
    var uuidList: MutableList<UUID> = ArrayList()
    if (ignoreMap[playerUUID] == null) {
      uuidList.add(targetPlayerUUID)
    } else {
      uuidList = ignoreMap[playerUUID] ?: return textService.fail("An error occurred.")
      if (uuidList.contains(targetPlayerUUID)) {
        return unIgnore(playerUUID, targetPlayerUUID)
      }
    }
    ignoreMap[playerUUID] = uuidList
    return textService.builder()
      .green().append("You are now ignoring ")
      .gold().append(userService.getUserName(targetPlayer.get() as TUser))
      .build()
  }

  override fun unIgnore(playerUUID: UUID, targetPlayerUUID: UUID): TString {
    val uuidList: MutableList<UUID> = ignoreMap[playerUUID] ?: return textService.fail("An error occurred.")
    if (isIgnored(playerUUID, targetPlayerUUID)) {
      uuidList.remove(targetPlayerUUID)
      ignoreMap.replace(playerUUID, uuidList)
    }
    return textService.success(
      "You are no longer ignoring " +
        userService.getUserName(targetPlayerUUID)
    )
  }

  override fun isIgnored(playerUUID: UUID, targetPlayerUUID: UUID): Boolean {
    val uuidList = ignoreMap[playerUUID] ?: return false
    return targetPlayerUUID in uuidList
  }

  override fun checkPlayerName(sender: TPlayer, msg: String): String {
    var message = msg
    for (player in userService.onlinePlayers) {
      val username = userService.getUserName(player as TUser)
      if (message.toLowerCase().contains(username.toLowerCase())) {
        val occurrences: MutableList<Int> = ArrayList()
        var startIndex = message.toLowerCase().indexOf(username.toLowerCase())
        while (startIndex != -1) {
          occurrences.add(startIndex)
          startIndex = message.toLowerCase().indexOf(username.toLowerCase(), startIndex + 1)
        }
        val chatColor = luckpermsService.getChatColor(sender)
        for (occurrence in occurrences) {
          message = message.substring(0, occurrence) + "&b@" + username + chatColor + message.substring(occurrence + username.length)
        }
      }
    }
    return message
  }

  override fun sendChatMessage(event: ChatEvent<TString, TPlayer>) {
    val prefix = luckpermsService.getPrefix(event.player)
    val chatColor = luckpermsService.getChatColor(event.player)
    val nameColor = luckpermsService.getNameColor(event.player)
    val suffix = luckpermsService.getSuffix(event.player)
    val userName = textService.serializePlain(textService.of(userService.getUserName(event.player as TUser)))
    val server = locationService.getServer(userName).map { obj: Named -> obj.name }
      .orElseThrow { IllegalStateException("$userName is not in a valid server!") }
    val playerUUID = userService.getUUID(event.player as TUser)
    var message = event.rawMessage
    val channelId = getChannelIdForUser(playerUUID)
    val hasColorPermission: Boolean = permissionService.hasPermission(event.player, registry.getOrDefault(CatalystKeys.CHAT_COLOR_PERMISSION))
    message = chatColor + message
    if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)
      && permissionService.hasPermission(event.player, registry.getOrDefault(CatalystKeys.EMOJI_PERMISSION))
    ) {
      message = emojiService.toEmoji(message, chatColor)
    }
    formatMessage(
      prefix,
      nameColor,
      userName,
      playerUUID,
      message,
      hasColorPermission,
      suffix,
      server,
      channelId
    ).thenApplyAsync { optionalMessage ->
      if (optionalMessage.isPresent) {
        logger.info(textService.serializePlain(optionalMessage.get()))
        sendMessageToChannel(channelId, optionalMessage.get(), playerUUID)
        return@thenApplyAsync optionalMessage.get()
      } else {
        textService.send(pluginMessages.muted, event.player as TCommandSource)
        return@thenApplyAsync null
      }
    }.join()
  }

  override fun toggleChatForUser(player: TPlayer) {
    val userUUID = userService.getUUID(player as TUser)
    if (!disabledList.contains(userUUID)) {
      disabledList.add(userUUID)
      return
    }
    disabledList.remove(userUUID)
  }

  override fun isDisabledForUser(player: TPlayer): Boolean {
    val userUUID = userService.getUUID(player as TUser)
    return disabledList.contains(userUUID)
  }
}
