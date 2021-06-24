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

class TempBanCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val permissionService: PermissionService,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>,
  private val pluginMessages: PluginMessages<TString>,
  private val memberManager: MemberManager<TString>,
  private val registry: Registry
) {
  fun execute(source: TCommandSource, args: Array<String>) {
    if (!permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.TEMP_BAN_PERMISSION))) {
      textService.send(pluginMessages.noPermission, source)
      return
    }
    if (args.size < 2) {
      textService.send(pluginMessages.notEnoughArgs, source)
      textService.send(pluginMessages.tempBanCommandUsage(), source)
      return
    }
    val userName = args[0]
    val duration = args[1]
    if (userService[userName].isPresent) {
      if (permissionService.hasPermission(userService[userName], registry.getOrDefault(CatalystKeys.BAN_EXEMPT_PERMISSION))) {
        textService.send(pluginMessages.banExempt, source)
        return
      }
    }
    if (args.size == 2) {
      memberManager.tempBan(userName, duration).thenAcceptAsync { textService.send(it, source) }
    } else {
      val reason = java.lang.String.join(" ", *args).replace("$userName ", "").replace(duration, "")
      memberManager.tempBan(userName, duration, reason).thenAcceptAsync { textService.send(it, source) }
    }
  }

  fun withoutReason(context: CommandContext<TCommandSource>): Int {
    execute(
      context.source, arrayOf(
        context.getArgument("target"),
        context.getArgument("duration")
      )
    )
    return 1
  }

  fun withReason(context: CommandContext<TCommandSource>): Int {
    execute(
      context.source, arrayOf(
        context.getArgument("target"),
        context.getArgument("duration"),
        context.getArgument("reason")
      )
    )
    return 1
  }
}
