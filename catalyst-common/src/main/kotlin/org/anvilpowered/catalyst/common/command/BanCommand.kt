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
import org.anvilpowered.registry.Registry

class BanCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val memberManager: MemberManager,
    private val permissionService: PermissionService,
    private val pluginMessages: PluginMessages,
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>
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
        val userName = args[0]
        if (userService[userName] != null) {
            if (permissionService.hasPermission(userService[userName], registry.getOrDefault(CatalystKeys.BAN_EXEMPT_PERMISSION))) {
                pluginMessages.banExempt.sendTo(source)
                return
            }
        }
        if (args.size == 1) {
            memberManager.ban(userName).thenAcceptAsync { it.sendTo(source) }
        } else {
            val reason = java.lang.String.join(" ", *args).replace("$userName ", " ")
            memberManager.ban(userName, reason).thenAcceptAsync { it.sendTo(source) }
        }
    }

    fun withReason(context: CommandContext<TCommandSource>): Int {
        execute(context.source, arrayOf(context.getArgument("target", String::class.java), context.getArgument("reason", String::class.java)))
        return 1
    }

    fun withoutReason(context: CommandContext<TCommandSource>): Int {
        execute(context.source, arrayOf(context.getArgument("target", String::class.java)))
        return 1
    }

}
