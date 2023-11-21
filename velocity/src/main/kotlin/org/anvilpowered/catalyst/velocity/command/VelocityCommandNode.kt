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

package org.anvilpowered.catalyst.velocity.command

import com.google.inject.Inject
import com.mojang.brigadier.tree.LiteralCommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.velocity.CatalystApi

context(Registry.Scope, ProxyServerScope)
class VelocityCommandNode : CommonCommandNode<Player, CommandSource>(
    registry,
    Player::class.java,
    ConsoleCommandSource::class.java,
) {

    public override fun loadCommands() {
        // We unregister the command velocity has provided so that ours
        // works properly
        if (registry.getOrDefault(CatalystKeys.SERVER_COMMAND_ENABLED)) {
            proxyServer.commandManager.unregister("server")
        }

        // register commands from CommonCommandNode
        val manager = proxyServer.commandManager
        commands.forEach { (aliases: List<String>, command: LiteralCommandNode<CommandSource>) ->
            val metaBuilder = manager.metaBuilder(aliases[0])
            // Skipping first entry as it is already defined
            for (i in 1 until aliases.size) {
                metaBuilder.aliases(aliases[i])
            }
            manager.register(metaBuilder.build(), BrigadierCommand(command))
        }
    }
}
