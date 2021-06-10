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
import com.vdurmont.emoji.EmojiParser
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.discord.DiscordCommandService
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.registry.ChatChannel
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.EmojiService
import org.slf4j.Logger
import java.util.stream.Collectors

class CommonDiscordListener<TString, TPlayer, TCommandSource> @Inject constructor(
  private val registry: Registry,
  private val userService: UserService<TPlayer, TPlayer>,
  private val discordCommandService: DiscordCommandService,
  private val textService: TextService<TString, TCommandSource>,
  private val logger: Logger,
  private val emojiService: EmojiService,
  private val chatService: ChatService<TString, TPlayer, TCommandSource>
) : ListenerAdapter() {
  
  override fun onMessageReceived(event: MessageReceivedEvent) {
    if (event.isWebhookMessage || event.author.isBot) {
      return
    }
    var message = EmojiParser.parseToAliases(event.message.contentDisplay)
    val messageRaw = event.message.toString()
    if (event.member != null && event.member!!.hasPermission(Permission.ADMINISTRATOR)
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
      val playerNames: String
      playerNames = if (onlinePlayers.size == 0) {
        "```There are currently no players online!```"
      } else {
        ("**Online Players:**```"
          + userService.onlinePlayers.stream()
          .map { p: TPlayer -> userService.getUserName(p) }
          .collect(Collectors.joining(", ")) + "```")
      }
      event.channel.sendMessage(playerNames).queue()
      return
    }
    if (registry.getOrDefault(CatalystKeys.EMOJI_ENABLE)) {
      for (key in registry.getOrDefault(CatalystKeys.EMOJI_MAP).keys) {
        message = message.replace(key, emojiService.emojis[key].toString())
      }
      sendMessage(event.channel.id, message, event.member!!.effectiveName)
    }
    logger.info("[Discord] " + event.member!!.effectiveName + " : " + EmojiParser.parseToAliases(event.message.contentDisplay))
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
    if (targetChannel == null && channelId == registry.getOrDefault(CatalystKeys.DISCORD_MAIN_CHANNEL)) {
      val mainChannel = chatService.getChannelFromId(registry.getOrDefault(CatalystKeys.CHAT_DEFAULT_CHANNEL))
      targetChannel = if (mainChannel.isPresent) {
        mainChannel.get()
      } else {
        logger.error("Could not fall back to the main discord channel! Please check your configuration!")
        return
      }
    }
    val finalMessage = textService.builder()
      .append(
        textService.deserialize(
          registry.getOrDefault(CatalystKeys.DISCORD_CHAT_FORMAT)
            .replace("%name%", userName)
            .replace("%message%", message)
        )
      )
      .onClickOpenUrl(registry.getOrDefault(CatalystKeys.DISCORD_URL))
      .onHoverShowText(textService.of(registry.getOrDefault(CatalystKeys.DISCORD_HOVER_MESSAGE)))
      .build()
    for (player in chatService.getUsersInChannel(targetChannel!!.id)) {
      textService.send(finalMessage, player as TCommandSource)
    }
  }
}
