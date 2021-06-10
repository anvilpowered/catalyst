/*
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.catalyst.common.listener

import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import org.anvilpowered.anvil.api.misc.Named
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.event.JoinEvent
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService
import org.anvilpowered.catalyst.api.service.BroadcastService
import org.anvilpowered.catalyst.api.service.EmojiService
import org.anvilpowered.catalyst.api.service.PrivateMessageService
import org.anvilpowered.catalyst.api.service.StaffListService
import org.slf4j.Logger
import java.util.UUID

class JoinListener<TString, TPlayer, TCommandSource> @Inject constructor(
  private val userService: UserService<TPlayer, TPlayer>,
  private val permissionService: PermissionService,
  private val textService: TextService<TString, TCommandSource>,
  private val registry: Registry,
  private val privateMessageService: PrivateMessageService<TString>,
  private val staffListService: StaffListService<TString>,
  private val broadcastService: BroadcastService<TString>,
  private val logger: Logger,
  private val serverService: AdvancedServerInfoService,
  private val locationService: LocationService,
  private val emojiService: EmojiService
) {
  @Subscribe
  fun onPlayerJoin(event: JoinEvent<TPlayer>) {
    val player = event.player
    val playerUUID = event.playerUUID
    val virtualHost = event.hostString
    if (permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.SOCIALSPY_ONJOIN_PERMISSION))) {
      privateMessageService.socialSpySet().add(playerUUID)
    }
    val userName = userService.getUserName(player as TPlayer)
    var server =
      if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) locationService.getServer(userName).map { obj: Named -> obj.name }
        .orElse("null") else locationService.getServer(playerUUID).map { obj: Named -> obj.name }
        .orElse("null")
    if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
      serverService.insertPlayer(userName, serverService.getPrefix(virtualHost))
      server = serverService.getPrefixForPlayer(userName)
    }
    staffListService.getStaffNames(
      userName,
      permissionService.hasPermission(
        player,
        registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN_PERMISSION)
      ),
      permissionService.hasPermission(
        player,
        registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF_PERMISSION)
      ),
      permissionService.hasPermission(
        player,
        registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER_PERMISSION)
      )
    )
    val joinMessage = if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) emojiService.toEmoji(
      registry.getOrDefault(CatalystKeys.JOIN_MESSAGE),
      "&f"
    ) else registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
    if (registry.getOrDefault(CatalystKeys.JOIN_LISTENER_ENABLED)) {
      broadcastService.broadcast(
        textService.deserialize(
          joinMessage
            .replace("%player%", userName)
            .replace("%server%", server)
        )
      )
      logger.info(
        textService.serializePlain(
          textService.of(
            registry.getOrDefault(CatalystKeys.JOIN_MESSAGE)
              .replace("%player%", userName)
              .replace("%server%", server)
          )
        )
      )
    }
  }
}
