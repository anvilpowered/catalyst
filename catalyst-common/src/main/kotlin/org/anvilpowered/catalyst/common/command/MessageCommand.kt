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
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.service.PrivateMessageService

class MessageCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val pluginMessages: PluginMessages,
    private val privateMessageService: PrivateMessageService,
    private val userService: UserService<TPlayer, TPlayer>,
) {

    fun execute(context: CommandContext<TCommandSource>, consoleClass: Class<*>): Int {
        val name = context.getArgument<String>("target")
        if (userService.getPlayer(name) != null) {
            val message = context.getArgument<String>("message")
            val targetPlayer = userService[name]
            if (consoleClass.isAssignableFrom(context.source!!::class.java) && targetPlayer != null) {
                privateMessageService.sendMessageFromConsole(userService.getUserName(targetPlayer), message)
                return 1
            }
            if (targetPlayer != null) {
                if (targetPlayer == context.source) {
                    pluginMessages.messageSelf().sendTo(context.source)
                    return 0
                }
                privateMessageService.sendMessage(userService.getUserName(context.source as TPlayer), name, message)
                privateMessageService.replyMap[userService.getUUID(targetPlayer)!!] = userService.getUUID(context.source as TPlayer)!!
                privateMessageService.replyMap[userService.getUUID(context.source as TPlayer)!!] = userService.getUUID(targetPlayer)!!
            } else {
                pluginMessages.offlineOrInvalidPlayer().sendTo(context.source)
            }
        }
        return 1
    }
}
