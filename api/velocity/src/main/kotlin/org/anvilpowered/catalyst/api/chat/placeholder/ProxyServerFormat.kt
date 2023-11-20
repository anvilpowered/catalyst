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

import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.velocity.ProxyServerScope

class ProxyServerFormat(private val format: Component, private val placeholders: Placeholders) : MessageFormat {

    context(ProxyServerScope)
    fun resolvePlaceholders(): Component = resolvePlaceholders(format, placeholders)

    override fun asComponent(): Component = format

    companion object : MessageFormat.Builder<Placeholders, ProxyServerFormat> {

        context(ProxyServerScope)
        fun resolvePlaceholders(format: Component, placeholders: Placeholders): Component {
            return sequenceOf<Component.() -> Component>(
                { replaceText { it.match(placeholders.version).replacement(proxyServer.version.version) } },
                { replaceText { it.match(placeholders.playerCount).replacement(proxyServer.playerCount.toString()) } },
            ).fold(format) { acc, transform -> transform(acc) }
        }

        override fun build(block: Placeholders.() -> Component): ProxyServerFormat {
            val placeholders = Placeholders()
            return ProxyServerFormat(block(placeholders), placeholders)
        }
    }

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<ProxyServerFormat> {

        private val prefix = path.joinToString { "$it." }

        val version: Placeholder = "%${prefix}version%"
        val playerCount: Placeholder = "%${prefix}playerCount%"
    }
}
