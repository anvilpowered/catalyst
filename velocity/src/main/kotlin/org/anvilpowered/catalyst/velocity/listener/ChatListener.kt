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

package org.anvilpowered.catalyst.velocity.listener

import com.google.common.eventbus.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.chat.build
import org.anvilpowered.catalyst.api.chat.userId
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.db.RepositoryScope
import org.anvilpowered.catalyst.velocity.chat.ChatFilter
import org.anvilpowered.catalyst.velocity.chat.ChatService

context(
    ProxyServerScope, Registry.Scope, LoggerScope, LuckpermsService.Scope, ChatService.Scope, ChannelService.Scope, ChatFilter.Scope,
    ChannelMessage.Scope, RepositoryScope
)
class ChatListener {
    @Subscribe
    fun onPlayerChat(event: PlayerChatEvent) = runBlocking {
        val player = event.player
        if (registry[CatalystKeys.PROXY_CHAT_ENABLED]) {
            if (chatService.isDisabledForPlayer(player) || channelService.getForPlayer(player.uniqueId).passthrough) {
                return@runBlocking
            }
            event.result = PlayerChatEvent.ChatResult.denied()
            val rawMessage = if (player.hasPermission(registry[CatalystKeys.CHAT_COLOR_PERMISSION])) {
                MiniMessage.miniMessage().deserialize(event.message)
            } else {
                Component.text(event.message)
            }
            // TODO: Move this to dedicated class
            var message = chatService.highlightPlayerNames(player, rawMessage)
            if (!player.hasPermission(registry[CatalystKeys.LANGUAGE_ADMIN_PERMISSION]) &&
                registry[CatalystKeys.CHAT_FILTER_ENABLED]
            ) {
                message = chatFilter.replaceSwears(message)
            }

            val channel = channelService.getForPlayer(player.uniqueId)

            val channelMessage = ChannelMessage.build {
                userId(player.uniqueId)
                message(message)
                prefix(luckpermsService.prefix(player.uniqueId))
                suffix(luckpermsService.suffix(player.uniqueId))
                messageFormatOverride(luckpermsService.messageFormat(player.uniqueId, channel.id))
                nameFormatOverride(luckpermsService.nameFormat(player.uniqueId, channel.id))
                server(player.currentServer.orElse(null)?.serverInfo?.name ?: "unknown")
                channel(channel)
            }

            // TODO: Use a listener instead?
            chatService.sendMessage(channelMessage)
            proxyServer.eventManager.fire(ChannelMessage.Event(channelMessage))
        }
    }
}
