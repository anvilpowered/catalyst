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

class TempMuteCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val permissionService: PermissionService,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>,
  private val pluginMessages: PluginMessages<TString>,
  private val memberManager: MemberManager<TString>,
  private val registry: Registry
){

  fun execute(context: CommandContext<TCommandSource>, reason: String): Int {
    val userName = context.getArgument("target", String::class.java)
    val duration = context.getArgument("duration", String::class.java)
    if (userService[userName].isPresent) {
      if (permissionService.hasPermission(
          userService[userName].get(),
          registry.getOrDefault(CatalystKeys.MUTE_EXEMPT_PERMISSION)
        )
      ) {
        textService.send(pluginMessages.muteExempt, context.source)
        return 0
      }
    }
    memberManager.tempMute(userName, duration, reason).thenAcceptAsync { textService.send(it, context.source) }
    return 1
  }

  fun withReason(context: CommandContext<TCommandSource>): Int {
    return execute(context, context.getArgument("reason", String::class.java))
  }

  fun withoutReason(context: CommandContext<TCommandSource>): Int {
    return execute(context, "You have been muted temporarily.")
  }
}
