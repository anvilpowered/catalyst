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
import org.anvilpowered.anvil.api.misc.Named
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages

class FindCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject  constructor(
  private val pluginMessages: PluginMessages<TString>,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>,
  private val locationService: LocationService
){
  fun execute(context: CommandContext<TCommandSource>): Int {
    val userName = context.getArgument("target", String::class.java)
    if (userService.getPlayer(userName).isPresent) {
      textService.send(
        pluginMessages.getCurrentServer(
          userName,
          locationService.getServer(userName).map { obj: Named -> obj.name }.orElse("null")
        ),
        context.source
      )
      return 1
    }
    textService.send(pluginMessages.offlineOrInvalidPlayer(), context.source)
    return 1
  }
}
