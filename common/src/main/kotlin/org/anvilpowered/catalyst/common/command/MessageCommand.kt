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
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.service.PrivateMessageService

class MessageCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val pluginMessages: PluginMessages<TString>,
  private val privateMessageService: PrivateMessageService<TString>,
  private val userService: UserService<TPlayer, TPlayer>,
  private val textService: TextService<TString, TCommandSource>
) {

  fun execute(context: CommandContext<TCommandSource>, consoleClass: Class<*>): Int {
    val name = context.getArgument<String>("target")
    if (userService.getPlayer(name).isPresent) {
      val message = context.getArgument<String>("message")
      val targetPlayer = userService[name]
      if (consoleClass.isAssignableFrom(context.source!!::class.java) && targetPlayer.isPresent) {
        privateMessageService.sendMessageFromConsole(userService.getUserName(targetPlayer.get()), message, consoleClass)
        return 1
      }
      if (targetPlayer.isPresent) {
        if (targetPlayer.get() == context.source) {
          textService.send(pluginMessages.messageSelf(), context.source)
          return 0
        }
        privateMessageService.sendMessage(userService.getUserName(context.source as TPlayer), name, message)
        privateMessageService.replyMap()[userService.getUUID(targetPlayer.get())] = userService.getUUID(context.source as TPlayer)
        privateMessageService.replyMap()[userService.getUUID(context.source as TPlayer)] = userService.getUUID(targetPlayer.get())
      } else {
        textService.send(pluginMessages.offlineOrInvalidPlayer(), context.source)
      }
    }
    return 1
  }
}
