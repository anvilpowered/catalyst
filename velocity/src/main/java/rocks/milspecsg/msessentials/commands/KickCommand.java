package rocks.milspecsg.msessentials.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import javax.inject.Inject;

import java.util.Optional;

public class KickCommand implements Command {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages pluginMessages;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        String kickReason = "you have been kicked!";
        if (!source.hasPermission(PluginPermissions.KICK)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }
        if (!(args.length >= 1)) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }
        if (args.length > 1) {
            kickReason = args[1];
        }

        Optional<Player> player = proxyServer.getPlayer(args[0]);
        if (player.isPresent()) {
            if (player.get().hasPermission(PluginPermissions.KICK_EXEMPT)) {
                source.sendMessage(pluginMessages.kickExempt);
                return;
            }
            player.get().disconnect(TextComponent.of(kickReason));
        } else {
            source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Offline or invalid player.")));
        }
    }
}
