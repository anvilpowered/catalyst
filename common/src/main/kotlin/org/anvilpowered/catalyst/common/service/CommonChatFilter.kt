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
import org.anvilpowered.catalyst.api.registry.CatalystKeys.CHAT_FILTER_EXCEPTIONS
import org.anvilpowered.catalyst.api.registry.CatalystKeys.CHAT_FILTER_SWEARS
import org.anvilpowered.catalyst.api.service.ChatFilter
import java.util.ArrayList
import java.util.Collections
import java.util.Locale
import java.util.stream.Collectors

class CommonChatFilter @Inject constructor(
  private val registry: Registry
) : ChatFilter {

  override fun stripMessage(checkMessage: String): String {
    return checkMessage.lowercase(Locale.getDefault())
      .replace("[*()/.,;'#~^+\\-]".toRegex(), " ").replace("[0@]".toRegex(), "o")
      .replace("1".toRegex(), "i").replace("\\$".toRegex(), "s")
  }


  override fun findSpacePositions(message: String, noSpaces: String): List<Int> {
    val spacePositions: MutableList<Int> = ArrayList()
    var regularIndex = 0
    for (noSpacesIndex in noSpaces.indices) {
      if (message[regularIndex] == ' ') {
        spacePositions.add(noSpacesIndex - 1)
        regularIndex++
      }
      regularIndex++
    }
    return spacePositions
  }

  override fun findSwears(message: String, spacePositions: List<Int>): List<IntArray> {
    val swearList: MutableList<IntArray> = ArrayList()
    val exceptions = registry.getOrDefault(CHAT_FILTER_EXCEPTIONS).stream().map { it.lowercase(Locale.getDefault()) }.collect(Collectors.toList())
    for (bannedWord in registry.getOrDefault(CHAT_FILTER_SWEARS)) {
      if (message.contains(bannedWord) && !exceptions.contains(bannedWord)) {
        var startIndex = message.indexOf(bannedWord)
        while (startIndex != -1) {
          val endIndex = startIndex + bannedWord.length - 1
          val extraStartSpace = spacePositions.indexOf(startIndex - 1) + 1
          var extraEndSpace = spacePositions.indexOf(endIndex)
          if (spacePositions.containsAll(listOf(startIndex - 1, endIndex))) {
            val wordLocation = intArrayOf(startIndex + extraStartSpace, endIndex + extraEndSpace + 1)
            swearList.add(wordLocation)
          } else if ((spacePositions.contains(startIndex - 1) || startIndex == 0) &&
            (endIndex == message.length - 1 || spacePositions.contains(endIndex))
          ) {
            if (endIndex == message.length - 1) {
              extraEndSpace = spacePositions.size
            }
            val wordLocation = intArrayOf(startIndex + extraStartSpace, endIndex + extraEndSpace + 1)
            swearList.add(wordLocation)
          }
          startIndex = message.indexOf(bannedWord, startIndex + 1)
        }
      }
    }
    return swearList
  }

  override fun replaceSwears(message: String): String {
    var message = message
    val strippedMessage = stripMessage(message)
    val noSpacesMessage = strippedMessage.replace(" ".toRegex(), "")
    val spacePositions = findSpacePositions(strippedMessage, noSpacesMessage)
    val swearPositions = findSwears(noSpacesMessage, spacePositions)
    for (swearPosition in swearPositions) {
      val swearStart = swearPosition[0]
      val swearEnd = swearPosition[1]
      val swearLength = swearEnd - swearStart
      message = if (swearEnd >= message.length) {
        message.substring(0, swearStart) + java.lang.String.join("", Collections.nCopies(swearLength, "*"))
      } else {
        (message.substring(0, swearStart)
          + java.lang.String.join("", Collections.nCopies(swearLength, "*")) + message.substring(swearEnd))
      }
    }
    return message
  }
}
