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
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.KickService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys

class KickCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val pluginMessages: PluginMessages,
    private val registry: Registry,
    private val kickService: KickService,
    private val permissionService: PermissionService,
    private val userService: UserService<TPlayer, TPlayer>
) {

    fun execute(context: CommandContext<TCommandSource>): Int {
        val player = userService.getPlayer(context.getArgument<String>("target"))
        return when {
            player == null -> {
                pluginMessages.offlineOrInvalidPlayer().sendTo(context.source)
                1
            }
            permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.KICK_EXEMPT_PERMISSION)) -> {
                pluginMessages.kickExempt.sendTo(context.source)
                0
            }
            else -> {
                kickService.kick(context.getArgument<String>("target"), context.getArgument<String>("reason"))
                1
            }
        }
    }

    fun withoutReason(context: CommandContext<TCommandSource>): Int {
        val player = userService.getPlayer(context.getArgument<String>("target"))
        return when {
            player == null -> {
                pluginMessages.offlineOrInvalidPlayer().sendTo(context.source)
                1
            }
            permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.KICK_EXEMPT_PERMISSION)) -> {
                pluginMessages.kickExempt.sendTo(context.source)
                0
            }
            else -> {
                kickService.kick(userService.getUUID(player)!!, "You have been kicked!")
                1
            }
        }
    }
}
