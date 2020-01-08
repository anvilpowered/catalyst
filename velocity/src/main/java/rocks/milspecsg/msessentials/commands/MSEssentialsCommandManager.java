package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msrepository.api.config.ConfigurationService;

@Singleton
public class MSEssentialsCommandManager implements CommandManager {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private ConfigurationService configService;

    @Inject
    private BroadcastCommand broadcastCommand;

    @Inject
    private InfoCommand infoCommand;

    @Inject
    private NickNameCommand nicknameCommand;

    @Inject
    private BanCommand banCommand;

    @Inject
    private UnBanCommand unBanCommand;

    @Inject
    private KickCommand kickCommand;

    @Inject
    private FindCommand findCommand;

    @Inject
    private SendCommand sendCommand;

    @Inject
    private MessageCommand messageCommand;

    @Inject
    private ReplyCommand replyCommand;

    @Inject
    private MuteCommand muteCommand;

    @Inject
    private UnMuteCommand unMuteCommand;

    @Inject
    private SocialSpyCommand socialSpyCommand;

    @Inject
    private StaffListCommand staffListCommand;

    @Inject
    private StaffChatCommand staffChatCommand;

    @Inject
    private ListCommand listCommand;

    @Inject
    private DeleteNicknameCommand deleteNicknameCommand;

    @Inject
    private TeleportCommand teleportCommand;

    @Inject
    private TeleportRequestCommand teleportRequestCommand;

    @Inject
    private TeleportAcceptCommand teleportAcceptCommand;

    @Inject
    Provider<ServerCommand> serverCommandProvider;

    @Inject
    private SwearListCommand swearListCommand;

    @Inject
    private SwearAddCommand swearAddCommand;

    @Inject
    private SwearRemoveCommand swearRemoveCommand;

    @Override
    public void register(Object plugin) {
        proxyServer.getCommandManager().register("ban", banCommand, "msban");
        proxyServer.getCommandManager().register("broadcast", broadcastCommand);
        proxyServer.getCommandManager().register("delnick", deleteNicknameCommand, "deletenick");
        proxyServer.getCommandManager().register("find", findCommand, "msfind");
        proxyServer.getCommandManager().register("list", listCommand, "mslist");
        proxyServer.getCommandManager().register("info", infoCommand, "msinfo");
        proxyServer.getCommandManager().register("kick", kickCommand, "mskick");
        proxyServer.getCommandManager().register("mute", muteCommand, "msmute");
        proxyServer.getCommandManager().register("msg", messageCommand, "w", "tell", "whisper", "m", "t");
        proxyServer.getCommandManager().register("nick", nicknameCommand, "msnick");
        proxyServer.getCommandManager().register("reply", replyCommand, "r");
        proxyServer.getCommandManager().register("send", sendCommand, "mssend");
        proxyServer.getCommandManager().register("socialspy", socialSpyCommand, "ss");
        proxyServer.getCommandManager().register("stafflist", staffListCommand);
        proxyServer.getCommandManager().register("staffchat", staffChatCommand, "sc");
        //proxyServer.getCommandManager().register("tp", teleportCommand, "mstp");
        // proxyServer.getCommandManager().register("tpaccept", teleportAcceptCommand);
        // proxyServer.getCommandManager().register("tpa", teleportRequestCommand, "tprequest");
        proxyServer.getCommandManager().register("unban", unBanCommand, "msunban", "pardon", "mspardon");
        proxyServer.getCommandManager().register("unmute", unMuteCommand, "msunmute");
        if (configService.getConfigBoolean(ConfigKeys.SERVER_COMMAND_ENABLED)) {
            for (RegisteredServer server : proxyServer.getAllServers()) {
                String serverName = server.getServerInfo().getName();
                ServerCommand command = serverCommandProvider.get();
                command.setRegisteredServer(serverName);
                proxyServer.getCommandManager().register(serverName, command);
            }
        }
        proxyServer.getCommandManager().register("swearlist", swearListCommand);
        proxyServer.getCommandManager().register("swearadd", swearAddCommand);
        proxyServer.getCommandManager().register("swearremove", swearRemoveCommand);

    }
}
