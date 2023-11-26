/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.api.chat.placeholder

import com.velocitypowered.api.proxy.Player
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.apache.logging.log4j.Logger

@Serializable(with = PlayerFormat.Serializer::class)
open class PlayerFormat(
    override val format: Component,
    private val placeholders: ConcretePlaceholders = ConcretePlaceholders(),
) : MessageFormat {

    class Resolver(
        private val logger: Logger,
        private val luckpermsService: LuckpermsService,
        private val backendFormatResolver: BackendFormat.Resolver,
        private val proxyFormatResolver: ProxyFormat.Resolver,
    ) {
        suspend fun resolve(format: Component, placeholders: ConcretePlaceholders, player: Player): Component {
            val backendFormat: (suspend Component.() -> Component)? = player.currentServer.orElse(null)?.server?.let {
                { backendFormatResolver.resolve(format, placeholders.backend, it) }
            }

            if (backendFormat == null) {
                logger.error("Could not resolve backend placeholders for ${player.username} because they are not connected to a server.")
            }

            return sequenceOf(
                backendFormat,
                { proxyFormatResolver.resolve(this, placeholders.proxy) },
                { replaceText { it.matchLiteral(placeholders.latency).replacement(player.ping.toString()) } },
                { replaceText { it.matchLiteral(placeholders.username).replacement(player.username) } },
                { replaceText { it.matchLiteral(placeholders.id).replacement(player.uniqueId.toString()) } },
                { replaceText { it.matchLiteral(placeholders.prefix).replacement(luckpermsService.prefix(player.uniqueId)) } },
                { replaceText { it.matchLiteral(placeholders.suffix).replacement(luckpermsService.suffix(player.uniqueId)) } },
            ).filterNotNull().fold(format) { acc, transform -> transform(acc) }
        }

        suspend fun resolve(format: PlayerFormat, player: Player): Component = resolve(format.format, format.placeholders, player)
    }

    companion object : MessageFormat.Builder<ConcretePlaceholders, PlayerFormat> {
        override fun build(block: ConcretePlaceholders.() -> Component): PlayerFormat {
            val placeholders = ConcretePlaceholders()
            return PlayerFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<PlayerFormat>(::PlayerFormat)

    interface Placeholders {
        val backend: BackendFormat.Placeholders
        val proxy: ProxyFormat.Placeholders

        val latency: Placeholder
        val username: Placeholder
        val id: Placeholder
        val prefix: Placeholder
        val suffix: Placeholder
    }

    class ConcretePlaceholders internal constructor(path: List<String> = listOf()) :
        MessageFormat.Placeholders<PlayerFormat>, Placeholders {

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
