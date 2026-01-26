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
import org.anvilpowered.anvil.core.platform.Server
import org.anvilpowered.anvil.core.platform.ServerPingService
import org.anvilpowered.anvil.core.platform.ServerPingService.ping
import cats.effect.Async
import cats.syntax.all.*
import io.circe.Codec
import org.spongepowered.configurate.serialize.TypeSerializer
import org.anvilpowered.catalyst.api.chat.placeholder.ServerFormat.Placeholders

class ServerFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) extends MessageFormat {

  def resolve[F[_]: Async as F](format: Component, placeholders: Placeholders, server: Server)(using ps: ServerPingService): F[Component] = {
    val ops = Seq[Component => Component](
      { _.replaceText { _.matchLiteral(placeholders.name).replacement(server.name) } },
      { _.replaceText { _.matchLiteral(placeholders.address).replacement(server.address.getHostString) } },
    )
    (if (Seq(placeholders.playerCount, placeholders.description).map(Component.text).exists(format.contains)) {
      for (ping <- server.ping[F]) yield {
        ops ++ Seq(
          { _.replaceText { _.matchLiteral(placeholders.playerCount).replacement(ping.players.online.toString()) } },
          { _.replaceText { _.matchLiteral(placeholders.description).replacement(ping.description) } },
        )
      }
    } else {
      F.pure(ops)
    }).map { _.foldl(format) { (acc, transform) => transform(acc) } }
  }
}

object ServerFormat {
  given codec: Codec[ServerFormat] = MessageFormat.codec(ServerFormat(_))
  given typeSerializer: TypeSerializer[ServerFormat] = MessageFormat.serializer(ServerFormat(_))
  object Builder extends MessageFormat.Builder[Placeholders, ServerFormat] {
    override def build(block: Placeholders ?=> Component): ServerFormat = {
      val placeholders = Placeholders()
      return ServerFormat(block(using placeholders), placeholders)
    }
  }
  class Placeholders(path: List[String] = List()) extends MessageFormat.Placeholders[ServerFormat] {

    private val pathPrefix = path.map(e => s"$e.").mkString("")

    val name: Placeholder = s"%${pathPrefix}name%"
    val address: Placeholder = s"%${pathPrefix}address%"
    val playerCount: Placeholder = s"%${pathPrefix}playerCount%"
    val description: Placeholder = s"%${pathPrefix}description%"
  }
}
