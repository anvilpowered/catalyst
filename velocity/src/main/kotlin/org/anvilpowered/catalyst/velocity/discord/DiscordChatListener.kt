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
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import kotlinx.coroutines.runBlocking
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.config.CatalystKeys

context(Registry.Scope, ProxyServerScope, ChannelService.Scope, LuckpermsService.Scope, WebhookSender.Scope, LoggerScope)
class DiscordChatListener {

    @Subscribe
    fun onChatEvent(message: ChannelMessage) = runBlocking {
        if (!registry[CatalystKeys.DISCORD_ENABLED]) {
            return@runBlocking
        }
//        if (!player.hasPermission(registry[CatalystKeys.LANGUAGE_ADMIN_PERMISSION])) {
//            message = message.replace("@".toRegex(), "")
//        }
        webhookSender.sendChannelMessage(
            message.source,
            message.content,
            message.channel.discordChannel,
        )
    }

    @Subscribe
    fun onPlayerJoinEvent(event: LoginEvent) = runBlocking {
        if (!registry[CatalystKeys.DISCORD_ENABLED]) {
            return@runBlocking
        }
        val discordChannel = channelService.getForPlayer(event.player.uniqueId)
        webhookSender.sendSpecialMessage(event.player, discordChannel.discordChannel, CatalystKeys.JOIN_MESSAGE)
    }

    @Subscribe
    fun onPlayerLeaveEvent(event: DisconnectEvent) = runBlocking {
        if (!registry[CatalystKeys.DISCORD_ENABLED]) {
            return@runBlocking
        }
        val discordChannel = channelService.getForPlayer(event.player.uniqueId)
        webhookSender.sendSpecialMessage(event.player, discordChannel.discordChannel, CatalystKeys.LEAVE_MESSAGE)
    }
}
