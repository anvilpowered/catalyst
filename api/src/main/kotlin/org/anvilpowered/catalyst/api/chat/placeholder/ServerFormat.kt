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
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.chat.placeholder.MessageFormat.Companion.trb

class ServerFormat(private val format: Component, private val placeholders: Placeholders) : MessageFormat {

    context(PlayerService.Scope)
    fun resolvePlaceholders(): Component = resolvePlaceholders(format, placeholders)

    override fun asComponent(): Component = format

    companion object : MessageFormat.Builder<Placeholders, ServerFormat> {

        context(PlayerService.Scope)
        fun resolvePlaceholders(format: Component, placeholders: Placeholders): Component {
            return sequenceOf<(Component) -> Component>(
                { it.replaceText(trb().match(placeholders.playerCount).replacement(playerService.getAll().count().toString()).build()) },
//                { it.replaceText(trb().match(placeholders.serverName).replacement(serverName).build()) },
            ).fold(format) { acc, transform -> transform(acc) }
        }

        override fun build(block: Placeholders.() -> Component): ServerFormat {
            val placeholders = Placeholders()
            return ServerFormat(block(placeholders), placeholders)
        }
    }

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<ServerFormat> {

        private val prefix = path.joinToString { "$it." }

        val playerCount: Placeholder = "%${prefix}playerCount%"
        val serverName: Placeholder = "%${prefix}serverName%"
    }
}
