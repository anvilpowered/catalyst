/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.catalyst.core.chat

import com.google.common.eventbus.Subscribe
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.event.CommandEvent

context(LoggerScope, Registry.Scope)
class CommandListener {

    @Subscribe
    fun onCommandExecution(event: CommandEvent) {
        if (registry[CatalystKeys.COMMAND_LOGGING_ENABLED]) {
            val commandList = registry[CatalystKeys.COMMAND_LOGGING_FILTER]
            if (commandList.size == 1 && commandList[0] == "*" || commandList.contains(event.command)) {
                logger.info(event.sourceName + " executed command : " + event.command)
            }
        }
    }
}
