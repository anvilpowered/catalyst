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
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys

class UnBanCommand<TString, TCommandSource> @Inject constructor(
  private val permissionService: PermissionService,
  private val textService: TextService<TString, TCommandSource>,
  private val pluginMessages: PluginMessages<TString>,
  private val memberManager: MemberManager<TString>, 
  private val registry: Registry,
){
  fun execute(source: TCommandSource, args: Array<String>) {
    if (!permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.BAN_PERMISSION))) {
      textService.send(pluginMessages.noPermission, source)
      return
    }
    if (args.isEmpty()) {
      textService.send(pluginMessages.notEnoughArgs, source)
      textService.send(pluginMessages.unbanCommandUsage(), source)
      return
    }
    memberManager.unBan(args[0]).thenAcceptAsync { textService.send(it, source) }
  }

  fun unban(context: CommandContext<TCommandSource>): Int {
    execute(context.source, arrayOf(context.getArgument("target")))
    return 1
  }
}
