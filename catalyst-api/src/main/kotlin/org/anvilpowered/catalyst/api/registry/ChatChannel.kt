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

package org.anvilpowered.catalyst.api.registry

import kotlin.experimental.ExperimentalTypeInference

class ChatChannel(
    var id: String,
    var format: String,
    var hoverMessage: String,
    var click: String,
    var servers: List<String>,
    var alwaysVisible: Boolean,
    var passthrough: Boolean,
    var discordChannel: String
) {

    interface Builder {

        fun id(_id: String): Builder

        fun format(_format: String): Builder

        fun hoverMessage(_hoverMessage: String): Builder

        fun click(_click: String): Builder

        fun servers(_servers: List<String>): Builder

        fun addServer(_server: String): Builder

        fun alwaysVisible(_visible: Boolean): Builder

        fun passthrough(_passthrough: Boolean): Builder

        fun discordChannel(_discordChannel: String): Builder

        fun build(): ChatChannel
    }

    companion object {
        fun builder(): Builder {
            return ChatChannelBuilder()
        }

        @OptIn(ExperimentalTypeInference::class)
        inline fun build(@BuilderInference block: Builder.() -> Unit): ChatChannel {
            return builder().apply(block).build()
        }
    }
}
