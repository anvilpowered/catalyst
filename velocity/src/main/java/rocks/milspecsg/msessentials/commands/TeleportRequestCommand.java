package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.events.ProxyTeleportRequestEvent;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;
import rocks.milspecsg.msessentials.modules.utils.ProxyTeleportUtils;

import java.util.Optional;

public class TeleportRequestCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private ProxyTeleportUtils proxyTeleportUtils;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.TELEPORT_REQUEST)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length < 1) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }

        if (source instanceof Player) {
            Player sourcePlayer = (Player) source;
            Optional<Player> targetPlayer = proxyServer.getPlayer(args[0]);
            if (targetPlayer.isPresent()) {
                if (sourcePlayer.equals(targetPlayer.get())) {
                    sourcePlayer.sendMessage(pluginMessages.teleportToSelf());
                }

                proxyTeleportUtils.teleportationMap.put(sourcePlayer.getUniqueId(), targetPlayer.get().getUniqueId());
                ProxyTeleportRequestEvent proxyTeleportRequestEvent = new ProxyTeleportRequestEvent(sourcePlayer, targetPlayer.get());
                proxyServer.getEventManager().fireAndForget(proxyTeleportRequestEvent);
            }
        }
    }
}
