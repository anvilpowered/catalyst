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
import net.kyori.text.TextComponent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.anvilpowered.catalyst.api.chat.ChatService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class ChannelCommand implements Command {

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ChatService<TextComponent, Player> chatService;

    @Inject
    private Registry registry;

    String channelId;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source.hasPermission(registry.getOrDefault(CatalystKeys.CHANNEL_BASE) + channelId)) {
            if (args.length == 0) {
                if (source instanceof Player) {
                    Player player = (Player) source;
                    chatService.switchChannel(player.getUniqueId(), channelId);
                    source.sendMessage(TextComponent.of("Channel switched to " + channelId));
                }
            }
        } else {
            source.sendMessage(pluginMessages.getNoPermission());
        }
    }
}
