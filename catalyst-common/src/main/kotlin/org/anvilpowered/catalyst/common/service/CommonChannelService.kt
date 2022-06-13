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

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import com.google.inject.Singleton
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.registry.ChatChannel
import org.anvilpowered.catalyst.api.service.ChannelService
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

@Singleton
class CommonChannelService<TPlayer> @Inject constructor(
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>
) : ChannelService<TPlayer> {

    private var channelMap = mutableMapOf<UUID, String>()

    //TODO force update when changes are made to the registry
    private val defaultChannel = CatalystKeys.CHAT_DEFAULT_CHANNEL

    override fun switchChannel(userUUID: UUID, channelId: String) {
        channelMap[userUUID] = channelId
    }

    override fun getDefaultChannel(): ChatChannel? = fromId(registry.getOrDefault(defaultChannel))
    override fun fromId(channelId: String): ChatChannel? = registry[CatalystKeys.CHAT_CHANNELS]?.find { it.id == channelId }
    override fun fromUUID(userUUID: UUID): ChatChannel = fromId(channelMap[userUUID] ?: registry.getOrDefault(defaultChannel))
        ?: fromId(registry.getOrDefault(defaultChannel))!!

    override fun discordChannelId(channelId: String): String? = fromId(channelId)?.discordChannel

    override fun userCount(channelId: String): Int =
        userService.onlinePlayers()
            .stream()
            .filter { fromUUID(userService.getUUID(it as TPlayer)!!).id == channelId }
            .count().toInt()

    override fun getUsersInChannel(channelId: String): MutableList<TPlayer> =
        userService.onlinePlayers()
            .stream()
            .filter { fromUUID(userService.getUUID(it as TPlayer)!!).id == channelId }
            .collect(Collectors.toList())

    override fun moveUsersToChannel(sourceChannel: String, targetChannel: String): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            for (user in getUsersInChannel(sourceChannel)) {
                switchChannel(userService.getUUID(user)!!, targetChannel)
            }
            return@supplyAsync true
        }
    }
}
