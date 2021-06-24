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

package org.anvilpowered.catalyst.bungee.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.server.BackendServer;
import org.anvilpowered.anvil.api.server.LocationService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.anvilpowered.catalyst.api.CommandSuggestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BungeeCommand extends Command implements TabExecutor {

    private final AdvancedServerInfoService advancedServerInfo;
    private final LocationService locationService;
    private final Registry registry;
    private final LiteralCommandNode<CommandSender> commandNode;
    private Map<Integer, CommandSuggestionType> suggestionPosition;

    public BungeeCommand(
        String name,
        String[] aliases,
        LiteralCommandNode<CommandSender> commandNode,
        Registry registry,
        AdvancedServerInfoService advancedServerInfo,
        LocationService locationService
    ) {
        super(name, "", aliases);
        this.commandNode = commandNode;
        this.registry = registry;
        this.advancedServerInfo = advancedServerInfo;
        this.locationService = locationService;
    }


    /**
     * This execute method is being handled by the {@link org.anvilpowered.catalyst.bungee.service.BungeeCommandDispatcher}
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        for (Map.Entry<Integer, CommandSuggestionType> entry : suggestionPosition.entrySet()) {
            Integer position = entry.getKey();
            if (args.length == position) {
                switch (entry.getValue()) {
                    case SERVER:
                        List<String> servers = new ArrayList<>();
                        if (registry.getOrDefault(CatalystKeys.INSTANCE.getADVANCED_SERVER_INFO_ENABLED())) {
                            String prefix = advancedServerInfo.getPrefixForPlayer(sender.getName());
                            for (BackendServer server : locationService.getServers()) {
                                if (server.getName().startsWith(prefix)) {
                                    servers.add(server.getName().replace(prefix, ""));
                                }
                            }
                        } else {
                            for (BackendServer server : locationService.getServers()) {
                                if (server.getName().toLowerCase().startsWith(args[entry.getKey() - 1].toLowerCase())) {
                                    servers.add(server.getName());
                                }
                            }
                        }
                        return servers;
                    case PLAYER:
                        List<String> players = new ArrayList<>();
                        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                            if (player.getDisplayName().toLowerCase().startsWith(args[entry.getKey() - 1].toLowerCase())) {
                                players.add(player.getName());
                            }
                        }
                        return players;
                }
            }
        }
        return ImmutableList.of();
    }

    void setSuggestions(Map<Integer, CommandSuggestionType> suggestions) {
        this.suggestionPosition = suggestions;
    }


    boolean compareNode(LiteralCommandNode<CommandSender> node) {
        return this.commandNode.equals(node);
    }
}
