package essentials.modules.server;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import essentials.MSEssentials;
import essentials.modules.commands.ServerCommand;

import java.util.ArrayList;
import java.util.List;

public class MSServer {


    private static List<String> registeredCommands = new ArrayList<>();

    public static void initializeServerCommands() {

        for (RegisteredServer server : MSEssentials.getServer().getAllServers()){
            String name = server.getServerInfo().getName();
            MSEssentials.logger.info(name);
            MSEssentials.getServer().getCommandManager().register(new ServerCommand(server), name);
            registeredCommands.add(name);
        }
    }
}
