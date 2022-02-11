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
package org.anvilpowered.catalyst.common.listener

import com.google.common.eventbus.Subscribe
import com.google.inject.Inject
import org.anvilpowered.catalyst.api.event.CommandEvent
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.registry.Registry
import org.slf4j.Logger

class CommandListener @Inject constructor(
    private val logger: Logger,
    private val registry: Registry
) {
    @Subscribe
    fun onCommandExecution(event: CommandEvent) {
        if (registry.getOrDefault(CatalystKeys.COMMAND_LOGGING_ENABLED)) {
            val commandList = registry.getOrDefault(CatalystKeys.COMMAND_LOGGING_FILTER)
            if (commandList.size == 1 && commandList[0] == "*" || commandList.contains(event.command)) {
                logger.info(event.sourceName + " executed command : " + event.command)
            }
        }
    }
}
