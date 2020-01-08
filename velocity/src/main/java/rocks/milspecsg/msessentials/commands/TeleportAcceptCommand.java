package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.entity.living.player.Player;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;
import rocks.milspecsg.msessentials.modules.utils.ProxyTeleportUtils;

public class TeleportAcceptCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyTeleportUtils proxyTeleportUtils;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.TELEPORT_REQUEST)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if(source instanceof Player) {
            Player player = (Player) source;
            if(proxyTeleportUtils.teleportationMap.containsKey(player.getUniqueId())) {
                //TODO Implement teleport accepting
                proxyTeleportUtils.teleportationMap.remove(player.getUniqueId());


            }

        }
    }
}
