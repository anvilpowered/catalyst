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
package org.anvilpowered.catalyst.velocity.discord

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Webhook
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.config.SimpleKey
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.anvilpowered.catalyst.api.user.getOnlineUser
import org.apache.logging.log4j.Logger
import java.util.UUID

class WebhookSender(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val logger: Logger,
    private val jdaService: JDAService,
    private val luckpermsService: LuckpermsService,
    private val minecraftUserRepository: MinecraftUserRepository,
    private val onlineUserFormatResolver: OnlineUserFormat.Resolver,
) {

    private val httpClient = HttpClient(CIO)

    suspend fun sendChannelMessage(player: Player, content: Component, discordChannelId: String) {
        val user = minecraftUserRepository.getOnlineUser(player)
        getWebhook(discordChannelId)?.send(
            WebhookPackage(
                registry[catalystKeys.AVATAR_URL].replace("%uuid%", player.uniqueId.toString()),
                PlainTextComponentSerializer.plainText()
                    .serialize(onlineUserFormatResolver.resolve(registry[catalystKeys.DISCORD_USERNAME_FORMAT], user)),
                PlainTextComponentSerializer.plainText().serialize(content),
            ),
        )
    }

    suspend fun sendSpecialMessage(
        player: Player,
        discordChannelId: String,
        messageKey: SimpleKey<OnlineUserFormat>,
    ) {
        val user = minecraftUserRepository.getOnlineUser(player)
        getWebhook(discordChannelId)?.send(
            WebhookPackage(
                registry[catalystKeys.AVATAR_URL].replace("%uuid%", player.uniqueId.toString()),
                "System",
                PlainTextComponentSerializer.plainText()
                    .serialize(onlineUserFormatResolver.resolve(registry[messageKey], user)),
            ),
        )
    }

    suspend fun sendLeaveMessage(userId: UUID, username: String, discordChannelId: String) {
        getWebhook(discordChannelId)?.send(
            WebhookPackage(
                registry[catalystKeys.AVATAR_URL].replace("%uuid%", userId.toString()),
                "System",
                "$username has left the game.",
            ),
        )
    }

    suspend fun sendConsoleChatMessage(message: String, channelId: String) {
        getWebhook(channelId)?.send(
            WebhookPackage(
                "",
                "Console",
                message,
            ),
        )
    }

    private suspend fun Webhook.send(webhookPackage: WebhookPackage) {
        httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(webhookPackage)
        }
    }

    private fun getWebhook(discordChannelId: String): Webhook? {
        val channel = jdaService.jda?.getTextChannelById(discordChannelId) ?: throw AssertionError("Discord channel may not be null!")
        if (channel.guild.selfMember.hasPermission(Permission.MANAGE_WEBHOOKS)) {
            throw AssertionError("Please allow the discord bot to handle webhooks!")
        }
        return channel.guild
            .retrieveWebhooks().complete().stream().filter { it.name.equals("Catalyst-DB: " + channel.name, ignoreCase = true) }
            .findFirst().orElse(null) ?: channel.createWebhook("Catalyst-DB " + channel.name).complete()
    }

    @Serializable
    class WebhookPackage(var avatarUrl: String, var name: String, var message: String)
}
