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

package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentials;
import rocks.milspecsg.msessentials.events.ProxyMessageEvent;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private CommandUsageMessages commandUsageMessages;


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        String name;
        if (args.length < 1) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            source.sendMessage(commandUsageMessages.messageCommandUsage);
            return;
        }

        Optional<Player> recipient = MSEssentials.getServer().getPlayer(args[0]);
        if (source instanceof ConsoleCommandSource) {
            if (recipient.isPresent()) {
                name = args[0];
                args[0] = "";

                String message = String.join(" ", args);
                source.sendMessage(ProxyMessageEvent.message("Me", name, message));
                recipient.get().sendMessage(ProxyMessageEvent.message("Console", "Me", message));
            }
        } else if (source instanceof Player) {
            Player sender = (Player) source;

            if (sender.hasPermission(PluginPermissions.MESSAGE)) {
                if (recipient.isPresent()) {
                    if (args[0].equalsIgnoreCase(recipient.get().getUsername())) {
                        args[0] = args[0].toLowerCase();
                        String recipientName = recipient.get().getUsername();
                        String message = String.join("", args).replace(recipientName.toLowerCase(), "");
                        ProxyMessageEvent.sendMessage(sender, recipient.get(), message, proxyServer);
                        if (sender.getUniqueId().equals(recipient.get().getUniqueId())) {
                            return;
                        }
                        ProxyMessageEvent.replyMap.put(recipient.get().getUniqueId(), sender.getUniqueId());
                    }
                }
            }
        }
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return MSEssentials.getServer().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}