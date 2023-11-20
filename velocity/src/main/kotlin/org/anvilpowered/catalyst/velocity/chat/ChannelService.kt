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

package org.anvilpowered.catalyst.velocity.chat

import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.catalyst.api.config.ChatChannel
import java.util.UUID

interface ChannelService {

    val defaultChannel: ChatChannel

    fun get(channelId: String): ChatChannel?

    fun getForPlayer(playerId: UUID): ChatChannel

    fun getPlayers(channelId: String): Sequence<Player>

    fun switch(userUUID: UUID, channelId: String)

    fun moveUsersToChannel(sourceChannel: String, targetChannel: String)

    interface Scope {
        val channelService: org.anvilpowered.catalyst.velocity.chat.ChannelService
    }
}
