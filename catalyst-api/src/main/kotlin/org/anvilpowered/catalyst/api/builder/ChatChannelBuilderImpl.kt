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

package org.anvilpowered.catalyst.api.builder

import org.anvilpowered.catalyst.api.registry.ChatChannel

internal class ChatChannelBuilderImpl: ChatChannel.Builder {
    private var id: String = ""
    private var format: String = ""
    private var hoverMessage: String = ""
    private var click: String = ""
    private var servers: List<String> = listOf()
    private var alwaysVisible: Boolean = false
    private var passthrough: Boolean = false
    private var discordChannel: String = ""

    override fun id(_id: String): ChatChannel.Builder {
        this.id = _id
        return this
    }

    override fun format(_format: String): ChatChannel.Builder {
        this.format = _format
        return this
    }

    override fun hoverMessage(_hoverMessage: String): ChatChannel.Builder {
        this.hoverMessage = _hoverMessage
        return this
    }

    override fun click(_click: String): ChatChannel.Builder {
        this.click = _click
        return this
    }

    override fun servers(_servers: List<String>): ChatChannel.Builder {
        this.servers = _servers
        return this
    }

    override fun addServer(_server: String): ChatChannel.Builder {
        this.servers = this.servers.plus(_server)
        return this
    }

    override fun alwaysVisible(_visible: Boolean): ChatChannel.Builder {
        this.alwaysVisible = _visible
        return this
    }

    override fun passthrough(_passthrough: Boolean): ChatChannel.Builder {
        this.passthrough = _passthrough
        return this
    }

    override fun discordChannel(_discordChannel: String): ChatChannel.Builder {
        this.discordChannel = _discordChannel
        return this
    }

    override fun build(): ChatChannel {
        return ChatChannel(id, format, hoverMessage, click, servers, alwaysVisible, passthrough, discordChannel)
    }
}
