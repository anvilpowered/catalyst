/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msrepository.api.util.PluginInfo;

public class ServerCommand implements Command {

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    private RegisteredServer registeredServer;

    public void setRegisteredServer(String serverName) {
        if (proxyServer.getServer(serverName).isPresent()) {
            this.registeredServer = proxyServer.getServer(serverName).get();
        }
    }

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            if (player.hasPermission("msessentials.server.join." + registeredServer.getServerInfo().getName())) {
                if (registeredServer.ping().join().getVersion().getName().equals(player.getProtocolVersion().getName())) {
                    player.createConnectionRequest(registeredServer).connect().thenAcceptAsync(connection -> {
                        if (connection.isSuccessful()) {
                            player.sendMessage(pluginInfo.getPrefix().append(TextComponent.of("Connected to server " + registeredServer.getServerInfo().getName())));
                        } else {
                            if (player.getCurrentServer().map(s -> s.getServerInfo().getName().equals(registeredServer.getServerInfo().getName())).orElse(false)) {
                                player.sendMessage(pluginInfo.getPrefix().append(TextComponent.of("You are already connected to " + registeredServer.getServerInfo().getName())));
                            } else {
                                player.sendMessage(pluginInfo.getPrefix().append(TextComponent.of("Failed to connect to " + registeredServer.getServerInfo().getName())));
                            }
                        }
                    });
                } else {
                    source.sendMessage(pluginMessages.getIncompatibleServerVersion());
                }
            } else {
                player.sendMessage(pluginMessages.getNoPermission());
            }
        }
    }
}
