package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BroadcastCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if(!source.hasPermission(PluginPermissions.BROADCAST)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if(args[0].isEmpty()) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }
        proxyServer.broadcast(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of(Stream.of(args).collect(Collectors.joining(" ")))));
    }
}
