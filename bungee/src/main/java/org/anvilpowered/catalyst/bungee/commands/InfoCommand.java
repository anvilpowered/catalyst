/*
 *   OnTime - AnvilPowered
 *   Copyright (C) 2020 STG_Allen
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

package org.anvilpowered.catalyst.bungee.commands;

import com.google.inject.Inject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.member.MemberManager;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

public class InfoCommand extends Command {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private Registry registry;

    public InfoCommand() {
        super("info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender.hasPermission(registry.getOrDefault(CatalystKeys.INFO)))) {
            sender.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(pluginMessages.getNotEnoughArgs());
            sender.sendMessage(pluginMessages.infoCommandUsage());
        } else {
            boolean isActive = ProxyServer.getInstance().getPlayers().contains(args[0]);
            memberManager.info(args[0], isActive).thenAcceptAsync(sender::sendMessage);
        }

    }
}
