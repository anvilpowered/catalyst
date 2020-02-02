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
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.velocity.utils.PluginPermissions;
import rocks.milspecsg.msrepository.api.plugin.PluginInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NickNameCommand implements Command {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private PluginInfo<TextComponent> pluginInfo;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private CommandUsageMessages commandUsage;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        String nick;
        if (source instanceof Player) {
            Player player = (Player) source;

            if (!source.hasPermission(PluginPermissions.NICKNAME)) {
                source.sendMessage(pluginMessages.getNoPermission());
                return;
            }

            if (args.length == 0) {
                source.sendMessage(pluginMessages.getNotEnoughArgs());
                source.sendMessage(commandUsage.nickNameCommandUsage);
                return;
            }

            if (args[0].equals("other") && source.hasPermission(PluginPermissions.NICKNAME_OTHER)) {
                nick = args[2];
                memberManager.setNickNameForUser(args[1], nick).thenAcceptAsync(source::sendMessage);
                return;
            } else {
                nick = args[0];
            }

            if (nick.contains("&") && player.hasPermission(PluginPermissions.NICKNAMECOLOR)) {
                memberManager.setNickName(player.getUsername(), args[0]).thenAcceptAsync(source::sendMessage);
            } else {
                memberManager.setNickName(player.getUsername(), pluginMessages.removeColor(nick)).thenAccept(source::sendMessage);
            }

        } else
            source.sendMessage(pluginInfo.getPrefix().append(TextComponent.of("Player only command!")));
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return proxyServer.matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
