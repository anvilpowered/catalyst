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
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.registry.CatalystKeys.PRIVATE_MESSAGE_FORMAT
import org.anvilpowered.catalyst.api.service.PrivateMessageService
import java.util.HashMap
import java.util.HashSet
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Singleton
class CommonPrivateMessageService<TPlayer, TString, TCommandSource> @Inject constructor(
  private val userService: UserService<TPlayer, TPlayer>,
  private val textService: TextService<TString, TCommandSource>,
  private val registry: Registry,
) : PrivateMessageService<TString> {
  private val socialSpySet: Set<UUID> = HashSet()
  private val replyMap: Map<UUID, UUID> = HashMap()
  private lateinit var source: String
  private lateinit var recipient: String
  private lateinit var rawMessage: String

  override fun socialSpySet(): Set<UUID> = socialSpySet
  override fun replyMap(): Map<UUID, UUID> = replyMap
  override fun getSource(): String = source
  override fun getRawMessage(): String = rawMessage
  override fun getRecipient(): String = recipient

  override fun setSource(sourceUserName: String) {
    source = sourceUserName
  }

  override fun setRecipient(recipient: String) {
    this.recipient = recipient
  }

  override fun setRawMessage(rawMessage: String) {
    this.rawMessage = rawMessage
  }

  override fun formatMessage(sender: String, recipient: String, rawMessage: String): TString {
    return textService.deserialize(
      registry.getOrDefault(PRIVATE_MESSAGE_FORMAT)
        .replace("%sender%", sender)
        .replace("%recipient%", recipient)
        .replace("%message%", rawMessage.trim { it <= ' ' })
    )
  }

  override fun sendMessage(sender: String, recipient: String, rawMessage: String): CompletableFuture<Void> {
    return CompletableFuture.runAsync {
      userService[sender].ifPresent { src: TPlayer ->
        textService.send(formatMessage("Me", recipient, rawMessage), src as TCommandSource)
        userService[recipient].ifPresent { textService.send(formatMessage(sender, "Me", rawMessage), it as TCommandSource) }
        socialSpy(sender, recipient, rawMessage)
      }
    }
  }

  override fun sendMessageFromConsole(recipient: String, rawMessage: String, console: Class<*>?): CompletableFuture<Void> {
    return CompletableFuture.runAsync {
      userService[recipient].ifPresent { textService.send(formatMessage("Console", "Me", rawMessage), it as TCommandSource) }
    }
  }

  override fun socialSpy(sender: String, recipient: String, rawMessage: String): CompletableFuture<Void> {
    return CompletableFuture.runAsync {
      userService.onlinePlayers.forEach {
        if (socialSpySet.isEmpty() && !socialSpySet.contains(userService.getUUID(it))) {
          return@forEach
        }
        if (userService.getUserName(it).equals(sender, ignoreCase = true)
          || userService.getUserName(it).equals(recipient, ignoreCase = true)
        ) {
          return@forEach
        }
        textService.send(formatSocialSpyMessage(sender, recipient, rawMessage), it as TCommandSource)
      }
    }
  }

  override fun formatSocialSpyMessage(sender: String, recipient: String, rawMessage: String): TString {
    return textService.builder()
      .gray().append("[SocialSpy] ")
      .dark_gray().append("[")
      .blue().append(sender)
      .gold().append(" -> ")
      .blue().append(recipient)
      .dark_gray().append("] ")
      .gray().append(rawMessage.trim { it <= ' ' })
      .build()
  }
}
