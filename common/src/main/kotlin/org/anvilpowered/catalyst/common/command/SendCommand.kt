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
package org.anvilpowered.catalyst.common.command

import com.google.inject.Inject
import com.mojang.brigadier.context.CommandContext
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.server.BackendServer
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import java.util.concurrent.CompletableFuture

class SendCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val locationService: LocationService,
  private val pluginInfo: PluginInfo<TString>,
  private val pluginMessages: PluginMessages<TString>,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>
) {

  fun execute(context: CommandContext<TCommandSource>): Int {
    val target = context.getArgument<String>("player")
    val serverName = context.getArgument<String>("server")
    val userName = userService.getUserName(context.source as TPlayer)
    val server = locationService.getServerForName(serverName).orElse(null)
    if (server == null) {
      textService.builder()
        .append(pluginInfo.prefix)
        .red().append("Could not find server ")
        .gold().append(serverName)
        .sendTo(context.source)
      return 0
    }
    if (target == "_a") {
      CompletableFuture.runAsync {
        var total = 0
        for (p in userService.onlinePlayers) {
          if (locationService.getServerForName(serverName).map { it.connect(p).join() }.orElse(false)) {
            ++total
            textService.builder()
              .append(pluginInfo.prefix)
              .green().append("You have been sent to ")
              .gold().append(serverName)
              .green().append(" by ")
              .gold().append(userName)
              .sendTo(p)
          }
        }
        textService.builder()
          .append(pluginInfo.prefix)
          .green().append("You have sent ")
          .gold().append(total)
          .green().append(" players to ")
          .gold().append(serverName)
          .sendTo(context.source)
      }
      return 1
    }
    if (target == "_r") {
      val onlinePlayers = userService.onlinePlayers
      val random = (onlinePlayers.size * Math.random()).toInt() - 1
      if (onlinePlayers is List<*> && random >= -1) {
        val randomPlayer = (onlinePlayers as List<TPlayer>)[random + 1]
        connect(server, context.source, randomPlayer)
        return 1
      }
      val iterator: Iterator<TPlayer> = onlinePlayers.iterator()
      var current: TPlayer? = null
      for (i in 0..random) {
        if (iterator.hasNext()) {
          current = iterator.next()
        }
        if (i == random) {
          break
        }
      }
      return if (current != null) {
        connect(server, context.source, current)
      } else 1
    }
    if (target.startsWith("_")) {
      val targetServer = target.replace("_", "")
      for (p in userService.onlinePlayers) {
        val targetUserName = userService.getUserName(p)
        if (targetServer == locationService.getServer(targetUserName).map { it.name }.orElse(null)) {
          server.connect(p).thenAccept {
            if (it) {
              textService.builder()
                .append(pluginInfo.prefix)
                .green().append("You were sent to ")
                .gold().append(serverName)
                .green().append(" by ")
                .gold().append(userName)
                .sendTo(p)
            }
          }
        }
      }
      textService.builder()
        .append(pluginInfo.prefix)
        .green().append("You have sent all players from ")
        .gold().append(targetServer)
        .green().append(" to ")
        .gold().append(serverName)
        .sendTo(context.source)
      return 1
    }
    val targetPlayer = userService.getPlayer(target)
    if (targetPlayer.isPresent) {
      connect(locationService.getServerForName(serverName).orElse(null), context.source, targetPlayer.get())
    } else {
      textService.send(textService.of(pluginMessages.offlineOrInvalidPlayer()), context.source)
      return 0
    }
    return 0
  }

  private fun connect(server: BackendServer?, source: TCommandSource, target: TPlayer): Int {
    if (server == null) {
      return 0
    }
    val targetName = userService.getUserName(target)
    server.connect(target).thenApply {
      if (it) {
        textService.builder()
          .append(pluginInfo.prefix)
          .green().append("You have been sent to ")
          .gold().append(server.name)
          .sendTo(target)
        if (source != target) {
          textService.builder()
            .append(pluginInfo.prefix)
            .gold().append(targetName)
            .green().append(" has been sent to ")
            .gold().append(server.name)
            .sendTo(source)
        }
        return@thenApply 1
      }
      textService.builder()
        .append(pluginInfo.prefix)
        .red().append("Could not send ")
        .gold().append(targetName)
        .red().append(" to ")
        .gold().append(server.name)
        .sendTo(source)
      1
    }
    return 0
  }
}
