package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.events.ProxyMessageEvent;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import java.util.UUID;

public class SocialSpyCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if(source instanceof Player) {
            Player player = (Player) source;
            if(source.hasPermission(PluginPermissions.SOCIALSPY) || source.hasPermission(PluginPermissions.SOCIALSPYONJOIN)) {
                UUID playerUUID = player.getUniqueId();
                if(ProxyMessageEvent.socialSpySet.contains(playerUUID)) {
                    ProxyMessageEvent.socialSpySet.remove(playerUUID);
                    source.sendMessage(pluginMessages.socialSpyToggle(false));
                } else {
                    ProxyMessageEvent.socialSpySet.add(playerUUID);
                    source.sendMessage(pluginMessages.socialSpyToggle(true));
                }
            }
        }
    }
}
