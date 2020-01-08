package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;


public class BroadcastCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private CommandUsageMessages commandUsage;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if (!source.hasPermission(PluginPermissions.BROADCAST)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args[0].isEmpty()) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            source.sendMessage(commandUsage.broadcastCommandUsage);
            return;
        }
        proxyServer.broadcast(pluginMessages.broadcastPrefix.append(pluginMessages.legacyColor("&a" + String.join(" ", args))));
    }
}
