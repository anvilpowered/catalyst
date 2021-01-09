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

package org.anvilpowered.catalyst.common.listener;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.event.ChatEvent;
import org.anvilpowered.catalyst.api.event.StaffChatEvent;
import org.anvilpowered.catalyst.api.listener.ChatListener;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
import org.anvilpowered.catalyst.api.registry.ChatChannel;
import org.anvilpowered.catalyst.api.service.ChatFilter;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.StaffChatService;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class CommonChatListener<
    TString,
    TPlayer,
    TCommandSource>
    implements ChatListener<TString, TPlayer> {

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private PermissionService permissionService;

    @Inject
    private StaffChatService staffChatService;

    @Inject
    private EventService eventService;

    @Inject
    private ChatFilter chatFilter;

    @Inject
    private Registry registry;

    @Inject
    private StaffChatEvent<TString, TPlayer> staffChatEvent;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Override
    public void onPlayerChat(ChatEvent<TString, TPlayer> event) {
        UUID playerUUID = userService.getUUID(event.getPlayer());
        Optional<TPlayer> player = userService.getPlayer(playerUUID);
        if (!player.isPresent()) {
            return;
        }
        String message = event.getRawMessage();
        if (staffChatService.contains(playerUUID)) {
            staffChatEvent.setPlayer(player.get());
            staffChatEvent.setMessage(event.getMessage());
            staffChatEvent.setIsConsole(false);
            staffChatEvent.setRawMessage(event.getRawMessage());
            eventService.getEventBus().post(staffChatEvent);
            return;
        }

        Optional<ChatChannel> channel = chatService.getChannelFromId(chatService.getChannelIdForUser(playerUUID));
        message = chatService.checkPlayerName(player.get(), message);

        if (channel.isPresent()) {
            if (!permissionService.hasPermission(player, registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN_PERMISSION))
                && registry.getOrDefault(CatalystKeys.CHAT_FILTER_ENABLED)) {
                event.setRawMessage(chatFilter.replaceSwears(message));
            }
            chatService.sendChatMessage(event);
        } else {
            throw new AssertionError(
                "If this is your first time running anvil, run /av reload Catalyst, if not report this" +
                    " github."
            );
        }
    }
}
