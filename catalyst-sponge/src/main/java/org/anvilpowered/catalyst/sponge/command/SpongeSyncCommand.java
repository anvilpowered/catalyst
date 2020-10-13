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

package org.anvilpowered.catalyst.sponge.command;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.messaging.RedisService;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class SpongeSyncCommand implements CommandExecutor {

    @Inject
    private RedisService redisService;

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) {
        String serverName = context.requireOne("server");
        String command = context.requireOne("command");
        redisService.getJedisPool().getResource().publish(serverName, command);
        return CommandResult.success();
    }
}
