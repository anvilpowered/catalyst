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
import org.anvilpowered.catalyst.api.config.ChatChannel

@Serializable(with = ChatChannelFormat.Serializer::class)
class ChatChannelFormat(
    override val format: Component,
    private val placeholders: Placeholders = Placeholders(),
) : MessageFormat {

    class Resolver {
        fun resolve(format: Component, placeholders: Placeholders, channel: ChatChannel): Component {
            return sequenceOf<Component.() -> Component>(
                { replaceText { it.matchLiteral(placeholders.id).replacement(channel.id) } },
                { replaceText { it.matchLiteral(placeholders.name).replacement(channel.name) } },
                { replaceText { it.matchLiteral(placeholders.alwaysVisible).replacement(channel.alwaysVisible.toString()) } },
                { replaceText { it.matchLiteral(placeholders.availableByDefault).replacement(channel.availableByDefault.toString()) } },
                { replaceText { it.matchLiteral(placeholders.discordChannelId).replacement(channel.discordChannelId) } },
            ).fold(format) { acc, transform -> transform(acc) }
        }

        fun resolve(format: ChatChannelFormat, channel: ChatChannel): Component = resolve(format.format, format.placeholders, channel)
    }

    companion object Builder : MessageFormat.Builder<Placeholders, ChatChannelFormat> {
        override fun build(block: Placeholders.() -> Component): ChatChannelFormat {
            val placeholders = Placeholders()
            return ChatChannelFormat(block(placeholders), placeholders)
        }
    }

    object Serializer : MessageFormat.Serializer<ChatChannelFormat>(::ChatChannelFormat)

    open class Placeholders internal constructor(path: List<String> = listOf()) : MessageFormat.Placeholders<ChatChannelFormat> {

        private val pathPrefix = path.joinToString("") { "$it." }

        // TODO: Replace with tag resolver
        val id: Placeholder = "%${pathPrefix}id%"
        val name: Placeholder = "%${pathPrefix}name%"
        val alwaysVisible: Placeholder = "%${pathPrefix}alwaysVisible%"
        val availableByDefault: Placeholder = "%${pathPrefix}availableByDefault%"
        val discordChannelId: Placeholder = "%${pathPrefix}discordChannelId%"
    }
}
