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


package org.anvilpowered.catalyst.bungee.service;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.md_5.bungee.api.CommandSender;
import org.anvilpowered.anvil.api.registry.Registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BungeeCommandDispatcher {

    protected final CommandDispatcher<CommandSender> commandDispatcher = new CommandDispatcher<>();

    protected final Map<String, List<String>> commands = new HashMap<>();

    @Inject
    public BungeeCommandDispatcher(Registry registry) {

    }

    public void register(String command, LiteralCommandNode<CommandSender> node, List<String> alias) {
        commands.put(command, alias);
        commandDispatcher.getRoot().addChild(node);
    }

    /*
    Custom command handling to properly dispatch brigadier commands.
     */
    public void execute(String input, CommandSender sender) throws CommandSyntaxException {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(sender);
        String command = input.split("\\s+")[0];

        for (String internal : commands.keySet()) {
            if (internal.equalsIgnoreCase(command)) {
                commandDispatcher.execute(input, sender);
                return;
            }
        }
        // Check for aliases
        for (Map.Entry<String, List<String>> entry : commands.entrySet()) {
            if (entry.getValue().contains(command)) {
                commandDispatcher.execute(input.replace(command, entry.getKey()), sender);
            }
        }
    }
}
