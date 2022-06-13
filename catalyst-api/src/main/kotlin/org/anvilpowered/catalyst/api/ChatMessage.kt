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

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.builder.ChatMessageBuilderImpl
import org.anvilpowered.catalyst.api.registry.ChatChannel
import java.util.UUID
import kotlin.experimental.ExperimentalTypeInference

// Utility class to construct a chat message and format it
class ChatMessage(
    val uuid: UUID,
    val component: Component
) {

    interface Builder {
        fun uuid(_uuid: UUID): Builder
        fun message(_message: String): Builder
        fun prefix(_prefix: String): Builder
        fun suffix(_suffix: String): Builder
        fun color(_color: String): Builder
        fun nameColor(_nameColor: String): Builder
        fun userName(_userName: String): Builder
        fun server(_server: String): Builder
        fun channel(_channel: ChatChannel): Builder
        fun hasColorPermission(_hasPermission: Boolean): Builder
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
