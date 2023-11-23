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
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.apache.logging.log4j.Logger

class RecipientFormat(override val format: Component, private val placeholders: Placeholders) : MessageFormat {

    suspend fun resolvePlaceholders(
        proxyServer: ProxyServer,
        logger: Logger,
        luckpermsService: LuckpermsService,
        recipient: Player,
    ): Component =
        resolvePlaceholders(proxyServer, logger, luckpermsService, format, placeholders, recipient)

    companion object : MessageFormat.Builder<Placeholders, RecipientFormat> {

        private val recipientContext = NestedFormat(PlayerFormat, Placeholders::recipient)

        suspend fun resolvePlaceholders(
            proxyServer: ProxyServer,
            logger: Logger,
            luckpermsService: LuckpermsService,
            format: Component,
            placeholders: Placeholders,
            recipient: Player,
        ): Component {
            return recipientContext.format.resolve(
                proxyServer,
                logger,
                luckpermsService,
                format,
                recipientContext.placeholderResolver(placeholders),
                recipient,
            )
        }

        override fun build(block: Placeholders.() -> Component): RecipientFormat {
            val placeholders = Placeholders()
            return RecipientFormat(block(placeholders), placeholders)
        }
    }

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<RecipientFormat> {
        val recipient = PlayerFormat.Placeholders(path + listOf("recipient"))
    }
}
