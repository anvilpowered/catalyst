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
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.catalyst.api.service.StaffListService

class StaffListCommand<TCommandSource> @Inject constructor(
    private val pluginInfo: PluginInfo,
    private val staffListService: StaffListService,
) {
    fun execute(context: CommandContext<TCommandSource>): Int {
        if (isStaffOnline) {
            line.sendTo(context.source)
            if (staffListService.staffNames().isNotEmpty()) {
                Component.text("Staff: ").color(NamedTextColor.GOLD).sendTo(context.source)
                for (name: Component in staffListService.staffNames()) {
                    name.sendTo(context.source)
                }
            }
            if (staffListService.adminNames().isNotEmpty()) {
                Component.text("Admin: ").color(NamedTextColor.GOLD).sendTo(context.source)
                for (name: Component in staffListService.adminNames()) {
                    name.sendTo(context.source)
                }
            }
            if (staffListService.ownerNames().isNotEmpty()) {
                Component.text("Owner: ").color(NamedTextColor.GOLD).sendTo(context.source)
                for (name: Component in staffListService.ownerNames()) {
                    name.sendTo(context.source)
                }
            }
            line.sendTo(context.source)
        } else {
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("There are no staff members online!").color(NamedTextColor.YELLOW))
                .sendTo(context.source)
        }
        return 1
    }

    private val isStaffOnline: Boolean
        get() = (staffListService.staffNames().isNotEmpty()
            || staffListService.adminNames().isNotEmpty()
            || staffListService.ownerNames().isNotEmpty())
    private val line: Component by lazy {
        Component.text("-----------------------------------------------------").color(NamedTextColor.DARK_AQUA)
    }
}
