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
import rocks.milspecsg.anvil.api.data.registry.Registry;
import rocks.milspecsg.msessentials.api.data.key.MSEssentialsKeys;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.velocity.messages.CommandUsageMessages;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MuteCommand implements Command {

    @Inject
    private CommandUsageMessages commandUsage;

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private Registry registry;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(registry.getOrDefault(MSEssentialsKeys.MUTE))) {
            source.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        if (args.length == 0) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            source.sendMessage(commandUsage.muteCommandUsage);
            return;
        }
        String username = args[0];

        if (proxyServer.getPlayer(username).filter(p -> p.hasPermission(registry.getOrDefault(MSEssentialsKeys.MUTE_EXEMPT))).isPresent()) {
            source.sendMessage(pluginMessages.getMuteExempt());
            return;
        }
        if (args.length == 1) {
            memberManager.mute(username).thenAcceptAsync(source::sendMessage);
        } else {
            String reason = String.join(" ", args).replace(username + " ", "");
            memberManager.mute(username, reason).thenAcceptAsync(source::sendMessage);
        }
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return proxyServer.matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
