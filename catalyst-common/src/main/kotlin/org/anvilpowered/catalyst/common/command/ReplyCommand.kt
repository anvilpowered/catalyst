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
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.service.PrivateMessageService

class ReplyCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val pluginMessages: PluginMessages,
    private val userService: UserService<TPlayer, TPlayer>,
    private val privateMessageService: PrivateMessageService
) {

    fun execute(context: CommandContext<TCommandSource>): Int {
        val message = context.getArgument<String>("message")
        val senderUUID = userService.getUUID(context.source as TPlayer)
        if (privateMessageService.replyMap().containsKey(senderUUID)) {
            val recipientUUID = privateMessageService.replyMap()[senderUUID] ?: return 0
            val recipient = userService.getPlayer(recipientUUID)
            if (recipient != null) {
                privateMessageService.sendMessage(userService.getUserName(context.source as TPlayer), userService.getUserName(recipient), message)
                privateMessageService.replyMap()[userService.getUUID(recipient)] = userService.getUUID(context.source as TPlayer)
            } else {
                pluginMessages.offlineOrInvalidPlayer().sendTo(context.source)
            }
        } else {
            Component.text("Nobody to reply to!").color(NamedTextColor.RED).sendTo(context.source)
        }
        return 1
    }
}
