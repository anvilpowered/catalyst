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

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.agent.command.CustomCommandScope
import org.anvilpowered.anvil.user.CommandSource
import org.anvilpowered.anvil.user.Player
import org.anvilpowered.catalyst.agent.PluginMessages
import org.anvilpowered.catalyst.agent.command.common.addHelpChild
import org.anvilpowered.catalyst.entity.CatalystUser
import org.anvilpowered.catalyst.service.CatalystUserScope
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.builder.executesSingleSuccess
import org.anvilpowered.kbrig.tree.LiteralCommandNode

context(CatalystUserScope.Nickname, CustomCommandScope)
fun NicknameCommand.createDelete(): LiteralCommandNode<CommandSource> {
    return ArgumentBuilder.literal<CommandSource>("delete")
        .addHelpChild("nickname|nick delete [<player>]")
        .executesSingleSuccess { context ->
            // without explicit target player
            (context.source as? Player)?.let { player ->
                if (CatalystUser.deleteNickname(player.user.id)) {
                    player.sendMessage(
                        PluginMessages.pluginPrefix
                            .append(Component.text("Your nickname has been deleted!", NamedTextColor.RED)),
                    )
                } else {
                    player.sendMessage(
                        PluginMessages.pluginPrefix
                            .append(Component.text("You don't have a nickname!", NamedTextColor.RED)),
                    )
                }
            } ?: run {
                context.source.sendMessage(
                    PluginMessages.pluginPrefix
                        .append(Component.text("You must be a player to use this command!", NamedTextColor.RED)),
                )
            }
        }
        .then(ArgumentBuilder.player { context, targetPlayer ->

        }

        )

        .build()
}
