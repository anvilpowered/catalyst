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

package org.anvilpowered.catalyst.api.config

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.builder.ChatChannelBuilderImpl

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
    }

    companion object {
        fun builder(): Builder = ChatChannelBuilderImpl()
        inline fun build(block: Builder.() -> Unit): ChatChannel = builder().apply(block).build()
    }
}
