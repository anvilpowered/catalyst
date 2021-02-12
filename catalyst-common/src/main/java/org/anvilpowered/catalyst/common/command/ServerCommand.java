/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import java.util.Optional;
import java.util.UUID;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.server.BackendServer;
import org.anvilpowered.anvil.api.server.LocationService;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.registry.ChatChannel;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.anvilpowered.catalyst.api.service.ChatService;

public class ServerCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private PluginInfo<TString> pluginInfo;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    @Inject
    private AdvancedServerInfoService advancedServerInfo;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private LocationService locationService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    public int execute(CommandContext<TCommandSource> context) {
        TPlayer player = (TPlayer) context.getSource();
        String userName = userService.getUserName(player);
        String prefix = advancedServerInfo.getPrefixForPlayer(userName);
        final boolean useAdvServerInfo = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED);
        Optional<? extends BackendServer> currentServer = locationService.getServer(userName);
        String targetServer = context.getArgument("server", String.class);

        if (registry.getOrDefault(CatalystKeys.ENABLE_PER_SERVER_PERMS)
            && !permissionService.hasPermission(player, "catalyst.server." + targetServer)) {
            textService.send(pluginMessages.getNoServerPermission(targetServer), player);
            return 0;
        }

        if ((useAdvServerInfo && currentServer.map(s -> s.getName().equalsIgnoreCase(prefix + targetServer)).orElse(false))
            || currentServer.map(server -> server.getName().equalsIgnoreCase(targetServer)).orElse(false)) {
            return alreadyConnected(targetServer, player);
        }


        for (BackendServer server : locationService.getServers()) {
            String serverName = server.getName();
            if (useAdvServerInfo && serverName.equalsIgnoreCase(prefix + targetServer)) {
                commenceConnection(player, serverName);
            }
            if (serverName.equalsIgnoreCase(targetServer)) {
                commenceConnection(player, serverName);
            }
        }
        return 1;
    }

    private void commenceConnection(TPlayer player, String server) {
        String userName = userService.getUserName(player);
        locationService.getServerForName(server).map(s -> s.connect(userName).thenApply(result -> {
            if (result) {
                textService.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Connected to ")
                    .gold().append(server)
                    .sendTo(player);
                testChannel(player, server);
                return 1;
            }
            textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Connection to ")
                .gold().append(server)
                .red().append(" failed.")
                .sendTo(player);
            return 0;
        }).join());
    }

    private void testChannel(TPlayer player, String server) {
        UUID playerUUID = userService.getUUID(player);
        Optional<ChatChannel> channel =
            chatService.getChannelFromId(chatService.getChannelIdForUser(playerUUID));
        if (channel.isPresent()) {
            if (!channel.get().servers.contains(server) && !permissionService.hasPermission(player,
                registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION))
                && !channel.get().servers.contains("*")) {
                ChatChannel defaultChannel =
                    chatService.getChannelFromId(registry.getOrDefault(CatalystKeys.CHAT_DEFAULT_CHANNEL))
                        .orElseThrow(() -> new AssertionError("A default channel must be specified!"));
                chatService.switchChannel(playerUUID, defaultChannel.id);
                textService.builder()
                    .appendPrefix()
                    .yellow().append("Channel ")
                    .gold().append(channel.get().id)
                    .yellow().append(" is not allowed in ")
                    .gold().append(server)
                    .yellow().append(". You have been moved to ")
                    .gold().append(defaultChannel.id)
                    .sendTo(player);
            }
        }
    }

    public int sendServers(CommandContext<TCommandSource> context) {
        TextService.Builder<TString, TCommandSource> availableServers = textService.builder();
        boolean useAdvServerInfo = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED);
        String userName = userService.getUserName((TPlayer) context.getSource());
        String prefix = advancedServerInfo.getPrefixForPlayer(userName);
        Optional<? extends BackendServer> currentServer = locationService.getServer(userName);
        int count = 0;

        if (!currentServer.isPresent()) {
            return 0;
        }

        for (BackendServer server : locationService.getServers()) {
            int onlinePlayers = server.getPlayerUUIDs().size();
            String serverName = server.getName();
            if (useAdvServerInfo) {
                if (server.getName().contains(prefix)) {
                    if (count >= 8) {
                        availableServers.append(textService.of("\n"));
                        count = 0;
                    }
                    if (currentServer.map(cs -> cs.getName().equalsIgnoreCase(serverName)).orElse(false)) {
                        availableServers.append(textService.builder()
                            .green().append(server.getName().replace(prefix, "") + " ")
                            .onHoverShowText(textService.of("Online Players: " + onlinePlayers))
                            .build());
                    } else {
                        availableServers.append(textService.builder()
                            .gray().append(server.getName().replace(prefix, "") + " ")
                            .onClickRunCommand("/server " + serverName)
                            .onHoverShowText(textService.of("Online Players: " + onlinePlayers))
                            .build());
                    }
                }
            } else {
                if (currentServer.map(cs -> cs.getName().equalsIgnoreCase(serverName)).orElse(false)) {
                    availableServers.append(textService.builder()
                        .green().append(server.getName() + " ")
                        .onHoverShowText(textService.of("Online Players: " + onlinePlayers))
                        .build());
                } else {
                    availableServers.append(textService.builder()
                        .gray().append(serverName + " ")
                        .onClickRunCommand("/server " + serverName)
                        .onHoverShowText(textService.of("Online Players: " + onlinePlayers))
                        .build());
                }
            }
            count++;
        }

        textService.builder()
            .append(pluginInfo.getPrefix())
            .green().append("Green = Current").yellow().append(", ")
            .gray().append("Gray = Available").yellow().append(", ")
            .dark_aqua().append("-----------------------------------------------------\n")
            .append(availableServers)
            .dark_aqua().append("\n-----------------------------------------------------\n")
            .gold().append("Click an available server to join!")
            .sendTo(context.getSource());
        return 1;
    }

    private int alreadyConnected(String targetServer, TPlayer player) {
        textService.builder()
            .append(pluginInfo.getPrefix())
            .red().append("You are already connected to ")
            .gold().append(targetServer)
            .sendTo(player);
        return 0;
    }
}
