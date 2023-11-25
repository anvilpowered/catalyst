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
import com.velocitypowered.api.event.connection.PostLoginEvent
import kotlinx.coroutines.runBlocking
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.config.CatalystKeys

class DiscordChatListener(
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val channelService: ChannelService,
    private val webhookSender: WebhookSender,
) {

    @Subscribe
    fun onChatEvent(event: ChannelMessage.Event) = runBlocking {
        if (!registry[catalystKeys.DISCORD_ENABLED]) {
            return@runBlocking
        }
//        if (!player.hasPermission(registry[CatalystKeys.LANGUAGE_ADMIN_PERMISSION])) {
//            message = message.replace("@".toRegex(), "")
//        }
        webhookSender.sendChannelMessage(
            event.message.backing.source.player,
            event.message.formatted.format, // TODO: Format for discord, create Player indirection
            event.message.backing.channel.discordChannelId,
        )
    }

    @Subscribe
    fun onPlayerJoinEvent(event: PostLoginEvent) = runBlocking {
        if (!registry[catalystKeys.DISCORD_ENABLED]) {
            return@runBlocking
        }
        val discordChannel = channelService.getForPlayer(event.player.uniqueId)
        webhookSender.sendSpecialMessage(event.player, discordChannel.discordChannelId, catalystKeys.JOIN_MESSAGE)
    }

    @Subscribe
    fun onPlayerLeaveEvent(event: DisconnectEvent) = runBlocking {
        if (!registry[catalystKeys.DISCORD_ENABLED]) {
            return@runBlocking
        }
        val discordChannel = channelService.getForPlayer(event.player.uniqueId)
        webhookSender.sendSpecialMessage(event.player, discordChannel.discordChannelId, catalystKeys.LEAVE_MESSAGE)
    }
}
