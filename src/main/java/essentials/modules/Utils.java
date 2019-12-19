package essentials.modules;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import net.kyori.text.TextComponent;

import java.util.concurrent.TimeUnit;

public class Utils {
    static String newMSG;
    static ScheduledTask task;

    public static  String removeColorCodes(String message)
    {

        newMSG = message
                .replace("&0", "").replace("&1", "").replace("&2", "")
                .replace("&3", "").replace("&4", "").replace("&5", "")
                .replace("&6", "").replace("&7", "").replace("&8", "")
                .replace("&9", "")

                .replace("&a", "").replace("&b", "").replace("&c", "")
                .replace("&d", "").replace("&e", "").replace("&f", "");

        return newMSG;
    }

    public static String removeStyleCodes(String message)
    {
        newMSG = message
                .replace("&k", "").replace("&l", "").replace("&m", "")
                .replace("&n", "").replace("&o", "").replace("&r", "");

        return newMSG;
    }

    public static String removeMagicCode(String message)
    {
        newMSG = message.replace("&k", "");
        return newMSG;
    }

    public static String getCurrentServer(Player player)
    {
        if(player.getCurrentServer().isPresent())
        {
            return player.getCurrentServer().get().getServerInfo().getName();
        }else
            return "null";
    }

    public static String getServerPlayerCount(Player player)
    {
        RegisteredServer server = null;
        if(player.getCurrentServer().isPresent())
        {
            return String.valueOf(player.getCurrentServer().get().getServer().getPlayersConnected().size());
        }return "null";
    }

    public static void muteTask(String name, int time)
    {
        MSEssentials.logger.info("Started");
        task = MSEssentials.getServer().getScheduler().buildTask(MSEssentials.instance, () ->{
            PlayerConfig.removeMute(name);
            MSEssentials.logger.info("Finished.");
        }).delay(time, TimeUnit.MINUTES).schedule();
    }

    public static TextComponent getOnlinePlayerInfo(Player p) {
        String username = p.getUsername();
        String currentServer = p.getCurrentServer().map(server -> server.getServerInfo().getName()).orElse("n/a");
        System.out.println(username + currentServer);
        return printInfo(username, currentServer);
    }

    public static TextComponent getOfflinePlayerInfo(String username) {
        if(PlayerConfig.getLastSeen(username) == null)
        {
            return TextComponent.of("Unknown player!");
        }
        return printInfo(username, "n/a");
    }

    private static TextComponent printInfo(String username, String currentServer) {
        boolean banned = PlayerConfig.checkBan(username);
        String ip = PlayerConfig.getIP(username);
        String joined = PlayerConfig.getJoined(username);
        String seen = PlayerConfig.getLastSeen(username);
        String nickname = "No Nickname";
        String uuid = PlayerConfig.getPlayerUUID(username).toString();
        if (PlayerConfig.hasNickName(username))
            nickname = PlayerConfig.getNickName(username);

        return TextComponent.builder()
                .append(PluginMessages.legacyColor("&b----------------Player Info----------------"))
                .append(PluginMessages.legacyColor(("\n&bName : &r")))
                .append(username)
                .append(uuid)
                .append(PluginMessages.legacyColor("\n&bNickname : &r"))
                .append(PluginMessages.legacyColor(nickname))
                .append(PluginMessages.legacyColor("\n&bCurrent Server : &r"))
                .append(currentServer)
                .append(PluginMessages.legacyColor("\n&bBanned : &r"))
                .append(String.valueOf(banned))
                .append(PluginMessages.legacyColor("\n&bIP : &r"))
                .append(ip)
                .append(PluginMessages.legacyColor("\n&bJoined Date : &r" ))
                .append(joined)
                .append(PluginMessages.legacyColor("\n&bLast Seen : &r"))
                .append(seen)
                .build();
    }
}