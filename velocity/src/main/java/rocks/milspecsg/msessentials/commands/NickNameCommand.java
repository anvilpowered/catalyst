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

package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;


public class NickNameCommand implements Command {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private CommandUsageMessages commandUsage;


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source instanceof Player) {
            if (!source.hasPermission(PluginPermissions.NICKNAME)) {
                source.sendMessage(pluginMessages.noPermission);
                return;
            }

            if (args.length == 0) {
                source.sendMessage(pluginMessages.notEnoughArgs);
                source.sendMessage(commandUsage.nickNameCommandUsage);
                return;
            }

            Player player = (Player) source;
            String nick = args[0];
            if (nick.contains("&") && player.hasPermission(PluginPermissions.NICKNAMECOLOR)) {
                memberManager.setNickName(player.getUsername(), args[0]).thenAcceptAsync(source::sendMessage);
            } else {
                memberManager.setNickName(player.getUsername(), pluginMessages.removeColor(nick)).thenAccept(source::sendMessage);
            }
        } else {
            source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Player only command!")));
        }
    }
}
