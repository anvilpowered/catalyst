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

import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BungeeCommand extends Command implements TabExecutor {

    private final String type;
    private final Map<String, Integer> additionalArguments;

    public BungeeCommand(String name,
                         String[] aliases,
                         String type,
                         Map<String, Integer> additionalArguments) {
        super(name, "", aliases);
        this.type = type;
        this.additionalArguments = additionalArguments;
    }

    /**
     * This execute method is being handled by the {@link org.anvilpowered.catalyst.bungee.service.BungeeCommandDispatcher}
     *
     * @param sender
     * @param args
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if ((args.length >= 2 && !args[0].equals(""))) {
            for (String suggestion : additionalArguments.keySet()) {
                if (additionalArguments.get(suggestion).equals(args.length - 1)) {
                    switch (suggestion) {
                        case "player":
                            return getPlayers();
                        case "reason":
                            return ImmutableList.of("reason");
                        case "duration":
                            return ImmutableList.of("duration");
                        case "server":
                            return getServers();
                    }
                    return ImmutableList.of(suggestion);
                }
            }
            return ImmutableList.of("");
        }
        switch (type) {
            case "player":
                return getPlayers();
            case "server":
                return getServers();
            default:
                return ImmutableList.of("Invalid command usage.");
        }
    }

    protected List<String> getPlayers() {
        List<String> playerNames = new ArrayList<>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            playerNames.add(player.getDisplayName());
        }
        return playerNames;
    }

    protected List<String> getServers() {
        List<String> servers = new ArrayList<>();
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            servers.add(server.getName());
        }
        return servers;
    }
}
