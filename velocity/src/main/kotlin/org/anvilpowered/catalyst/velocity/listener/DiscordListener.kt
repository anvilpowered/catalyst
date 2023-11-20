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

package org.anvilpowered.catalyst.velocity.listener

import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.core.command.CommandExecutor
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.velocity.CatalystApi

context(CatalystApi, CommandExecutor.Scope, LoggerScope, ChannelService.Scope, PlayerService.Scope)
internal class DiscordListener : ListenerAdapter() {

    private val loggingCommandExecutor = CommandExecutor.withLogging("discord")

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
            // TODO: Use coroutines properly
            runBlocking {
                loggingCommandExecutor.executeAsConsole(command)
            }
            return
        } else if (messageRaw.contains("!players")
            || messageRaw.contains("!online")
            || messageRaw.contains("!list")
        ) {
            val onlinePlayers = playerService.getAll().toList()
            val playerNames: String = if (onlinePlayers.isEmpty()) {
                "```There are currently no players online!```"
            } else {
                onlinePlayers.joinToString(
                    separator = ", ",
                    prefix = "**Online Players:**```",
                    postfix = "```",
                ) { it.username }
            }
            event.channel.sendMessage(playerNames).queue()
            return
        }
        sendMessage(event.channel.id, messageRaw, event.member!!.effectiveName)
        logger.info("[Discord] " + event.member!!.effectiveName + " : " + event.message.contentDisplay)
    }

    private fun sendMessage(channelId: String, message: String, userName: String) {
        val targetChannel = registry[CatalystKeys.CHAT_CHANNELS].values.firstOrNull { it.discordChannel == channelId }
            ?: channelService.defaultChannel

        val finalMessage = Component.text()
            .append(
                LegacyComponentSerializer.legacyAmpersand().deserialize(
                    registry[CatalystKeys.DISCORD_CHAT_FORMAT]
                        .replace("%name%", userName)
                        .replace("%message%", message),
                ),
            )
            .clickEvent(ClickEvent.openUrl(registry[CatalystKeys.DISCORD_URL]))
            .hoverEvent(
                HoverEvent.showText(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(registry[CatalystKeys.DISCORD_HOVER_MESSAGE]),
                ),
            )
            .build()

        for (player in channelService.getPlayers(targetChannel.id)) {
            player.sendMessage(finalMessage)
        }
    }

    interface Scope {
        val discordListener: DiscordListener
    }
}
