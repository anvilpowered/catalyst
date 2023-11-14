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

import org.anvilpowered.catalyst.api.config.ChatChannel

internal class ChatChannelBuilderImpl: ChatChannel.Builder {
    private var id: String = ""
    private var format: String = ""
    private var hoverMessage: String = ""
    private var click: String = ""
    private var servers: List<String> = listOf()
    private var alwaysVisible: Boolean = false
    private var passthrough: Boolean = false
    private var discordChannel: String = ""

    override fun id(id: String): ChatChannel.Builder {
        this.id = id
        return this
    }

    override fun format(format: String): ChatChannel.Builder {
        this.format = format
        return this
    }

    override fun hoverMessage(hoverMessage: String): ChatChannel.Builder {
        this.hoverMessage = hoverMessage
        return this
    }

    override fun click(click: String): ChatChannel.Builder {
        this.click = click
        return this
    }

    override fun servers(servers: List<String>): ChatChannel.Builder {
        this.servers = servers
        return this
    }

    override fun addServer(server: String): ChatChannel.Builder {
        this.servers = this.servers.plus(server)
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

    override fun build(): ChatChannel {
        return ChatChannel(id, format, hoverMessage, click, servers, alwaysVisible, passthrough, discordChannel)
    }
}
