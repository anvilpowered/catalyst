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

import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.chat.ChannelMessage

@Serializable(with = ChannelMessageFormat.Serializer::class)
class ChannelMessageFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) : MessageFormat {

    class Resolver(private val chatChannelFormatResolver: ChatChannelFormat.Resolver) {
        suspend fun resolve(format: Component, placeholders: Placeholders, message: ChannelMessage): PlayerFormat {
            val resultFormat = sequenceOf<suspend Component.() -> Component>(
                { chatChannelFormatResolver.resolve(this, placeholders.channel, message.channel) },
                { replaceText { it.matchLiteral(placeholders.name).replacement(message.name) } },
                { replaceText { it.matchLiteral(placeholders.content).replacement(message.content) } },
            ).fold(format) { acc, transform -> transform(acc) }
            return PlayerFormat(resultFormat, PlayerFormat.ConcretePlaceholders(listOf("recipient")))
        }

        suspend fun resolve(format: ChannelMessageFormat, message: ChannelMessage): PlayerFormat =
            resolve(format.format, format.placeholders, message)
    }

    companion object Builder : MessageFormat.Builder<Placeholders, ChannelMessageFormat> {
        override fun build(block: Placeholders.() -> Component): ChannelMessageFormat {
            val placeholders = Placeholders()
            return ChannelMessageFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<ChannelMessageFormat>(::ChannelMessageFormat)

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<ChannelMessageFormat> {

        private val pathPrefix = path.joinToString("") { "$it." }

        // TODO: Replace with tag resolver
        val channel = ChatChannelFormat.Placeholders(path + "channel")
        val name: Placeholder = "%${pathPrefix}name%"
        val content: Placeholder = "%${pathPrefix}content%"
    }
}
