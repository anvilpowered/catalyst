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
import java.util.List;

public class CommonChatFilter implements ChatFilter {

    @Inject
    private Registry registry;

    @Override
    public List<String> aggressiveMode(String checkMessage) {
        String message = checkMessage.toLowerCase()
                .replaceAll("[*()/.,;'#~^+\\-]", " ").replaceAll("[0@]", "o")
                .replaceAll("1", "i").replaceAll("\\$", "s"
                );

        return new ArrayList<>(Arrays.asList(message.split(" ")));
    }

    @Override
    public List<String> checkSwear(List<String> finalWords) {
        List<String> swearList = new ArrayList<>();

        for (String bannedWord : registry.getOrDefault(CatalystKeys.CHAT_FILTER_SWEARS)) {
            for (String word : finalWords) {
                if (word.equalsIgnoreCase(bannedWord)) {
                    if (!(swearList.contains(bannedWord.toLowerCase()))) {
                        swearList.add(bannedWord.toLowerCase());
                    }
                }
            }
        }
        for (String exception : registry.getOrDefault(CatalystKeys.CHAT_FILTER_EXCEPTIONS)) {
            for (String swear : swearList) {
                if (swear.equalsIgnoreCase(exception.toLowerCase())) {
                    swearList.remove(exception.toLowerCase());
                }
            }
        }
        if (swearList.isEmpty()) return null;

        return swearList;
    }

    @Override
    public List<String> isSwear(String message) {
        return checkSwear(aggressiveMode(message));
    }

}
