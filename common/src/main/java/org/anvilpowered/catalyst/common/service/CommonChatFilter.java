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
        int regularIndex = 0;
        for (int noSpacesIndex = 0; noSpacesIndex < noSpaces.length(); noSpacesIndex++) {
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
                int startIndex = message.indexOf(bannedWord);
                while (startIndex != -1) {
                    int endIndex = startIndex + bannedWord.length() - 1; // Inclusive
                    int extraStartSpace = spacePositions.indexOf(startIndex - 1) + 1; // Adjusts the indexing to account for deleted spaces.
                    int extraEndSpace = spacePositions.indexOf(endIndex);
                    if (spacePositions.containsAll(Arrays.asList(startIndex - 1, endIndex))) { // If there are spaces before and after the word.
                        // Array to store the full location of the word in the original message.
                        int[] wordLocation = new int[]{startIndex + extraStartSpace, endIndex + extraEndSpace + 1};
                        swearList.add(wordLocation);
                    } else if ((spacePositions.contains(startIndex - 1) || startIndex == 0) &&
                            (endIndex == message.length() - 1|| spacePositions.contains(endIndex))) {
                        if (endIndex == message.length() - 1) {
                            extraEndSpace = spacePositions.size();
                        }
                        // +1 to extraEndSpace as the next function in replaceSwears is exclusive
                        int[] wordLocation = new int[]{startIndex + extraStartSpace, endIndex + extraEndSpace + 1};
                        swearList.add(wordLocation);
                    }
                    startIndex = message.indexOf(bannedWord, startIndex+1); // checks for any more instances of the banned word
                }
            }
        }
        return swearList;
    }

    @Override
    public String replaceSwears(String message) {
        String strippedMessage = stripMessage(message); // Replaces special characters
        String noSpacesMessage = strippedMessage.replaceAll(" ", ""); // Removes spaces
        List<Integer> spacePositions = findSpacePositions(strippedMessage, noSpacesMessage); // Finds where the spaces are.
        List<int[]> swearPositions = findSwears(noSpacesMessage, spacePositions); // List of arrays, one for each occurrence
        for (int[] swearPosition : swearPositions) { // Iterates through each occurrence of swearing
            int swearStart = swearPosition[0]; // Beginning of the swear, inclusive
            int swearEnd = swearPosition[1]; // End of the swear, exclusive
            int swearLength = swearEnd - swearStart; // Length of the swear
            /* If the swear is due to end at the end of the string, add *s for the length of the swear and finish,
            else add the rest of the string after the end of the swear.
             */
            if (swearEnd >= message.length()) { // If the swear is at the end of the message, skip adding the rest
                message = message.substring(0, swearStart) + // This join method adds swearLength '*'s.
                        String.join("", Collections.nCopies(swearLength, "*"));
            } else { // Add the beginning & the end of the message around the swear
                message = message.substring(0, swearStart) +
                        String.join("", Collections.nCopies(swearLength, "*"))
                        + message.substring(swearEnd);
            }
        }
        return message;
    }

}