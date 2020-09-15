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

package org.anvilpowered.catalyst.bungee.command;

import com.google.inject.Inject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.bungee.CatalystBungee;
import org.anvilpowered.catalyst.common.command.CommonCommandNode;

public class BungeeCommandNode
    extends CommonCommandNode<TextComponent, ProxiedPlayer, CommandSender> {


    @Inject
    private BungeeIgnoreCommand ignoreCommand;

    @Inject
    private BungeeListCommand listCommand;

    @Inject
    private CatalystBungee plugin;

    @Inject
    public BungeeCommandNode(Registry registry) {
        super(registry, ProxiedPlayer.class);
    }

    @Override
    public void loadCommands() {
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        //There is little to 0 documentation on registering brigadier commands
        //This may need to be changed
        commands.forEach(command -> pluginManager.registerCommand(plugin,
            (net.md_5.bungee.api.plugin.Command) command.getCommand()));
        pluginManager.registerCommand(plugin, ignoreCommand);
        pluginManager.registerCommand(plugin, listCommand);
    }
}
