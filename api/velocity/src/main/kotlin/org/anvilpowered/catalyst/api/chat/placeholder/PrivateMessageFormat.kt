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

import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.chat.PrivateMessage
import org.apache.logging.log4j.Logger

@Serializable(with = PrivateMessageFormat.Serializer::class)
class PrivateMessageFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) : MessageFormat {

    suspend fun resolve(
        proxyServer: ProxyServer,
        logger: Logger,
        luckpermsService: LuckpermsService,
        message: PrivateMessage,
    ): Component = resolve(proxyServer, logger, luckpermsService, format, placeholders, message)

    companion object : MessageFormat.Builder<Placeholders, PrivateMessageFormat> {

        private val sourceContext = NestedFormat(OnlineUserFormat, Placeholders::source)
        private val recipientContext = NestedFormat(OnlineUserFormat, Placeholders::recipient)

        suspend fun resolve(
            proxyServer: ProxyServer,
            logger: Logger,
            luckpermsService: LuckpermsService,
            format: Component,
            placeholders: Placeholders,
            message: PrivateMessage,
        ): Component {
            return sequenceOf<suspend Component.() -> Component>(
                {
                    sourceContext.format.resolve(
                        proxyServer,
                        logger,
                        luckpermsService,
                        this,
                        sourceContext.placeholderResolver(placeholders),
                        message.source,
                    )
                },
                {
                    recipientContext.format.resolve(
                        proxyServer,
                        logger,
                        luckpermsService,
                        this,
                        recipientContext.placeholderResolver(placeholders),
                        message.recipient,
                    )
                },
                { replaceText { it.matchLiteral(placeholders.content).replacement(message.content) } },
            ).fold(format) { acc, transform -> transform(acc) }
        }

        override fun build(block: Placeholders.() -> Component): PrivateMessageFormat {
            val placeholders = Placeholders()
            return PrivateMessageFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<PrivateMessageFormat>(::PrivateMessageFormat)

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<PrivateMessageFormat> {

        private val pathPrefix = path.joinToString("") { "$it." }

        val source = OnlineUserFormat.Placeholders(path + listOf("source"))
        val recipient = OnlineUserFormat.Placeholders(path + listOf("recipient"))
        val content: Placeholder = "%${pathPrefix}content%"
    }
}
