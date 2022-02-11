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

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import com.google.inject.Singleton
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.event.ChatEvent
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.LuckpermsService
import org.anvilpowered.registry.Registry
import org.slf4j.Logger
import java.util.Locale
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Singleton
class CommonChatService<TPlayer, TCommandSource> @Inject constructor(
    private val channelService: ChannelService<TPlayer>,
    private val logger: Logger,
    private val locationService: LocationService,
    private val luckpermsService: LuckpermsService,
    private val memberManager: MemberManager,
    private val permissionService: PermissionService,
    private val pluginMessages: PluginMessages,
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>
) : ChatService<TPlayer, TCommandSource> {

    var ignoreMap = mutableMapOf<UUID, MutableList<UUID>>()
    var disabledList = mutableListOf<UUID>()

    override fun sendMessageToChannel(channelId: String, message: Component, senderUUID: UUID): CompletableFuture<Void?>? {
        return CompletableFuture.runAsync {
            userService.onlinePlayers.forEach {
                if (permissionService.hasPermission(it, registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION))
                    || channelService.getChannelIdForUser(userService.getUUID(it)) == channelId
                ) {
                    if (senderUUID != userService.getUUID(it)) {
                        if (!isIgnored(userService.getUUID(it)!!, senderUUID)) {
                            message.sendTo(it)
                        }
                    } else {
                        message.sendTo(it)
                    }
                }
            }
        }
    }

    override fun sendGlobalMessage(player: TPlayer, message: Component): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            userService.onlinePlayers.forEach { message.sendTo(it) }
        }
    }

    override fun formatMessage(
        prefix: String,
        nameColor: String,
        userName: String,
        userUUID: UUID,
        message: String,
        hasChatColorPermission: Boolean,
        suffix: String,
        serverName: String,
        channelId: String
    ): CompletableFuture<Component?> {
        val channel = channelService.getChannelFromId(channelId) ?: channelService.defaultChannel
        ?: throw java.lang.IllegalStateException("Invalid channel configuration!")
        val format = channel.format
        val hover = channel.hoverMessage
        val click = channel.click
        return memberManager.primaryComponent.getOneForUser(userUUID)
            .thenApplyAsync { member ->
                if (member == null) {
                    return@thenApplyAsync Component.text("Could not find a user matching that name!").color(NamedTextColor.RED)
                }
                if (member.isMuted) {
                    return@thenApplyAsync null
                }

                var finalName = member.userName
                finalName = if (member.nickName != "") {
                    member.nickName + "&r"
                } else {
                    "$nameColor$finalName&r"
                }
                if (hasChatColorPermission) {
                    return@thenApplyAsync Component.text()
                        .append(
                            LegacyComponentSerializer.legacyAmpersand().deserialize(
                                replacePlaceholders(
                                    message,
                                    prefix,
                                    member.userName,
                                    finalName,
                                    suffix,
                                    serverName,
                                    format
                                )
                            )
                        )
                        .hoverEvent(
                            HoverEvent.showText(
                                LegacyComponentSerializer.legacyAmpersand().deserialize(
                                    replacePlaceholders(
                                        message,
                                        prefix,
                                        member.userName,
                                        finalName,
                                        suffix,
                                        serverName,
                                        hover
                                    )
                                )
                            )
                        )
                        .clickEvent(
                            ClickEvent.suggestCommand(
                                replacePlaceholders(
                                    message,
                                    prefix,
                                    member.userName,
                                    userName,
                                    suffix,
                                    finalName,
                                    click
                                )
                            )
                        )
                        .build()
                }
                Component.text()
                    .append(
                        Component.text(
                            replacePlaceholders(
                                message,
                                prefix,
                                member.userName,
                                finalName,
                                suffix,
                                serverName,
                                format
                            )
                        )
                    )
                    .hoverEvent(
                        HoverEvent.showText(
                            Component.text(
                                replacePlaceholders(
                                    message,
                                    prefix,
                                    member.userName,
                                    finalName,
                                    suffix,
                                    serverName,
                                    hover
                                )
                            )
                        )
                    )
                    .clickEvent(
                        ClickEvent.suggestCommand(
                            replacePlaceholders(
                                message,
                                prefix,
                                member.userName,
                                userName,
                                suffix,
                                finalName,
                                click
                            )
                        )
                    )
                    .build()
            }
    }

    private fun replacePlaceholders(
        rawMessage: String,
        prefix: String,
        rawUserName: String,
        userName: String,
        suffix: String,
        serverName: String,
        format: String
    ): String {
        return format
            .replace("%server%", locationService.getServer(rawUserName)?.name ?: "null")
            .replace("%servername%", serverName)
            .replace("%prefix%", prefix)
            .replace("%player%", userName)
            .replace("%suffix%", suffix)
            .replace("%message%", rawMessage)
    }

    override fun getPlayerList(): List<Component> {
        val playerList = mutableListOf<String>()
        userService.onlinePlayers.forEach { playerList.add(userService.getUserName(it)) }
        val tempList = mutableListOf<Component>()
        var builder = StringBuilder()
        for (s in playerList) {
            if (builder.length + s.length < 50) {
                builder.append(" ").append(s)
            } else {
                tempList.add(Component.text(builder.toString()))
                builder = StringBuilder(s)
            }
        }
        return tempList
    }

    override fun sendList(commandSource: TCommandSource) {
/*    textService.paginationBuilder()
      .header(textService.builder().green().append("Online Players").build())
      .padding(textService.of("-"))
      .contents(playerList)
      .build()
      .sendTo(commandSource)*/
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
        for (player in userService.onlinePlayers) {
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

    override fun sendChatMessage(event: ChatEvent<TPlayer>) {
        val prefix = luckpermsService.getPrefix(event.player)
        val chatColor = luckpermsService.getChatColor(event.player)
        val nameColor = luckpermsService.getNameColor(event.player)
        val suffix = luckpermsService.getSuffix(event.player)
        val userName = userService.getUserName(event.player)
        val server = locationService.getServer(userService.getUUID(event.player)!!)?.name ?: "null"
        val playerUUID = userService.getUUID(event.player)!!
        var message = event.rawMessage
        val channelId = channelService.getChannelIdForUser(playerUUID)
        val hasColorPermission: Boolean = permissionService.hasPermission(event.player, registry.getOrDefault(CatalystKeys.CHAT_COLOR_PERMISSION))
        message = chatColor + message
        formatMessage(
            prefix,
            nameColor,
            userName,
            playerUUID,
            message,
            hasColorPermission,
            suffix,
            server,
            channelId
        ).thenApplyAsync { message ->
            if (message != null) {
                //TODO serialize plain
                logger.info(message.toString())
                sendMessageToChannel(channelId, message, playerUUID)
                return@thenApplyAsync message
            } else {
                pluginMessages.muted.sendTo(event.player as TCommandSource)
                return@thenApplyAsync null
            }
        }.join()
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
