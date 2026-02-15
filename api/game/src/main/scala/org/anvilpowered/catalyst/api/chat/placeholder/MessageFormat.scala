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
import net.kyori.adventure.text.minimessage.MiniMessage
import org.anvilpowered.anvil.chat.MiniMessageCodec

type Placeholder = String

trait MessageFormat {

  val format: Component
}

object MessageFormat {
  trait Placeholders[-M <: MessageFormat]
  trait Builder[+P, +M <: MessageFormat] {
    def build(block: P ?=> Component): M
  }
  def codec[T <: MessageFormat](constructor: (format: Component) => T): Codec[T] =
    MiniMessageCodec.codec.iemap(c => Right(constructor(c)))(_.format)

  def serializer[T <: MessageFormat](constructor: (format: Component) => T): TypeSerializer[T] = new TypeSerializer[T] {
    override def deserialize(`type`: Type, node: ConfigurationNode): T =
      constructor(MiniMessageCodec.typeSerializer.deserialize(`type`, node))
    override def serialize(`type`: Type, obj: T, node: ConfigurationNode): Unit =
      MiniMessageCodec.typeSerializer.serialize(`type`, obj.format, node)
  }
}

// inline def [reified T] TypeSerializerCollection.Builder.register(serializer: TypeSerializer[T]): TypeSerializerCollection.Builder {
//     return register(T::class.java, serializer)
// }
