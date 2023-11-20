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

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Webhook
import org.anvilpowered.anvil.core.command.CommandSource
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.config.CatalystKeys

context(Registry.Scope, PlayerService.Scope, JDAService.Scope)
class WebhookSender {

    private val httpClient = HttpClient(CIO)

    suspend fun sendWebhookMessage(player: String, message: String, channelId: String, source: CommandSource) {
        sendWebhook(
            getWebhook(channelId) ?: return,
            WebhookPackage(
                registry[CatalystKeys.WEBHOOK_URL].replace("%uuid%", source.player?.id.toString()),
                player.withoutColor(),
                message.withoutColor(),
            ),
        )
    }

    suspend fun sendConsoleWebhookMessage(message: String, channelId: String) {
        sendWebhook(
            getWebhook(channelId) ?: return,
            WebhookPackage(
                "",
                "Console",
                message.withoutColor(),
            ),
        )
    }

    private suspend fun sendWebhook(webhook: Webhook, webhookPackage: WebhookPackage) {
        httpClient.post(webhook.url) {
            contentType(ContentType.Application.Json)
            setBody(webhookPackage)
        }
    }

    private fun getWebhook(channelID: String): Webhook? {
        if (channelID.isEmpty()) {
            return null
        }
        val channel = jdaService.jda?.getTextChannelById(channelID) ?: throw AssertionError("Discord channel may not be null!")
        if (channel.guild.selfMember.hasPermission(Permission.MANAGE_WEBHOOKS)) {
            throw AssertionError("Please allow the discord bot to handle webhooks!")
        }
        return channel.guild
            .retrieveWebhooks().complete().stream().filter { it.name.equals("Catalyst-DB: " + channel.name, ignoreCase = true) }
            .findFirst().orElse(null) ?: channel.createWebhook("Catalyst-DB " + channel.name).complete()
    }

    private fun String.withoutColor() = replace("&[0-9a-fklmnor]".toRegex(), "")

    interface Scope {
        val webhookSender: WebhookSender
    }

    class WebhookPackage(var avatarUrl: String, var name: String, var message: String)
}
