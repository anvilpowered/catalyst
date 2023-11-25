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
import org.anvilpowered.catalyst.api.chat.ChannelMessage

@Serializable(with = ChannelMessageFormat.Serializer::class)
class ChannelMessageFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) : MessageFormat {

    suspend fun resolve(message: ChannelMessage): PlayerFormat = resolve(format, placeholders, message)

    // TODO: Consider DI
    companion object : MessageFormat.Builder<Placeholders, ChannelMessageFormat> {

        private val channelContext = NestedFormat(ChatChannelFormat, Placeholders::channel)

        suspend fun resolve(format: Component, placeholders: Placeholders, message: ChannelMessage): PlayerFormat {
            val resultFormat = sequenceOf<suspend Component.() -> Component>(
                { channelContext.format.resolve(this, channelContext.placeholderResolver(placeholders), message.channel) },
                { replaceText { it.match(placeholders.name).replacement(message.name) } },
                { replaceText { it.match(placeholders.content).replacement(message.content) } },
            ).fold(format) { acc, transform -> transform(acc) }
            return PlayerFormat(resultFormat, PlayerFormat.ConcretePlaceholders(listOf("recipient")))
        }

        override fun build(block: Placeholders.() -> Component): ChannelMessageFormat {
            val placeholders = Placeholders()
            return ChannelMessageFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<ChannelMessageFormat>(::ChannelMessageFormat)

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<ChannelMessageFormat> {

        private val prefix = path.joinToString { "$it." }

        // TODO: Replace with tag resolver
        val channel = ChatChannelFormat.Placeholders(path + "channel")
        val name: Placeholder = "%${prefix}name%"
        val content: Placeholder = "%${prefix}content%"
    }
}
