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

package rocks.milspecsg.msessentials.modules.chatutils;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;


public class ChatFilter {

    private ConfigurationService configurationService;

    @Inject
    public ChatFilter(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public List<String> aggressiveMode(String swear) {

        String message = swear.toLowerCase();

        List<String> finalwords = new ArrayList<>();

        String mess = message.replace("*", " ");
        String mes3 = mess.replace("()", "o");
        String mes2 = mes3.replace("(", " ");
        String mes = mes2.replace(")", " ");
        String me1 = mes.replace("/", " ");
        String me2 = me1.replace(".", " ");
        String me3 = me2.replace(",", " ");
        String me4 = me3.replace("4", "a");
        String me5 = me4.replace(";", " ");
        String me6 = me5.replace("'", " ");
        String me7 = me6.replace("#", " ");
        String me8 = me7.replace("~", " ");
        String me9 = me8.replace("^", " ");
        String me10 = me9.replace("-", " ");
        String me11 = me10.replace("+", " ");
        String me12 = me11.replace("1", "i");
        String me13 = me12.replace("0", "o");
        String me14 = me13.replace("$", "s");
        String messageo = me14.replace("@", "o");
        String messagea = me14.replace("@", "a");

        String removespaceso = messageo.replaceAll(" ", "");
        String removespacesa = messagea.replaceAll(" ", "");

        String finalchecko = removeDups(removespaceso);
        String finalchecka = removeDups(removespacesa);

        finalwords.add(finalchecko);
        finalwords.add(finalchecka);
        finalwords.add(message);
        return finalwords;

    }

    public String removeDups(String s) {
        if (s.length() <= 1) return s;
        if (s.substring(1, 2).equalsIgnoreCase(s.substring(0, 1))) return removeDups(s.substring(1));
        else return s.substring(0, 1) + removeDups(s.substring(1));
    }

    public List<String> checkswear(List<String> finalwords) {
        List<String> swearlist = new ArrayList<>();
        for (String exception : configurationService.getConfigList(ConfigKeys.CHAT_FILTER_EXCEPTIONS, new TypeToken<List<String>> (){})) {
            for (String swear : finalwords) {
                if (swear.contains(exception)) {
                    return null;
                }
            }
        }
        for (String swears : configurationService.getConfigList(ConfigKeys.CHAT_FILTER_SWEARS, new TypeToken<List<String>> (){})) {
            for (String swear : finalwords) {
                String newswear = swear.toLowerCase();
                if (newswear.contains(swears.toLowerCase())) {
                    if (!(swearlist.contains(swears.toLowerCase()))) {
                        swearlist.add(swears.toLowerCase());
                    }
                }
            }
        }
        if (swearlist.isEmpty()) return null;

        return swearlist;
    }

    public List<String> isswear(String s) {
        return checkswear(aggressiveMode(s));
    }

}
