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
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.event.LeaveEvent
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.BroadcastService
import org.anvilpowered.catalyst.api.service.EmojiService
import org.anvilpowered.catalyst.api.service.StaffListService
import org.slf4j.Logger

class LeaveListener<TString, TPlayer, TCommandSource> @Inject constructor(
  private val userService: UserService<TPlayer, TPlayer>,
  private val textService: TextService<TString, TCommandSource>,
  private val registry: Registry,
  private val staffListService: StaffListService<TString>,
  private val broadcastService: BroadcastService<TString>,
  private val logger: Logger,
  private val emojiService: EmojiService
){
  @Subscribe
  fun onPlayerLeave(event: LeaveEvent<TPlayer>) {
    val player = event.player
    staffListService.removeStaffNames(userService.getUserName(player as TPlayer))
    val message =
      if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
        emojiService.toEmoji(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE), "&f")
      } else {
        registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE)
      }

    if (registry.getOrDefault(CatalystKeys.LEAVE_LISTENER_ENABLED)) {
      broadcastService.broadcast(textService.deserialize(message.replace("%player%", userService.getUserName(player))))
      logger.info(
        textService.serializePlain(
          textService.of(registry.getOrDefault(CatalystKeys.LEAVE_MESSAGE).replace("%player%", userService.getUserName(player))
          )
        )
      )
    }
  }
}
