package essentials.modules.tab;
/*
Code used from this module was provieded by Aang23's Globaltab
can be found at : https://github.com/Aang23/GlobalTab/
*/

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.util.GameProfile;
import essentials.MSEssentials;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.UUID;

public class TimerHandler extends TimerTask {
    public static boolean stop = false;

    @Override
    public void run() {
        if (stop) {
            this.cancel();
            stop = false;
        }

        try {
            ProxyServer server = MSEssentials.server;
            if (server.getPlayerCount() > 0) {
                for (Player currentPlayerToProcess : server.getAllPlayers()) {
                    if (ConfigManager.isServerAllowed(currentPlayerToProcess.getCurrentServer())) {

                        List<UUID> toKeep = new ArrayList<UUID>();

                        for (int i2 = 0; i2 < server.getPlayerCount(); i2++) {
                            Player currentPlayer = (Player) server.getAllPlayers().toArray()[i2];

                            TabListEntry currentEntry = TabListEntry.builder().profile(currentPlayer.getGameProfile())
                                    .displayName(TabBuilder.formatPlayerTab(
                                            (String) ConfigManager.config.get("player_format"), currentPlayer))
                                    .tabList(currentPlayerToProcess.getTabList()).build();

                            GlobalTab.insertIntoTab(currentPlayerToProcess.getTabList(), currentEntry,
                                    toKeep);
                        }

                        if (ConfigManager.customTabsEnabled()) {
                            List<String> customtabs = ConfigManager.getCustomTabs();

                            for (int i3 = 0; i3 < customtabs.size(); i3++) {
                                GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + String.valueOf(i3));

                                TabListEntry currentEntry = TabListEntry.builder().profile(tabProfile)
                                        .displayName(
                                                TabBuilder.formatCustomTab(customtabs.get(i3), currentPlayerToProcess))
                                        .tabList(currentPlayerToProcess.getTabList()).build();

                                GlobalTab.insertIntoTab(currentPlayerToProcess.getTabList(), currentEntry,
                                        toKeep);
                            }
                        }

                        for (TabListEntry current : currentPlayerToProcess.getTabList().getEntries()) {
                            if (!toKeep.contains(current.getProfile().getId()))
                                currentPlayerToProcess.getTabList().removeEntry(current.getProfile().getId());
                        }

                        currentPlayerToProcess.getTabList().setHeaderAndFooter(
                                TabBuilder.formatCustomTab((String) ConfigManager.config.get("header"),
                                        currentPlayerToProcess),
                                TabBuilder.formatCustomTab((String) ConfigManager.config.get("footer"),
                                        currentPlayerToProcess));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

