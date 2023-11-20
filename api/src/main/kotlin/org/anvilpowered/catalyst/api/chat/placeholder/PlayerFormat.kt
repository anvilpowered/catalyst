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
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.chat.placeholder.MessageFormat.Companion.trb
import org.anvilpowered.catalyst.api.user.LocationScope

class PlayerFormat(private val format: Component, private val placeholders: Placeholders) : MessageFormat {

    context(LocationScope, PlayerService.Scope)
    fun resolvePlaceholders(player: Player): Component = resolvePlaceholders(format, placeholders, player)

    override fun asComponent(): Component = format

    companion object : MessageFormat.Builder<Placeholders, PlayerFormat> {

        private val server = NestedFormat(ServerFormat, Placeholders::server)

        context(LocationScope, PlayerService.Scope)
        fun resolvePlaceholders(format: Component, placeholders: Placeholders, player: Player): Component {
            return sequenceOf<(Component) -> Component>(
                { server.format.resolvePlaceholders(it, server.placeholderResolver(placeholders)) },
                { it.replaceText(trb().match(placeholders.ping).replacement(player.latencyMs.toString()).build()) },
                { it.replaceText(trb().match(placeholders.playerName).replacement(player.username).build()) },
                { it.replaceText(trb().match(placeholders.playerId).replacement(player.id.toString()).build()) },
            ).fold(format) { acc, transform -> transform(acc) }
        }

        override fun build(block: Placeholders.() -> Component): PlayerFormat {
            val placeholders = Placeholders()
            return PlayerFormat(block(placeholders), placeholders)
        }
    }

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<PlayerFormat> {

        private val prefix = path.joinToString { "$it." }

        val server = ServerFormat.Placeholders(path + listOf("server"))

        val ping: Placeholder = "%${prefix}ping%"
        val playerName: Placeholder = "%${prefix}playerName%"
        val playerId: Placeholder = "%${prefix}playerId%"
    }
}
