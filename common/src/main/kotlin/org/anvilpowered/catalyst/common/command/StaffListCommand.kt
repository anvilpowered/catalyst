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
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.catalyst.api.service.StaffListService

class StaffListCommand<TString, TCommandSource>@Inject constructor(
  private val pluginInfo: PluginInfo<TString>,
  private val staffListService: StaffListService<TString>,
  private val textService: TextService<TString, TCommandSource>
){
  fun execute(context: CommandContext<TCommandSource>): Int {
    if (isStaffOnline) {
      textService.send(line, context.source)
      if (staffListService.staffNames().isNotEmpty()) {
        textService.send(textService.builder().append("Staff:").gold().build(), context.source)
        for (tString: TString in staffListService.staffNames()) {
          textService.send(tString, context.source)
        }
      }
      if (staffListService.adminNames().isNotEmpty()) {
        textService.send(textService.builder().append("Admin:").gold().build(), context.source)
        for (tString: TString in staffListService.adminNames()) {
          textService.send(tString, context.source)
        }
      }
      if (staffListService.ownerNames().isNotEmpty()) {
        textService.send(textService.builder().append("Owner:").gold().build(), context.source)
        for (tString: TString in staffListService.ownerNames()) {
          textService.send(tString, context.source)
        }
      }
      textService.send(line, context.source)
    } else {
      textService.send(
        textService.builder()
          .append(pluginInfo.prefix)
          .append("There are no staff members online!")
          .build(),
        context.source
      )
    }
    return 1
  }

  private val isStaffOnline: Boolean
    get() = (staffListService.staffNames().isNotEmpty()
      || staffListService.adminNames().isNotEmpty()
      || staffListService.ownerNames().isNotEmpty())
  private val line: TString
    get() {
      return textService.builder()
        .append("-----------------------------------------------------")
        .dark_aqua()
        .build()
    }
}
