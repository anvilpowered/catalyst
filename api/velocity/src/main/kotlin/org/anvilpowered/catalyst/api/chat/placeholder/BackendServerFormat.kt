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

import com.velocitypowered.api.proxy.server.RegisteredServer
import kotlinx.coroutines.future.await
import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.velocity.ProxyServerScope

class BackendServerFormat(override val format: Component, private val placeholders: Placeholders) : MessageFormat {

    context(ProxyServerScope)
    suspend fun resolvePlaceholders(server: RegisteredServer): Component = resolvePlaceholders(format, placeholders, server)

    companion object : MessageFormat.Builder<Placeholders, BackendServerFormat> {

        context(ProxyServerScope)
        suspend fun resolvePlaceholders(format: Component, placeholders: Placeholders, server: RegisteredServer): Component {
            val ops = sequenceOf<Component.() -> Component>(
                { replaceText { it.match(placeholders.name).replacement(server.serverInfo.name) } },
                { replaceText { it.match(placeholders.address).replacement(server.serverInfo.address.hostString) } },
            )
            return if (
                format.contains(Component.text(placeholders.version)) ||
                format.contains(Component.text(placeholders.playerCount)) ||
                format.contains(Component.text(placeholders.description))
            ) {
                val ping = server.ping().await()
                ops + sequenceOf(
                    { replaceText { it.match(placeholders.version).replacement(ping.version.toString()) } },
                    { replaceText { it.match(placeholders.playerCount).replacement(ping.players.orElse(null)?.online.toString()) } },
                    { replaceText { it.match(placeholders.description).replacement(ping.descriptionComponent) } },
                )
            } else {
                ops
            }.fold(format) { acc, transform -> transform(acc) }
        }

        override fun build(block: Placeholders.() -> Component): BackendServerFormat {
            val placeholders = Placeholders()
            return BackendServerFormat(block(placeholders), placeholders)
        }
    }

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<BackendServerFormat> {

        private val prefix = path.joinToString { "$it." }

        val name: Placeholder = "%${prefix}name%"
        val address: Placeholder = "%${prefix}address%"
        val version: Placeholder = "%${prefix}version%"
        val playerCount: Placeholder = "%${prefix}playerCount%"
        val description: Placeholder = "%${prefix}description%"
    }
}
