/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.velocity.discord

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.ServerPing
import com.velocitypowered.api.util.ModInfo
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.placeholder.ProxyFormat
import org.anvilpowered.catalyst.api.config.CatalystKeys
import java.util.UUID

class ProxyPingListener(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val proxyFormatResolver: ProxyFormat.Resolver,
) {

    @Subscribe
    fun onServerListPing(proxyPingEvent: ProxyPingEvent) {
        val serverPing = proxyPingEvent.ping
        val builder = ServerPing.builder()
        var modInfo: ModInfo? = null
        if (registry[catalystKeys.MOTD_ENABLED]) {
            builder.description(proxyFormatResolver.resolve(registry[catalystKeys.MOTD]))
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
        if (registry[catalystKeys.SERVER_PING].equals("players", ignoreCase = true)) {
            if (proxyServer.playerCount > 0) {
                val samplePlayers = arrayOfNulls<ServerPing.SamplePlayer>(proxyServer.playerCount)
                val proxiedPlayers: List<Player> = ArrayList(proxyServer.allPlayers)
                for (i in 0 until proxyServer.playerCount) {
                    samplePlayers[i] = ServerPing.SamplePlayer(proxiedPlayers[i].username, UUID.randomUUID())
                }
                builder.samplePlayers(*samplePlayers)
            }
        } else if (registry[catalystKeys.SERVER_PING].equals("MESSAGE", ignoreCase = true)) {
            builder.samplePlayers(ServerPing.SamplePlayer(registry[catalystKeys.SERVER_PING_MESSAGE], UUID.randomUUID()))
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
