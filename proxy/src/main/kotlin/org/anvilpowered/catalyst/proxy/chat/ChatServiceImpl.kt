/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
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

package org.anvilpowered.catalyst.proxy.chat

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.chat.placeholder.PlayerFormat
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.apache.logging.log4j.Logger
import java.util.UUID

class ChatServiceImpl(
    private val proxyServer: ProxyServer,
    private val logger: Logger,
    private val luckpermsService: LuckpermsService,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val channelService: ChannelService,
    private val playerFormatResolver: PlayerFormat.Resolver,
) : ChatService {
    private var ignoreMap = mutableMapOf<UUID, MutableList<UUID>>()
    private var disabledList = mutableListOf<UUID>()

    override suspend fun sendMessage(message: ChannelMessage.Resolved) {
        proxyServer.consoleCommandSource.sendMessage(message.formatted.format) // won't resolve 'recipient.*' placeholders

        val channelId = message.backing.channel.id
        val sourceUserId = message.backing.source.player.uniqueId

        val spyAllPlayers = proxyServer.allPlayers
            .filter { it.hasPermission(registry[catalystKeys.PERMISSION_CHANNEL_SPYALL]) }
            .toMutableList()

        channelService.getReceivers(channelId)
            .filterNot { isIgnored(it.uniqueId, sourceUserId) }
            .onEach { spyAllPlayers.remove(it) }
            .forEach { player -> player.sendMessage(playerFormatResolver.resolve(message.formatted, player)) }

        spyAllPlayers.forEach { player ->
            player.sendMessage(
                Component.text()
                    .append(Component.text("[SPYALL] ", NamedTextColor.DARK_GRAY))
                    .append(playerFormatResolver.resolve(message.formatted, player))
                    .build(),
            )
        }
    }

    override fun ignore(playerUUID: UUID, targetPlayerUUID: UUID): Component {
        val targetPlayer = proxyServer.getPlayer(targetPlayerUUID).orElse(null) ?: return Component.text("That user does not exist!")
            .color(NamedTextColor.RED)
        var uuidList: MutableList<UUID> = ArrayList()
        if (ignoreMap[playerUUID] == null) {
            uuidList.add(targetPlayerUUID)
        } else {
            uuidList = ignoreMap[playerUUID] ?: return Component.text("An error occurred.").color(NamedTextColor.RED)
            if (uuidList.contains(targetPlayerUUID)) {
                return unIgnore(playerUUID, targetPlayerUUID)
            }
        }
        ignoreMap[playerUUID] = uuidList
        return Component.text().append(Component.text("You are now ignoring ").color(NamedTextColor.GREEN))
            .append(Component.text(targetPlayer.username).color(NamedTextColor.GOLD)).build()
    }

    override fun unIgnore(playerUUID: UUID, targetPlayerUUID: UUID): Component {
        val uuidList: MutableList<UUID> =
            ignoreMap[playerUUID] ?: return Component.text("That user does not exist!").color(NamedTextColor.RED)
        if (isIgnored(playerUUID, targetPlayerUUID)) {
            uuidList.remove(targetPlayerUUID)
            ignoreMap.replace(playerUUID, uuidList)
        }
        // TODO: What if the player leaves after being ignored?
        return Component.text("You are no longer ignoring").append(
            Component.text(proxyServer.getPlayer(targetPlayerUUID).map { it.username }.orElse(targetPlayerUUID.toString()))
                .color(NamedTextColor.GREEN),
        )
    }

    override fun isIgnored(playerUUID: UUID, targetPlayerUUID: UUID): Boolean {
        val uuidList = ignoreMap[playerUUID] ?: return false
        return targetPlayerUUID in uuidList
    }

    override fun highlightPlayerNames(sender: Player, message: Component): Component {
        return proxyServer.allPlayers.map { it.username }.fold(message) { msg, username ->
            msg.replaceText {
                it.match(username)
                it.replacement(Component.text("@$username").color(NamedTextColor.AQUA))
                // TODO: On click DM?
            }
        }
    }

    override fun toggleChatForPlayer(player: Player) {
        if (!disabledList.contains(player.uniqueId)) {
            disabledList.add(player.uniqueId)
            return
        }
        disabledList.remove(player.uniqueId)
    }

    override fun isDisabledForPlayer(player: Player): Boolean {
        return disabledList.contains(player.uniqueId)
    }
}
