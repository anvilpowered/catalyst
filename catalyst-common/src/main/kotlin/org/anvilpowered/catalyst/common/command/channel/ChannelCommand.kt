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

package org.anvilpowered.catalyst.common.command.channel

import com.google.inject.Inject
import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.feature.pagination.Pagination
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.common.command.getArgument

class ChannelCommand<TPlayer : TCommandSource, TCommandSource> @Inject constructor(
    private val channelService: ChannelService<TPlayer>,
    private val locationService: LocationService,
    private val permissionService: PermissionService,
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>,
    private val pluginInfo: PluginInfo
) {

    private fun exists(channelId: String): Boolean = registry.getOrDefault(CatalystKeys.CHAT_CHANNELS).any { it.id == channelId }

    fun setAlias(context: CommandContext<TCommandSource>, channelId: String, playerClass: Class<*>): Int {
        val source = context.source
        if (!playerClass.isAssignableFrom(source!!::class.java)) {
            Component.text("Player only command!").sendTo(source)
            return 0
        }
        val userName = userService.getUserName(source as TPlayer)

        if (!exists(channelId)) {
            Component.text("Invalid Channel!").color(NamedTextColor.YELLOW).sendTo(source)
            return 0
        }
        val chatChannel = channelService.fromId(channelId)
        if (channelId == channelService.fromUUID(userService.getUUID(source as TPlayer)!!).id) {
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("You are already in \"").color(NamedTextColor.YELLOW))
                .append(Component.text(channelId).color(NamedTextColor.GREEN))
                .append(Component.text("\"!").color(NamedTextColor.YELLOW))
                .sendTo(source)
            return 0
        }

        val currentServer = locationService.getServer(userName) ?: return 0
        val server = currentServer.name

        if (permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION).toString() + channelId)) {
            if (!chatChannel?.servers?.contains(server)!!
                && !permissionService.hasPermission(source, registry.getOrDefault(CatalystKeys.ALL_CHAT_CHANNELS_PERMISSION))
                && !chatChannel.servers.contains("*")
            ) {
                Component.text()
                    .append(pluginInfo.prefix)
                    .append(Component.text("Could not join ").color(NamedTextColor.YELLOW))
                    .append(Component.text(chatChannel.id).color(NamedTextColor.GREEN))
                    .append(Component.text(" because you are not in a permitted server!").color(NamedTextColor.YELLOW))
                    .sendTo(source)
                return 0
            }
            channelService.switch(userService.getUUID(source)!!, channelId)
            Component.text()
                .append(Component.text("Successfully switched to ").color(NamedTextColor.GREEN))
                .append(Component.text(channelId).color(NamedTextColor.GOLD))
                .append(Component.text(" channel.").color(NamedTextColor.GREEN))
                .sendTo(source)
        }
        return 1
    }

    fun set(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
        val channel = context.getArgument<String>("channel")
        if (channel.contains(" ")) {
            Component.text()
                .append(Component.text("Channel names mut not contain a space!").color(NamedTextColor.RED))
                .sendTo(context.source)
            return 0
        }
        return setAlias(context, channel, playerClass)
    }

    fun abortEdit(context: CommandContext<TCommandSource>): Int {
        val source = context.source
        if (ChannelEdit.currentChannel.containsKey(source.uuid())) {
            val channelId = ChannelEdit.currentChannel[source.uuid()]?.id
            ChannelEdit.currentChannel.remove(source.uuid())
            ChannelEdit.editedChannel.remove(source.uuid())
            Component.text("You are no longer editing $channelId").color(NamedTextColor.GREEN).sendTo(source)
            return 1
        }
        Component.text("You are not currently editing a channel.").color(NamedTextColor.RED).sendTo(source)
        return 0
    }

    fun list(context: CommandContext<TCommandSource>, playerClass: Class<*>): Int {
        if (!playerClass.isAssignableFrom(context.source!!::class.java)) {
            Component.text("Player only command!").sendTo(context.source)
            return 0
        }
        val basePerm = registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION)
        val channels = registry.getOrDefault(CatalystKeys.CHAT_CHANNELS)
        val availableChannels: MutableList<Component> = ArrayList()
        for (channel in channels) {
            if (permissionService.hasPermission(context.source, basePerm + channel.id)) {
                availableChannels.add(channelInfo(channel.id, channelService.fromUUID(context.source.uuid()!!).id == channel.id))
            }
        }

        val pagination = Pagination.builder()
            .resultsPerPage(availableChannels.size)
            .build(
                Component.text("Available Channels").color(NamedTextColor.GOLD),
                Renderer()
            ) { page -> "/page $page" }

        val rendered = pagination.render(availableChannels, 1)
        for (i in availableChannels.indices) {
            rendered[i].sendTo(context.source)
        }
        return 1
    }

    private inner class Renderer : Pagination.Renderer.RowRenderer<Component> {
        val rows = mutableListOf<Component>()
        override fun renderRow(value: Component?, index: Int): MutableCollection<Component> {
            if (rows.contains(value)) {
                return mutableSetOf()
            }
            rows.add(index, value ?: Component.text(""))
            return mutableSetOf(value ?: Component.text(""))
        }

    }

    private fun channelInfo(channelId: String, active: Boolean): Component {
        val component = Component.text()
        if (active) {
            component.append(Component.text(channelId).color(NamedTextColor.GREEN))
        } else {
            component.append(Component.text(channelId).clickEvent(ClickEvent.runCommand("/channel set $channelId")).color(NamedTextColor.GRAY))
        }
        component.hoverEvent(
            HoverEvent.showText(
                Component.text()
                    .append(Component.text("Status: ").color(NamedTextColor.GRAY))
                    .append(Component.text(if (active) "Active" else "Inactive").color(NamedTextColor.GREEN))
                    .append(Component.text(channelService.usersInChannel(channelId).size).color(NamedTextColor.GREEN))
                    .build()
            )
        )
        return component.build()
    }

    fun startEdit(context: CommandContext<TCommandSource>): Int {
        val source = context.source
        val channelId = context.getArgument<String>("channel")
        if (!exists(channelId)) {
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("Invalid Channel ID ").color(NamedTextColor.RED))
                .append(Component.text(channelId).color(NamedTextColor.YELLOW))
                .sendTo(source)
            return 0
        }
        ChannelEdit.currentChannel[source.uuid()!!] = channelService.fromId(channelId) ?: return 0
        editableInfo(channelId).sendTo(source)
        return 1
    }

    fun editProperty(context: CommandContext<TCommandSource>): Int {
        val source = context.source
        val uuid = source.uuid()

        if (!ChannelEdit.currentChannel.containsKey(uuid)) {
            Component.text("You must select a channel to edit first!").sendTo(source)
            return 0
        }

        val channel = ChannelEdit.currentChannel[uuid]!!
        val value = context.getArgument<String>("value")

        val edited = ChannelEdit.editProperty(source.uuid()!!, context.getArgument("name", String::class.java), channel, value)

        if (edited) {
            Component.text()
                .append(Component.text("Successfully changed property ").color(NamedTextColor.GREEN))
                .append(Component.text(context.getArgument<String>("name")).color(NamedTextColor.GOLD))
                .append(Component.text(" to ").color(NamedTextColor.GREEN))
                .append(Component.text(value).color(NamedTextColor.GOLD))
                .append(Component.text(" for ").color(NamedTextColor.GREEN))
                .append(
                    Component.text()
                        .append(Component.text("\n[ Commit ]").color(NamedTextColor.GREEN))
                        .clickEvent(ClickEvent.runCommand("/channel edit commit"))
                        .hoverEvent(HoverEvent.showText(Component.text("Save your changes.")))
                        .build()
                )
                .append(
                    Component.text()
                        .append(Component.text(" [ Abort ]").color(NamedTextColor.RED))
                        .clickEvent(ClickEvent.runCommand("/channel edit abort"))
                        .hoverEvent(HoverEvent.showText(Component.text("Abort your changes.")))
                        .build()
                )
                .sendTo(source)
        } else {
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("Failed to edit property \"").color(NamedTextColor.YELLOW))
                .append(Component.text(context.getArgument<String>("name")).color(NamedTextColor.GREEN))
                .append(Component.text("\" for ").color(NamedTextColor.YELLOW))
                .append(Component.text(channel.id).color(NamedTextColor.GREEN))
                .sendTo(source)
            return 0
        }
        return 1
    }

    fun commit(context: CommandContext<TCommandSource>): Int {
        val source = context.source
        if (!ChannelEdit.editedChannel.containsKey(source.uuid())) {
            Component.text("You must edit a channel before you can commit changes!").color(NamedTextColor.RED).sendTo(source)
            return 0
        }
        Component.text()
            .append(Component.text("Successfulyl edited ").color(NamedTextColor.GREEN))
            .append(Component.text(ChannelEdit.editedChannel[source.uuid()]?.id ?: "null").color(NamedTextColor.GOLD))
            .sendTo(source)
        ChannelEdit.commit(source.uuid()!!, registry)
        return 1
    }

    fun info(context: CommandContext<TCommandSource>): Int {
        val info = buildInfo(context.getArgument("channel"))
        val source = context.source
        if (info == null) {
            Component.text()
                .append(pluginInfo.prefix)
                .append(Component.text("Failed to edit channel ").color(NamedTextColor.RED))
                .append(Component.text(context.getArgument<String>("channel")).color(NamedTextColor.GOLD))
                .sendTo(source)
            return 0
        }
        info.sendTo(source)
        return 1
    }

    private fun buildInfo(channelId: String): Component {
        if (!exists(channelId)) {
            Component.text("Invalid channel id $channelId").color(NamedTextColor.RED)
        }

        val chatChannel = channelService.fromId(channelId)!!
        return Component.text()
            .append(infoBar(channelId))
            .append(basicProperty("Active Users", channelService.usersInChannel(channelId).size.toString()))
            .append(basicProperty("Format", chatChannel.format))
            .append(basicProperty("Hover Message", chatChannel.hoverMessage))
            .append(basicProperty("OnClick", chatChannel.click))
            .append(basicProperty("Server Requirement", chatChannel.servers.joinToString(", ")))
            .append(basicProperty("Always Visible", chatChannel.alwaysVisible.toString()))
            .append(basicProperty("Discord Channel ID", chatChannel.discordChannel))
            .append(basicProperty("Passthrough", chatChannel.passthrough.toString()))
            .build()
    }

    private fun editableInfo(channelId: String): Component {
        if (!exists(channelId)) {
            return Component.text("Invalid channel id!").color(NamedTextColor.RED)
        }

        val chatChannel = channelService.fromId(channelId)!!
        return Component.text()
            .append(editBar(channelId))
            .append(basicProperty("Active Users", channelService.usersInChannel(channelId).size.toString()))
            .append(editableProperty("Format", chatChannel.format))
            .append(editableProperty("Hover Message", chatChannel.hoverMessage))
            .append(editableProperty("OnClick", chatChannel.click))
            .append(editableProperty("Server Requirement", chatChannel.servers.joinToString(", ")))
            .append(editableProperty("Always Visible", chatChannel.alwaysVisible.toString()))
            .append(editableProperty("Discord Channel ID", chatChannel.discordChannel))
            .append(editableProperty("Passthrough", chatChannel.passthrough.toString()))
            .build()
    }

    private fun infoBar(name: String): Component {
        return Component.text()
            .append(Component.text("========= ").color(NamedTextColor.DARK_GREEN))
            .append(Component.text("Channel - $name").color(NamedTextColor.GOLD))
            .append(Component.text(" =========\n").color(NamedTextColor.DARK_GREEN))
            .build()
    }

    private fun editBar(name: String): Component {
        return Component.text()
            .append(Component.text("========= ").color(NamedTextColor.DARK_GREEN))
            .append(Component.text("Edit Channel - $name").color(NamedTextColor.GOLD))
            .append(Component.text(" =========\n").color(NamedTextColor.DARK_GREEN))
            .build()
    }

    private fun basicProperty(name: String, value: String): Component {
        return Component.text()
            .append(Component.text("\n $name: ").color(NamedTextColor.GRAY))
            .append(Component.text(value).color(NamedTextColor.YELLOW))
            .build()
    }

    private fun editableProperty(name: String, value: String): Component {
        val propertyName = name.replace("\\s+".toRegex(), "")
        return Component.text()
            .append(Component.text("\n $name: ").color(NamedTextColor.GRAY))
            .append(Component.text(value).color(NamedTextColor.YELLOW))
            .hoverEvent(
                HoverEvent.showText(
                    Component.text("Click to edit").color(NamedTextColor.GREEN)
                )
            )
            .clickEvent(ClickEvent.suggestCommand("/channel edit property ${propertyName.lowercase()}"))
            .build()
    }

    private fun TCommandSource.uuid() = userService.getUUID(this as TPlayer)
}
