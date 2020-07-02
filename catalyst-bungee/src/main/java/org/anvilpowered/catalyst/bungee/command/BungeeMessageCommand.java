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

package org.anvilpowered.catalyst.bungee.command;

import com.google.inject.Inject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.anvilpowered.catalyst.common.command.CommonMessageCommand;
import org.bukkit.command.ConsoleCommandSender;

public class BungeeMessageCommand extends Command {

    @Inject
    private CommonMessageCommand<
        TextComponent,
        ProxiedPlayer,
        CommandSender> messageCommand;

    public BungeeMessageCommand() {
        super("message", "",
            "msg", "tell", "whisper", "w", "m", "t");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        messageCommand.execute(sender, args, ConsoleCommandSender.class);
    }
}
