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
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VelocityServerCommand implements Command {

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private AdvancedServerInfoService advancedServerInfoService;

    @Inject
    private TextService<TextComponent, CommandSource> textService;

    @Inject
    private Registry registry;

    private RegisteredServer registeredServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            String playerPrefix = advancedServerInfoService.getPrefixForPlayer(((Player) source).getUsername());
            final boolean useAdvancedInformation = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED);
            List<TextComponent> availableServers = new ArrayList<>();

            if (args.length == 0) {
                AtomicInteger count = new AtomicInteger();
                proxyServer.getAllServers().forEach(s -> {
                    if (useAdvancedInformation) {
                        if (s.getServerInfo().getName().contains(playerPrefix)
                            && player.getCurrentServer().isPresent()) {
                            if (count.get() >= 8) {
                                availableServers.add(textService.of("\n"));
                                count.set(0);
                            }
                            if (player.getCurrentServer().get().getServer().equals(s)) {
                                availableServers.add(textService.of(
                                    s.getServerInfo().getName().replace(playerPrefix, "") + " ")
                                    .color(TextColor.GREEN)
                                    .hoverEvent(HoverEvent.showText(textService.of(
                                        "Online Players: " + s.getPlayersConnected().size()))));
                            } else {
                                availableServers.add(textService.of(
                                    s.getServerInfo().getName().replace(playerPrefix, "") + " ")
                                    .color(TextColor.GRAY)
                                    .clickEvent(ClickEvent.runCommand("/server " + s.getServerInfo().getName()))
                                    .hoverEvent(HoverEvent.showText(textService.of(
                                        "Online Players : " + s.getPlayersConnected().size())
                                    )));
                            }
                        }
                    } else {
                        if (player.getCurrentServer().get().getServer().equals(s)) {
                            availableServers.add(textService.of(
                                s.getServerInfo().getName() + " ")
                                .color(TextColor.GREEN)
                                .hoverEvent(HoverEvent.showText(textService.of(
                                    "Online Players: " + s.getPlayersConnected().size()))));
                        } else {
                            availableServers.add(textService.of(s.getServerInfo().getName() + " ")
                                .color(TextColor.GRAY)
                                .clickEvent(ClickEvent.runCommand("/server " + s.getServerInfo().getName()))
                                .hoverEvent(HoverEvent.showText(textService.of(
                                    "Online Players : " + s.getPlayersConnected().size())
                                )));
                        }
                    }
                    count.getAndIncrement();
                });
                TextComponent servers = TextComponent.builder()
                    .append(pluginInfo.getPrefix())
                    .append(textService.of("Green = Current").color(TextColor.GREEN))
                    .append(textService.of(", ").color(TextColor.YELLOW))
                    .append(textService.of("Gray = Available").color(TextColor.GRAY))
                    .append(textService.of(", ").color(TextColor.YELLOW))
                    .append(textService.of("Red = Offline\n").color(TextColor.RED))
                    .append(textService.of("-----------------------------------------------------\n")
                        .color(TextColor.DARK_AQUA))
                    .append(availableServers)
                    .append(textService.of("\n-----------------------------------------------------\n")
                        .color(TextColor.DARK_AQUA))
                    .append(textService.of("Click an available server to join!")
                        .color(TextColor.GOLD))
                    .build();
                player.sendMessage(servers);
                return;
            }

            if (useAdvancedInformation) {
                if (!args[0].contains(playerPrefix)
                    && !proxyServer.getServer(playerPrefix + args[0]).isPresent()) {
                    source.sendMessage(pluginMessages.getInvalidServer());
                    return;
                } else {
                    args[0] = args[0].replace(playerPrefix, "");
                }
                proxyServer.getAllServers().forEach(s -> {
                    if (s.getServerInfo().getName().equalsIgnoreCase(playerPrefix + args[0])) {
                        commenceConnection(s, player);
                    }
                });
            } else {
                proxyServer.getAllServers().forEach(s -> {
                    if (s.getServerInfo().getName().equalsIgnoreCase(args[0].replaceAll(" ", ""))) {
                        commenceConnection(s, player);
                    }
                });
            }
        }
    }

    private void commenceConnection(RegisteredServer s, Player player) {
        String serverName = s.getServerInfo().getName();
        if (registry.getOrDefault(CatalystKeys.ENABLE_PER_SERVER_PERMS)
            && !player.hasPermission("catalyst.server." + serverName)) {
            player.sendMessage(pluginMessages.getNoServerPermission());
            return;
        }
        registeredServer = s;
        registeredServer.ping().thenAcceptAsync(ping -> {
            if (ping.getVersion().getName().equals(player.getProtocolVersion().getName())
                || registry.getOrDefault(CatalystKeys.VIA_VERSION_ENABLED)) {
                player.createConnectionRequest(registeredServer).connect().thenAcceptAsync(connection -> {
                    if (connection.isSuccessful()) {
                        player.sendMessage(pluginInfo.getPrefix().append(textService.of(
                            "Connected to server " + registeredServer.getServerInfo().getName())
                        ));
                    } else {
                        if (player.getCurrentServer().map(server ->
                            server.getServerInfo().getName().equals(serverName)).orElse(false)) {
                            player.sendMessage(pluginInfo.getPrefix().append(textService.of(
                                "You are already " + "connected to "
                                    + registeredServer.getServerInfo().getName())
                            ));
                        } else {
                            player.sendMessage(pluginInfo.getPrefix().append(textService.of(
                                "Failed to connect to " + registeredServer.getServerInfo().getName())));
                        }
                    }
                });
            } else {
                player.sendMessage(pluginMessages.getIncompatibleServerVersion());
            }
        });
    }
}