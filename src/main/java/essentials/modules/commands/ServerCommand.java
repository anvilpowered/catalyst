package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class ServerCommand implements Command {
    /**
     * Executes the command for the specified {@link CommandSource}.
     *
     * @param source the source of this command
     * @param args   the arguments for this command
     */
    private RegisteredServer server;

    public ServerCommand(RegisteredServer registeredServer)
    {
        server = registeredServer;
    }


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (source instanceof Player) {
            Player player = (Player) source;
            Optional<ServerConnection> connection = player.getCurrentServer();
            if (!player.hasPermission("msessentials.server.join." + server.getServerInfo().getName())) {
                source.sendMessage(TextComponent.of("[MSEssentials] ").append(TextComponent.of("You do not have permission for this server!")));
                return;
            }
            else
            if(player.getCurrentServer().filter(s -> s.getServerInfo().getName().equals(server.getServerInfo().getName())).isPresent()){
                source.sendMessage(TextComponent.of("[MSEssentials] ")
                        .append(TextComponent.of("You are already connected to this server!").color(TextColor.YELLOW)));
                return;
            } else {
                if (connection.isPresent() && connection.get().getServer() == server) {
                    source.sendMessage(TextComponent.of("[MSEssentials] ")
                            .append(TextComponent.of("Connection failed! Please try again in a few moments!").color(TextColor.RED)));                return;
                } else {
                    // player.createConnectionRequest(server).fireAndForget();
                    source.sendMessage(TextComponent.of("[MSEssentials] ")
                            .append(TextComponent.of("Connection failed! Please try again in a few moments!")).color(TextColor.RED));            }
            }
        } else {
            source.sendMessage(TextComponent.of(
                    "This is a player only command!"));
        }
    }
}
