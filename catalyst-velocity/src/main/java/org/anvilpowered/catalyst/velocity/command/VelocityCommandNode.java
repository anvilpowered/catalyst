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

package org.anvilpowered.catalyst.velocity.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.TextComponent;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.common.command.CommonCommandNode;

@Singleton
public class VelocityCommandNode
    extends CommonCommandNode<TextComponent, Player, CommandSource> {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private VelocityListCommand listCommand;

    @Inject
    public VelocityCommandNode(Registry registry) {
        super(registry, Player.class, ConsoleCommandSource.class);
    }

    @Override
    public void loadCommands() {
        // We unregister the command velocity has provided so that ours
        // works properly
        if (registry.getOrDefault(CatalystKeys.SERVER_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().unregister("server");
        }

        //register commands from CommonCommandNode
        CommandManager manager = proxyServer.getCommandManager();
        commands.forEach((aliases, command) -> {
            CommandMeta.Builder metaBuilder = manager.metaBuilder(aliases.get(0));
            //Skipping first entry as it is already defined
            for (int i = 1; i < aliases.size(); i++) {
                metaBuilder.aliases(aliases.get(i));
            }
            manager.register(metaBuilder.build(), new BrigadierCommand(command));
        });

        if (registry.getOrDefault(CatalystKeys.LIST_COMMAND_ENABLED)) {
            proxyServer.getCommandManager().register(
                "list", listCommand, "clist");
        }
    }
}
