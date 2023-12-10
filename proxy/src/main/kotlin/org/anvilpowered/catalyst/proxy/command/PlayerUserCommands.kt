/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
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

package org.anvilpowered.catalyst.proxy.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import kotlin.jvm.optionals.getOrNull
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.kbrig.argument.StringArgumentType
import org.anvilpowered.kbrig.builder.ArgumentBuilder.Companion.required
import org.anvilpowered.kbrig.builder.RequiredArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.context.get


fun ProxyServer.playerArgument(
    argumentName: String = "player",
    command: (context: CommandContext<CommandSource>, player: Player) -> Int,
): RequiredArgumentBuilder<CommandSource, String> =
    required<CommandSource, String>(argumentName, StringArgumentType.SingleWord)
        .suggests { _, builder ->
            allPlayers.forEach { player -> builder.suggest(player.username) }
            builder.build()
        }
        .executes { context ->
            val playerName = context.get<String>(argumentName)
            getPlayer(playerName).getOrNull()?.let { velocityPlayer ->
                command(context, velocityPlayer)
            } ?: run {
                context.source.sendMessage(
                    Component.text()
                        .append(Component.text("Player with name ", NamedTextColor.RED))
                        .append(Component.text(playerName, NamedTextColor.GOLD))
                        .append(Component.text(" not found!", NamedTextColor.RED)),
                )
                0
            }
        }
