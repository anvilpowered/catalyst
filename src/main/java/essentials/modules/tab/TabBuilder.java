package essentials.modules.tab;

/*
Code used from this module was provieded by Aang23's Globaltab
can be found at : https://github.com/Aang23/GlobalTab/
*/

import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import net.kyori.text.TextComponent;

import static essentials.modules.Utils.getCurrentServer;
import static essentials.modules.Utils.getServerPlayerCount;

public class TabBuilder {
    public static TextComponent formatPlayerTab(String raw, Player player)
    {
        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", UserInfoGetter.getPrefixFromUsername(player.getUsername()));
        raw = raw.replace("%suffix%", UserInfoGetter.getSuffixFromUsername(player.getUsername()));
        raw = raw.replace("%server%", getCurrentServer(player));

        return PluginMessages.legacyColor(raw);
    }

    public static TextComponent formatCustomTab(String raw, Player player)
    {
        raw = raw.replace("%username%", player.getUsername());
        raw = raw.replace("%prefix%", UserInfoGetter.getPrefixFromUsername(player.getUsername()));
        raw = raw.replace("%suffix%", UserInfoGetter.getSuffixFromUsername(player.getUsername()));
        raw = raw.replace("%server%", getCurrentServer(player));
        raw = raw.replace("%ping%", String.valueOf(player.getPing()));
        raw = raw.replace("%playercount%", String.valueOf(MSEssentials.server.getPlayerCount()));
        raw = raw.replace("%localplayercount%", getServerPlayerCount(player));
        raw = raw.replace("%totalmaxplayer%", String.valueOf(MSEssentials.server.getConfiguration().getShowMaxPlayers()));
        raw = raw.replace("%motd%", MSEssentials.server.getConfiguration().getMotdComponent().toString());
        raw = raw.replace("%uuid%", player.getUniqueId().toString());
        raw = raw.replace("%ip%", player.getRemoteAddress().toString());
        //raw = raw.replace("%balance%", getBalance(player));

        return PluginMessages.legacyColor(raw);

    }

}
