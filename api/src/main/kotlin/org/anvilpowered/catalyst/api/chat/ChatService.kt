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

package org.anvilpowered.catalyst.api.chat

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.ChatMessage
import org.anvilpowered.catalyst.api.user.GameUser
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface ChatService {

    fun sendMessageToChannel(
        channelId: String,
        message: Component,
        userUUID: UUID,
    ): CompletableFuture<Void>

    fun sendChatMessage(message: ChatMessage)
    fun ignore(playerUUID: UUID, targetPlayerUUID: UUID): Component
    fun unIgnore(playerUUID: UUID, targetPlayerUUID: UUID): Component
    fun isIgnored(playerUUID: UUID, targetPlayerUUID: UUID): Boolean
    fun checkPlayerName(sender: GameUser, message: String): String
    fun toggleChatForUser(player: GameUser)
    fun isDisabledForUser(player: GameUser): Boolean
}
