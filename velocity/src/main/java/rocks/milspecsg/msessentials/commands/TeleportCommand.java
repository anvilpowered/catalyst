package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

import java.util.Optional;

public class TeleportCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.TELEPORT_FORCE)) {
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
                    return;
                }
                source.sendMessage(TextComponent.of("Sending player"));
            }
        } else {
            source.sendMessage(pluginMessages.legacyColor("Player only command!"));
        }
    }
}
