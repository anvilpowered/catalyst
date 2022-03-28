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
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.anvil.api.registry.Registry

class NickNameCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val memberManager: MemberManager,
    private val pluginMessages: PluginMessages,
    private val registry: Registry,
    private val permissionService: PermissionService,
    private val userService: UserService<TPlayer, TPlayer>
) {

    fun execute(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
        var nick = context.getArgument<String>("nickname")
        if (!playerClass.isAssignableFrom(context.source!!::class.java)) {
            Component.text("Player only command!").sendTo(context.source)
            return 1
        }
        if (nick.contains("&")) {
            if (permissionService.hasPermission(context.source, registry.getOrDefault(CatalystKeys.NICKNAME_COLOR_PERMISSION))) {
                if (!permissionService.hasPermission(context.source, registry.getOrDefault(CatalystKeys.NICKNAME_MAGIC_PERMISSION))
                    && nick.contains("&k")
                ) {
                    pluginMessages.noNickMagicPermission.sendTo(context.source)
                    nick = nick.replace("&k".toRegex(), "")
                }
            } else {
                nick = PlainTextComponentSerializer.plainText().serialize(Component.text(nick))
                pluginMessages.noNickColorPermission.sendTo(context.source)
            }
        }
        memberManager.setNickName(userService.getUserName(context.source as TPlayer), nick)
            .thenAcceptAsync { it.sendTo(context.source) }
        return 1
    }

    fun executeOther(context: CommandContext<TCommandSource>): Int {
        memberManager.setNickNameForUser(context.getArgument<String>("target"), context.getArgument<String>("targetnick"))
            .thenAcceptAsync { it.sendTo(context.source) }
        return 1
    }
}
