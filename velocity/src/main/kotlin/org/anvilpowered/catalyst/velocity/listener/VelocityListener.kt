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

package org.anvilpowered.catalyst.velocity.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.command.CommandExecuteEvent
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import com.velocitypowered.api.proxy.server.ServerPing
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer
import com.velocitypowered.api.util.ModInfo
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.platform.Server
import org.anvilpowered.anvil.core.user.hasPermissionSet
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.anvil.velocity.user.toAnvilPlayer
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.event.CommandEvent
import org.anvilpowered.catalyst.api.event.EventBusScope
import org.anvilpowered.catalyst.api.event.JoinEvent
import org.anvilpowered.catalyst.api.event.LeaveEvent
import org.anvilpowered.catalyst.velocity.chat.ChannelService
import org.anvilpowered.catalyst.core.chat.ChatService
import org.anvilpowered.catalyst.velocity.discord.DiscordCommandSource
import java.util.UUID

context(ChannelService.Scope, ChatService.Scope, ProxyServerScope, Registry.Scope, Server.Scope, EventBusScope, org.anvilpowered.catalyst.velocity.db.RepositoryScope)
class VelocityListener {

    @Subscribe
    fun onPlayerLeave(event: DisconnectEvent) {
        if (event.loginStatus == DisconnectEvent.LoginStatus.PRE_SERVER_JOIN) {
            return
        }
        eventBus.post(LeaveEvent(event.player.toAnvilPlayer()))
    }

    @Subscribe
    fun onServerSelect(event: PlayerChooseInitialServerEvent) {
        val player = event.player
        event.initialServer.map { rs: RegisteredServer ->
            rs.ping().exceptionally { null }
                .thenAcceptAsync { s: ServerPing? ->
                    if (s == null || s.version == null) {
                        event.player.disconnect(Component.text("Failed to connect."))
                    } else {
                        val virtualHost = player.virtualHost
                        if (virtualHost.isPresent) {
                            eventBus.post(JoinEvent(player.toAnvilPlayer(), virtualHost.get().hostName))
                        }
                    }
                }.join()
        }
    }

    @Subscribe
    fun onPlayerJoin(event: LoginEvent) = runBlocking {
        val player = event.player
        val user = userRepository.initializeFromGameUser(player.uniqueId, player.username)
        val result = gameUserRepository.initialize(player.uniqueId, user.id, player.username, player.remoteAddress.hostString)

        if (result.firstJoin && registry[CatalystKeys.JOIN_LISTENER_ENABLED]) {
            server.broadcastAudience.sendMessage(
                registry[CatalystKeys.FIRST_JOIN].replaceText {
                    // TODO: Make a dedicated service for replacements with appropriate context
                    it.match("%player%")
                    it.replacement(player.username)
                },
            )
        }
    }

    @Subscribe
    fun onChat(event: PlayerChatEvent) {
        val anvilPlayer = event.player.toAnvilPlayer()
        if (registry[CatalystKeys.PROXY_CHAT_ENABLED]) {
            val player = event.player
            if (chatService.isDisabledForPlayer(anvilPlayer) || channelService.getForPlayer(player.uniqueId).passthrough) {
                return
            } else {
                event.result = PlayerChatEvent.ChatResult.denied()
                val message = if (anvilPlayer.hasPermissionSet(registry[CatalystKeys.CHAT_COLOR_PERMISSION])) {
                    MiniMessage.miniMessage().deserialize(event.message)
                } else {
                    Component.text(event.message)
                }
                eventBus.post(ChatEvent(anvilPlayer, message))
            }
        }
    }

    @Subscribe
    fun onCommand(e: CommandExecuteEvent) {
        val sourceName: String = when (e.commandSource) {
            is ConsoleCommandSource -> "Console"
            is DiscordCommandSource -> "Discord"
            else -> (e.commandSource as Player).username
        }
        eventBus.post(CommandEvent(e.commandSource, sourceName, e.command, e.result.isAllowed))
    }

    @Subscribe
    fun onServerListPing(proxyPingEvent: ProxyPingEvent) {
        val serverPing = proxyPingEvent.ping
        val builder = ServerPing.builder()
        var modInfo: ModInfo? = null
        if (registry[CatalystKeys.MOTD_ENABLED]) {
            builder.description(registry[CatalystKeys.MOTD])
        } else {
            return
        }
        if (proxyServer.configuration.isAnnounceForge) {
            var end = false
            for (server in proxyServer.configuration.attemptConnectionOrder) {
                val registeredServer = proxyServer.getServer(server)
                if (!registeredServer.isPresent) return
                if (end) break
                registeredServer.get().ping()
                    .thenApply {
                        if (it.modinfo.isPresent) {
                            modInfo = it.modinfo.get()
                            end = true
                        }
                    }
            }
        }
        if (modInfo != null) {
            builder.mods(modInfo)
        }
        if (registry[CatalystKeys.SERVER_PING].equals("players", ignoreCase = true)) {
            if (proxyServer.playerCount > 0) {
                val samplePlayers = arrayOfNulls<SamplePlayer>(proxyServer.playerCount)
                val proxiedPlayers: List<Player> = ArrayList(proxyServer.allPlayers)
                for (i in 0 until proxyServer.playerCount) {
                    samplePlayers[i] = SamplePlayer(proxiedPlayers[i].username, UUID.randomUUID())
                }
                builder.samplePlayers(*samplePlayers)
            }
        } else if (registry[CatalystKeys.SERVER_PING].equals("MESSAGE", ignoreCase = true)) {
            builder.samplePlayers(SamplePlayer(registry[CatalystKeys.SERVER_PING_MESSAGE], UUID.randomUUID()))
        }
        if (serverPing.favicon.isPresent) {
            builder.favicon(serverPing.favicon.get())
        }
        builder.onlinePlayers(proxyServer.playerCount)
        if (serverPing.version != null) {
            builder.version(serverPing.version)
        }
        builder.maximumPlayers(proxyServer.configuration.showMaxPlayers)
        proxyPingEvent.ping = builder.build()
    }
}
