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

import com.google.inject.Inject
import com.google.inject.Singleton
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.api.Anvil
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.misc.sendToConsole
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.SendTextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.ChatMessage
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.LuckpermsService
import java.util.Locale
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Singleton
class CommonChatService<TPlayer, TCommandSource> @Inject constructor(
    private val channelService: ChannelService<TPlayer>,
    private val luckpermsService: LuckpermsService,
    private val permissionService: PermissionService,
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>
) : ChatService<TPlayer, TCommandSource> {

    var ignoreMap = mutableMapOf<UUID, MutableList<UUID>>()
    var disabledList = mutableListOf<UUID>()

    override fun sendMessageToChannel(channelId: String, message: Component, senderUUID: UUID): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            userService.onlinePlayers().forEach {
                if (permissionService.hasPermission(it, registry.get(CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION))
                    || channelService.fromUUID(userService.getUUID(it)).id == channelId
                ) {
                    if (senderUUID != userService.getUUID(it)) {
                        if (!isIgnored(userService.getUUID(it)!!, senderUUID)) {
                            message.sendTo(it)
                        }
                    } else {
                        Anvil.environment?.injector?.getInstance(SendTextService::class.java)?.send(it, message)
                    }
                }
            }
        }
    }

    override fun ignore(playerUUID: UUID, targetPlayerUUID: UUID): Component {
        val targetPlayer = userService.getPlayer(targetPlayerUUID) ?: return Component.text("That user does not exist!").color(NamedTextColor.RED)
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
            .append(Component.text(userService.getUserName(targetPlayer)).color(NamedTextColor.GOLD))
            .build()
    }

    override fun unIgnore(playerUUID: UUID, targetPlayerUUID: UUID): Component {
        val uuidList: MutableList<UUID> = ignoreMap[playerUUID] ?: return Component.text("That user does not exist!").color(NamedTextColor.RED)
        if (isIgnored(playerUUID, targetPlayerUUID)) {
            uuidList.remove(targetPlayerUUID)
            ignoreMap.replace(playerUUID, uuidList)
        }
        return Component.text("You are no longer ignoring ${userService.getUserName(targetPlayerUUID)}").color(NamedTextColor.GREEN)
    }

    override fun isIgnored(playerUUID: UUID, targetPlayerUUID: UUID): Boolean {
        val uuidList = ignoreMap[playerUUID] ?: return false
        return targetPlayerUUID in uuidList
    }

    override fun checkPlayerName(sender: TPlayer, msg: String): String {
        var message = msg
        for (player in userService.onlinePlayers()) {
            val username = userService.getUserName(player)
            if (message.lowercase(Locale.getDefault()).contains(username.lowercase(Locale.getDefault()))) {
                val occurrences: MutableList<Int> = ArrayList()
                var startIndex = message.lowercase(Locale.getDefault()).indexOf(username.lowercase(Locale.getDefault()))
                while (startIndex != -1) {
                    occurrences.add(startIndex)
                    startIndex = message.lowercase(Locale.getDefault()).indexOf(username.lowercase(Locale.getDefault()), startIndex + 1)
                }
                val chatColor = luckpermsService.getChatColor(sender)
                for (occurrence in occurrences) {
                    message = message.substring(0, occurrence) + "&b@" + username + chatColor + message.substring(occurrence + username.length)
                }
            }
        }
        return message
    }

    override fun sendChatMessage(chatMessage: ChatMessage) {
        if (chatMessage.component == Component.text("")) {
            return
        }
        chatMessage.component.sendToConsole<TCommandSource>()
        sendMessageToChannel(channelService.fromUUID(chatMessage.uuid).id, chatMessage.component, chatMessage.uuid)
    }

    override fun toggleChatForUser(player: TPlayer) {
        val userUUID = userService.getUUID(player)
        if (!disabledList.contains(userUUID)) {
            disabledList.add(userUUID!!)
            return
        }
        disabledList.remove(userUUID)
    }

    override fun isDisabledForUser(player: TPlayer): Boolean {
        val userUUID = userService.getUUID(player)
        return disabledList.contains(userUUID)
    }
}
