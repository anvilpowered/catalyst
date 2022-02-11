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
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.registry.Registry

class ToggleProxyChat<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val chatService: ChatService<TPlayer, TCommandSource>,
    private val permissionService: PermissionService,
    private val pluginMessages: PluginMessages,
    private val registry: Registry,
    private val pluginInfo: PluginInfo
) {
    fun execute(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
        if (!permissionService.hasPermission(context.source, registry.getOrDefault(CatalystKeys.TOGGLE_CHAT_PERMISSION))) {
            pluginMessages.noPermission.sendTo(context.source)
            return 0
        }
        if (!playerClass.isAssignableFrom(context.source!!::class.java)) {
            Component.text("Player only command!").sendTo(context.source)
            return 0
        }
        val player = context.source as TPlayer
        if (chatService.isDisabledForUser(player)) {
            chatService.toggleChatForUser(player)
            sendToggle(context.source, true)
            return 1
        }
        chatService.toggleChatForUser(player)
        sendToggle(context.source, false)
        return 1
    }

    private fun sendToggle(source: TCommandSource, enabled: Boolean) {
        val setting = if (enabled) "enabled" else "disabled"
        Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("Proxy-Wide chat has been ").color(NamedTextColor.GREEN))
            .append(Component.text(setting).color(NamedTextColor.GOLD))
            .append(Component.text("\nNote: This only affects you.").color(NamedTextColor.YELLOW))
            .sendTo(source)
    }
}
