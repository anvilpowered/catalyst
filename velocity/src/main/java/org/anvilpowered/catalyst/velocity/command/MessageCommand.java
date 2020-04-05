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

package org.anvilpowered.catalyst.velocity.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.anvilpowered.catalyst.common.command.CommonMessageCommand;
import org.anvilpowered.catalyst.velocity.plugin.CatalystVelocity;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MessageCommand extends CommonMessageCommand<
    TextComponent,
    Player,
    CommandSource,
    PermissionSubject>
    implements Command {


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        boolean isConsole = true;

        if (source instanceof Player) {
            isConsole = false;
        }

        execute(source, source, args, isConsole);
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return CatalystVelocity.getServer().matchPlayer(args[0])
                .stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}