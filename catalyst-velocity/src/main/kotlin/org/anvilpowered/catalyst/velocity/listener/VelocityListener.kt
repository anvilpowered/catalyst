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

import com.google.inject.Inject
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.command.CommandExecuteEvent
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import com.velocitypowered.api.proxy.server.ServerPing
import com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer
import com.velocitypowered.api.util.ModInfo
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.Anvil
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.event.CommandEvent
import org.anvilpowered.catalyst.api.event.JoinEvent
import org.anvilpowered.catalyst.api.event.LeaveEvent
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.BroadcastService
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.EventService
import org.anvilpowered.catalyst.velocity.discord.DiscordCommandSource
import org.anvilpowered.registry.Registry
import java.util.UUID

class VelocityListener @Inject constructor(
    private val channelService: ChannelService<Player>,
    private val chatService: ChatService<Player, CommandSource>,
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val eventService: EventService,
    private val pluginMessages: PluginMessages,
    private val broadcastService: BroadcastService,
    private val memberManager: MemberManager
) {

    @Subscribe
    fun onPlayerLeave(event: DisconnectEvent) {
        if (event.loginStatus == DisconnectEvent.LoginStatus.PRE_SERVER_JOIN) {
            return
        }
        eventService.post(LeaveEvent(event.player))
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
                            eventService.post(JoinEvent(player, virtualHost.get().hostName, player.uniqueId))
                        }
                    }
                }.join()
        }
    }

    @Subscribe
    fun onPlayerJoin(event: LoginEvent) {
        val player = event.player
        val flags = BooleanArray(8)
        memberManager.primaryComponent
            .getOneOrGenerateForUser(
                player.uniqueId,
                player.username,
                player.remoteAddress.hostString,
                flags
            ).thenAcceptAsync { member ->
                if (member == null) {
                    return@thenAcceptAsync
                }
                if (Anvil.serviceManager!!.provide(MemberManager::class.java).primaryComponent.checkBanned(member)) {
                    event.result = ResultedEvent.ComponentResult.denied(pluginMessages.getBanMessage(member.banReason, member.banEndUtc))
                }
                if (flags[0] && registry.getOrDefault(CatalystKeys.JOIN_LISTENER_ENABLED)) {
                    broadcastService.broadcast(
                        LegacyComponentSerializer.legacyAmpersand().deserialize(
                            registry.getOrDefault(CatalystKeys.FIRST_JOIN).replace("%player%", player.username)
                        )
                    )
                }
            }.join()
    }

    @Subscribe
    fun onChat(e: PlayerChatEvent) {
        if (registry.getOrDefault(CatalystKeys.PROXY_CHAT_ENABLED)) {
            val player = e.player
            if (chatService.isDisabledForUser(player)
                || channelService.getChannelFromUUID(player.uniqueId)?.passthrough == true
            ) {
                return
            } else {
                e.result = PlayerChatEvent.ChatResult.denied()
                memberManager.primaryComponent
                    .getOneForUser(player.uniqueId)
                    .thenAcceptAsync { member ->
                        if (member == null) {
                            return@thenAcceptAsync
                        }
                        if (memberManager.primaryComponent.checkMuted(member)) {
                            player.sendMessage(Identity.nil(), pluginMessages.getMuteMessage(member.muteReason, member.muteEndUtc))
                        } else {
                            eventService.post(ChatEvent(player, e.message, Component.text(e.message)))
                        }
                    }
            }
        }
    }

    @Subscribe
    fun onCommand(e: CommandExecuteEvent) {
        val sourceName: String = when (e.commandSource) {
            is ConsoleCommandSource -> {
                "Console"
            }
            is DiscordCommandSource -> {
                "Discord"
            }
            else -> {
                (e.commandSource as Player).username
            }
        }
        eventService.post(CommandEvent(e.commandSource, sourceName, e.command, e.result.isAllowed))
    }

    @Subscribe
    fun onServerListPing(proxyPingEvent: ProxyPingEvent) {
        val serverPing = proxyPingEvent.ping
        val builder = ServerPing.builder()
        var modInfo: ModInfo? = null
        if (registry.getOrDefault(CatalystKeys.MOTD_ENABLED)) {
            val withColorCodes = LegacyComponentSerializer.legacyAmpersand().deserialize(registry.getOrDefault(CatalystKeys.MOTD))
            builder.description(withColorCodes.asComponent())
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
        if (registry.getOrDefault(CatalystKeys.SERVER_PING).equals("players", ignoreCase = true)) {
            if (proxyServer.playerCount > 0) {
                val samplePlayers = arrayOfNulls<SamplePlayer>(proxyServer.playerCount)
                val proxiedPlayers: List<Player> = ArrayList(proxyServer.allPlayers)
                for (i in 0 until proxyServer.playerCount) {
                    samplePlayers[i] = SamplePlayer(proxiedPlayers[i].username, UUID.randomUUID())
                }
                builder.samplePlayers(*samplePlayers)
            }
        } else if (registry.getOrDefault(CatalystKeys.SERVER_PING).equals("MESSAGE", ignoreCase = true)) {
            builder.samplePlayers(SamplePlayer(registry.getOrDefault(CatalystKeys.SERVER_PING_MESSAGE), UUID.randomUUID()))
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
