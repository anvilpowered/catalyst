/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
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

package org.anvilpowered.catalyst.api.chat

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.placeholder.PlayerFormat
import org.anvilpowered.catalyst.api.config.ChatChannel
import org.anvilpowered.catalyst.api.user.MinecraftUser
import java.util.UUID

// Utility class to construct a chat message and format it
class ChannelMessage(
    val source: MinecraftUser.Online,
    val channel: ChatChannel,
    val name: Component,
    val content: Component,
) {

    interface Builder {
        /**
         * Use [Builder.userId] if you only have a userId.
         */
        fun user(user: MinecraftUser): Builder

        suspend fun userId(userId: UUID): Builder

        /**
         * Use [Builder.channelId] if you only have a channelId.
         */
        fun channel(channel: ChatChannel): Builder

        suspend fun channelId(channelId: String): Builder

        fun rawContent(rawContent: Component): Builder
        suspend fun build(): ChannelMessage

        interface Factory {
            fun builder(): Builder
        }
    }

    data class Resolved(val backing: ChannelMessage, val formatted: PlayerFormat)
}

suspend inline fun ChannelMessage.Builder.Factory.build(block: ChannelMessage.Builder.() -> Unit): ChannelMessage =
    builder().apply(block).build()
