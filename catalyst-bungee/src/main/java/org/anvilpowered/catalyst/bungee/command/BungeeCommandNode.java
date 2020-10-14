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
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.bungee.CatalystBungee;
import org.anvilpowered.catalyst.bungee.service.BungeeCommandDispatcher;
import org.anvilpowered.catalyst.common.command.CommonCommandNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BungeeCommandNode
    extends CommonCommandNode<TextComponent, ProxiedPlayer, CommandSender> {

    @Inject
    private BungeeCommandDispatcher commandDispatcher;

    @Inject
    private CatalystBungee plugin;

    @Inject
    public BungeeCommandNode(Registry registry) {
        super(registry, ProxiedPlayer.class, ProxyServer.getInstance().getConsole().getClass());
    }

    @Override
    public void loadCommands() {
        //There is little to 0 documentation on registering brigadier commands
        //This may need to be changed
        for (Map.Entry<List<String>, LiteralCommandNode<CommandSender>> entry : commands.entrySet()) {
            List<String> k = entry.getKey();
            LiteralCommandNode<CommandSender> v = entry.getValue();
            List<String> withoutFirst = new ArrayList<>(k);
            commandDispatcher.register(k.get(0), v, withoutFirst);
            withoutFirst.remove(0);
            ProxyServer.getInstance().getPluginManager()
                .registerCommand(plugin, new Command(k.get(0), "", withoutFirst.toArray(new String[0])) {
                    @Override
                    public void execute(CommandSender sender, String[] args) {
                        try {
                            commandDispatcher.execute(Arrays.toString(args), sender);
                        } catch (CommandSyntaxException ignored) {
                        }
                    }
                });
        }
    }
}
