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

package org.anvilpowered.catalyst.api.service

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.ChatMessage
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface ChatService<TPlayer, TCommandSource> {

    fun sendMessageToChannel(
        channelId: String,
        message: Component,
        userUUID: UUID
    ): CompletableFuture<Void>

    fun sendChatMessage(message: ChatMessage)
    fun ignore(playerUUID: UUID, targetPlayerUUID: UUID): Component
    fun unIgnore(playerUUID: UUID, targetPlayerUUID: UUID): Component
    fun isIgnored(playerUUID: UUID, targetPlayerUUID: UUID): Boolean
    fun checkPlayerName(sender: TPlayer, message: String): String
    fun toggleChatForUser(player: TPlayer)
    fun isDisabledForUser(player: TPlayer): Boolean
}
