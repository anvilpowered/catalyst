/*
 *     MSEssentials - MilSpecSG
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
