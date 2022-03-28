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
package org.anvilpowered.catalyst.common.discord

import com.google.inject.Inject
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.discord.DiscordCommandService
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.registry.ChatChannel
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.anvil.api.registry.Registry
import org.slf4j.Logger
import java.util.stream.Collectors

class CommonDiscordListener<TPlayer> @Inject constructor(
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>,
    private val discordCommandService: DiscordCommandService,
    private val logger: Logger,
    private val channelService: ChannelService<TPlayer>
) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.isWebhookMessage || event.author.isBot) {
            return
        }
        val messageRaw = event.message.toString()
        if (event.member != null
            && event.member!!.hasPermission(Permission.ADMINISTRATOR)
            && messageRaw.contains("!cmd")
        ) {
            val command = event.message.contentRaw.replace("!cmd ", "")
            discordCommandService.channelId = event.channel.id
            discordCommandService.executeDiscordCommand(command)
            return
        } else if (messageRaw.contains("!players")
            || messageRaw.contains("!online")
            || messageRaw.contains("!list")
        ) {
            val onlinePlayers = userService.onlinePlayers
            val playerNames: String = if (onlinePlayers.isEmpty()) {
                "```There are currently no players online!```"
            } else {
                ("**Online Players:**```"
                    + userService.onlinePlayers.stream()
                    .map { userService.getUserName(it) }
                    .collect(Collectors.joining(", ")) + "```")
            }
            event.channel.sendMessage(playerNames).queue()
            return
        }
        sendMessage(event.channel.id, messageRaw, event.member!!.effectiveName)
        logger.info("[Discord] " + event.member!!.effectiveName + " : " + event.message.contentDisplay)
    }

    private fun sendMessage(channelId: String, message: String, userName: String) {
        val channels = registry.getOrDefault(CatalystKeys.CHAT_CHANNELS)
        var targetChannel: ChatChannel? = null
        for (channel in channels) {
            if (channel.discordChannel == null) {
                continue
            }
            if (channel.discordChannel == channelId) {
                targetChannel = channel
            }
        }
        if (targetChannel == null) {
            val mainChannel = channelService.defaultChannel
            targetChannel = if (mainChannel != null) {
                mainChannel
            } else {
                logger.error("Could not find a channel to fall back to! Please ensure you have properly setup discord channels in your config.")
                return
            }
        }
        val finalMessage = Component.text()
            .append(LegacyComponentSerializer.legacyAmpersand().deserialize(
                registry.getOrDefault(CatalystKeys.DISCORD_CHAT_FORMAT)
                    .replace("%name%", userName)
                    .replace("%message%", message)
            ))
            .clickEvent(ClickEvent.openUrl(registry.getOrDefault(CatalystKeys.DISCORD_URL)))
            .hoverEvent(HoverEvent.showText(LegacyComponentSerializer.legacyAmpersand().deserialize(registry.getOrDefault(CatalystKeys.DISCORD_HOVER_MESSAGE))))
            .build()
        for (player in channelService.getUsersInChannel(targetChannel.id)) {
            finalMessage.sendTo(player)
        }
    }
}
