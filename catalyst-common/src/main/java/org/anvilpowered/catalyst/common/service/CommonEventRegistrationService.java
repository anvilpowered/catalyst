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

package org.anvilpowered.catalyst.common.service;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.api.service.EventRegistrationService;
import org.anvilpowered.catalyst.api.service.EventService;
import org.anvilpowered.catalyst.common.listener.ChatListener;
import org.anvilpowered.catalyst.common.listener.CommandListener;
import org.anvilpowered.catalyst.common.listener.DiscordChatListener;
import org.anvilpowered.catalyst.common.listener.JoinListener;
import org.anvilpowered.catalyst.common.listener.LeaveListener;

public class CommonEventRegistrationService<
    TString,
    TPlayer,
    TCommandSource>
    implements EventRegistrationService {

    @Inject
    private EventService eventService;

    @Inject
    private ChatListener<TString, TPlayer, TCommandSource> chatListener;

    @Inject
    private JoinListener<TString, TPlayer, TCommandSource> joinListener;

    @Inject
    private LeaveListener<TString, TPlayer, TCommandSource> leaveListener;

    @Inject
    private DiscordChatListener<TString, TPlayer, TCommandSource> discordChatListener;

    @Inject
    private CommandListener commandListener;

    @Inject
    public CommonEventRegistrationService(Registry registry) {
        registry.whenLoaded(this::registerEvents).register();
    }

    public void registerEvents() {
        eventService.getEventBus().register(chatListener);
        eventService.getEventBus().register(joinListener);
        eventService.getEventBus().register(leaveListener);
        eventService.getEventBus().register(discordChatListener);
        eventService.getEventBus().register(commandListener);
    }
}
