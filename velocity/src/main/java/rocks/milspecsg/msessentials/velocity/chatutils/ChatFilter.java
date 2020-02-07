/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.velocity.chatutils;

import com.google.inject.Inject;
import rocks.milspecsg.anvil.api.data.registry.Registry;
import rocks.milspecsg.msessentials.api.data.key.MSEssentialsKeys;

import java.util.ArrayList;
import java.util.List;


public class ChatFilter {

    private Registry registry;

    @Inject
    public ChatFilter(Registry registry) {
        this.registry = registry;
    }

    public List<String> aggressiveMode(String swear) {
        List<String> finalWords = new ArrayList<>();

        String message = swear.toLowerCase()
            .replaceAll("[*()/.,;'#~^+-]", " ").replaceAll("[0@]", "o")
            .replaceAll("1", "i").replaceAll("$", "s"
            ).replaceAll(" ", "");

        finalWords.add(removeDuplicates(message));
        finalWords.add(swear.toLowerCase());
        return finalWords;
    }

    public String removeDuplicates(String s) {
        if (s.length() <= 1) return s;
        if (s.substring(1, 2).equalsIgnoreCase(s.substring(0, 1))) return removeDuplicates(s.substring(1));
        else return s.substring(0, 1) + removeDuplicates(s.substring(1));
    }

    public List<String> checkSwear(List<String> finalWords) {
        List<String> swearList = new ArrayList<>();
        for (String exception : registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS)) {
            for (String swear : finalWords) {
                if (swear.contains(exception)) {
                    return null;
                }
            }
        }
        for (String swears : registry.getOrDefault(MSEssentialsKeys.CHAT_FILTER_SWEARS)) {
            for (String swear : finalWords) {
                String newSwear = swear.toLowerCase();
                if (newSwear.contains(swears.toLowerCase())) {
                    if (!(swearList.contains(swears.toLowerCase()))) {
                        swearList.add(swears.toLowerCase());
                    }
                }
            }
        }
        if (swearList.isEmpty()) return null;

        return swearList;
    }

    public List<String> isSwear(String swear) {
        return checkSwear(aggressiveMode(swear));
    }

}
