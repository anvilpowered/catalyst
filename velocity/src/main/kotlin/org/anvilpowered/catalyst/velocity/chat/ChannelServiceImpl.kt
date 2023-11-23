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

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.config.ChatChannel
import java.util.UUID

class ChannelServiceImpl(
    private val registry: Registry,
    private val proxyServer: ProxyServer,
    private val catalystKeys: CatalystKeys,
) : ChannelService {
    private var playerChannelMapping = mutableMapOf<UUID, String>()

    private var defaultChannelId = registry[catalystKeys.CHAT_DEFAULT_CHANNEL]
    override val defaultChannel: ChatChannel = requireNotNull(get(defaultChannelId)) { "Default chat channel not found" }

    override fun get(channelId: String): ChatChannel? = registry[catalystKeys.CHAT_CHANNELS][channelId]
    override fun getForPlayer(playerId: UUID): ChatChannel = playerChannelMapping[playerId]?.let { get(it) } ?: defaultChannel
    override fun getPlayers(channelId: String): Sequence<Player> =
        proxyServer.allPlayers.asSequence().filter { player -> getForPlayer(player.uniqueId).id == channelId }

    override fun switch(userUUID: UUID, channelId: String) {
        playerChannelMapping[userUUID] = channelId
    }

    override fun moveUsersToChannel(sourceChannel: String, targetChannel: String) {
        for (player in getPlayers(sourceChannel)) {
            switch(player.uniqueId, targetChannel)
        }
    }
}
