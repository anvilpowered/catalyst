/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.common.listener;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.event.CommandEvent;
import org.anvilpowered.catalyst.api.listener.CommandListener;
import org.slf4j.Logger;

import java.util.List;

public class CommonCommandListener implements CommandListener {

    @Inject
    private Logger logger;

    @Inject
    private Registry registry;

    @Override
    public void onCommandExecution(CommandEvent event) {
        if (registry.getOrDefault(CatalystKeys.COMMAND_LOGGING_ENABLED)) {
            List<String> commandList = registry.getOrDefault(CatalystKeys.COMMAND_LOGGING_FILTER);
            if ((commandList.size() == 1 && commandList.get(0).equals("*"))
                || commandList.contains(event.getCommand())) {
                logger.info(event.getSourceName() + " executed command : " + event.getCommand());
            }
        }
    }
}
