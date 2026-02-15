/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2026 Contributors
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

package org.anvilpowered.catalyst.api.chat.placeholder

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import cats.effect.Async
import io.circe.Codec
import org.anvilpowered.catalyst.api.chat.placeholder.ChannelMessageFormat.Placeholders

class ChannelMessageFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) extends MessageFormat {
  def resolve[F[_]: Async as F](format: Component, placeholders: Placeholders, message: ChannelMessage): PlayerFormat = {
    ???
    // val resultFormat = sequenceOf[suspend Component.() -] Component](
    //     { chatChannelFormatResolver.resolve(this, placeholders.channel, message.channel) },
    //     { replaceText { it.matchLiteral(placeholders.name).replacement(message.name) } },
    //     { replaceText { it.matchLiteral(placeholders.content).replacement(message.content) } },
    // ).fold(format) { acc, transform -] transform(acc) }
    // return PlayerFormat(resultFormat, PlayerFormat.ConcretePlaceholders(listOf("recipient")))
  }

  // class Resolver(private val chatChannelFormatResolver: ChatChannelFormat.Resolver) {

  // suspend def resolve(format: ChannelMessageFormat, message: ChannelMessage): PlayerFormat =
  //     resolve(format.format, format.placeholders, message)
  // }

  // object Serializer extends MessageFormat.Serializer[ChannelMessageFormat](::ChannelMessageFormat)

}

object ChannelMessageFormat {
  given codec: Codec[ChannelMessageFormat] = MessageFormat.codec(ChannelMessageFormat(_))
  object Builder extends MessageFormat.Builder[Placeholders, ChannelMessageFormat] {
    override def build(block: Placeholders ?=> Component): ChannelMessageFormat = {
      val placeholders = Placeholders()
      // ChannelMessageFormat(block(using placeholders), placeholders)
      ???
    }
  }

  class Placeholders(path: List[String] = List()) extends MessageFormat.Placeholders[ChannelMessageFormat] {

    private val pathPrefix = path.map(e => s"$e.").mkString("")
    val channel = ChatChannelFormat.Placeholders(path :+ "channel")
    val name: Placeholder = "%${pathPrefix}name%"
    val content: Placeholder = "%${pathPrefix}content%"
  }
}
