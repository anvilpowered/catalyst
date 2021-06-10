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
package org.anvilpowered.catalyst.common.listener

import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import org.anvilpowered.anvil.api.misc.Named
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.discord.WebhookSender
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.event.JoinEvent
import org.anvilpowered.catalyst.api.event.LeaveEvent
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.registry.ChatChannel
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.EmojiService
import org.anvilpowered.catalyst.api.service.LuckpermsService

class DiscordChatListener<TString, TPlayer, TCommandSource> @Inject constructor(
  private val registry: Registry,
  private val luckPermsService: LuckpermsService,
  private val webHookSender: WebhookSender,
  private val userService: UserService<TPlayer, TPlayer>,
  private val serverService: AdvancedServerInfoService,
  private val chatService: ChatService<TString, TPlayer, TCommandSource>,
  private val locationService: LocationService,
  private val emojiService: EmojiService,
  private val permissionService: PermissionService
){
  
  @Subscribe
  fun onChatEvent(event: ChatEvent<TString, TPlayer>) {
    if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
      return
    }
    val optionalChannel = chatService.getChannelFromId(chatService.getChannelIdForUser(userService.getUUID(event.player)))
    var dChannelId = registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL)
    if (optionalChannel.isPresent) {
      if (optionalChannel.get().discordChannel != null) {
        dChannelId = optionalChannel.get().discordChannel
      }
    }
    var message = event.rawMessage
    if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
      for (key in emojiService.emojis.keys) {
        message = message.replace(emojiService.emojis[key].toString(), key)
      }
    }
    if (!permissionService.hasPermission(
        event.player,
        registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN_PERMISSION)
      )
    ) {
      message = message.replace("@".toRegex(), "")
    }
    var server = locationService.getServer(userService.getUserName(event.player)).map { obj: Named -> obj.name }
      .orElse("null")
    if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
      server = serverService.getPrefixForPlayer(userService.getUserName(event.player))
    }
    val name = registry.getOrDefault(CatalystKeys.DISCORD_PLAYER_CHAT_FORMAT)
      .replace("%server%", server)
      .replace("%player%", userService.getUserName(event.player))
      .replace("%prefix%", luckPermsService.getPrefix(event.player))
      .replace("%suffix%", luckPermsService.getSuffix(event.player))
    webHookSender.sendWebhookMessage(
      registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
      name,
      message,
      dChannelId,
      event.player
    )
  }

  @Subscribe
  fun onPlayerJoinEvent(event: JoinEvent<TPlayer>) {
    if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
      return
    }
    var joinMessage = registry.getOrDefault(CatalystKeys.DISCORD_JOIN_FORMAT)
    if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
      for (key in emojiService.emojis.keys) {
        joinMessage = joinMessage.replace(emojiService.emojis[key].toString(), key)
      }
    }
    val server = if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) serverService.getPrefixForPlayer(
      userService.getUserName(event.player)
    ) else locationService.getServer(userService.getUserName(event.player)).map { obj: Named -> obj.name }
      .orElse("null")
    webHookSender.sendWebhookMessage(
      registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
      registry.getOrDefault(CatalystKeys.BOT_NAME),
      joinMessage.replace(
        "%player%", userService.getUserName(event.player)
      ).replace(
        "%server%", server
      ),
      registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL),
      event.player
    )
  }

  @Subscribe
  fun onPlayerLeaveEvent(event: LeaveEvent<TPlayer>) {
    if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) return
    var leaveMessage = registry.getOrDefault(CatalystKeys.DISCORD_LEAVE_FORMAT)
    if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
      for (key in emojiService.emojis.keys) {
        leaveMessage = leaveMessage.replace(emojiService.emojis[key].toString(), key)
      }
    }
    val server = if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) serverService.getPrefixForPlayer(
      userService.getUserName(event.player)
    ) else locationService.getServer(userService.getUserName(event.player)).map { obj: Named -> obj.name }
      .orElse("null")
    webHookSender.sendWebhookMessage(
      registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
      registry.getOrDefault(CatalystKeys.BOT_NAME),
      leaveMessage.replace(
        "%player%",
        userService.getUserName(event.player)
      ).replace(
        "%server%",
        server
      ),
      registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL),
      event.player
    )
  }
}
