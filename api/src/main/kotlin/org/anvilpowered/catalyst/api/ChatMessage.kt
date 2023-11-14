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

package org.anvilpowered.catalyst.api

import org.anvilpowered.catalyst.api.builder.ChatMessageBuilderImpl
import org.anvilpowered.catalyst.api.config.ChatChannel
import java.util.UUID
import kotlin.experimental.ExperimentalTypeInference

// Utility class to construct a chat message and format it
class ChatMessage(
    val uuid: UUID,
    val component: net.kyori.adventure.text.Component
) {

    interface Builder {
        fun uuid(uuid: UUID): Builder
        fun message(message: String): Builder
        fun prefix(prefix: String): Builder
        fun suffix(suffix: String): Builder
        fun color(color: String): Builder
        fun nameColor(nameColor: String): Builder
        fun userName(userName: String): Builder
        fun server(server: String): Builder
        fun channel(channel: ChatChannel): Builder
        fun hasColorPermission(hasPermission: Boolean): Builder
        fun build(): ChatMessage
    }

    companion object {
        fun <T> builder(): Builder {
            return ChatMessageBuilderImpl<T>()
        }

        @OptIn(ExperimentalTypeInference::class)
        inline fun <reified T> build(@BuilderInference block: Builder.() -> Unit): ChatMessage {
            return builder<T>().apply(block).build()
        }
    }
}
