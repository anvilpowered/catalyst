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
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.StaffChatEvent;
import org.anvilpowered.catalyst.api.listener.DiscordChatListener;
import org.anvilpowered.catalyst.api.listener.StaffChatListener;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.LoggerService;

import java.util.UUID;

public class CommonStaffChatListener<
    TUser,
    TString,
    TPlayer,
    TCommandSource,
    TSubject,
    TEvent>
    implements StaffChatListener<TPlayer> {

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private StaffChatEvent<TString, TPlayer> staffChatEvent;

    @Inject
    private EventService<TEvent> eventService;

    @Inject
    private DiscordChatListener<TString, TPlayer> discordChatListener;

    @Inject
    private LoggerService<TString> loggerService;


    @Override
    public void onStaffChatEvent(TPlayer player, UUID playerUUID, String message) {
        userService.getOnlinePlayers().forEach(p -> {
            if (permissionService.hasPermission((TSubject) p, registry.getOrDefault(CatalystKeys.STAFFCHAT))) {
                textService.send(pluginMessages.getStaffChatMessageFormatted(userService.getUserName((TUser) player), textService.of(message)), (TCommandSource) p);
            }
        });
        loggerService.info("[STAFF] " + userService.getUserName(playerUUID).orElse("null") + " : " + message);
        staffChatEvent.setSender(player);
        staffChatEvent.setRawMessage(message);
        staffChatEvent.setMessage(textService.of(message));
        eventService.fire((TEvent) staffChatEvent);
        discordChatListener.onStaffChatEvent(staffChatEvent);
    }
}
