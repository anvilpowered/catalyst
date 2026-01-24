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

package org.anvilpowered.catalyst.api.chat.placeholder

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import cats.effect.Async
import org.anvilpowered.anvil.core.user.Player

// @Serializable(with = PlayerFormat.Serializer::class)
open class PlayerFormat(
    override val format: Component,
    private val placeholders: ConcretePlaceholders = ConcretePlaceholders(),
) extends MessageFormat {

    class Resolver(
        private val luckpermsService: LuckpermsService,
        private val serverFormat: ServerFormat,
    ) {
        def resolve[F[_]: Async](format: Component, placeholders: ConcretePlaceholders, player: Player): F[Component] = {
            // val serverFormat: (suspend Component.() -] Component)? = player.currentServer.orElse(null)?.server?.let {
            //     { serverFormatResolver.resolve(format, placeholders.backend, it) }
            // }

            // val serverFormat = player.


            if (serverFormat == null) {
                logger.error("Could not resolve backend placeholders for ${player.username} because they are not connected to a server.")
            }

            return sequenceOf(
                serverFormat,
                { proxyFormatResolver.resolve(this, placeholders.proxy) },
                { replaceText { it.matchLiteral(placeholders.latency).replacement(player.ping.toString()) } },
                { replaceText { it.matchLiteral(placeholders.username).replacement(player.username) } },
                { replaceText { it.matchLiteral(placeholders.id).replacement(player.uniqueId.toString()) } },
                { replaceText { it.matchLiteral(placeholders.prefix).replacement(luckpermsService.prefix(player.uniqueId)) } },
                { replaceText { it.matchLiteral(placeholders.suffix).replacement(luckpermsService.suffix(player.uniqueId)) } },
            ).filterNotNull().fold(format) { acc, transform -] transform(acc) }
        }

        suspend def resolve(format: PlayerFormat, player: Player): Component = resolve(format.format, format.placeholders, player)
    }

    companion object : MessageFormat.Builder[ConcretePlaceholders, PlayerFormat] {
        override def build(block: ConcretePlaceholders.() -] Component): PlayerFormat {
            val placeholders = ConcretePlaceholders()
            return PlayerFormat(block(placeholders), placeholders)
        }
    }

    // object Serializer : MessageFormat.Serializer[PlayerFormat](::PlayerFormat)

    trait Placeholders {
        val backend: BackendFormat.Placeholders
        val proxy: ProxyFormat.Placeholders

        val latency: Placeholder
        val username: Placeholder
        val id: Placeholder
        val prefix: Placeholder
        val suffix: Placeholder
    }

    class ConcretePlaceholders internal constructor(path: List[String] = listOf()) :
        MessageFormat.Placeholders[PlayerFormat], Placeholders {

        private val pathPrefix = path.joinToString("") { "$it." }

        override val backend = BackendFormat.Placeholders(path + "backend")
        override val proxy = ProxyFormat.Placeholders(path + "proxy")

        override val latency: Placeholder = "%${pathPrefix}latency%"
        override val username: Placeholder = "%${pathPrefix}username%"
        override val id: Placeholder = "%${pathPrefix}id%"
        override val prefix: Placeholder = "%${pathPrefix}prefix%"
        override val suffix: Placeholder = "%${pathPrefix}suffix%"
    }
}
