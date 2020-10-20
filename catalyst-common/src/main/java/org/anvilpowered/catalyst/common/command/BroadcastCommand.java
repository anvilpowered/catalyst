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
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.BroadcastService;


public class BroadcastCommand<
    TString,
    TCommandSource> {

    @Inject
    private BroadcastService<TString> broadcastService;

    @Inject
    private PluginMessages<TString> pluginMessages;

    public int execute(CommandContext<TCommandSource> context) {
        broadcastService.broadcast(pluginMessages.getBroadcast(context.getArgument(
            "message", String.class)));
        return 1;
    }
}
