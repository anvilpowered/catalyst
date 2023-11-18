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

package org.anvilpowered.catalyst.core.chat

import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.core.user.Player
import java.util.UUID

interface ChatService {

    suspend fun sendMessageToChannel(channelId: String, message: Component, userId: UUID)
    suspend fun sendChatMessage(message: ChatMessage)
    fun ignore(playerUUID: UUID, targetPlayerUUID: UUID): Component
    fun unIgnore(playerUUID: UUID, targetPlayerUUID: UUID): Component
    fun isIgnored(playerUUID: UUID, targetPlayerUUID: UUID): Boolean
    fun highlightPlayerNames(sender: Player, message: Component): Component
    fun toggleChatForPlayer(player: Player)
    fun isDisabledForPlayer(player: Player): Boolean

    interface Scope {
        val chatService: ChatService
    }
}
