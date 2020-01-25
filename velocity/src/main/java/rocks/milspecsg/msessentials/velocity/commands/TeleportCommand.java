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

package rocks.milspecsg.msessentials.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.utils.PluginPermissions;

import java.util.Optional;

public class TeleportCommand implements Command {

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.TELEPORT_FORCE)) {
            source.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        if (args.length < 1) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            return;
        }

        if (source instanceof Player) {
            Player sourcePlayer = (Player) source;
            Optional<Player> targetPlayer = proxyServer.getPlayer(args[0]);
            if (targetPlayer.isPresent()) {
                if (sourcePlayer.equals(targetPlayer.get())) {
                    sourcePlayer.sendMessage(pluginMessages.getTeleportToSelf());
                    return;
                }
                source.sendMessage(TextComponent.of("Sending player"));
            }
        } else {
            source.sendMessage(TextComponent.of("Player only command!"));
        }
    }
}
