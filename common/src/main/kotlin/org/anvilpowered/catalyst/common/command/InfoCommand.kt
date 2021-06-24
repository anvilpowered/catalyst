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
package org.anvilpowered.catalyst.common.command

import com.google.inject.Inject
import com.mojang.brigadier.context.CommandContext
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys

class InfoCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val memberManager: MemberManager<TString>,
  private val pluginMessages: PluginMessages<TString>,
  private val registry: Registry,
  private val permissionService: PermissionService,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>
) {
  fun execute(context: CommandContext<TCommandSource>): Int {
    if (!permissionService.hasPermission(context.source, registry.getOrDefault(CatalystKeys.INFO_PERMISSION))) {
      textService.send(pluginMessages.noPermission, context.source)
      return 0
    }
    val isActive = userService[context.getArgument("target", String::class.java)].isPresent

    val permissions = BooleanArray(3)
    permissions[0] = permissionService.hasPermission(context.source, registry.getOrDefault(CatalystKeys.INFO_IP_PERMISSION))
    permissions[1] = permissionService.hasPermission(context.source, registry.getOrDefault(CatalystKeys.INFO_BANNED_PERMISSION))
    permissions[2] = permissionService.hasPermission(context.source, registry.getOrDefault(CatalystKeys.INFO_CHANNEL_PERMISSION))

    memberManager.info(context.getArgument("target", String::class.java), isActive, permissions)
      .thenAcceptAsync { textService.send(it, context.source) }
    return 1
  }
}
