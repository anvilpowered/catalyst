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

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable(with = MessageContentFormat.Serializer::class)
class MessageContentFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) : MessageFormat {

    class Resolver {
        fun resolve(format: Component, placeholders: Placeholders, content: Component): Component =
            format.replaceText { it.matchLiteral(placeholders.content).replacement(content) }

        fun resolve(format: MessageContentFormat, content: Component): Component = resolve(format.format, format.placeholders, content)
    }

    companion object : MessageFormat.Builder<Placeholders, MessageContentFormat> {
        override fun build(block: Placeholders.() -> Component): MessageContentFormat {
            val placeholders = Placeholders()
            return MessageContentFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<MessageContentFormat>(::MessageContentFormat)

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<MessageContentFormat> {

        private val pathPrefix = path.joinToString("") { "$it." }

        val content: Placeholder = "%${pathPrefix}content%"
    }
}
