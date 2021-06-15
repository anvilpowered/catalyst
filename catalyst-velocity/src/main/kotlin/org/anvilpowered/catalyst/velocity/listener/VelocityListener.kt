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
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.Anvil
import org.anvilpowered.anvil.api.coremember.CoreMemberManager
import org.anvilpowered.anvil.api.model.coremember.CoreMember
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.event.CommandEvent
import org.anvilpowered.catalyst.api.event.JoinEvent
import org.anvilpowered.catalyst.api.event.LeaveEvent
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.AdvancedServerInfo
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.BroadcastService
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.EventService
import org.anvilpowered.catalyst.velocity.discord.DiscordCommandSource
import java.util.ArrayList
import java.util.UUID
import java.util.concurrent.ExecutionException
import java.util.concurrent.atomic.AtomicBoolean

class VelocityListener @Inject constructor(
  private val channelService: ChannelService<Player>,
  private val chatService: ChatService<TextComponent, Player, CommandSource>,
  private val proxyServer: ProxyServer,
  private val registry: Registry,
  private val eventService: EventService,
  private val pluginMessages: PluginMessages<TextComponent>,
  private val broadcastService: BroadcastService<TextComponent>,
  private val textService: TextService<TextComponent, CommandSource>
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
            event.player.disconnect(textService.of("Failed to connect."))
          } else {
            val virtualHost = player.virtualHost
            if (virtualHost.isPresent) {
              if (registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)) {
                val hostNameExists = AtomicBoolean(false)
                for (serverInfo in registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO)) {
                  if (serverInfo.hostName.equals(virtualHost.get().hostString, ignoreCase = true)) {
                    hostNameExists.set(true)
                  }
                }
                if (!hostNameExists.get()) {
                  player.disconnect(textService.deserialize("&4Please re-connect using the correct IP!"))
                }
              }
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
    Anvil.getServiceManager().provide(CoreMemberManager::class.java).primaryComponent
      .getOneOrGenerateForUser(
        player.uniqueId,
        player.username,
        player.remoteAddress.hostString,
        flags
      ).thenAcceptAsync { optionalMember ->
        if (!optionalMember.isPresent) {
          return@thenAcceptAsync
        }
        if (flags[0] && registry.getOrDefault(CatalystKeys.JOIN_LISTENER_ENABLED)) {
          broadcastService.broadcast(
            textService.deserialize(registry.getOrDefault(CatalystKeys.FIRST_JOIN).replace("%player%", player.username))
          )
        }
        val coreMember: CoreMember<*> = optionalMember.get()
        if (Anvil.getServiceManager().provide(CoreMemberManager::class.java).primaryComponent.checkBanned(coreMember)) {
          event.result = ResultedEvent.ComponentResult.denied(pluginMessages.getBanMessage(coreMember.banReason, coreMember.banEndUtc))
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
        Anvil.getServiceManager().provide(CoreMemberManager::class.java).primaryComponent
          .getOneForUser(player.uniqueId)
          .thenAcceptAsync { optionalMember ->
            if (!optionalMember.isPresent) {
              return@thenAcceptAsync
            }
            val coreMember: CoreMember<*> = optionalMember.get()
            if (Anvil.getServiceManager().provide(CoreMemberManager::class.java).primaryComponent.checkMuted(coreMember)) {
              player.sendMessage(Identity.nil(), pluginMessages.getMuteMessage(coreMember.muteReason, coreMember.muteEndUtc))
            } else {
              eventService.post(ChatEvent(player, e.message, textService.of(e.message)))
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
    var serverPing = proxyPingEvent.ping
    val builder = ServerPing.builder()
    var modInfo: ModInfo? = null
    val hostNameExists = AtomicBoolean(false)
    var advancedServerInfoList: List<AdvancedServerInfo> = ArrayList()
    val playerProvidedHost: String = if (proxyPingEvent.connection.virtualHost.isPresent) {
      proxyPingEvent.connection.virtualHost.get().hostString
    } else {
      return
    }
    val useCatalyst = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
    if (useCatalyst) {
      advancedServerInfoList =
        registry.get(CatalystKeys.ADVANCED_SERVER_INFO).orElseThrow { IllegalArgumentException("Invalid server configuration!") }
      advancedServerInfoList.forEach {
        if (playerProvidedHost == it.hostName) {
          hostNameExists.set(true)
          val withColorCodes = LegacyComponentSerializer.legacyAmpersand().deserialize(it.motd)
          builder.description(MiniMessage.get().deserialize(MiniMessage.markdown().serialize(withColorCodes.asComponent())))
        }
      }
      if (!hostNameExists.get()) {
        builder.description(textService.deserialize("&4Using the direct IP to connect has been disabled!"))
      }
    } else if (registry.getOrDefault(CatalystKeys.MOTD_ENABLED)) {
      val withColorCodes = LegacyComponentSerializer.legacyAmpersand().deserialize(registry.getOrDefault(CatalystKeys.MOTD))
      builder.description(MiniMessage.get().deserialize(MiniMessage.markdown().serialize(withColorCodes.asComponent())))
    } else {
      return
    }
    if (proxyServer.configuration.isAnnounceForge) {
      if (useCatalyst) {
        for (advancedServerInfo in advancedServerInfoList) {
          if (playerProvidedHost.startsWith(advancedServerInfo.hostName)) {
            for (pServer in proxyServer.allServers) {
              try {
                if (pServer.serverInfo.name.contains(advancedServerInfo.prefix)) {
                  serverPing = pServer.ping().get()
                  if (serverPing.modinfo.isPresent) {
                    modInfo = serverPing.modinfo.get()
                    break
                  }
                }
              } catch (ignored: InterruptedException) {
              } catch (ignored: ExecutionException) {
              }
            }
          }
        }
      } else {
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
