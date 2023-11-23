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

package org.anvilpowered.catalyst.api.config

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
data class ChatChannel(
    val id: String,
    val nameFormat: Component,
    val messageFormat: Component,
    val hoverFormat: Component,
    val clickFormat: String,
    val alwaysVisible: Boolean,
    val passthrough: Boolean,
    val discordChannel: String,
) {

    interface Builder {

        fun id(id: String): Builder

        fun nameFormat(nameFormat: Component): Builder

        fun messageFormat(messageFormat: Component): Builder

        fun hoverFormat(hoverFormat: Component): Builder

        fun clickFormat(clickFormat: String): Builder

        fun alwaysVisible(visible: Boolean): Builder

        fun passThrough(passThrough: Boolean): Builder

        fun discordChannel(discordChannel: String): Builder

        fun build(): ChatChannel

        interface Factory {
            fun builder(): Builder
        }
    }
}

inline fun ChatChannel.Builder.Factory.build(block: ChatChannel.Builder.() -> Unit): ChatChannel = builder().apply(block).build()
