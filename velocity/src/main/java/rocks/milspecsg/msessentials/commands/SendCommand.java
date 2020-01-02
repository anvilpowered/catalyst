package rocks.milspecsg.msessentials.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import javax.inject.Inject;
import java.util.Optional;

public class SendCommand implements Command {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages pluginMessages;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.SEND)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length == 0 || !(args.length >= 2)) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }

        if (proxyServer.getPlayer(args[0]).isPresent()) {
            Optional<Player> player = proxyServer.getPlayer(args[0]);
            Optional<RegisteredServer> server = proxyServer.getServer(args[1]);
            if (server.isPresent()) {
                if (player.get().getCurrentServer().get().equals(server)) {
                    Player sender = (Player) source;
                    source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("sending " + player.get().getUsername() + " to " + server.get().getServerInfo().getName())));
                    player.get().sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("you have been sent to " + server.get().getServerInfo().getName() + " by " + sender.getUsername())));
                    player.get().createConnectionRequest(server.get()).fireAndForget();
                } else {
                    source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of(player.get().getUsername() + " is already connected to that server.")));
                }
            } else {
                source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Invalid or offline server.")));
            }
        } else {
            source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Offline or invalid player.")));
        }
    }
}
