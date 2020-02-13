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

public class BanCommand extends Command {

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private Registry registry;

    @Inject
    private ProxyServer proxyServer;


    @Inject
    public BanCommand(Registry registry) {
        super("ban", registry.getOrDefault(CatalystKeys.BAN), "cban");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(registry.getOrDefault(CatalystKeys.BAN))) {
            sender.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(pluginMessages.getNotEnoughArgs());
            sender.sendMessage(pluginMessages.banCommandUsage());
            return;
        }

        String userName = args[0];

        if(proxyServer.getPlayer(userName).hasPermission(registry.getOrDefault(CatalystKeys.BAN_EXEMPT))) {
            sender.sendMessage(pluginMessages.getBanExempt());
            return;
        }

        if(args.length == 1) {
            memberManager.ban(userName).thenAcceptAsync(sender::sendMessage);
        } else {
            String reason = String.join(" ", args).replace(userName, "");
            memberManager.ban(userName, reason).thenAcceptAsync(sender::sendMessage);
        }
    }
}
