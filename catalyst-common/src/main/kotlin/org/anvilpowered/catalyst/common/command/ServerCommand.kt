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
package org.anvilpowered.catalyst.common.command

import com.google.inject.Inject
import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.server.BackendServer
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.registry.Registry

class ServerCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val channelService: ChannelService<TPlayer>,
    private val pluginInfo: PluginInfo,
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>,
    private val locationService: LocationService,
    private val permissionService: PermissionService,
    private val pluginMessages: PluginMessages
) {

    fun execute(context: CommandContext<TCommandSource>): Int {
        val player = context.source as TPlayer
        val userName = userService.getUserName(player)
        val currentServer = locationService.getServer(userName)
        val targetServer = context.getArgument<String>("server")
        if (registry.getOrDefault(CatalystKeys.ENABLE_PER_SERVER_PERMS)
            && !permissionService.hasPermission(player, "catalyst.server.$targetServer")
        ) {
            pluginMessages.getNoServerPermission(targetServer).sendTo(player)
            return 0
        }
        if (currentServer?.name.equals(targetServer, ignoreCase = true)) {
            return alreadyConnected(targetServer, player)
        }
        for (server in locationService.getServers()) {
            val serverName = server.name
            if (serverName.equals(targetServer, ignoreCase = true)) {
                commenceConnection(player, serverName)
            }
        }
        return 1
    }

    private fun commenceConnection(player: TPlayer, server: String) {
        val userName = userService.getUserName(player)
        locationService.getServerForName(server).also { s: BackendServer? ->
            s?.connect(userName)?.thenApply { result: Boolean ->
                if (result) {
                    Component.text()
                        .append(pluginInfo.prefix)
                        .append(Component.text("Connected to ").color(NamedTextColor.GREEN))
                        .append(Component.text(server).color(NamedTextColor.GOLD))
                        .sendTo(player)
                    testChannel(player, server)
                    return@thenApply 1
                }
                Component.text()
                    .append(pluginInfo.prefix)
                    .append(Component.text("Connection to ").color(NamedTextColor.RED))
                    .append(Component.text(server).color(NamedTextColor.GOLD))
                    .append(Component.text(" failed.").color(NamedTextColor.RED))
                    .sendTo(player)
                0
            }?.join()
        }
    }

    private fun testChannel(player: TPlayer, server: String) {
        val playerUUID = userService.getUUID(player)
        val channel = channelService.getChannelFromId(channelService.getChannelIdForUser(playerUUID))
            ?: channelService.defaultChannel
            ?: throw IllegalStateException("Invalid chat channel configuration!")
        if (!channel.servers.contains(server)
            && !permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION))
            && !channel.servers.contains("*")
        ) {
            val defaultChannel = channelService.defaultChannel ?: throw AssertionError("A default chat channel must be defined")
            channelService.switchChannel(playerUUID, defaultChannel.id)
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("Channel ").color(NamedTextColor.YELLOW))
                .append(Component.text(channel.id).color(NamedTextColor.GOLD))
                .append(Component.text(" is not allowed in ").color(NamedTextColor.YELLOW))
                .append(Component.text(server).color(NamedTextColor.GOLD))
                .append(Component.text(". You have been moved to ").color(NamedTextColor.YELLOW))
                .append(Component.text(defaultChannel.id).color(NamedTextColor.GOLD))
                .sendTo(player)
        }
    }

    fun sendServers(context: CommandContext<TCommandSource>): Int {
        val availableServers = Component.text()
        val userName = userService.getUserName(context.source as TPlayer)
        val currentServer = locationService.getServer(userName) ?: return 0
        for (server in locationService.getServers()) {
            val onlinePlayers = server.playerUUIDs.size
            val serverName = server.name
            if (currentServer.name.equals(serverName, ignoreCase = true)) {
                availableServers.append(
                    Component.text("${server.name} ")
                        .hoverEvent(HoverEvent.showText(Component.text("Online Players: $onlinePlayers")))
                        .toBuilder()
                )
            } else {
                availableServers.append(
                    Component.text()
                        .append(Component.text(serverName).color(NamedTextColor.GRAY))
                        .clickEvent(ClickEvent.runCommand("/server $serverName"))
                        .hoverEvent(HoverEvent.showText(Component.text("Online Players: $onlinePlayers")))
                        .build()
                )
            }
        }
        Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("Green = Current").color(NamedTextColor.GREEN))
            .append(Component.text(", ").color(NamedTextColor.YELLOW))
            .append(Component.text("Gray = Available").color(NamedTextColor.GRAY))
            .append(Component.text(", ").color(NamedTextColor.YELLOW))
            .append(Component.text("-----------------------------------------------------\n").color(NamedTextColor.DARK_AQUA))
            .append(availableServers.build())
            .append(Component.text("\n-----------------------------------------------------\n").color(NamedTextColor.DARK_AQUA))
            .append(Component.text("Click an available server to join!").color(NamedTextColor.GOLD))
            .sendTo(context.source)
        return 1
    }

    private fun alreadyConnected(targetServer: String, player: TPlayer): Int {
        Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("You are already connected to ").color(NamedTextColor.RED))
            .append(Component.text(targetServer).color(NamedTextColor.GOLD))
            .sendTo(player)
        return 0
    }
}
