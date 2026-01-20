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

package org.anvilpowered.catalyst.api.chat

import org.anvilpowered.catalyst.api.config.ChatChannel
import java.util.UUID
import org.anvilpowered.anvil.core.user.Player

trait ChannelService {

  val defaultChannel: ChatChannel

  def apply(channelId: String): Option[ChatChannel]

  def getForPlayer(playerId: UUID): ChatChannel

  def getAvailable(player: Option[Player] = None): Seq[ChatChannel]

  def getReceivers(channelId: String): Seq[Player]

  def switch(userUUID: UUID, channelId: String): Unit
}
