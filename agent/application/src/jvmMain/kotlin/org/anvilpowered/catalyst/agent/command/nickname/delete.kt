/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.agent.command.nickname

import org.anvilpowered.anvil.user.CommandSource
import org.anvilpowered.catalyst.agent.command.CommandDefaults
import org.anvilpowered.catalyst.service.CatalystUserScope
import org.anvilpowered.kbrig.builder.LiteralArgumentBuilder
import org.anvilpowered.kbrig.builder.executesSingleSuccess
import org.anvilpowered.kbrig.tree.LiteralCommandNode

context(CatalystUserScope.Nickname)
fun NicknameCommand.createDelete(): LiteralCommandNode<CommandSource> {
    return LiteralArgumentBuilder<CommandSource>("delete")
        .executes(CommandDefaults.usage("nickname|nick delete"))
        .build()
}
