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

package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.registry.ChatChannel;
import org.anvilpowered.catalyst.api.service.ChatService;

import java.util.ArrayList;
import java.util.List;

public class ChannelCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private PermissionService permissionService;

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    private boolean exists(String channel) {
        List<ChatChannel> channels = registry.getOrDefault(CatalystKeys.CHAT_CHANNELS);
        for (ChatChannel chatChannel : channels) {
            if (chatChannel.id.equals(channel)) {
                return true;
            }
        }
        return false;
    }

    public int set(CommandContext<TCommandSource> context, Class<?> playerClass) {
        if (!playerClass.isAssignableFrom(context.getSource().getClass())) {
            textService.send(textService.of("Player only command!"), context.getSource());
            return 0;
        }
        String channel = context.getArgument("channel", String.class);

        if (channel.contains(" ")) {
            return 0;
        }
        if (!exists(channel)) {
            textService.send(textService.of("Invalid channel!"), context.getSource());
        }
        if (channel.equals(chatService.getChannelIdForUser(userService.getUUID((TPlayer) context.getSource())))) {
            return 0;
        }
        if (permissionService.hasPermission(context.getSource(), registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION) + channel)) {
            chatService.switchChannel(userService.getUUID((TPlayer) context.getSource()), channel);
            textService.send(textService.of("Switched to channel " + channel), context.getSource());
        }

        return 1;
    }

    public int list(CommandContext<TCommandSource> context, Class<?> playerClass) {
        if (!playerClass.isAssignableFrom(context.getSource().getClass())) {
            textService.send(textService.of("Player only command!"), context.getSource());
            return 0;
        }

        String basePerm = registry.getOrDefault(CatalystKeys.CHANNEL_BASE_PERMISSION);
        List<ChatChannel> channels = registry.getOrDefault(CatalystKeys.CHAT_CHANNELS);
        List<TString> availableChannels = new ArrayList<>();
        for (ChatChannel channel : channels) {
            if (permissionService.hasPermission(context.getSource(), basePerm + channel.id)) {
                availableChannels.add(channelInfo(
                    channel.id,
                    chatService.getChannelIdForUser(userService.getUUID((TPlayer) context.getSource())).equals(channel.id)
                    )
                );
            }
        }

        textService.paginationBuilder()
            .title(textService.builder().gold().append("Available Channels").build())
            .padding(textService.builder().dark_green().append("-"))
            .contents(availableChannels).linesPerPage(20)
            .build().sendTo(context.getSource());
        return 1;
    }

    private TString channelInfo(String channelId, boolean active) {
        TextService.Builder<TString, TCommandSource> component = textService.builder();

        if (active) {
            component.green().append(channelId);
        } else {
            component.gray().append(channelId)
                .onClickRunCommand("/channel set " + channelId);
        }
        component.onHoverShowText(
            textService.builder()
                .gray().append("Status: ")
                .green().append(active ? "Active" : "Inactive")
                .gray().append("\nActive Users: ")
                .green().append(chatService.getChannelUserCount(channelId))
                .build()
        );
        return component.build();
    }
}
