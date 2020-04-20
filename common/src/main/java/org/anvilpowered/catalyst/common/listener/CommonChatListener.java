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
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.config.ChatChannel;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.ChatEvent;
import org.anvilpowered.catalyst.api.listener.ChatListener;
import org.anvilpowered.catalyst.api.listener.DiscordChatListener;
import org.anvilpowered.catalyst.api.listener.StaffChatListener;
import org.anvilpowered.catalyst.api.service.ChatFilter;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.StaffChatService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CommonChatListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource,
    TSubject,
    TEvent>
    implements ChatListener<TPlayer> {

    @Inject
    private ChatService<TString, TPlayer, TCommandSource> chatService;

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private StaffChatService staffChatService;

    @Inject
    private ChatEvent<TString, TPlayer> chatEvent;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private EventService<TEvent> eventService;

    @Inject
    private ChatFilter chatFilter;

    @Inject
    private Registry registry;

    @Inject
    private StaffChatListener<TPlayer> staffChatListener;

    @Inject
    private DiscordChatListener<TString, TPlayer> discordChatListener;

    @Override
    public void onPlayerChat(TPlayer player, UUID playerUUID, String message) {
        if (staffChatService.contains(playerUUID)) {
            staffChatListener.onStaffChatEvent(player, playerUUID, message);
            return;
        }

        Optional<ChatChannel> channel = chatService.getChannelFromId(chatService.getChannelIdForUser(playerUUID));
        List<String> swearList = chatFilter.isSwear(message);
        message = chatService.checkPlayerName(message);

        if (channel.isPresent()) {

            if (!permissionService.hasPermission((TSubject) player, registry.getOrDefault(CatalystKeys.LANGUAGE_ADMIN))) {
                message = chatFilter.replaceSwears(message);
            }

            chatEvent.setSender(player);
            chatEvent.setMessage(textService.of(message));
            chatEvent.setRawMessage(message);
            eventService.fire((TEvent) chatEvent);
            discordChatListener.onChatEvent(chatEvent);
            chatService.sendChatMessage(player, playerUUID, message);
        } else {
            throw new AssertionError(
                "Unable to find a chat channel for " + userService.getUserName((TUser) player) +
                    " please report this on github."
            );
        }
    }
}
