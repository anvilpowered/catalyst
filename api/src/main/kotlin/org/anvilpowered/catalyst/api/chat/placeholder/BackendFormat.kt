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

import com.velocitypowered.api.proxy.server.RegisteredServer
import kotlinx.coroutines.future.await
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable(with = BackendFormat.Serializer::class)
class BackendFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) : MessageFormat {

    class Resolver {
        suspend fun resolve(format: Component, placeholders: Placeholders, server: RegisteredServer): Component {
            val ops = sequenceOf<Component.() -> Component>(
                { replaceText { it.matchLiteral(placeholders.name).replacement(server.serverInfo.name) } },
                { replaceText { it.matchLiteral(placeholders.address).replacement(server.serverInfo.address.hostString) } },
            )
            return if (
                format.contains(Component.text(placeholders.version)) ||
                format.contains(Component.text(placeholders.playerCount)) ||
                format.contains(Component.text(placeholders.description))
            ) {
                val ping = server.ping().await()
                ops + sequenceOf(
                    { replaceText { it.matchLiteral(placeholders.version).replacement(ping.version.toString()) } },
                    { replaceText { it.matchLiteral(placeholders.playerCount).replacement(ping.players.orElse(null)?.online.toString()) } },
                    { replaceText { it.matchLiteral(placeholders.description).replacement(ping.descriptionComponent) } },
                )
            } else {
                ops
            }.fold(format) { acc, transform -> transform(acc) }
        }
    }

    companion object : MessageFormat.Builder<Placeholders, BackendFormat> {

        override fun build(block: Placeholders.() -> Component): BackendFormat {
            val placeholders = Placeholders()
            return BackendFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<BackendFormat>(::BackendFormat)

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<BackendFormat> {

        private val pathPrefix = path.joinToString("") { "$it." }

        val name: Placeholder = "%${pathPrefix}name%"
        val address: Placeholder = "%${pathPrefix}address%"
        val version: Placeholder = "%${pathPrefix}version%"
        val playerCount: Placeholder = "%${pathPrefix}playerCount%"
        val description: Placeholder = "%${pathPrefix}description%"
    }
}
