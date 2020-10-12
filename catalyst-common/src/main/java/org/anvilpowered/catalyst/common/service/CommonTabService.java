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
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.LocationService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.AdvancedServerInfoService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.api.service.TabService;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class CommonTabService<
    TString,
    TPlayer,
    TCommandSource> implements TabService<TString, TPlayer> {

    public Map<String, Double> playerBalances = new HashMap<>();

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private LocationService locationService;

    @Inject
    private AdvancedServerInfoService advancedServerInfoService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private LuckpermsService luckpermsService;

    @Inject
    private Registry registry;

    @Override
    public TString format(TPlayer player, int ping, int playerCount) {
        return textService.deserialize(
            replacePlaceholders(
                registry.getOrDefault(CatalystKeys.TAB_FORMAT),
                player,
                ping,
                playerCount
            ));
    }

    @Override
    public TString formatCustom(String format, TPlayer player, int ping, int playerCount) {
        return textService.deserialize(replacePlaceholders(format, player, ping, playerCount));
    }

    @Override
    public TString formatHeader(TPlayer player, int ping, int playerCount) {
        return textService.deserialize(
            replacePlaceholders(
                registry.getOrDefault(CatalystKeys.TAB_HEADER),
                player,
                ping,
                playerCount
            ));
    }

    @Override
    public TString formatFooter(TPlayer player, int ping, int playerCount) {
        return textService.deserialize(
            replacePlaceholders(
                registry.getOrDefault(CatalystKeys.TAB_FOOTER),
                player,
                ping,
                playerCount
            ));
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

    private String replacePlaceholders(String format, TPlayer player, int ping, int playerCount) {
        String userName = userService.getUserName(player);
        return format.replace("%player%", userName)
            .replace("%prefix%", luckpermsService.getPrefix(player))
            .replace("%suffix%", luckpermsService.getSuffix(player))
            .replace("%server%",
                registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
                    ? locationService.getServerName(userName).orElse("null")
                    .replace(advancedServerInfoService.getPrefixForPlayer(userName), "")
                    : locationService.getServerName(userName).orElse("null"))
            .replace("%ping%", String.valueOf(ping))
            .replace("%playercount%", String.valueOf(playerCount))
            .replace("%balance%", getBalance(userName));
    }
}
