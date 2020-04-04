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

package org.anvilpowered.catalyst.velocity.service;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import org.anvilpowered.catalyst.api.service.ExecuteCommandService;
import org.anvilpowered.catalyst.velocity.discord.DiscordCommandSource;

public class VelocityExecuteCommandService implements ExecuteCommandService<CommandSource> {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private DiscordCommandSource discordCommandSource;

    @Override
    public void executeCommand(CommandSource source, String command) {
        proxyServer.getCommandManager().execute(source, command);
    }

    @Override
    public void executeDiscordCommand(String command) {
        proxyServer.getCommandManager().execute(discordCommandSource, command);
    }


}
