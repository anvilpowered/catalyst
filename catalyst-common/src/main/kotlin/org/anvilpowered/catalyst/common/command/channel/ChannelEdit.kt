/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.common.command.channel

import org.anvilpowered.anvil.api.registry.ConfigurationService
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.registry.ChatChannel
import java.util.UUID

object ChannelEdit {

  val currentChannel = mutableMapOf<UUID, ChatChannel>()
  val editedChannel = mutableMapOf<UUID, ChatChannel>()

  fun editProperty(uuid: UUID, propertyName: String, channel: ChatChannel, value: String): Boolean {
    when (propertyName.toLowerCase()) {
      "format" -> {
        channel.format = value
      }
      "hover", "hovermessage" -> {
        channel.hoverMessage = value
      }
      "click", "onclick", "clickcommand" -> {
        channel.click = value
      }
      "discord", "discordid", "discordchannelid", "discordchannel" -> {
        channel.discordChannel = value
      }
      "visibility", "alwaysvisible" -> {
        channel.alwaysVisible = value.toBoolean()
      }
      "id" -> {
        channel.id = value
      }
      "servers", "server" -> {
        channel.servers = channel.servers.plus(value)
      }
      "passthrough" -> {
        channel.passthrough = value.toBoolean()
      }
      else -> return false
    }

    editedChannel[uuid] = channel
    return true
  }

  fun commit(uuid: UUID, registry: Registry) {
    registry.transform(CatalystKeys.CHAT_CHANNELS) { it: MutableList<ChatChannel>? ->
      it?.remove(currentChannel[uuid])
      it?.add(editedChannel[uuid]!!)
      it
    }
    (registry as? ConfigurationService)?.save()
    currentChannel.remove(uuid)
    editedChannel.remove(uuid)
  }
}
