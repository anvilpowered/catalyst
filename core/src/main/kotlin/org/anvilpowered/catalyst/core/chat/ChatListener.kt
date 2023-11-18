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
package org.anvilpowered.catalyst.core.chat

import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.ChatFilter
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.LuckpermsService

class ChatListener<TPlayer, TCommandSource> @Inject constructor(
    private val chatService: ChatService<TPlayer, TCommandSource>,
    private val permissionService: PermissionService,
    private val chatFilter: ChatFilter,
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>,
    private val channelService: ChannelService<TPlayer>,
    private val luckpermsService: LuckpermsService,
    private val locationService: LocationService,
) {

    @Subscribe
    fun onPlayerChat(event: ChatEvent) {
        val playerUUID = userService.getUUID(event.player)
        val player = userService.getPlayer(playerUUID!!) ?: return
        var message = event.rawMessage
        message = chatService.checkPlayerName(player, message)
        if (!permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN_PERMISSION))
            && registry.get(CatalystKeys.CHAT_FILTER_ENABLED)
        ) {
            event.rawMessage = chatFilter.replaceSwears(message)
        }
        val chatMessage = ChatMessage.builder<TPlayer>()
            .userId(userService.getUUID(player)!!)
            .message(event.rawMessage)
            .prefix(luckpermsService.prefix(player))
            .suffix(luckpermsService.suffix(player))
            .messageColor(luckpermsService.chatColor(player))
            .nameColor(luckpermsService.nameColor(player))
            .userName(userService.getUserName(player))
            .server(locationService.getServer(playerUUID)?.name ?: "null")
            .channel(channelService.fromUUID(playerUUID))
            .hasColorPermission(permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.CHAT_COLOR_PERMISSION)))
            .build()
        chatService.sendChatMessage(chatMessage)
    }
}
