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
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.BackendServer
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService
import org.anvilpowered.catalyst.api.service.ChannelService

class ServerCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val channelService: ChannelService<TPlayer>,
  private val pluginInfo: PluginInfo<TString>,
  private val textService: TextService<TString, TCommandSource>,
  private val registry: Registry,
  private val advancedServerInfo: AdvancedServerInfoService,
  private val userService: UserService<TPlayer, TPlayer>,
  private val locationService: LocationService,
  private val permissionService: PermissionService,
  private val pluginMessages: PluginMessages<TString>
) {

  fun execute(context: CommandContext<TCommandSource>): Int {
    val player = context.source as TPlayer
    val userName = userService.getUserName(player)
    val prefix = advancedServerInfo.getPrefixForPlayer(userName)
    val useAdvServerInfo = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
    val currentServer = locationService.getServer(userName)
    val targetServer = context.getArgument<String>("server")
    if (registry.getOrDefault(CatalystKeys.ENABLE_PER_SERVER_PERMS)
      && !permissionService.hasPermission(player, "catalyst.server.$targetServer")
    ) {
      textService.send(pluginMessages.getNoServerPermission(targetServer), player)
      return 0
    }
    if (useAdvServerInfo && currentServer.map { it.name.equals(prefix + targetServer, ignoreCase = true) }.orElse(false)
      || currentServer.map { server: BackendServer -> server.name.equals(targetServer, ignoreCase = true) }.orElse(false)
    ) {
      return alreadyConnected(targetServer, player)
    }
    for (server in locationService.servers) {
      val serverName = server.name
      if (useAdvServerInfo && serverName.equals(prefix + targetServer, ignoreCase = true)) {
        commenceConnection(player, serverName)
      }
      if (serverName.equals(targetServer, ignoreCase = true)) {
        commenceConnection(player, serverName)
      }
    }
    return 1
  }

  private fun commenceConnection(player: TPlayer, server: String) {
    val userName = userService.getUserName(player)
    locationService.getServerForName(server).map { s: BackendServer ->
      s.connect(userName).thenApply { result: Boolean ->
        if (result) {
          textService.builder()
            .append(pluginInfo.prefix)
            .green().append("Connected to ")
            .gold().append(server)
            .sendTo(player)
          testChannel(player, server)
          return@thenApply 1
        }
        textService.builder()
          .append(pluginInfo.prefix)
          .red().append("Connection to ")
          .gold().append(server)
          .red().append(" failed.")
          .sendTo(player)
        0
      }.join()
    }
  }

  private fun testChannel(player: TPlayer, server: String) {
    val playerUUID = userService.getUUID(player)
    val channel = channelService.getChannelFromId(channelService.getChannelIdForUser(playerUUID)) ?: channelService.defaultChannel
    ?: throw IllegalStateException("Invalid chat channnel configuration!")
    if (!channel.servers.contains(server) && !permissionService.hasPermission(
        player,
        registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION)
      )
      && !channel.servers.contains("*")
    ) {
      val defaultChannel = channelService.defaultChannel ?: throw AssertionError("A default chat channel must be defined")
      channelService.switchChannel(playerUUID, defaultChannel.id)
      textService.builder()
        .appendPrefix()
        .yellow().append("Channel ")
        .gold().append(channel.id)
        .yellow().append(" is not allowed in ")
        .gold().append(server)
        .yellow().append(". You have been moved to ")
        .gold().append(defaultChannel.id)
        .sendTo(player)
    }
  }

  fun sendServers(context: CommandContext<TCommandSource>): Int {
    val availableServers = textService.builder()
    val useAdvServerInfo = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
    val userName = userService.getUserName(context.source as TPlayer)
    val prefix = advancedServerInfo.getPrefixForPlayer(userName)
    val currentServer = locationService.getServer(userName)
    var count = 0
    if (!currentServer.isPresent) {
      return 0
    }
    for (server in locationService.servers) {
      val onlinePlayers = server.playerUUIDs.size
      val serverName = server.name
      if (useAdvServerInfo) {
        if (server.name.contains(prefix)) {
          if (count >= 8) {
            availableServers.append(textService.of("\n"))
            count = 0
          }
          if (currentServer.map { cs: BackendServer -> cs.name.equals(serverName, ignoreCase = true) }.orElse(false)) {
            availableServers.append(
              textService.builder()
                .green().append(server.name.replace(prefix, "") + " ")
                .onHoverShowText(textService.of("Online Players: $onlinePlayers"))
                .build()
            )
          } else {
            availableServers.append(
              textService.builder()
                .gray().append(server.name.replace(prefix, "") + " ")
                .onClickRunCommand("/server $serverName")
                .onHoverShowText(textService.of("Online Players: $onlinePlayers"))
                .build()
            )
          }
        }
      } else {
        if (currentServer.map { cs: BackendServer -> cs.name.equals(serverName, ignoreCase = true) }.orElse(false)) {
          availableServers.append(
            textService.builder()
              .green().append(server.name + " ")
              .onHoverShowText(textService.of("Online Players: $onlinePlayers"))
              .build()
          )
        } else {
          availableServers.append(
            textService.builder()
              .gray().append("$serverName ")
              .onClickRunCommand("/server $serverName")
              .onHoverShowText(textService.of("Online Players: $onlinePlayers"))
              .build()
          )
        }
      }
      count++
    }
    textService.builder()
      .append(pluginInfo.prefix)
      .green().append("Green = Current").yellow().append(", ")
      .gray().append("Gray = Available").yellow().append(", ")
      .dark_aqua().append("-----------------------------------------------------\n")
      .append(availableServers)
      .dark_aqua().append("\n-----------------------------------------------------\n")
      .gold().append("Click an available server to join!")
      .sendTo(context.source)
    return 1
  }

  private fun alreadyConnected(targetServer: String, player: TPlayer): Int {
    textService.builder()
      .append(pluginInfo.prefix)
      .red().append("You are already connected to ")
      .gold().append(targetServer)
      .sendTo(player)
    return 0
  }
}
