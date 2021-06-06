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

package org.anvilpowered.catalyst.common.command

import com.google.inject.Inject
import com.mojang.brigadier.context.CommandContext
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.BackendServer
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.common.command.channel.ChannelEdit
import org.anvilpowered.catalyst.common.registry.ProxyConfigurationService
import java.util.ArrayList
import java.util.Optional

class ChannelCommand<TString, TPlayer : TCommandSource, TCommandSource> @Inject constructor(
  private val chatService: ChatService<TString, TPlayer, TCommandSource>,
  private val locationService: LocationService,
  private val permissionService: PermissionService,
  private val configurationService: ProxyConfigurationService,
  private val registry: Registry,
  private val textService: TextService<TString, TCommandSource>,
  private val userService: UserService<TPlayer, TPlayer>
) {

  private fun exists(channelId: String): Boolean = registry.getOrDefault(CatalystKeys.CHAT_CHANNELS).any { it.id == channelId }

  fun set(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
    val source = context.source
    if (!playerClass.isAssignableFrom(source!!::class.java)) {
      textService.send(textService.of("Player only command!"), source)
      return 0
    }
    val channel = context.getArgument("channel", String::class.java)
    val userName = userService.getUserName(source as TPlayer)

    if (channel.contains(" ")) {
      return 0
    }

    if (!exists(channel)) {
      textService.send(textService.of("Invalid channel!"), source)
      return 0
    }

    val chatChannel = chatService.getChannelFromId(channel).get()
    if (channel == chatService.getChannelIdForUser(userService.getUUID(source as TPlayer))) {
      textService.builder()
        .appendPrefix()
        .yellow().append("You are already in \"")
        .green().append(channel)
        .yellow().append("\"!")
        .sendTo(source)
      return 0
    }

    val currentServer = locationService.getServer(userName) as Optional<BackendServer>
    if (!currentServer.isPresent) {
      return 0
    }
    val server = currentServer.get().name

    if (permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION).toString() + channel)) {
      if (!chatChannel.servers.contains(server)
        && !permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION))
        && !chatChannel.servers.contains("*")
      ) {
        textService.builder()
          .appendPrefix()
          .yellow().append("Could not join ")
          .green().append(chatChannel.id)
          .yellow().append(" because you are not in a permitted server!")
          .sendTo(source)
        return 0
      }
      chatService.switchChannel(userService.getUUID(source), channel)
      textService.send(
        textService.builder()
          .green().append("Successfully switched to ")
          .gold().append(channel)
          .build(),
        source
      )
    }
    return 1
  }

  fun abort(context: CommandContext<TCommandSource>): Int {
    val source = context.source
    if (ChannelEdit.currentChannel.containsKey(source.uuid())) {
      val channelId = ChannelEdit.currentChannel[source.uuid()]?.id
      ChannelEdit.currentChannel.remove(source.uuid())
      ChannelEdit.editedChannel.remove(source.uuid())
      textService.send(
        textService.success("You are no longer editing $channelId"),
        source
      )
      return 1
    }
    textService.send(
      textService.fail("You are not currently editing a channel!"),
      source
    )
    return 0
  }

  fun list(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
    if (!playerClass.isAssignableFrom(context.source!!::class.java)) {
      textService.send(textService.of("Player only command!"), context.source)
      return 0
    }
    val basePerm = registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION)
    val channels = registry.getOrDefault(CatalystKeys.CHAT_CHANNELS)
    val availableChannels: MutableList<TString> = ArrayList()
    for (channel in channels) {
      if (permissionService.hasPermission(context.source, basePerm + channel.id)) {
        availableChannels.add(
          channelInfo(
            channel.id, chatService.getChannelIdForUser(context.source.uuid()) == channel.id
          )
        )
      }
    }
    textService.paginationBuilder()
      .title(textService.builder().gold().append("Available Channels").build())
      .padding(textService.builder().dark_green().append("-"))
      .contents(availableChannels).linesPerPage(20)
      .build().sendTo(context.source)
    return 1
  }

  private fun channelInfo(channelId: String, active: Boolean): TString {
    val component = textService.builder()
    if (active) {
      component.green().append(channelId)
    } else {
      component.gray().append(channelId).onClickRunCommand("/channel set $channelId")
    }
    component.onHoverShowText(
      textService.builder()
        .gray().append("Status: ")
        .green().append(if (active) "Active" else "Inactive")
        .gray().append("\nActive Users: ")
        .green().append(chatService.getChannelUserCount(channelId))
        .build()
    )
    return component.build()
  }

  fun startEdit(context: CommandContext<TCommandSource>): Int {
    val source = context.source
    val channelId = context.getArgument("channel", String::class.java)
    if (!exists(channelId)) {
      textService.builder()
        .appendPrefix()
        .red().append("Invalid Channel ID ")
        .gold().append(context.getArgument("channel", String::class.java))
        .sendTo(source)
      return 0
    }
    ChannelEdit.currentChannel[source.uuid()] = chatService.getChannelFromId(channelId).get()
    textService.send(editableInfo(channelId), source)
    return 1
  }

  fun editProperty(context: CommandContext<TCommandSource>): Int {
    val source = context.source
    val uuid = source.uuid()

    if (!ChannelEdit.currentChannel.containsKey(uuid)) {
      textService.send(
        textService.fail("You must select a channel to edit first!"),
        source
      )
      return 0
    }

    val channel = ChannelEdit.currentChannel[uuid]!!
    val value = context.getArgument("value", String::class.java)

    val edited = ChannelEdit.editProperty(source.uuid(), context.getArgument("name", String::class.java), channel, value)
    if (edited) {
      textService.builder()
        .green().append("Successfully edited property ")
        .gold().append(context.getArgument("name", String::class.java))
        .green().append(
          textService.builder()
            .append("\nClick here to commit changes.")
            .onClickRunCommand("/channel edit commit")
            .build()
        )
        .sendTo(source)
    } else {
      return 0
    }

    return 1
  }

  fun commit(context: CommandContext<TCommandSource>): Int {
    val source = context.source
    if (!ChannelEdit.editedChannel.containsKey(source.uuid())) {
      textService.send(
        textService.fail("You must edit a channel before you can commit changes!"),
        source
      )
      return 0
    }
    textService.builder()
      .green().append("Successfully edited ")
      .gold().append(ChannelEdit.editedChannel[source.uuid()]?.id)
      .sendTo(source)
    ChannelEdit.commit(source.uuid(), registry, configurationService)
    return 1
  }

  fun info(context: CommandContext<TCommandSource>): Int {
    val info = buildInfo(context.getArgument("channel", String::class.java))
    val source = context.source
    if (info == null) {
      textService.builder()
        .appendPrefix()
        .red().append("Failed to edit channel")
        .gold().append(context.getArgument("channel", String::class.java))
        .sendTo(source)
      return 0
    }
    textService.send(info, source)
    return 1
  }

  private fun buildInfo(channelId: String): TString? {
    if (!exists(channelId)) {
      return null
    }

    val chatChannel = chatService.getChannelFromId(channelId).get()
    val format = chatChannel.format
    val hoverMessage = chatChannel.hoverMessage
    val click = chatChannel.click
    val servers = chatChannel.servers
    val visibility = chatChannel.alwaysVisible
    val discordChannel = chatChannel.discordChannel

    return textService.builder()
      .append(infoBar(channelId))
      .append(basicProperty("Active Users", chatService.getUsersInChannel(channelId).size.toString()))
      .append(basicProperty("Format", format))
      .append(basicProperty("Hover Message", hoverMessage))
      .append(basicProperty("OnClick", click))
      .append(basicProperty("Server Requirement", servers.joinToString(", ")))
      .append(basicProperty("Always Visible", visibility.toString()))
      .append(basicProperty("Discord Channel ID", discordChannel))
      .build()
  }

  private fun editableInfo(channelId: String): TString? {
    if (!exists(channelId)) {
      return null
    }

    val chatChannel = chatService.getChannelFromId(channelId).get()
    return textService.builder()
      .append(editBar(channelId))
      .append(basicProperty("Active Users", chatService.getUsersInChannel(channelId).size.toString()))
      .append(editableProperty(channelId, "Format", chatChannel.format))
      .append(editableProperty(channelId, "Hover Message", chatChannel.hoverMessage))
      .append(editableProperty(channelId, "OnClick", chatChannel.click))
      .append(editableProperty(channelId, "Server Requirement", chatChannel.servers.joinToString(", ")))
      .append(editableProperty(channelId, "Always Visible", chatChannel.alwaysVisible.toString()))
      .append(editableProperty(channelId, "Discord Channel ID", chatChannel.discordChannel))
      .build()
  }

  private fun infoBar(name: String): TString {
    return textService.builder()
      .dark_green().append("========= ")
      .gold().append("Channel - ", name)
      .dark_green().append(" =========\n")
      .build()
  }

  private fun editBar(name: String): TString {
    return textService.builder()
      .dark_green().append("========= ")
      .gold().append("Edit Channel - ", name)
      .dark_green().append(" =========\n")
      .build()
  }

  private fun basicProperty(name: String, value: String): TString {
    return textService.builder()
      .gray().append("\n", name, ": ")
      .yellow().append(value)
      .build()
  }

  private fun editableProperty(channelId: String, name: String, value: String): TString {
    val propertyName = name.replace("\\s+".toRegex(), "")
    return textService.builder()
      .gray().append("\n", name, ": ")
      .yellow().append(value)
      .onHoverShowText(
        textService.builder()
          .green().append("Click to edit.")
          .build()
      )
      .onClickSuggestCommand("/channel edit property ${propertyName.toLowerCase()} \"\"")
      .build()
  }

  private fun TCommandSource.uuid() = userService.getUUID(this as TPlayer)
}
