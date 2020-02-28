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

package org.anvilpowered.catalyst.velocity.tab;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.anvilpowered.anvil.api.util.StringResult;
import org.anvilpowered.catalyst.velocity.utils.LuckPermsUtils;

public class TabBuilder {

    @Inject
    private StringResult<TextComponent, CommandSource> stringResult;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private TabUtils tabUtils;

    @Inject
    private LuckPermsUtils luckPermsUtils;


    public TextComponent formatPlayerTab(String raw, Player player) {
        raw = raw.replace("%player%", player.getUsername());
        raw = raw.replace("%prefix%", luckPermsUtils.getPrefix(player));
        raw = raw.replace("%suffix%", luckPermsUtils.getSuffix(player));

        raw = raw.replace("%server%", player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("null"));

        return stringResult.deserialize(raw);
    }

    public TextComponent formatTab(String raw, Player player) {
        raw = raw.replace("%player%", player.getUsername())
            .replace("%prefix%", luckPermsUtils.getPrefix(player))
            .replace("%suffix%", luckPermsUtils.getSuffix(player))
            .replace("%server%", player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("null"))
            .replace("%ping%", String.valueOf(player.getPing()))
            .replace("%playercount%", String.valueOf(proxyServer.getPlayerCount()))
            .replace("%balance%", getBalance(player));

        return stringResult.deserialize(raw);
    }

    private String getBalance(Player player) {
        return String.valueOf(tabUtils.playerBalances.get(player.getUsername()));
    }
}
