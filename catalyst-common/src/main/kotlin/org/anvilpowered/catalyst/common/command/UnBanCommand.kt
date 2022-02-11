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
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.registry.Registry

class UnBanCommand<TCommandSource> @Inject constructor(
    private val permissionService: PermissionService,
    private val pluginMessages: PluginMessages,
    private val memberManager: MemberManager,
    private val registry: Registry,
) {
    fun execute(source: TCommandSource, args: Array<String>) {
        if (!permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.BAN_PERMISSION))) {
            pluginMessages.noPermission.sendTo(source)
            return
        }
        if (args.isEmpty()) {
            pluginMessages.notEnoughArgs.sendTo(source)
            return
        }
        memberManager.unBan(args[0]).thenAcceptAsync { it.sendTo(source) }
    }

    fun unban(context: CommandContext<TCommandSource>): Int {
        execute(context.source, arrayOf(context.getArgument("target")))
        return 1
    }
}
