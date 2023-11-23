/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2024 Contributors
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
import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.apache.logging.log4j.Logger

class ChannelMessageFormat(override val format: Component, private val placeholders: Placeholders) : MessageFormat {

    suspend fun resolve(
        proxyServer: ProxyServer,
        logger: Logger,
        luckpermsService: LuckpermsService,
        message: ChannelMessage,
    ): RecipientFormat = resolve(proxyServer, logger, luckpermsService, format, placeholders, message)

    // TODO: Consider DI
    companion object : MessageFormat.Builder<Placeholders, ChannelMessageFormat> {

        private val source = NestedFormat(PlayerFormat, Placeholders::source)

        suspend fun resolve(
            proxyServer: ProxyServer,
            logger: Logger,
            luckpermsService: LuckpermsService,
            format: Component,
            placeholders: Placeholders,
            message: ChannelMessage,
        ): RecipientFormat {
            val resultFormat = sequenceOf<suspend Component.() -> Component>(
                {
                    source.format.resolve(
                        proxyServer,
                        logger,
                        luckpermsService,
                        this,
                        source.placeholderResolver(placeholders),
                        message.source,
                    )
                },
                { replaceText { it.match(placeholders.content).replacement(message.content) } },
            ).fold(format) { acc, transform -> transform(acc) }
            return RecipientFormat(resultFormat, RecipientFormat.Placeholders())
        }

        override fun build(block: Placeholders.() -> Component): ChannelMessageFormat {
            val placeholders = Placeholders()
            return ChannelMessageFormat(block(placeholders), placeholders)
        }
    }

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<ChannelMessageFormat> {

        private val prefix = path.joinToString { "$it." }

        // TODO: Replace with tag resolver
        val source = PlayerFormat.Placeholders(path + listOf("source"))
        val channel: Placeholder = "%${prefix}channel%"
        val content: Placeholder = "%${prefix}content%"
    }
}
