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

    /*@Inject
    private UnMuteCommand unMuteCommand;*/

    @Inject
    private SocialSpyCommand socialSpyCommand;

    @Inject
    private StaffListCommand staffListCommand;

    @Inject
    private StaffChatCommand staffChatCommand;

    @Override
    public void register(Object plugin) {
        proxyServer.getCommandManager().register("broadcast", broadcastCommand);
        proxyServer.getCommandManager().register("info", infoCommand, "msinfo");
        proxyServer.getCommandManager().register("nick", nicknameCommand);
        proxyServer.getCommandManager().register("ban", banCommand);
        proxyServer.getCommandManager().register("unban", unBanCommand);
        proxyServer.getCommandManager().register("kick", kickCommand);
        proxyServer.getCommandManager().register("find", findCommand);
        proxyServer.getCommandManager().register("send", sendCommand);
        proxyServer.getCommandManager().register("msg", messageCommand, "w", "tell", "whisper", "m", "t");
        proxyServer.getCommandManager().register("reply", replyCommand, "r");
        proxyServer.getCommandManager().register("mute", muteCommand);
       // proxyServer.getCommandManager().register("unmute", unMuteCommand);
        proxyServer.getCommandManager().register("socialspy", socialSpyCommand, "ss");
        proxyServer.getCommandManager().register("stafflist", staffListCommand);
        proxyServer.getCommandManager().register("staffchat", staffChatCommand, "sc");

    }
}
