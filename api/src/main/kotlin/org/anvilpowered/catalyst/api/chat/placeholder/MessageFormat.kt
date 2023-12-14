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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.Component
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import java.lang.reflect.Type

typealias Placeholder = String

interface MessageFormat {

    val format: Component

    interface Builder<out P, out M : MessageFormat> {
        fun build(block: P.() -> Component): M
    }

    interface Placeholders<in M : MessageFormat>

    open class Serializer<T : MessageFormat>(private val constructor: (format: Component) -> T) : KSerializer<T>, TypeSerializer<T> {
        override val descriptor: SerialDescriptor = MiniMessageSerializer.descriptor
        override fun deserialize(decoder: Decoder): T = constructor(MiniMessageSerializer.deserialize(decoder))
        override fun serialize(encoder: Encoder, value: T) = MiniMessageSerializer.serialize(encoder, value.format)
        override fun deserialize(type: Type, node: ConfigurationNode): T =
            constructor(MiniMessageSerializer.deserialize(Component::class.java, node))

        override fun serialize(type: Type, obj: T?, node: ConfigurationNode) = MiniMessageSerializer.serialize(type, obj?.format, node)
    }
}

inline fun <reified T> TypeSerializerCollection.Builder.register(serializer: TypeSerializer<T>): TypeSerializerCollection.Builder {
    return register(T::class.java, serializer)
}
