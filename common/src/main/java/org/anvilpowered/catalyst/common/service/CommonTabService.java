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
import com.google.inject.Singleton;
import org.anvilpowered.anvil.api.util.CurrentServerService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.anvilpowered.catalyst.api.service.TabService;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class CommonTabService<TString, TCommandSource> implements TabService<TString> {

    public Map<String, Double> playerBalances = new HashMap<>();
    @Inject
    private TextService<TString, TCommandSource> textService;
    @Inject
    private CurrentServerService currentServerService;
    @Inject
    private AdvancedServerInfoService advancedServerInfoService;

    @Override
    public TString formatTab(String format, String userName, String prefix, String suffix) {
        format = format.replace("%player%", userName);
        format = format.replace("%prefix%", prefix);
        format = format.replace("%suffix%", suffix);

        format = format.replace("%server%",
            currentServerService.getName(userName).orElse("Null"));
        return textService.deserialize(format);
    }

    @Override
    public TString formatPlayerSpecificTab(String format, String userName, String prefix, String suffix, long ping, int playerCount, boolean useAdvancedServerInfo) {
        format = format.replace("%player%", userName)
            .replace("%prefix%", prefix)
            .replace("%suffix%", suffix)
            .replace("%server%",
                useAdvancedServerInfo
                    ? currentServerService.getName(userName).orElse("null").replace(advancedServerInfoService.getPrefixForPlayer(userName), "")
                    : currentServerService.getName(userName).orElse("null"))
            .replace("%ping%", String.valueOf(ping))
            .replace("%playercount%", String.valueOf(playerCount))
            .replace("%balance%", getBalance(userName));

        return textService.deserialize(format);
    }

    @Override
    public String getBalance(String userName) {
        return String.valueOf(this.playerBalances.get(userName));
    }

    @Override
    public void setBalance(String userName, double balance) {
        if (containsKey(userName)) {
            playerBalances.replace(userName, balance);
        } else {
            playerBalances.put(userName, balance);
        }
    }

    private boolean containsKey(String userName) {
        return playerBalances.containsKey(userName);
    }
}
