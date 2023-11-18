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

package org.anvilpowered.catalyst.core.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.platform.Server
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.anvil.core.user.hasPermissionSet
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.service.LuckpermsService
import java.util.UUID

context(ChannelService.Scope, LuckpermsService.Scope, Registry.Scope, PlayerService.Scope, Server.Scope)
class ChatServiceImpl : ChatService {
    private var ignoreMap = mutableMapOf<UUID, MutableList<UUID>>()
    private var disabledList = mutableListOf<UUID>()

    override suspend fun sendMessageToChannel(channelId: String, message: Component, userId: UUID) {
        playerService.getAll().forEach { player ->
            if (player.hasPermissionSet(registry[CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION])
                || channelService.getForPlayer(player.id).id == channelId
            ) {
                // TODO: This is icky
                if (userId != player.id) {
                    if (!isIgnored(player.id, userId)) {
                        player.sendMessage(message)
                    }
                } else {
                    player.sendMessage(message)
                }
            }
        }
    }

    override suspend fun sendChatMessage(message: ChatMessage) {
        if (message.component == Component.text("")) {
            return
        }
        server.systemSubject.sendMessage(message.component)
        sendMessageToChannel(channelService.getForPlayer(message.userId).id, message.component, message.userId)
    }

    override fun ignore(playerUUID: UUID, targetPlayerUUID: UUID): Component {
        val targetPlayer = playerService[targetPlayerUUID] ?: return Component.text("That user does not exist!").color(NamedTextColor.RED)
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
        return Component.text()
            .append(Component.text("You are now ignoring ").color(NamedTextColor.GREEN))
            .append(targetPlayer.displayname.color(NamedTextColor.GOLD))
            .build()
    }

    override fun unIgnore(playerUUID: UUID, targetPlayerUUID: UUID): Component {
        val uuidList: MutableList<UUID> =
            ignoreMap[playerUUID] ?: return Component.text("That user does not exist!").color(NamedTextColor.RED)
        if (isIgnored(playerUUID, targetPlayerUUID)) {
            uuidList.remove(targetPlayerUUID)
            ignoreMap.replace(playerUUID, uuidList)
        }
        // TODO: What if the player leaves after being ignored?
        return Component.text("You are no longer ignoring")
            .append(
                (playerService[targetPlayerUUID]?.displayname
                    ?: Component.text(targetPlayerUUID.toString())
                    ).color(NamedTextColor.GREEN),
            )
    }

    override fun isIgnored(playerUUID: UUID, targetPlayerUUID: UUID): Boolean {
        val uuidList = ignoreMap[playerUUID] ?: return false
        return targetPlayerUUID in uuidList
    }

    override fun highlightPlayerNames(sender: Player, message: Component): Component {
        return playerService.getAll().map { it.username }.fold(message) { msg, username ->
            msg.replaceText {
                it.match(username)
                it.replacement(Component.text(username).color(NamedTextColor.AQUA))
                // TODO: On click DM?
            }
        }
    }

    override fun toggleChatForPlayer(player: Player) {
        if (!disabledList.contains(player.id)) {
            disabledList.add(player.id)
            return
        }
        disabledList.remove(player.id)
    }

    override fun isDisabledForPlayer(player: Player): Boolean {
        return disabledList.contains(player.id)
    }
}
