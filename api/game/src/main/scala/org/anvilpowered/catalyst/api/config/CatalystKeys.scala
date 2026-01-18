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
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.core.config.Key
import org.anvilpowered.anvil.core.config.KeyNamespace
import io.circe.generic.auto.*
import io.circe.Codec
import io.circe.Decoder
import io.circe.Encoder
import io.circe.syntax.*
import cats.kernel.Monoid
import org.anvilpowered.catalyst.api.chat.placeholder.MiniMessageCodec

// @Suppress("PropertyName")
class CatalystKeys(private val chatChannelBuilderFactory: ChatChannel.Factory) {

  // private def SimpleKey.BuilderFacet[Component, *].miniMessageFallback(fallbackValue: Component) {
  //     fallback(fallbackValue)
  //     serializer(MiniMessageSerializer)
  // }
  //
  // private def ListKey.BuilderFacet[Component, *].miniMessageListFallback(fallbackValue: List[Component]) {
  //     fallback(fallbackValue)
  //     elementSerializer(MiniMessageSerializer)
  // }
  //
  // private def [
  //     M : MessageFormat,
  //     P : MessageFormat.Placeholders[M],
  //     B : MessageFormat.Builder[P, M],
  //     ] SimpleKey.BuilderFacet[M, *].miniMessageFallbackFormat(
  //     builder: B,
  //     block: P.() -] Component,
  // ) {
  //     fallback(builder.build(block))
  // }

  // private def [
  //     M : MessageFormat,
  //     P : MessageFormat.Placeholders[M],
  //     B : MessageFormat.Builder[P, M],
  //     ] ListKey.BuilderFacet[M, *].miniMessageListFallbackFormat(
  //     builder: B,
  //     blocks: List[P.() -] Component],
  // ) {
  //     fallback(blocks.map { builder.build(it) })
  // }
  given Codec[String] = Codec.from(Decoder.decodeString, Encoder.encodeString)
  given Codec[Int] = Codec.from(Decoder.decodeInt, Encoder.encodeInt)
  given Codec[Boolean] = Codec.from(Decoder.decodeBoolean, Encoder.encodeBoolean)
  given Codec[Component] = MiniMessageCodec.codec
  given Monoid[Component] = Monoid.instance(Component.empty(), (a, _) => a)
  given Monoid[Boolean] = Monoid.instance(false, _ || _)

  val DB_TYPE = Key.create("postgresql")

  val DB_URL = Key.create("jdbc:postgresql://db:5432/catalyst")

  val DB_USER = Key.create("catalyst")

  val DB_PASSWORD = Key.create("catalyst")

  val CHAT_ENABLED = Key.create(true)

  val CHAT_FILTER_ENABLED = Key.create(false)

  val CHAT_FILTER_SWEARS = Key.create(Seq("fuck", "shit", "ass"))

  val CHAT_FILTER_EXCEPTIONS = Key.create(Seq("assassin", "jkass"))

  // val CHAT_DM_FORMAT_SOURCE = Key.create {
  //     miniMessageFallbackFormat(PrivateMessageFormat) {
  //         // TODO: Nice builder api with + unary operator
  //         Component.text().append(Component.text("[").color(NamedTextColor.DARK_GRAY))
  //             .append(Component.text("me(${source.backend.name})").color(NamedTextColor.BLUE))
  //             .append(Component.text(" -] ").color(NamedTextColor.GOLD))
  //             .append(Component.text("${recipient.displayname}(${recipient.backend.name})").color(NamedTextColor.BLUE))
  //             .append(Component.text("] ").color(NamedTextColor.DARK_GRAY)).append(Component.text(content).color(NamedTextColor.GRAY))
  //             .build()
  //     }
  // }
  //
  // val CHAT_DM_FORMAT_RECIPIENT = Key.create {
  //     miniMessageFallbackFormat(PrivateMessageFormat) {
  //         Component.text().append(Component.text("[").color(NamedTextColor.DARK_GRAY))
  //             .append(Component.text("${source.displayname}(${source.backend.name})").color(NamedTextColor.BLUE))
  //             .append(Component.text(" -] ").color(NamedTextColor.GOLD))
  //             .append(Component.text("me(${recipient.backend.name})").color(NamedTextColor.BLUE))
  //             .append(Component.text("] ").color(NamedTextColor.DARK_GRAY)).append(Component.text(content).color(NamedTextColor.GRAY))
  //             .build()
  //     }
  // }
  //
  // val CHAT_DM_FORMAT_SOCIALSPY = Key.create {
  //     miniMessageFallbackFormat(PrivateMessageFormat) {
  //         Component.text().append(Component.text("[SocialSpy] ").color(NamedTextColor.GRAY))
  //             .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
  //             .append(Component.text(source.displayname).color(NamedTextColor.BLUE))
  //             .append(Component.text(" -] ").color(NamedTextColor.GOLD))
  //             .append(Component.text(recipient.displayname).color(NamedTextColor.BLUE))
  //             .append(Component.text("] ").color(NamedTextColor.DARK_GRAY)).append(Component.text(content).color(NamedTextColor.GRAY))
  //             .build()
  //     }
  // }

  val CHAT_DISCORD_ENABLED = Key.create(false)

  val CHAT_DISCORD_BOT_NAME = Key.create("System")

  val CHAT_DISCORD_BOT_AVATAR = Key.create("https://crafthead.net/avatar/%uuid%")

  val CHAT_DISCORD_BOT_TOKEN = Key.create("bot token")

  val CHAT_DISCORD_BOT_STATUS = Key.create("A Minecraft Server!")

  // val CHAT_DISCORD_USERNAME_FORMAT = Key.create {
  //     miniMessageFallbackFormat(OnlineUserFormat) {
  //         Component.text("[${backend.name}] $prefix$displayname$suffix")
  //     }
  // }

  // TODO: Use ChannelFormat
  // val CHAT_DISCORD_MESSAGE_FORMAT = Key.create {
  //     miniMessageFallback(
  //         Component.text()
  //             .append(Component.text("%channel.name% "))
  //             .append(Component.text("[Discord] ", NamedTextColor.GOLD))
  //             .append(Component.text("%name%: "))
  //             .append(Component.text("%content%"))
  //             .color(NamedTextColor.GRAY)
  //             .build(),
  //     )
  // }

  val CHAT_DISCORD_TOPIC_FORMAT = Key.create("Player Count: <players>")

  val CHAT_DISCORD_TOPIC_ENABLED = Key.create(false)

  val CHAT_DISCORD_TOPIC_REFRESHRATE = Key.create(5)

  val CHAT_DISCORD_TOPIC_NOPLAYERS = Key.create("There are no players online!")

  val CHAT_DISCORD_INVITE = Key.create("https://discord.gg/8RUzuwu")

  val CHAT_DISCORD_HOVER_MESSAGE = Key.create[Component](Component.text("Click here to join our discord!"))

  val CHAT_NICKNAME_PREFIX = Key.create[Component](Component.text("~"))

  val CHAT_DEFAULT_CHANNEL = Key.create("global")

  val CHAT_CHANNELS = Key.create(
    Map(
      "global" -> chatChannelBuilderFactory.build {
        id("global")
        name(Component.text("[Global]").color(NamedTextColor.GREEN))
        commandAliases(listOf("g", "global"))
        alwaysVisible(true)
        availableByDefault(true)
        discordChannelId("123456789")
      },
      "staff" -> chatChannelBuilderFactory.build {
        id("staff")
        name(Component.text("[Staff]").color(NamedTextColor.AQUA))
        messageFormat {
          Component
            .text()
            .append(Component.text(channel.name))
            .append(Component.space())
            .append(Component.text(name))
            .append(Component.text(": "))
            .append(Component.text(content))
            .color(NamedTextColor.AQUA)
            .build()
        }
        commandAliases(listOf("s", "staff"))
        alwaysVisible(true)
        availableByDefault(false)
        discordChannelId("123456789")
      },
    ),
  ) {}

  val TAB_ENABLED = Key.create(false)

  // val TAB_HEADER = Key.create {
  //     miniMessageFallbackFormat(PlayerFormat) {
  //         Component.text("Welcome to")
  //     }
  // }
  //
  // val TAB_FOOTER = Key.create {
  //     miniMessageFallbackFormat(PlayerFormat) {
  //         Component.text("A Velocity Server")
  //     }
  // }
  //
  // val TAB_FORMAT_PLAYER = Key.create {
  //     miniMessageFallbackFormat(PlayerFormat) {
  //         Component.text("$prefix$username$suffix")
  //     }
  // }

  val TAB_FORMAT_CUSTOM = Key.buildingList {
    miniMessageListFallbackFormat(
      PlayerFormat,
      listOf(
        {
          Component
            .text()
            .append(Component.text("Your latency").color(NamedTextColor.DARK_AQUA))
            .append(Component.text(": ").color(NamedTextColor.GRAY))
            .append(Component.text(latency).color(NamedTextColor.YELLOW))
            .build()
        }, {
          Component
            .text()
            .append(Component.text("Current Server").color(NamedTextColor.DARK_AQUA))
            .append(Component.text(": ").color(NamedTextColor.GRAY))
            .append(Component.text(backend.name).color(NamedTextColor.YELLOW))
            .build()
        }, {
          Component
            .text()
            .append(Component.text("Player Count").color(NamedTextColor.DARK_AQUA))
            .append(Component.text(": ").color(NamedTextColor.GRAY))
            .append(Component.text(proxy.playerCount).color(NamedTextColor.YELLOW))
            .build()
        },
      ),
    )
  }

  val TAB_REFRESHRATE = Key.create(1)

  val JOIN_LISTENER_ENABLED = Key.create(true)

  val JOIN_MESSAGE_FIRST = Key.create {
    miniMessageFallbackFormat(OnlineUserFormat) {
      Component
        .text()
        .append(Component.text("Welcome to the server, "))
        .append(Component.text(displayname).color(NamedTextColor.AQUA))
        .append(Component.text("!"))
        .color(NamedTextColor.GREEN)
        .build()
    }
  }

  val JOIN_MESSAGE_NORMAL = Key.create {
    miniMessageFallbackFormat(OnlineUserFormat) {
      Component
        .text()
        .append(Component.text(displayname).color(NamedTextColor.GOLD))
        .append(Component.text(" has joined the network").color(NamedTextColor.GRAY))
        .build()
    }
  }

  val LEAVE_LISTENER_ENABLED = Key.create {
    fallback(true)
  }

  val LEAVE_MESSAGE = Key.create {
    miniMessageFallbackFormat(OnlineUserFormat) {
      Component
        .text()
        .append(Component.text(displayname).color(NamedTextColor.GOLD))
        .append(Component.text(" has left the network").color(NamedTextColor.GRAY))
        .build()
    }
  }

  val SERVER_WEBSITE = Key.create("https://www.anvilpowered.org")

  val SERVER_PING_TYPE = Key.create("players", Some("Choose one of 'players' or 'message'. You may configure the message with SERVER_PING_MESSAGE"))

  val SERVER_PING_MESSAGE = Key.create("Change this message in the config!")

  // val SERVER_MOTD_MESSAGE = Key.create {
  //   miniMessageFallbackFormat(ProxyFormat) {
  //     Component.text("A Velocity Proxy running version $version!").color(NamedTextColor.DARK_AQUA)
  //   }
  // }

  val SERVER_MOTD_ENABLED = Key.create(false)
  val COMMAND_LOGGING_ENABLED = Key.create(true)

  // TODO: Regex?
  val COMMAND_LOGGING_FILTER = Key.create(Seq("*"), Some("A list of root commands to log. Use * to log all commands."))

  val CATALYST_PREFIX = Key.create("Catalyst")
  val PERMISSION_BROADCAST = Key.create("catalyst.command.broadcast")
  val PERMISSION_CHANNEL_EDIT = Key.create("catalyst.command.channel.edit")
  val PERMISSION_CHAT_COLOR = Key.create("catalyst.chat.color")
  val PERMISSION_FIND = Key.create("catalyst.command.find")
  val PERMISSION_IGNORE_BASE = Key.create("catalyst.command.ignore.base")
  val PERMISSION_IGNORE_EXEMPT = Key.create("catalyst.command.ignore.exempt")
  val PERMISSION_INFO_BASE = Key.create("catalyst.command.info.base")
  val PERMISSION_INFO_IP = Key.create("catalyst.command.info.ip")
  val PERMISSION_INFO_CHANNEL = Key.create("catalyst.command.info.channel")
  val PERMISSION_KICK_BASE = Key.create("catalyst.command.kick.base")
  val PERMISSION_KICK_EXEMPT = Key.create("catalyst.command.kick.exempt")
  val PERMISSION_LANGUAGE_ADMIN = Key.create("catalyst.command.language.admin")
  val PERMISSION_LANGUAGE_LIST = Key.create("catalyst.command.language.list")
  val PERMISSION_LIST = Key.create("catalyst.command.list")
  val PERMISSION_MESSAGE = Key.create("catalyst.command.message")
  val PERMISSION_MUTE_BASE = Key.create("catalyst.command.mute.base")
  val PERMISSION_MUTE_EXEMPT = Key.create("catalyst.command.mute.exempt")
  val PERMISSION_NICKNAME_BASE = Key.create("catalyst.command.nickname.base")
  val PERMISSION_NICKNAME_COLOR = Key.create("catalyst.command.nickname.color")
  val PERMISSION_NICKNAME_MAGIC = Key.create("catalyst.command.nickname.magic")
  val PERMISSION_SEND = Key.create("catalyst.admin.command.send")
  val PERMISSION_SOCIALSPY_BASE = Key.create("catalyst.admin.command.socialspy,base")
  val PERMISSION_SOCIALSPY_ONJOIN = Key.create("catalyst.admin.command.socialspy.onjoin")
  val PERMISSION_STAFFLIST_BASE = Key.create("catalyst.stafflist.base")
  val PERMISSION_STAFFLIST_STAFF = Key.create("catalyst.stafflist.staff")
  val PERMISSION_STAFFLIST_ADMIN = Key.create("catalyst.stafflist.admin")
  val PERMISSION_STAFFLIST_OWNER = Key.create("catalyst.stafflist.owner")
  val PERMISSION_CHAT_TOGGLE = Key.create("catalyst.chat.toggle")
  val PERMISSION_CHANNEL_BASE = Key.create("catalyst.channel.base", Some("Permission to use the /channel command"))
  val PERMISSION_CHANNEL_SPYALL = Key.create("catalyst.channel.spyall", Some("Permission to see always see all channels"))
  val PERMISSION_CHANNEL_PREFIX = Key.create(
    "catalyst.channel.access",
    Some(
      """
        Permission prefix for channels. For example, if the prefix is 'catalyst.channel.access' (the default), then the permission for
        the channel with id 'foo' would be 'catalyst.channel.access.foo'.

        Players always have permission to join the default channel (default 'global').
        """,
    ),
  )
}

object CatalystKeys {
  given namespace: KeyNamespace = KeyNamespace.create("CATALYST")
}
