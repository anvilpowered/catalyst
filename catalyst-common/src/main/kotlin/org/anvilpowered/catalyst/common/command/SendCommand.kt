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
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.server.BackendServer
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import java.util.concurrent.CompletableFuture

class SendCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val locationService: LocationService,
    private val pluginInfo: PluginInfo,
    private val pluginMessages: PluginMessages,
    private val userService: UserService<TPlayer, TPlayer>
) {

    fun execute(context: CommandContext<TCommandSource>): Int {
        val target = context.getArgument<String>("player")
        val serverName = context.getArgument<String>("server")
        val userName = userService.getUserName(context.source as TPlayer)
        val server = locationService.getServerForName(serverName)
        if (server == null) {
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("Could not find server ").color(NamedTextColor.RED))
                .append(Component.text(serverName).color(NamedTextColor.GOLD))
                .sendTo(context.source)
            return 0
        }
        if (target == "_a") {
            CompletableFuture.runAsync {
                var total = 0
                for (p in userService.onlinePlayers) {
                    val tempServer = locationService.getServerForName(serverName)
                    if (tempServer?.connect(p as Any)?.join() == true) {
                        ++total
                        Component.text()
                            .append(pluginInfo.prefix)
                            .append(Component.text("You have been sent to ").color(NamedTextColor.GREEN))
                            .append(Component.text(serverName).color(NamedTextColor.GOLD))
                            .append(Component.text(" by ").color(NamedTextColor.GREEN))
                            .append(Component.text(userName).color(NamedTextColor.GOLD))
                            .sendTo(p)
                    }
                }
                Component.text()
                    .append(pluginInfo.prefix)
                    .append(Component.text("You have sent ").color(NamedTextColor.GREEN))
                    .append(Component.text(total).color(NamedTextColor.GOLD))
                    .append(Component.text(" players to ").color(NamedTextColor.GREEN))
                    .append(Component.text(serverName).color(NamedTextColor.GOLD))
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
                if (targetServer == locationService.getServer(targetUserName)?.name) {
                    server.connect(p as Any).thenAccept {
                        if (it) {
                            Component.text()
                                .append(pluginInfo.prefix)
                                .append(Component.text("You were sent to ").color(NamedTextColor.GREEN))
                                .append(Component.text(serverName).color(NamedTextColor.GOLD))
                                .append(Component.text(" by ").color(NamedTextColor.GREEN))
                                .append(Component.text(userName).color(NamedTextColor.GOLD))
                                .sendTo(p)
                        }
                    }
                }
            }
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("You have sent all players from ").color(NamedTextColor.GREEN))
                .append(Component.text(targetServer).color(NamedTextColor.GOLD))
                .append(Component.text(" to ").color(NamedTextColor.GREEN))
                .append(Component.text(serverName).color(NamedTextColor.GOLD))
                .sendTo(context.source)
            return 1
        }
        val targetPlayer = userService.getPlayer(target)
        if (targetPlayer != null) {
            connect(locationService.getServerForName(serverName), context.source, targetPlayer)
        } else {
            pluginMessages.offlineOrInvalidPlayer().sendTo(context.source)
            return 0
        }
        return 0
    }

    private fun connect(server: BackendServer?, source: TCommandSource, target: TPlayer): Int {
        if (server == null) {
            return 0
        }
        val targetName = userService.getUserName(target)
        server.connect(target as Any).thenApply {
            if (it) {
                Component.text()
                    .append(pluginInfo.prefix)
                    .append(Component.text("You have been sent to ").color(NamedTextColor.GREEN))
                    .append(Component.text(server.name).color(NamedTextColor.GOLD))
                    .sendTo(target)
                if (source != target) {
                    Component.text()
                        .append(pluginInfo.prefix)
                        .append(Component.text(targetName).color(NamedTextColor.GOLD))
                        .append(Component.text(" has been sent to ").color(NamedTextColor.GOLD))
                        .append(Component.text(server.name).color(NamedTextColor.GOLD))
                        .sendTo(source)
                }
                return@thenApply 1
            }
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("Could not send ").color(NamedTextColor.RED))
                .append(Component.text(targetName).color(NamedTextColor.GOLD))
                .append(Component.text(" to ").color(NamedTextColor.RED))
                .append(Component.text(server.name).color(NamedTextColor.GOLD))
                .sendTo(source)
            1
        }
        return 0
    }
}
