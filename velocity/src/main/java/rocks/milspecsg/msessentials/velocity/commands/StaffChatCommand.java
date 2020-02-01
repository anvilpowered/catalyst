/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.velocity.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.events.ProxyStaffChatEvent;
import rocks.milspecsg.msessentials.velocity.utils.PluginPermissions;

public class StaffChatCommand implements Command {

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.STAFFCHAT)) {
            source.sendMessage(pluginMessages.getNoPermission());
        }

        if (source instanceof Player) {
            Player player = (Player) source;
            if (args.length == 0) {
                if (ProxyStaffChatEvent.staffChatSet.contains(player.getUniqueId())) {
                    ProxyStaffChatEvent.staffChatSet.remove(player.getUniqueId());
                    player.sendMessage(pluginMessages.getStaffChat(false));
                } else {
                    ProxyStaffChatEvent.staffChatSet.add(player.getUniqueId());
                    source.sendMessage(pluginMessages.getStaffChat(true));
                }
            } else {
                ProxyStaffChatEvent.staffChatSet.add(player.getUniqueId());
                String message = String.join(" ", args);
                ProxyStaffChatEvent proxyStaffChatEvent = new ProxyStaffChatEvent(player, message, TextComponent.of(message));
                proxyServer.getEventManager().fire(proxyStaffChatEvent).join();
            }
        } else {
            if (args.length == 0) {
                source.sendMessage(pluginMessages.getNotEnoughArgs());
            } else {
                String message = String.join(" ", args);
                proxyServer.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                    .forEach(target ->
                        target.sendMessage(pluginMessages.getStaffChatMessageFormattedConsole(TextComponent.of(message)))
                    );
            }
        }
    }
}
