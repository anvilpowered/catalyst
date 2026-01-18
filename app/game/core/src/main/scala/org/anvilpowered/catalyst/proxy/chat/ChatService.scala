/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
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
 *     along with this program.  If not, see [https://www.gnu.org/licenses/].
 */

package org.anvilpowered.catalyst.proxy.chat

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import java.util.UUID

trait ChatService {

    suspend def sendMessage(message: ChannelMessage.Resolved)
    def ignore(playerUUID: UUID, targetPlayerUUID: UUID): Component
    def unIgnore(playerUUID: UUID, targetPlayerUUID: UUID): Component
    def isIgnored(playerUUID: UUID, targetPlayerUUID: UUID): Boolean
    def highlightPlayerNames(sender: Player, message: Component): Component
    def toggleChatForPlayer(player: Player)
    def isDisabledForPlayer(player: Player): Boolean
}
