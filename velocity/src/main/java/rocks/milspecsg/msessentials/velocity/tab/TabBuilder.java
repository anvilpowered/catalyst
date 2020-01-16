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

package rocks.milspecsg.msessentials.velocity.tab;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.velocity.utils.LuckPermsUtils;
import rocks.milspecsg.msessentials.velocity.messages.PluginMessages;

public class TabBuilder {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private TabUtils tabUtils;


    public TextComponent formatPlayerTab(String raw, Player player) {
        raw = raw.replace("%player%", player.getUsername());
        raw = raw.replace("%prefix%", LuckPermsUtils.getPrefix(player));
        raw = raw.replace("%suffix%", LuckPermsUtils.getSuffix(player));

        raw = raw.replace("%server%", player.getCurrentServer().get().getServerInfo().getName());

        return pluginMessages.legacyColor(raw);
    }

    public TextComponent formatTab(String raw, Player player) {
        raw = raw.replace("%player%", player.getUsername())
                .replace("%prefix%", LuckPermsUtils.getPrefix(player))
                .replace("%suffix%", LuckPermsUtils.getSuffix(player))
                .replace("%server%", player.getCurrentServer().get().getServerInfo().getName())
                .replace("%ping%", String.valueOf(player.getPing()))
                .replace("%playercount%", String.valueOf(proxyServer.getPlayerCount()))
                .replace("%balance%", getBalance(player));

        return pluginMessages.legacyColor(raw);
    }

    private String getBalance(Player player) {
        if (tabUtils.playerBalances.containsKey(player.getUsername())) {
            return String.valueOf(tabUtils.playerBalances.get(player.getUsername()));
        } else {
            return "null";
        }
    }
}
