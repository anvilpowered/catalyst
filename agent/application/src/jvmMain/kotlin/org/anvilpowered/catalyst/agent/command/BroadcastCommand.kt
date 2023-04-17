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

package org.anvilpowered.catalyst.agent.command

import org.anvilpowered.anvil.user.CommandSource
import org.anvilpowered.anvil.user.Component
import org.anvilpowered.anvil.user.hasPermissionSet
import org.anvilpowered.catalyst.agent.PluginMessages
import org.anvilpowered.catalyst.agent.chat.BroadcastScope
import org.anvilpowered.kbrig.argument.StringArgumentType
import org.anvilpowered.kbrig.builder.LiteralArgumentBuilder
import org.anvilpowered.kbrig.builder.RequiredArgumentBuilder
import org.anvilpowered.kbrig.context.get
import org.anvilpowered.kbrig.tree.LiteralCommandNode

context(BroadcastScope)
fun CatalystCommand.createBroadcast(): LiteralCommandNode<CommandSource> {
    return LiteralArgumentBuilder<CommandSource>("broadcast")
        .executes(::notEnoughArgs)
        .requires { it.hasPermissionSet("catalyst.chat.broadcast") }
        .then(
            RequiredArgumentBuilder<CommandSource, String>("message", StringArgumentType.GreedyPhrase)
                .executes { context ->
                    broadcast(PluginMessages.getBroadcast(Component.text(context.get<String>("message"))))
                    1
                }
                .build(),
        )
        .build()
}
