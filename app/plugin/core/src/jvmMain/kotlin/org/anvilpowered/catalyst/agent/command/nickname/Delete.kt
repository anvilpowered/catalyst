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
import org.anvilpowered.anvil.domain.command.CommandSource
import org.anvilpowered.anvil.domain.command.GameUserCommandScope
import org.anvilpowered.anvil.domain.user.hasPermissionSet
import org.anvilpowered.catalyst.agent.PluginMessages
import org.anvilpowered.catalyst.agent.command.common.addHelpChild
import org.anvilpowered.catalyst.entity.CatalystUser
import org.anvilpowered.catalyst.service.CatalystUserScope
import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.builder.executesSuspending
import org.anvilpowered.kbrig.tree.LiteralCommandNode

context(CatalystUserScope.Nickname, GameUserCommandScope)
fun NicknameCommand.createDelete(): LiteralCommandNode<CommandSource> =
    ArgumentBuilder.literal<CommandSource>("delete")
        .addHelpChild("nickname|nick delete [<player>]")
        .executesSuspending { context ->
            // without explicit target player
            context.source.player?.let { player ->
                if (!player.hasPermissionSet("catalyst.nickname.delete.base")) {
                    player.sendMessage(
                        PluginMessages.pluginPrefix
                            .append(Component.text("You don't have permission to delete your nickname!", NamedTextColor.RED)),
                    )
                    0
                } else if (CatalystUser.deleteNickname(player.user.id)) {
                    player.sendMessage(
                        PluginMessages.pluginPrefix
                            .append(Component.text("Your nickname has been deleted!", NamedTextColor.RED)),
                    )
                    Command.SINGLE_SUCCESS
                } else {
                    player.sendMessage(
                        PluginMessages.pluginPrefix
                            .append(Component.text("You don't have a nickname!", NamedTextColor.RED)),
                    )
                    0
                }
            } ?: run {
                context.source.audience.sendMessage(
                    PluginMessages.pluginPrefix
                        .append(Component.text("You must be a player to use this command without arguments!", NamedTextColor.RED)),
                )
                0
            }
        }
        .then(
            ArgumentBuilder.gameUser { context, gameUser ->
                if (!context.source.permissionSubject.hasPermissionSet("catalyst.nickname.delete.other")) {
                    context.source.audience.sendMessage(
                        PluginMessages.pluginPrefix
                            .append(Component.text("You don't have permission to delete other players' nicknames!", NamedTextColor.RED)),
                    )
                    0
                } else if (CatalystUser.deleteNickname(gameUser.id)) {
                    context.source.audience.sendMessage(
                        PluginMessages.pluginPrefix
                            .append(Component.text("The nickname of ", NamedTextColor.RED))
                            .append(Component.text(gameUser.username, NamedTextColor.GOLD))
                            .append(Component.text(" has been deleted!", NamedTextColor.RED)),
                    )
                    Command.SINGLE_SUCCESS
                } else {
                    context.source.audience.sendMessage(
                        PluginMessages.pluginPrefix
                            .append(Component.text("The player ", NamedTextColor.RED))
                            .append(Component.text(gameUser.username, NamedTextColor.GOLD))
                            .append(Component.text(" doesn't have a nickname!", NamedTextColor.RED)),
                    )
                    0
                }
            },
        )
        .build()
