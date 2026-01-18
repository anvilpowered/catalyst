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
 *     along with this program.  If not, see [https://www.gnu.org/licenses/].
 */

package org.anvilpowered.catalyst.api.chat.placeholder

import net.kyori.adventure.text.Component
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import java.lang.reflect.Type
import io.circe.Codec

type Placeholder = String

trait MessageFormat {

  val format: Component

  // trait Builder[+P, +M <: MessageFormat] {
  //     def build(block: P.() -> Component): M
  // }


  // open class Serializer[T <: MessageFormat](private val constructor: (format: Component) => T) extends Codec[T], TypeSerializer[T] {
  //   override val descriptor: SerialDescriptor = MiniMessageSerializer.descriptor
  //   override def deserialize(decoder: Decoder): T = constructor(MiniMessageSerializer.deserialize(decoder))
  //   override def serialize(encoder: Encoder, value: T) = MiniMessageSerializer.serialize(encoder, value.format)
  //   override def deserialize(typ: Type, node: ConfigurationNode): T =
  //     constructor(MiniMessageSerializer.deserialize(classOf[Component], node))
  //
  //   override def serialize(typ: Type, obj: Option[T], node: ConfigurationNode) = MiniMessageSerializer.serialize(typ, obj.map(_.format), node)
  // }
}

object MessageFormat {

  trait Placeholders[-M <: MessageFormat]
}

// inline def [reified T] TypeSerializerCollection.Builder.register(serializer: TypeSerializer[T]): TypeSerializerCollection.Builder {
//     return register(T::class.java, serializer)
// }
