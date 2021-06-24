/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.catalyst.api.registry.CatalystKeys.EMOJI_MAP
import org.anvilpowered.catalyst.api.service.EmojiService

class CommonEmojiService @Inject constructor(
  private val registry: Registry
): EmojiService {
  
  override fun getEmojis(): Map<String, Char> = registry.getOrDefault(EMOJI_MAP)

  override fun toEmoji(message: String, chatColor: String): String {
    var message = message
    for (key in emojis.keys) {
      message = message.replace(key, "&f" + emojis[key] + chatColor)
    }
    return message
  }

  override fun toEmojiWithoutColor(message: String): String {
    var message = message
    for (key in emojis.keys) {
      message = message.replace(key, "" + emojis[key])
    }
    return message
  }
}
