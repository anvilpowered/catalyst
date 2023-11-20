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
package org.anvilpowered.catalyst.velocity.chat

import com.google.common.eventbus.Subscribe
import kotlinx.coroutines.runBlocking
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.anvil.core.user.hasPermissionNotSet
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.user.LocationScope
import org.anvilpowered.catalyst.velocity.db.RepositoryScope

context(
    ChatService.Scope, Registry.Scope, PlayerService.Scope, ChannelService.Scope, LuckpermsService.Scope, ChatFilter.Scope,
    RepositoryScope, org.anvilpowered.catalyst.api.user.LocationScope
)
class ChatListener {

    @Subscribe
    fun onPlayerChat(event: ChatEvent) = runBlocking {
        val player = event.player
        var message = chatService.highlightPlayerNames(player, event.message)
        if (player.hasPermissionNotSet(registry[CatalystKeys.LANGUAGE_ADMIN_PERMISSION]) &&
            registry[CatalystKeys.CHAT_FILTER_ENABLED]
        ) {
            message = chatFilter.replaceSwears(message)
        }

        val channel = channelService.getForPlayer(player.id)

        val chatMessage = ChatMessage.builder()
            .userId(player.id)
            .message(message)
            .prefix(luckpermsService.prefix(player.id))
            .suffix(luckpermsService.suffix(player.id))
            .messageFormatOverride(luckpermsService.messageFormat(player.id, channel.id))
            .nameFormatOverride(luckpermsService.nameFormat(player.id, channel.id))
            .server(player.serverName)
            .channel(channel)
            .build()
        chatService.sendChatMessage(chatMessage)
    }
}
