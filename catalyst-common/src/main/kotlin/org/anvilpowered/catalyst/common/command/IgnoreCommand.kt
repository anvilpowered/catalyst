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
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys.IGNORE_EXEMPT_PERMISSION
import org.anvilpowered.catalyst.api.service.ChatService
import org.slf4j.Logger

class IgnoreCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val registry: Registry,
  private val pluginMessages: PluginMessages<TString>,
  private val chatService: ChatService<TString, TPlayer, TCommandSource>,
  private val permissionService: PermissionService,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>,
  private val logger: Logger
){

  fun execute(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
    val targetPlayer = userService.getPlayer(context.getArgument("target", String::class.java))
    if (!playerClass.isAssignableFrom(context.source!!::class.java)) {
      logger.error("Player only command!")
      return 0
    }
    if (!targetPlayer.isPresent) {
      textService.send(pluginMessages.offlineOrInvalidPlayer(), context.source)
      return 0
    }
    if (permissionService.hasPermission(targetPlayer.get(), registry.getOrDefault(IGNORE_EXEMPT_PERMISSION))) {
      textService.send(pluginMessages.ignoreExempt(), context.source)
      return 0
    }
    textService.send(
      chatService.ignore(
        userService.getUUID(context.source as TPlayer),
        userService.getUUID(targetPlayer.get())
      ),
      context.source
    )
    return 1
  }
}
