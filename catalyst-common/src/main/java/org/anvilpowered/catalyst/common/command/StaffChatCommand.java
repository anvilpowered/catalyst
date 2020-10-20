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
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.event.StaffChatEvent;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.api.service.StaffChatService;

import java.util.UUID;

public class StaffChatCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private PluginMessages<TString> pluginMessages;

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

    public int execute(CommandContext<TCommandSource> context, Class<?> playerClass) {
        String message = context.getArgument("message", String.class);
        if (!playerClass.isAssignableFrom(context.getSource().getClass())) {
            staffChatEvent.setRawMessage(message);
            staffChatEvent.setMessage(textService.of(message));
            staffChatEvent.setIsConsole(true);
            eventService.getEventBus().post(staffChatEvent);
            return 1;
        }

        staffChatEvent.setRawMessage(message);
        staffChatEvent.setMessage(textService.of(message));
        staffChatEvent.setIsConsole(false);
        staffChatEvent.setPlayerUUID(userService.getUUID((TPlayer) context.getSource()));
        staffChatEvent.setPlayer((TPlayer) context.getSource());
        eventService.getEventBus().post(staffChatEvent);
        return 1;
    }

    public int toggle(CommandContext<TCommandSource> context, Class<?> playerClass) {
        if (!playerClass.isAssignableFrom(context.getSource().getClass())) {
            textService.send(pluginMessages.getNotEnoughArgs(), context.getSource());
            return 1;
        }

        UUID playerUUID = userService.getUUID((TPlayer) context.getSource());
        if (staffChatService.contains(playerUUID)) {
            staffChatService.remove(playerUUID);
            textService.send(pluginMessages.getStaffChat(false), context.getSource());
        } else {
            staffChatService.add(playerUUID);
            staffChatService.add(userService.getUUID((TPlayer) context.getSource()));
            textService.send(pluginMessages.getStaffChat(true), context.getSource());
        }
        return 1;
    }
}
