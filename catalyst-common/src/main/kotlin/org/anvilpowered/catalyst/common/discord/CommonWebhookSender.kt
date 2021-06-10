/*
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.catalyst.common.discord

import com.google.inject.Inject
import com.mashape.unirest.http.Unirest
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.Webhook
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.discord.JDAService
import org.anvilpowered.catalyst.api.discord.WebhookSender
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.json.JSONObject
import java.util.Objects

class CommonWebhookSender<TPlayer> @Inject constructor(
  private val registry: Registry,
  private val jdaService: JDAService,
  private val userService: UserService<TPlayer, TPlayer>,
) : WebhookSender {

  private val COLOR_REGEX = "&(0-9a-fA-FlkKrR)"

  override fun sendWebhookMessage(webHook: String, player: String, message: String, channelId: String, source: Any) {
    val webhook = getWebhook(channelId)
    if (webHook == null) {
      return
    }
    if (webhook != null) {
      sendWebhook(
        webhook, org.anvilpowered.catalyst.api.discord.Webhook.of(
          registry.getOrDefault(CatalystKeys.WEBHOOK_URL)
            .replace("%uuid%", userService.getUUID(source as TPlayer).toString()),
          player.replace(COLOR_REGEX.toRegex(), ""),
          message.replace(COLOR_REGEX.toRegex(), "")
        )
      )
    }
  }

  override fun sendConsoleWebhookMessage(webHook: String, message: String, channelId: String) {
    val webhook = getWebhook(channelId)
    if (webhook != null) {
      sendWebhook(
        webhook, org.anvilpowered.catalyst.api.discord.Webhook.of(
          "",
          "Console",
          message.replace(COLOR_REGEX.toRegex(), "")
        )
      )
    }
  }

  override fun sendWebhook(webhook: Webhook, webhookUtils: org.anvilpowered.catalyst.api.discord.Webhook) {
    val jsonObject = JSONObject()
    jsonObject.put("content", webhookUtils.message)
      .put("username", webhookUtils.name.replace(COLOR_REGEX.toRegex(), ""))
      .put("avatar_url", webhookUtils.avatarURL)
    try {
      Unirest.post(webhook.url).header("Content-Type", "application/json").body(jsonObject).asJsonAsync()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  override fun getWebhook(channelID: String): Webhook? {
    if (channelID == "") {
      return null
    }
    val channel = jdaService.jda.getTextChannelById(channelID)
    if (!Objects.requireNonNull(channel)!!.guild.selfMember.hasPermission(Permission.MANAGE_WEBHOOKS)) {
      throw AssertionError("Please allow the discord bot to handle webhooks!")
    }
    var webhook = channel!!.guild
      .retrieveWebhooks().complete().stream().filter { it.name.equals("Catalyst-DB: " + channel.name, ignoreCase = true) }
      .findFirst().orElse(null)
    if (webhook == null) {
      webhook = channel.createWebhook("Catalyst-DB: " + channel.name).complete()
    }
    return webhook
  }
}
