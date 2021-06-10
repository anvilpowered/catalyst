/*
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.catalyst.common.command

import com.google.inject.Inject
import com.mojang.brigadier.context.CommandContext
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.service.PrivateMessageService

class SocialSpyCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val pluginMessages: PluginMessages<TString>,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>,
  private val privateMessageService: PrivateMessageService<TString>
) {

  fun execute(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
    if (!playerClass.isAssignableFrom(context.source!!::class.java)) {
      textService.send(textService.of("Player only command!"), context.source)
      return 0
    }
    val playerUUID = userService.getUUID(context.source as TPlayer)
    if (privateMessageService.socialSpySet().contains(playerUUID)) {
      privateMessageService.socialSpySet().remove(playerUUID)
      textService.send(pluginMessages.getSocialSpy(false), context.source)
    } else {
      privateMessageService.socialSpySet().add(playerUUID)
      textService.send(pluginMessages.getSocialSpy(true), context.source)
    }
    return 1
  }
}
