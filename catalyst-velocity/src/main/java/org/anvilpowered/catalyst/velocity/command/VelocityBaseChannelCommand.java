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
import net.kyori.adventure.text.TextComponent;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.checkerframework.checker.nullness.qual.NonNull;

public class VelocityBaseChannelCommand implements Command {

    @Inject
    private Registry registry;

    @Inject
    private ChatService<TextComponent, Player, CommandSource> chatService;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private TextService<TextComponent, Player> textService;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if (source instanceof Player) {
            Player player = (Player) source;

            switch (args[0]) {
                case ("info"): {
                    if (source.hasPermission(
                        registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION) + args[1])) {

                        source.sendMessage(textService.builder()
                            .gold().append(args[1])
                            .green().append("\n" + chatService.getChannelUserCount(args[1]))
                            .onClickRunCommand("/channel list " + args[1])
                            .build());
                    } else {
                        source.sendMessage(pluginMessages.getNoPermission());
                    }
                    return;
                }
                case ("join"): {
                    if (source.hasPermission(
                        registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION) + args[1])) {
                        chatService.switchChannel(player.getUniqueId(), args[1]);
                    } else {
                        source.sendMessage(pluginMessages.getNoPermission());
                    }
                }
                case ("list"): {
                    chatService.getUsersInChannel(args[1]);
                }
            }
        }
    }

    @Override
    public boolean hasPermission(CommandSource source, @NonNull String[] args) {
        return source.hasPermission(registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION) + args[0]);
    }
}

