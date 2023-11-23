/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2024 Contributors
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

package org.anvilpowered.catalyst.velocity.chat.builder

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.config.ChatChannel

internal class ChatChannelBuilderImpl : ChatChannel.Builder {
    private var id: String = ""
    private var nameFormat: Component = Component.text("%name%")
    private var messageFormat: Component = Component.text("%message%")
    private var hoverFormat: Component = Component.text("%message%")
    private var clickFormat: String = ""
    private var alwaysVisible: Boolean = false
    private var passthrough: Boolean = false
    private var discordChannel: String = ""

    override fun id(id: String): ChatChannel.Builder {
        this.id = id
        return this
    }

    override fun nameFormat(nameFormat: Component): ChatChannel.Builder {
        this.nameFormat = nameFormat
        return this
    }

    override fun messageFormat(messageFormat: Component): ChatChannel.Builder {
        this.messageFormat = messageFormat
        return this
    }

    override fun hoverFormat(hoverFormat: Component): ChatChannel.Builder {
        this.hoverFormat = hoverFormat
        return this
    }

    override fun clickFormat(clickFormat: String): ChatChannel.Builder {
        this.clickFormat = clickFormat
        return this
    }

    override fun alwaysVisible(visible: Boolean): ChatChannel.Builder {
        this.alwaysVisible = visible
        return this
    }

    override fun passThrough(passThrough: Boolean): ChatChannel.Builder {
        this.passthrough = passThrough
        return this
    }

    override fun discordChannel(discordChannel: String): ChatChannel.Builder {
        this.discordChannel = discordChannel
        return this
    }

    override fun build(): ChatChannel =
        ChatChannel(id, nameFormat, messageFormat, hoverFormat, clickFormat, alwaysVisible, passthrough, discordChannel)

    class Factory : ChatChannel.Builder.Factory {
        override fun builder(): ChatChannel.Builder = ChatChannelBuilderImpl()
    }
}
