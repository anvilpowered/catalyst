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
package org.anvilpowered.catalyst.common.listener

import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.ChatFilter
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.registry.Registry

class ChatListener<TPlayer, TCommandSource> @Inject constructor(
    private val chatService: ChatService<TPlayer, TCommandSource>,
    private val channelService: ChannelService<TPlayer>,
    private val permissionService: PermissionService,
    private val chatFilter: ChatFilter,
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>,
) {

    @Subscribe
    fun onPlayerChat(event: ChatEvent<TPlayer>) {
        val playerUUID = userService.getUUID(event.player)
        val player = userService.getPlayer(playerUUID!!) ?: return
        var message = event.rawMessage
        val channel = channelService.getChannelFromId(channelService.getChannelIdForUser(playerUUID))
        message = chatService.checkPlayerName(player, message)
        if (channel != null) {
            if (!permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN_PERMISSION))
                && registry.getOrDefault(CatalystKeys.CHAT_FILTER_ENABLED)
            ) {
                event.rawMessage = chatFilter.replaceSwears(message)
            }
            chatService.sendChatMessage(event)
        } else {
            throw AssertionError("If this is your first time running anvil, run /av reload Catalyst, if not report this github.")
        }
    }
}
