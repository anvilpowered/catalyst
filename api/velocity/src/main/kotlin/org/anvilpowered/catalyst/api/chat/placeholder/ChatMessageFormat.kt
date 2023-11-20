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
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.api.chat.ChatMessage
import org.anvilpowered.catalyst.api.chat.LuckpermsService

class ChatMessageFormat(private val format: Component, private val placeholders: Placeholders) : MessageFormat {

    context(ProxyServerScope, LoggerScope, LuckpermsService.Scope)
    suspend fun resolvePlaceholders(message: ChatMessage): Component = resolvePlaceholders(format, placeholders, message)

    override fun asComponent(): Component = format

    companion object : MessageFormat.Builder<Placeholders, ChatMessageFormat> {

        private val source = NestedFormat(PlayerFormat, Placeholders::source)
        private val recipient = NestedFormat(PlayerFormat, Placeholders::recipient)

        context(ProxyServerScope, LoggerScope, LuckpermsService.Scope)
        suspend fun resolvePlaceholders(format: Component, placeholders: Placeholders, message: ChatMessage): Component {
            return sequenceOf<suspend Component.() -> Component>(
                { source.format.resolvePlaceholders(this, source.placeholderResolver(placeholders), message.source) },
                { recipient.format.resolvePlaceholders(this, recipient.placeholderResolver(placeholders), message.recipient) },
                { replaceText { it.match(placeholders.content).replacement(message.content) } },
            ).fold(format) { acc, transform -> transform(acc) }
        }

        override fun build(block: Placeholders.() -> Component): ChatMessageFormat {
            val placeholders = Placeholders()
            return ChatMessageFormat(block(placeholders), placeholders)
        }
    }

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<ChatMessageFormat> {

        private val prefix = path.joinToString { "$it." }

        val source = PlayerFormat.Placeholders(path + listOf("source"))
        val recipient = PlayerFormat.Placeholders(path + listOf("recipient"))
        val content: Placeholder = "%${prefix}content%"
    }
}
