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
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.anvil.api.registry.Registry

class TempBanCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val permissionService: PermissionService,
    private val userService: UserService<TPlayer, TPlayer>,
    private val pluginMessages: PluginMessages,
    private val memberManager: MemberManager,
    private val registry: Registry
) {
    fun execute(source: TCommandSource, args: Array<String>) {
        if (!permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.TEMP_BAN_PERMISSION))) {
            pluginMessages.noPermission.sendTo(source)
            return
        }
        if (args.size < 2) {
            pluginMessages.notEnoughArgs.sendTo(source)
            return
        }
        val userName = args[0]
        val duration = args[1]
        if (userService[userName] != null) {
            if (permissionService.hasPermission(userService[userName], registry.getOrDefault(CatalystKeys.BAN_EXEMPT_PERMISSION))) {
                pluginMessages.banExempt.sendTo(source)
                return
            }
        }
        if (args.size == 2) {
            memberManager.tempBan(userName, duration).thenAcceptAsync { it.sendTo(source) }
        } else {
            val reason = java.lang.String.join(" ", *args).replace("$userName ", "").replace(duration, "")
            memberManager.tempBan(userName, duration, reason).thenAcceptAsync { it.sendTo(source) }
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
