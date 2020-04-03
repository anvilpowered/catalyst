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
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.velocity.plugin.CatalystVelocity;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageCommand implements Command {

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private PrivateMessageService<TextComponent> privateMessageService;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        String name;
        if (args.length < 1) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            source.sendMessage(pluginMessages.messageCommandUsage());
            return;
        }

        Optional<Player> recipient = CatalystVelocity.getServer().getPlayer(args[0]);
        if (source instanceof ConsoleCommandSource) {
            if (recipient.isPresent()) {
                name = args[0];
                args[0] = "";

                String message = String.join(" ", args);
                privateMessageService.sendMessage("Me", name, message);
                privateMessageService.sendMessage("Console", "Me", message);
            }
        } else if (source instanceof Player) {
            Player sender = (Player) source;

            if (sender.hasPermission(registry.getOrDefault(CatalystKeys.MESSAGE))) {
                if (recipient.isPresent()) {
                    if (args[0].equalsIgnoreCase(recipient.get().getUsername())) {
                        args[0] = args[0].toLowerCase();
                        String recipientName = recipient.get().getUsername();
                        String message = String.join(" ", args)
                            .replace(recipientName.toLowerCase(), "");
                        privateMessageService.sendMessage(
                            sender.getUsername(), recipient.get().getUsername(), message);
                        if (sender.getUniqueId().equals(recipient.get().getUniqueId())) {
                            return;
                        }
                        privateMessageService.replyMap().put(
                            recipient.get().getUniqueId(), sender.getUniqueId());
                    }
                }
            }
        }
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return CatalystVelocity.getServer().matchPlayer(args[0])
                .stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}