package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.ProxyServer;

@Singleton
public class MSEssentialsCommandManager implements CommandManager {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private BroadcastCommand broadcastCommand;

    @Inject
    private InfoCommand infoCommand;
    @Inject
    private NickNameCommand nicknameCommand;

    @Override
    public void register(Object plugin) {
        proxyServer.getCommandManager().register(broadcastCommand, "broadcast");
        proxyServer.getCommandManager().register(infoCommand, "info");
        proxyServer.getCommandManager().register(nicknameCommand, "nick");
    }
}
