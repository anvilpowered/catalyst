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
import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.api.chat.LuckpermsService

class PlayerFormat(private val format: Component, private val placeholders: Placeholders) : MessageFormat {

    context(ProxyServerScope, LoggerScope, LuckpermsService.Scope)
    suspend fun resolvePlaceholders(player: Player): Component = resolvePlaceholders(format, placeholders, player)

    override fun asComponent(): Component = format

    companion object : MessageFormat.Builder<Placeholders, PlayerFormat> {

        private val backendServer = NestedFormat(BackendServerFormat, Placeholders::backendServer)
        private val proxyServer = NestedFormat(ProxyServerFormat, Placeholders::proxyServer)

        context(ProxyServerScope, LoggerScope, LuckpermsService.Scope)
        suspend fun resolvePlaceholders(format: Component, placeholders: Placeholders, player: Player): Component {
            val server = player.currentServer.orElse(null)?.server ?: run {
                logger.error("Could not resolve placeholders for player ${player.username} because they are not connected to a server.")
                return format
            }
            return sequenceOf<suspend Component.() -> Component>(
                { backendServer.format.resolvePlaceholders(this, backendServer.placeholderResolver(placeholders), server) },
                { proxyServer.format.resolvePlaceholders(this, proxyServer.placeholderResolver(placeholders)) },
                { replaceText { it.match(placeholders.latency).replacement(player.ping.toString()) } },
                { replaceText { it.match(placeholders.name).replacement(player.username) } },
                { replaceText { it.match(placeholders.id).replacement(player.uniqueId.toString()) } },
                { replaceText { it.match(placeholders.prefix).replacement(luckpermsService.prefix(player.uniqueId)) } },
                { replaceText { it.match(placeholders.suffix).replacement(luckpermsService.suffix(player.uniqueId)) } },
            ).fold(format) { acc, transform -> transform(acc) }
        }

        override fun build(block: Placeholders.() -> Component): PlayerFormat {
            val placeholders = Placeholders()
            return PlayerFormat(block(placeholders), placeholders)
        }
    }

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<PlayerFormat> {

        private val pathPrefix = path.joinToString { "$it." }

        val backendServer = BackendServerFormat.Placeholders(path + listOf("backendServer"))
        val proxyServer = ProxyServerFormat.Placeholders(path + listOf("proxyServer"))

        val latency: Placeholder = "%${pathPrefix}ping%"
        val name: Placeholder = "%${pathPrefix}name%"
        val id: Placeholder = "%${pathPrefix}id%"

        val prefix: Placeholder = "%${pathPrefix}prefix%"
        val suffix: Placeholder = "%${pathPrefix}suffix%"
    }
}
