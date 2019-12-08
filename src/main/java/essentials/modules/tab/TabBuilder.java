package essentials.modules.tab;

/*
Code used from this module was provieded by Aang23's Globaltab
can be found at : https://github.com/Aang23/GlobalTab/
*/

import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.LuckpermsHook;
import essentials.modules.PluginMessages;
import net.kyori.text.TextComponent;

import static essentials.modules.Utils.getCurrentServer;
import static essentials.modules.Utils.getServerPlayerCount;

public class TabBuilder {
    public static TextComponent formatPlayerTab(String raw, Player player) {
        raw = raw.replace("%username%", player.getUsername());

        if (LuckpermsHook.getPrefix(player) == null) {
            raw = raw.replace("%prefix%", " ");
        } else {
            raw = raw.replace("%prefix%", LuckpermsHook.getPrefix(player));
        }

        if (LuckpermsHook.getSuffix(player) == null) {
            raw = raw.replace("%suffix%", "");
        } else {
            raw = raw.replace("%suffix%", LuckpermsHook.getSuffix(player));
        }

        raw = raw.replace("%server%", getCurrentServer(player));

        return PluginMessages.legacyColor(raw);
    }

    public static TextComponent formatCustomTab(String raw, Player player) {
        raw = raw.replace("%username%", player.getUsername());
        if (LuckpermsHook.getPrefix(player) == null) {
            raw = raw.replace("%prefix%", " ");
        } else {
            raw = raw.replace("%prefix%", LuckpermsHook.getPrefix(player));
        }

        if (LuckpermsHook.getSuffix(player) == null) {
            raw = raw.replace("%suffix%", "");
        } else {
            raw = raw.replace("%suffix%", LuckpermsHook.getSuffix(player));
        }

        raw = raw.replace("%server%", getCurrentServer(player));
        raw = raw.replace("%server%", getCurrentServer(player));
        raw = raw.replace("%ping%", String.valueOf(player.getPing()));
        raw = raw.replace("%playercount%", String.valueOf(MSEssentials.server.getPlayerCount()));
        raw = raw.replace("%localplayercount%", getServerPlayerCount(player));
        raw = raw.replace("%totalmaxplayer%", String.valueOf(MSEssentials.server.getConfiguration().getShowMaxPlayers()));
        raw = raw.replace("%motd%", MSEssentials.server.getConfiguration().getMotdComponent().toString());
        raw = raw.replace("%uuid%", player.getUniqueId().toString());
        raw = raw.replace("%ip%", player.getRemoteAddress().toString());
        raw = raw.replace("%balance%", getBalance(player));

        return PluginMessages.legacyColor(raw);

    }

    private static String getBalance(Player player) {
        if (MSEssentials.playerBalances.containsKey(player.getUsername())) {
            return String.valueOf(MSEssentials.playerBalances.get(player.getUsername()));
        } else
            return "null";
    }


}
