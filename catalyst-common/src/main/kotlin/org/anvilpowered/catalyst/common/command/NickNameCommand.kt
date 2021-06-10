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
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys

class NickNameCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val memberManager: MemberManager<TString>,
  private val pluginMessages: PluginMessages<TString>,
  private val registry: Registry,
  private val permissionService: PermissionService,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>
){
  
  fun execute(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
    var nick = context.getArgument("nickname", String::class.java)
    if (!playerClass.isAssignableFrom(context.source!!::class.java)) {
      textService.send(textService.of("Player only command!"), context.source)
      return 1
    }
    if (nick.contains("&")) {
      if (permissionService.hasPermission(
          context.source,
          registry.getOrDefault(CatalystKeys.NICKNAME_COLOR_PERMISSION)
        )
      ) {
        if (!permissionService.hasPermission(
            context.source,
            registry.getOrDefault(CatalystKeys.NICKNAME_MAGIC_PERMISSION)
          ) && nick.contains("&k")
        ) {
          textService.send(pluginMessages.noNickMagicPermission, context.source)
          nick = nick.replace("&k".toRegex(), "")
        }
      } else {
        nick = textService.serializePlain(textService.of(nick))
        textService.send(pluginMessages.noNickColorPermission, context.source)
      }
    }
    memberManager.setNickName(userService.getUserName(context.source as TPlayer), nick)
      .thenAcceptAsync { m: TString -> textService.send(m, context.source) }
    return 1
  }

  fun executeOther(context: CommandContext<TCommandSource>): Int {
    memberManager.setNickNameForUser(
      context.getArgument("target", String::class.java),
      context.getArgument("targetnick", String::class.java)
    ).thenAcceptAsync { textService.send(it, context.source) }
    return 1
  }
}
