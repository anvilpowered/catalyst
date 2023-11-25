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

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChatEvent
import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.chat.build
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.anvilpowered.catalyst.api.user.getOnlineUser
import org.anvilpowered.catalyst.velocity.chat.ChatFilter
import org.anvilpowered.catalyst.velocity.chat.ChatService
import org.apache.logging.log4j.Logger

class ChatListener(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val logger: Logger,
    private val chatService: ChatService,
    private val channelService: ChannelService,
    private val luckpermsService: LuckpermsService,
    private val chatFilter: ChatFilter,
    private val channelMessageBuilderFactory: ChannelMessage.Builder.Factory,
    private val minecraftUserRepository: MinecraftUserRepository,
) {
    @Subscribe
    fun onPlayerChat(event: PlayerChatEvent) = runBlocking {
        logger.info("Player ${event.player.username} sent message: ${event.message}")
        val player = event.player
        if (!registry[catalystKeys.PROXY_CHAT_ENABLED] ||
            chatService.isDisabledForPlayer(player) ||
            channelService.getForPlayer(player.uniqueId).passthrough
        ) {
            return@runBlocking
        }
        event.result = PlayerChatEvent.ChatResult.denied()
        val rawMessage = if (player.hasPermission(registry[catalystKeys.CHAT_COLOR_PERMISSION])) {
            MiniMessage.miniMessage().deserialize(event.message)
        } else {
            Component.text(event.message)
        }
        // TODO: Move this to dedicated class
        var rawContent = chatService.highlightPlayerNames(player, rawMessage)
        if (!player.hasPermission(registry[catalystKeys.LANGUAGE_ADMIN_PERMISSION]) &&
            registry[catalystKeys.CHAT_FILTER_ENABLED]
        ) {
            rawContent = chatFilter.replaceSwears(rawContent)
        }

        val user = minecraftUserRepository.getOnlineUser(player)

        val formattedRawContent = OnlineUserFormat(rawContent, OnlineUserFormat.Placeholders(listOf("source")))
            .resolve(proxyServer, logger, luckpermsService, user)

        val channel = channelService.getForPlayer(player.uniqueId)

        val channelMessage = channelMessageBuilderFactory.build {
            user(user.user)
            channel(channel)
            rawContent(formattedRawContent)
        }

        val formatted = channel.messageFormat.resolve(channelMessage)
        val resolved = ChannelMessage.Resolved(channelMessage, formatted)
        proxyServer.eventManager.fire(ChannelMessage.Event(resolved))
        chatService.sendMessage(resolved)
    }
}
