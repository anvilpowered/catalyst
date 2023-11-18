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

package org.anvilpowered.catalyst.core.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.config.ChatChannel
import org.anvilpowered.catalyst.api.user.GameUser
import org.anvilpowered.catalyst.core.chat.builder.ChatMessageBuilderImpl
import org.anvilpowered.catalyst.core.db.RepositoryScope
import java.util.UUID

// Utility class to construct a chat message and format it
class ChatMessage(
    val userId: UUID,
    val component: Component,
) {

    interface Builder {
        /**
         * Use [Builder.userId] if you only have a userId.
         */
        fun user(user: GameUser): Builder

        /**
         * Use [Builder.channelId] if you only have a channelId.
         */
        fun channel(channel: ChatChannel): Builder

        fun message(message: Component): Builder
        fun prefix(prefix: Component): Builder
        fun suffix(suffix: Component): Builder

        /**
         * Override channel-specific message format.
         */
        fun messageFormatOverride(format: Component?): Builder
        fun nameFormatOverride(format: Component?): Builder

        fun server(server: String): Builder
        fun build(): ChatMessage
    }

    companion object {
        context(PlayerService.Scope)
        fun builder(): Builder = ChatMessageBuilderImpl()

        context(PlayerService.Scope)
        inline fun build(block: Builder.() -> Unit): ChatMessage = builder().apply(block).build()
    }
}

context(RepositoryScope)
suspend fun ChatMessage.Builder.userId(userId: UUID): ChatMessage.Builder = user(
    requireNotNull(gameUserRepository.getById(userId)) { "Could not find user with id $userId" },
)

context(ChannelService.Scope)
fun ChatMessage.Builder.channelId(channelId: String): ChatMessage.Builder = channel(
    requireNotNull(channelService.get(channelId)) { "Could not find channel with id $channelId" },
)
