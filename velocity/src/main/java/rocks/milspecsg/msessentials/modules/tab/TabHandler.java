package rocks.milspecsg.msessentials.modules.tab;

import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.util.*;

public class TabHandler {

    private ScheduledTask task;

    public void insertIntoTab(TabList tabList, TabListEntry tabListEntry, List<UUID> onlinePlayers) {
        UUID entryUUID = tabListEntry.getProfile().getId();
        List<UUID> current = new ArrayList<>();
        Map<UUID, TabListEntry> tabListCache = new HashMap<UUID, TabListEntry>();

        for (TabListEntry currentEntry : tabList.getEntries()) {
            current.add(currentEntry.getProfile().getId());
            tabListCache.put(currentEntry.getProfile().getId(), currentEntry);
        }

        if(current.contains(entryUUID)) {
            TabListEntry currentTabListEntry = tabListCache.get(entryUUID);
            if(!currentTabListEntry.getDisplayName().equals(tabListEntry.getDisplayName())) {
                tabList.removeEntry(entryUUID);
                tabList.addEntry(tabListEntry);
                onlinePlayers.add(entryUUID);
            }
        } else {
            tabList.addEntry(tabListEntry);
            onlinePlayers.add(entryUUID);
        }
    }
}
