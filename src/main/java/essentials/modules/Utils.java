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

    public static TextComponent getOnlinePlayerInfo(Player p)
    {
        String username = p.getUsername();
        String currentServer = p.getCurrentServer().get().getServerInfo().getName();
        boolean banned = PlayerConfig.checkBan(username);
        String ip = PlayerConfig.getIP(username);
        String joined = PlayerConfig.getJoined(username);
       // String seen = PlayerConfig.getLastSeen(username);

        TextComponent finalComponent = TextComponent.builder()
            .append(PluginMessages.legacyColor("&b----------------Player Info----------------"))
            .append("\nCurrent Server : " + currentServer )
            .append("\nBanned : " + banned)
            .append("\nIP : " + ip)
            .append("\nJoined Date : " + joined)
            .build();

        return finalComponent;
    }

    public static TextComponent getOfflinePlayerInfo(String username)
    {
        boolean banned = PlayerConfig.checkBan(username);
        String ip = PlayerConfig.getIP(username);
        String joined = PlayerConfig.getJoined(username);
        String seen = PlayerConfig.getLastSeen(username);

        TextComponent finalComponent = TextComponent.builder()
                .append(PluginMessages.legacyColor("&b----------------Player Info----------------"))
                .append("\nBanned : " + banned)
                .append("\nIP : " + ip)
                .append("\nJoined Date : " + joined)
                .append("\nLast Seen : " + seen)
                .build();

        return finalComponent;
    }
}
