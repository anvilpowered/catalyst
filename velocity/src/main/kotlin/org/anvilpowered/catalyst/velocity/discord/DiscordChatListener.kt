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
package org.anvilpowered.catalyst.velocity.discord

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.config.CatalystKeys

context(Registry.Scope, PlayerService, LuckpermsService.Scope, WebhookSender.Scope)
class DiscordChatListener {

    @Subscribe
    fun onChatEvent(message: ChannelMessage) {
        if (!registry[CatalystKeys.DISCORD_ENABLE]) {
            return
        }
        val player = message.source
//        val channel = channelService.getForPlayer(userService.getUUID(event.player)!!)
        if (!player.hasPermission(registry[CatalystKeys.LANGUAGE_ADMIN_PERMISSION])) {
            message = message.replace("@".toRegex(), "")
        }
//        val server = locationService.getServer(userService.getUserName(event.player))?.name ?: "null"
        val name = registry.getOrDefault(CatalystKeys.DISCORD_PLAYER_CHAT_FORMAT)
            .replace("%server%", server)
            .replace("%channel%", channel.id)
            .replace("%player%", userService.getUserName(event.player))
            .replace("%prefix%", luckPermsService.prefix(event.player!!))
            .replace("%suffix%", luckPermsService.suffix(event.player!!))
        webHookSender.sendWebhookMessage(
            registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
            name.withoutColor(),
            message.withoutColor(),
            channel.discordChannel,
            event.player,
        )
    }

    @Subscribe
    fun onPlayerJoinEvent(event: LoginEvent) {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) {
            return
        }
        val joinMessage = registry.getOrDefault(CatalystKeys.DISCORD_JOIN_FORMAT)
        val server = locationService.getServer(userService.getUserName(event.player))?.name ?: "null"
        webHookSender.sendWebhookMessage(
            registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
            registry.getOrDefault(CatalystKeys.BOT_NAME),
            joinMessage.replace("%player%", userService.getUserName(event.player)).replace("%server%", server),
            channelService.defaultChannel()?.discordChannel,
            event.player,
        )
    }

    @Subscribe
    fun onPlayerLeaveEvent(event: LeaveEvent<TPlayer>) {
        if (!registry.getOrDefault(CatalystKeys.DISCORD_ENABLE)) return
        webHookSender.sendWebhookMessage(
            registry.getOrDefault(CatalystKeys.WEBHOOK_URL),
            registry.getOrDefault(CatalystKeys.BOT_NAME),
            registry.getOrDefault(CatalystKeys.DISCORD_LEAVE_FORMAT)
                .replace("%player%", userService.getUserName(event.player))
                .replace("%server%", locationService.getServer(userService.getUserName(event.player))?.name ?: "null"),
            channelService.fromUUID(userService.getUUID(event.player)!!).discordChannel,
            event.player,
        )
    }
}
