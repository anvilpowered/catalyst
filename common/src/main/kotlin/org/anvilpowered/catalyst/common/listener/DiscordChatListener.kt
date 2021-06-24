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
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.EmojiService
import org.anvilpowered.catalyst.api.service.LuckpermsService
import org.anvilpowered.catalyst.common.command.withoutColor

class DiscordChatListener<TString, TPlayer> @Inject constructor(
  private val registry: Registry,
  private val luckPermsService: LuckpermsService,
  private val webHookSender: WebhookSender,
  private val userService: UserService<TPlayer, TPlayer>,
  private val serverService: AdvancedServerInfoService,
  private val channelService: ChannelService<TPlayer>,
  private val locationService: LocationService,
  private val emojiService: EmojiService,
  private val permissionService: PermissionService
) {

  @Subscribe
  fun onChatEvent(event: ChatEvent<TString, TPlayer>) {
    if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
      return
    }
    var channel = channelService.getChannelFromId(channelService.getChannelIdForUser(userService.getUUID(event.player)))
    if (channel == null) {
      channel = channelService.defaultChannel ?: return
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
      .replace("%channel%", channelService.getChannelIdForUser(userService.getUUID(event.player)))
      .replace("%player%", userService.getUserName(event.player))
      .replace("%prefix%", luckPermsService.getPrefix(event.player))
      .replace("%suffix%", luckPermsService.getSuffix(event.player))
    webHookSender.sendWebhookMessage(
      registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
      name.withoutColor(),
      message.withoutColor(),
      channel.discordChannel,
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
      joinMessage.replace("%player%", userService.getUserName(event.player)).replace("%server%", server),
      channelService.defaultChannel?.discordChannel,
      event.player
    )
  }

  @Subscribe
  fun onPlayerLeaveEvent(event: LeaveEvent<TPlayer>) {
    if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) return
    var leaveMessage = registry.getOrDefault(CatalystKeys.DISCORD_LEAVE_FORMAT)
    val channel = channelService.getChannelFromUUID(userService.getUUID(event.player))
    if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
      for (key in emojiService.emojis.keys) {
        leaveMessage = leaveMessage.replace(emojiService.emojis[key].toString(), key)
      }
    }
    val server = if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) serverService.getPrefixForPlayer(
      userService.getUserName(event.player)
    ) else locationService.getServer(userService.getUserName(event.player)).map { obj: Named -> obj.name }
      .orElse("null")
    val discordChannel = channelService.getDiscordChannelId(channel?.id) ?: channelService.defaultChannel?.discordChannel ?: return
    webHookSender.sendWebhookMessage(
      registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
      registry.getOrDefault(CatalystKeys.BOT_NAME),
      leaveMessage.replace("%player%", userService.getUserName(event.player)).replace("%server%", server),
      discordChannel,
      event.player
    )
  }
}
