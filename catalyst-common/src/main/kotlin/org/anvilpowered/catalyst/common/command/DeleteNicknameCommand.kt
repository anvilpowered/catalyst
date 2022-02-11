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
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.member.MemberManager

class DeleteNicknameCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val memberManager: MemberManager,
    private val userService: UserService<TPlayer, TPlayer>
) {

    fun execute(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
        if (!playerClass.isAssignableFrom(context.source!!::class.java)) {
            Component.text("Player only command!").sendTo(context.source)
            return 0
        }
        memberManager.deleteNickName(userService.getUserName(context.source as TPlayer)).thenAcceptAsync { it.sendTo(context.source) }
        return 1
    }

    fun executeOther(context: CommandContext<TCommandSource>): Int {
        memberManager.deleteNickNameForUser(context.getArgument("target")).thenAcceptAsync { it.sendTo(context.source) }
        return 1
    }
}
