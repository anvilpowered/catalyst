/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import org.anvilpowered.catalyst.api.service.EventRegistrationService
import org.anvilpowered.catalyst.api.service.EventService
import org.anvilpowered.catalyst.common.listener.ChatListener
import org.anvilpowered.catalyst.common.listener.CommandListener
import org.anvilpowered.catalyst.common.listener.DiscordChatListener
import org.anvilpowered.catalyst.common.listener.JoinListener
import org.anvilpowered.catalyst.common.listener.LeaveListener
import org.anvilpowered.anvil.api.registry.Registry

class CommonEventRegistrationService<TPlayer, TCommandSource> @Inject constructor(
    registry: Registry,
    private val eventService: EventService,
    private val chatListener: ChatListener<TPlayer, TCommandSource>,
    private val joinListener: JoinListener<TPlayer>,
    private val leaveListener: LeaveListener<TPlayer>,
    private val discordChatListener: DiscordChatListener<TPlayer>,
    private val commandListener: CommandListener
) : EventRegistrationService {

    init {
        registry.whenLoaded { registerEvents() }.register()
    }

    override fun registerEvents() {
        eventService.eventBus.register(chatListener)
        eventService.eventBus.register(joinListener)
        eventService.eventBus.register(leaveListener)
        eventService.eventBus.register(discordChatListener)
        eventService.eventBus.register(commandListener)
    }
}
