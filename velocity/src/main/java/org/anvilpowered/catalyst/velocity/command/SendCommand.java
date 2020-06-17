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
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.text.TextComponent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class SendCommand implements Command {

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Registry registry;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(registry.getOrDefault(CatalystKeys.SEND_PERMISSION))) {
            source.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        if (args.length < 2) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            source.sendMessage(pluginMessages.sendCommandUsage());
            return;
        }

        Optional<Player> player = proxyServer.getPlayer(args[0]);
        if (player.isPresent()) {
            Optional<RegisteredServer> server = proxyServer.getServer(args[1]);
            if (server.isPresent()) {
                if (player.get().getCurrentServer()
                    .map(ServerConnection::getServerInfo).map(ServerInfo::getName)
                    .map(s ->
                        s.equalsIgnoreCase(
                            server.get().getServerInfo().getName())).orElse(false)) {
                    source.sendMessage(
                        pluginInfo.getPrefix().append(
                            TextComponent.of(player.get().getUsername()
                                + " is already connected to that server.")));
                } else {
                    String sender = "Console";
                    if(source instanceof Player) {
                         sender =  ((Player) source).getUsername();
                    }
                    source.sendMessage(
                        pluginInfo.getPrefix().append(
                            TextComponent.of("sending " + player.get().getUsername()
                                + " to " + server.get().getServerInfo().getName())));
                    player.get().sendMessage(
                        pluginInfo.getPrefix().append(
                            TextComponent.of("you have been sent to "
                                + server.get().getServerInfo().getName()
                                + " by " + sender)));
                    player.get().createConnectionRequest(server.get()).fireAndForget();
                }
            } else {
                source.sendMessage(
                    pluginInfo.getPrefix().append(TextComponent.of("Invalid or offline server.")));
            }
        } else {
            source.sendMessage(
                pluginInfo.getPrefix().append(TextComponent.of("Offline or invalid player.")));
        }
    }
}
