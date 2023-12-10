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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.api.chat.placeholder

import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable(with = ProxyFormat.Serializer::class)
class ProxyFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) : MessageFormat {

    class Resolver(private val proxyServer: ProxyServer) {
        fun resolve(format: Component, placeholders: Placeholders): Component {
            return sequenceOf<Component.() -> Component>(
                { replaceText { it.matchLiteral(placeholders.version).replacement(proxyServer.version.version) } },
                { replaceText { it.matchLiteral(placeholders.playerCount).replacement(proxyServer.playerCount.toString()) } },
            ).fold(format) { acc, transform -> transform(acc) }
        }

        fun resolve(format: ProxyFormat): Component = resolve(format.format, format.placeholders)
    }

    companion object Builder : MessageFormat.Builder<Placeholders, ProxyFormat> {
        override fun build(block: Placeholders.() -> Component): ProxyFormat {
            val placeholders = Placeholders()
            return ProxyFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<ProxyFormat>(::ProxyFormat)

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<ProxyFormat> {

        private val pathPrefix = path.joinToString("") { "$it." }

        val version: Placeholder = "%${pathPrefix}version%"
        val playerCount: Placeholder = "%${pathPrefix}playerCount%"
    }
}
