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

package org.anvilpowered.catalyst.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.entity.living.player.Player;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.StringResult;
import org.anvilpowered.catalyst.api.chat.ChatService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class BaseChannelCommand implements Command {

    @Inject
    private Registry registry;

    @Inject
    private ChatService<TextComponent> chatService;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private StringResult<TextComponent, Player> stringResult;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if (source instanceof Player) {
            Player player = (Player) source;

            switch (args[0]) {
                case ("info"): {
                    if (source.hasPermission(registry.getOrDefault(CatalystKeys.CHANNEL_BASE) + args[1])) {

                        source.sendMessage(stringResult.builder()
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
                    if (source.hasPermission(registry.getOrDefault(CatalystKeys.CHANNEL_BASE) + args[1])) {
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
        return source.hasPermission(registry.getOrDefault(CatalystKeys.CHANNEL_BASE) + args[0]);
    }
}

