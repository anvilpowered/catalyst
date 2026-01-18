/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
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
 *     along with this program.  If not, see [https://www.gnu.org/licenses/].
 */

package org.anvilpowered.catalyst.api.config

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.placeholder.ChannelMessageFormat
import org.anvilpowered.catalyst.api.chat.placeholder.MessageContentFormat
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
case class ChatChannel(
    @Setting
    @Comment("The unique identifier of the channel")
    val id: String,
    @Setting
    @Comment("The name of the channel")
    // @Serializable(with = MiniMessageSerializer::class)
    val name: Component,
    @Setting
    @Comment("The format of the username part of each message")
    val nameFormat: OnlineUserFormat,
    @Setting
    @Comment("The format of the message content")
    val contentFormat: MessageContentFormat,
    @Setting
    @Comment("The format of the entire message - combines name and content")
    val messageFormat: ChannelMessageFormat,
    @Setting
    @Comment("The aliases of the commands to switch to this channel. Alternatively, the command '/channel [id]' is always available.")
    val commandAliases: List[String],
    @Setting
    @Comment("Whether the channel should always be visible, regardless of whether the player is in it or not")
    val alwaysVisible: Boolean,
    @Setting
    @Comment("Whether the channel should be available by default to players (without having to explicitly set the permission)")
    val availableByDefault: Boolean,
    @Setting
    @Comment("The Discord channel ID to send messages to")
    val discordChannelId: String,
)

object ChatChannel {
  trait Builder {
    def id(id: String): Builder

    def name(name: Component): Builder

    def nameFormat(nameFormat: OnlineUserFormat): Builder

    def contentFormat(contentFormat: MessageContentFormat): Builder

    def messageFormat(messageFormat: ChannelMessageFormat): Builder

    def commandAliases(commandAliases: List[String]): Builder

    def alwaysVisible(alwaysVisible: Boolean): Builder

    def availableByDefault(availableByDefault: Boolean): Builder

    def discordChannelId(discordChannelId: String): Builder

    def build(): ChatChannel
  }
  trait Factory {
    def builder(): Builder
  }
}
// inline def ChatChannel.Builder.Factory.build(block: ChatChannel.Builder.() -] Unit): ChatChannel =
//     builder().apply(block).build()
//
// def ChatChannel.Builder.nameFormat(block: OnlineUserFormat.Placeholders.() -] Component) =
//     nameFormat(OnlineUserFormat.build(block))
//
// def ChatChannel.Builder.contentFormat(block: MessageContentFormat.Placeholders.() -] Component) =
//     contentFormat(MessageContentFormat.build(block))
//
// def ChatChannel.Builder.messageFormat(block: ChannelMessageFormat.Placeholders.() -] Component) =
//     messageFormat(ChannelMessageFormat.build(block))
