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

package org.anvilpowered.catalyst.velocity.command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class KickCommand implements Command {

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Registry registry;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        String kickReason = "You have been kicked!";
        if (!source.hasPermission(registry.getOrDefault(CatalystKeys.KICK))) {
            source.sendMessage(pluginMessages.getNoPermission());
            return;
        }
        if (!(args.length >= 1)) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            source.sendMessage(pluginMessages.kickCommandUsage());
            return;
        }
        if (args.length > 1) {
            kickReason = args[1];
        }

        Optional<Player> player = proxyServer.getPlayer(args[0]);
        if (player.isPresent()) {
            if (player.get().hasPermission(registry.getOrDefault(CatalystKeys.KICK_EXEMPT))) {
                source.sendMessage(pluginMessages.getKickExempt());
                return;
            }
            player.get().disconnect(TextComponent.of(kickReason));
        } else {
            source.sendMessage(
                pluginInfo.getPrefix().append(TextComponent.of("Offline or invalid player.")));
        }
    }
}
