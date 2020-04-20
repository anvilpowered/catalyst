/*
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.common.service;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.ChatFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommonChatFilter implements ChatFilter {

    @Inject
    private Registry registry;

    @Override
    public String stripMessage(String checkMessage) {
        return checkMessage.toLowerCase()
                .replaceAll("[*()/.,;'#~^+\\-]", " ").replaceAll("[0@]", "o")
                .replaceAll("1", "i").replaceAll("\\$", "s"
                ); // Replaces any special characters with their letter equivalent or a space
    }

    // Generates a list with the locations of all the spaces in the original message - for index replacement later
    @Override
    public List<Integer> findSpacePositions(String message, String noSpaces) {
        List<Integer> spacePositions = new ArrayList<>();
        int noSpacesIndex = 0;
        int regularIndex = 0;
        for (noSpacesIndex++; noSpacesIndex<noSpaces.length();) {
            if (message.charAt(regularIndex) == ' ') {
                spacePositions.add(noSpacesIndex - 1);
                regularIndex++;
            }
            regularIndex++;
        }
        return spacePositions;
    }

    @Override
    public List<int[]> findSwears(String message, List<Integer> spacePositions) {
        List<int[]> swearList = new ArrayList<>(); // List of all swear word locations
        List<String> exceptions = registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS).stream()
                .map(String::toLowerCase).collect(Collectors.toList()); /* Gets exceptions from API, then, via a map,
                                                                           makes them lowercase. */
        for (String bannedWord : registry.getOrDefault(CatalystKeys.CHAT_FILTER_SWEARS)) { /* Iterates through every
                                                                                            banned word to see if the message contains it.*/
            if (message.contains(bannedWord) && (!exceptions.contains(bannedWord))) { /* If the message contains the
                                                                                         word and the word is not exempt. */
                int lastWordIndex = 0;
                while (lastWordIndex != -1) {
                    int startIndex = message.indexOf(bannedWord, lastWordIndex);
                    int endIndex = startIndex + bannedWord.length();
                    int extraStartSpace = spacePositions.indexOf(startIndex - 1) + 1; // Adjusts the indexing to account for deleted spaces.
                    if (spacePositions.containsAll(Arrays.asList(startIndex - 1, endIndex))) { // If there are spaces before and after the word.
                        int extraEndSpace = spacePositions.indexOf(endIndex) + 1;
                        // Array to store the full location of the word in the original message.
                        int[] wordLocation = new int[]{startIndex + extraStartSpace, endIndex + extraEndSpace};
                        swearList.add(wordLocation);
                    } else if (spacePositions.contains(startIndex - 1) && endIndex == message.length()) {
                        int[] wordLocation = new int[]{startIndex + extraStartSpace, endIndex + spacePositions.size()};
                        swearList.add(wordLocation);
                    }
                    lastWordIndex = endIndex;
                }
            }
        }
        return swearList;
    }

    @Override
    public String replaceSwears(String message) {
        String strippedMessage = stripMessage(message); // Replaces special characters
        String noSpacesMessage = strippedMessage.replaceAll(" ", ""); // Removes spaces
        List<Integer> spacePositions = findSpacePositions(strippedMessage, noSpacesMessage);
        List<int[]> swearPositions = findSwears(noSpacesMessage, spacePositions);
        for (int[] swearPosition : swearPositions) {
            int swearStart = swearPosition[0];
            int swearEnd = swearPosition[1];
            int swearLength = swearEnd - swearStart;
            /* If the swear is due to end at the end of the string, add *s for the length of the swear and finish,
            else add the rest of the string after the end of the swear.
             */
            if (swearEnd >= message.length()) {
                message = message.substring(0, swearStart) + // This join method adds swearLength '*'s.
                        String.join("", Collections.nCopies(swearLength, "*"));
            } else {
                message = message.substring(0, swearStart) +
                        String.join("", Collections.nCopies(swearLength, "*"))
                        + message.substring(swearEnd);
            }
        }
        return message;
    }

}