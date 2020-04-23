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
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.PermissionService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.StaffChatEvent;
import org.anvilpowered.catalyst.api.listener.StaffChatListener;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.StaffChatService;

import java.util.UUID;

public class CommonStaffChatCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource,
    TSubject,
    TEvent> {

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private PermissionService<TSubject> permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private StaffChatService staffChatService;

    @Inject
    private EventService<TEvent> eventService;

    @Inject
    private StaffChatEvent<TString, TPlayer> staffChatEvent;

    @Inject
    private StaffChatListener<TPlayer> staffChatListener;

    public void execute(TCommandSource source, TSubject subject, String[] args) {
        if (!permissionService.hasPermission(subject, registry.getOrDefault(CatalystKeys.STAFFCHAT_PERMISSION))) {
            textService.send(pluginMessages.getNoPermission(), source);
            return;
        }

        UUID playerUUID = userService.getUUID((TPlayer) source);

        if (args.length == 0) {
            if (staffChatService.contains(playerUUID)) {
                staffChatService.remove(playerUUID);
                textService.send(pluginMessages.getStaffChat(false), source);
                return;
            } else {
                staffChatService.add(playerUUID);
                textService.send(pluginMessages.getStaffChat(true), source);
                return;
            }
        } else {
            String message = String.join(" ", args);
            staffChatEvent.setRawMessage(message);
            staffChatEvent.setMessage(textService.of(message));
            staffChatEvent.setSender((TPlayer) source);
            eventService.fire((TEvent) staffChatEvent);
            staffChatListener.onStaffChatEvent((TPlayer) source, playerUUID, message);
        }
    }
}
