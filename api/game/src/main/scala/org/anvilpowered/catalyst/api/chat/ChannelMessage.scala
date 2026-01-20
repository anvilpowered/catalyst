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
 *     along with this program.  If not, see [https://www.gnu.org/licenses/].
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
)

object ChannelMessage {
  trait Builder {

    /** Use [Builder.userId] if you only have a userId.
      */
    def user(user: MinecraftUser): Builder

    def userId[F[_]](userId: UUID): Builder

    /** Use [Builder.channelId] if you only have a channelId.
      */
    def channel(channel: ChatChannel): Builder

    def channelId[F[_]](channelId: String): Builder

    def rawContent(rawContent: Component): Builder
    def build(): ChannelMessage

    trait Factory {
      def builder(): Builder
    }
  }
  case class Resolved(val backing: ChannelMessage, val formatted: PlayerFormat)
}

