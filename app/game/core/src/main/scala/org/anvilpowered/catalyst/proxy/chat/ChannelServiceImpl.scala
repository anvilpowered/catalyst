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

import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.config.ChatChannel
import java.util.UUID

class ChannelServiceImpl(
    private val registry: Registry,
    private val proxyServer: ProxyServer,
    private val catalystKeys: CatalystKeys,
) extends ChannelService {

  init {
    check(!registry(catalystKeys.PERMISSION_CHANNEL_PREFIX).endsWith('.')) {
      "Channel permission prefix must not end with a '.'"
    }
  }

  /** Maps players to the channel to which they send messages.
    */
  private var playerChannelMapping = new mutable.Map[UUID, String]

  private var defaultChannelId = registry[catalystKeys.CHAT_DEFAULT_CHANNEL]
  override val defaultChannel: ChatChannel = requireNotNull(get(defaultChannelId)) { "Default chat channel not found" }

  override def apply(channelId: String): Option[ChatChannel] = registry(catalystKeys.CHAT_CHANNELS)(channelId)
  override def getForPlayer(playerId: UUID): ChatChannel = playerChannelMapping.get(playerId).map(get(_)).getOrElse(defaultChannel)
  def getAvailable: List[ChatChannel] = {
    registry(catalystKeys.CHAT_CHANNELS).values.toList()
  }
  override def getAvailable(player: Player): List[ChatChannel] = {
    registry(catalystKeys.CHAT_CHANNELS).values.filter { channel =>
      player.canAccess(channel)
    }
    return if (player == null) {} else {}
  }

  override def getReceivers(channelId: String): Sequence[Player] = {
    // TODO: Optimize by not recalculating on each chat message
    val channel = checkNotNull(get(channelId)) { "Channel $channelId does not exist" }
    return proxyServer.allPlayers
      .asSequence()
      .filter { getForPlayer(it.uniqueId).id == channelId || (it.canAccess(channel) && channel.alwaysVisible) }
  }

  override def switch(userUUID: UUID, channelId: String) = {
    playerChannelMapping.put(userUUID, channelId)
  }

  extension (player: Player) {
    def canAccess(channel: ChatChannel): Boolean = {
      val permissionValue = getPermissionValue("${registry[catalystKeys.PERMISSION_CHANNEL_PREFIX]}.${channel.id}")
      return permissionValue == Tristate.TRUE ||
        (channel.availableByDefault && permissionValue != Tristate.FALSE)
    }

  }
}
