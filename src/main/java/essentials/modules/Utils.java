package essentials.modules;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class Utils {
    static String newMSG;

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
}
