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
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import io.circe.Encoder
import io.circe.Decoder
import io.circe.Codec

object MiniMessageCodec { // KSerializer[Component], TypeSerializer[Component] {
  given codec: Codec[Component] = Codec.from(
    Decoder.decodeString.map { MiniMessage.miniMessage().deserialize(_, Seq.empty*) },
    Encoder.encodeString.contramap { MiniMessage.miniMessage().serialize(_) },
  )
  //
  // override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Component", PrimitiveKind.STRING)
  // override def deserialize(decoder: Decoder): Component = MiniMessage.miniMessage().deserialize(decoder.decodeString())
  // override def serialize(encoder: Encoder, value: Component) = encoder.encodeString(MiniMessage.miniMessage().serialize(value))
  //
  // override def deserialize(type: Type, node: ConfigurationNode): Component {
  //     return MiniMessage.miniMessage().deserialize(checkNotNull(node.string) { "Unable to parse String from node ${node.path()}" })
  // }
  //
  // override def serialize(type: Type, obj: Component?, node: ConfigurationNode) {
  //     node.set(obj?.let { MiniMessage.miniMessage().serialize(it) })
  // }
}
