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
package org.anvilpowered.catalyst.common.command

import com.google.inject.Inject
import com.mojang.brigadier.context.CommandContext
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.KickService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys

class KickCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val pluginMessages: PluginMessages<TString>,
  private val registry: Registry,
  private val kickService: KickService,
  private val textService: TextService<TString, TCommandSource>,
  private val permissionService: PermissionService,
  private val userService: UserService<TPlayer, TPlayer>
) {

  fun execute(context: CommandContext<TCommandSource>): Int {
    val player = userService.getPlayer(context.getArgument("target", String::class.java))
    if (player.isPresent) {
      if (permissionService.hasPermission(
          player.get(),
          registry.getOrDefault(CatalystKeys.KICK_EXEMPT_PERMISSION)
        )
      ) {
        textService.send(pluginMessages.kickExempt, context.source)
        return 0
      }
      kickService.kick(
        context.getArgument("target", String::class.java),
        context.getArgument("reason", String::class.java)
      )
    } else {
      textService.send(pluginMessages.offlineOrInvalidPlayer(), context.source)
    }
    return 1
  }

  fun withoutReason(context: CommandContext<TCommandSource>): Int {
    val reason = "You have been kicked!"
    val player = userService.getPlayer(context.getArgument("target", String::class.java))
    if (player.isPresent) {
      if (permissionService.hasPermission(
          player.get(),
          registry.getOrDefault(CatalystKeys.KICK_EXEMPT_PERMISSION)
        )
      ) {
        textService.send(pluginMessages.kickExempt, context.source)
        return 0
      }
      kickService.kick(context.getArgument("target", String::class.java), reason)
    } else {
      textService.send(pluginMessages.offlineOrInvalidPlayer(), context.source)
    }
    return 1
  }
}
