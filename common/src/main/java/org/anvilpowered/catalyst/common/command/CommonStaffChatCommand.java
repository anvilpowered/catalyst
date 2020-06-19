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
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.StaffChatService;

import java.util.UUID;

public class CommonStaffChatCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private Registry registry;

    @Inject
    private PermissionService permissionService;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private StaffChatService staffChatService;

    @Inject
    private EventService eventService;

    @Inject
    private StaffChatEvent<TString, TPlayer> staffChatEvent;

    public void execute(TCommandSource source, String[] args, Class<?> playerClass) {
        String message = String.join(" ", args);

        if (!permissionService.hasPermission(source,
            registry.getOrDefault(CatalystKeys.STAFFCHAT_PERMISSION))) {
            textService.send(pluginMessages.getNoPermission(), source);
            return;
        }

        if (!playerClass.isAssignableFrom(source.getClass())) {
            if (args.length == 0) {
                textService.send(textService.of(pluginMessages.getNotEnoughArgs()), source);
                return;
            }
            staffChatEvent.setRawMessage(message);
            staffChatEvent.setMessage(textService.of(message));
            staffChatEvent.setIsConsole(true);
            eventService.getEventBus().post(staffChatEvent);
            return;
        }
        //Don't get the UUID until after checking if the source is console.
        UUID playerUUID = userService.getUUID((TPlayer) source);

        if (args.length == 0) {
            if (staffChatService.contains(playerUUID)) {
                staffChatService.remove(playerUUID);
                textService.send(pluginMessages.getStaffChat(false), source);
            } else {
                staffChatService.add(playerUUID);
                textService.send(pluginMessages.getStaffChat(true), source);
            }
        } else {
            staffChatEvent.setRawMessage(message);
            staffChatEvent.setMessage(textService.of(message));
            staffChatEvent.setIsConsole(false);
            staffChatEvent.setPlayerUUID(userService.getUUID((TPlayer) source));
            staffChatEvent.setPlayer((TPlayer) source);
            eventService.getEventBus().post(staffChatEvent);
        }
    }
}
