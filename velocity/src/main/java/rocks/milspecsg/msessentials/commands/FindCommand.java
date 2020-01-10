package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FindCommand implements Command {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private CommandUsageMessages commandUsage;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.FIND)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (!(args.length >= 1)) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            source.sendMessage(commandUsage.findCommandUsage);
        } else {
            Optional<Player> player = proxyServer.getPlayer(args[0]);

            if (player.isPresent()) {
                String serverName = player.get().getCurrentServer().map(ServerConnection::getServerInfo).get().getName();
                source.sendMessage(pluginMessages.currentServer(player.get().getUsername(), serverName));
            } else {
                source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Offline or invalid player.")));
            }
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
