package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;

public class ServerCommand implements Command {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages pluginMessages;

    private RegisteredServer registeredServer;

    public void setRegisteredServer(String serverName) {
        this.registeredServer = proxyServer.getServer(serverName).get();
    }

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            if (player.hasPermission("msessentials.server.join." + registeredServer.getServerInfo().getName())) {
                if(registeredServer.ping().join().getVersion().getName().equals(player.getProtocolVersion().getName())) {

                    player.createConnectionRequest(registeredServer).connect().thenAcceptAsync(connection -> {
                        if (connection.isSuccessful()) {
                            player.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Connected to server " + registeredServer.getServerInfo().getName())));
                        } else {
                            if (player.getCurrentServer().get().getServerInfo().getName().equals(registeredServer.getServerInfo().getName())) {
                                player.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("You are already connected to " + registeredServer.getServerInfo().getName())));
                            } else {
                                player.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Failed to connect to " + registeredServer.getServerInfo().getName())));
                            }
                        }
                    });
                } else {
                    source.sendMessage(pluginMessages.incompatibleServerVersion);
                }
            } else {
                player.sendMessage(pluginMessages.noPermission);
            }
        }
    }
}
