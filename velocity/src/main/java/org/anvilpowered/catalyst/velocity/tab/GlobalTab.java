/*
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

package org.anvilpowered.catalyst.velocity.tab;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.util.GameProfile;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class GlobalTab {

    @Inject
    private ProxyServer proxyServer;

    private Registry registry;

    @Inject
    private TabBuilder tabBuilder;

    @Inject
    Plugin<?> catalyst;

    @Inject
    public GlobalTab(Registry registry) {
        this.registry = registry;
        this.registry.addRegistryLoadedListener(this::registryLoaded);
    }

    private void registryLoaded() {
        try {
            schedule();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void insertIntoTab(TabList list, TabListEntry entry, List<UUID> toKeep) {

        UUID inUUID = entry.getProfile().getId();
        List<UUID> contained = new ArrayList<>();
        Map<UUID, TabListEntry> cache = new HashMap<>();


        for (TabListEntry e : list.getEntries()) {
            contained.add(e.getProfile().getId());
            cache.put(e.getProfile().getId(), e);
        }

        if (!contained.contains(inUUID)) {
            list.addEntry(entry);
            toKeep.add(inUUID);
        } else {
            TabListEntry tabListEntry = cache.get(inUUID);
            if (!tabListEntry.getDisplayName().equals(entry.getDisplayName())) {
                list.removeEntry(inUUID);
                list.addEntry(entry);
            }
            toKeep.add(inUUID);
        }
    }

    public void schedule() {
        proxyServer.getScheduler().buildTask(catalyst, () -> {
            try {
                if (proxyServer.getPlayerCount() > 0) {
                    for (Player currentPlayerToProcess : proxyServer.getAllPlayers()) {

                        List<UUID> toKeep = new ArrayList<>();

                        for (int i2 = 0; i2 < proxyServer.getPlayerCount(); i2++) {
                            Player currentPlayer = (Player) proxyServer.getAllPlayers().toArray()[i2];

                            TabListEntry currentEntry = TabListEntry.builder().profile(currentPlayer.getGameProfile())
                                .displayName(tabBuilder.formatPlayerTab(
                                    registry.getOrDefault(CatalystKeys.TAB_FORMAT), currentPlayer))
                                .tabList(currentPlayerToProcess.getTabList()).build();

                            insertIntoTab(currentPlayerToProcess.getTabList(), currentEntry,
                                toKeep);
                        }

                        if (registry.getOrDefault(CatalystKeys.TAB_ENABLED)) {
                            List<String> customtabs = new ArrayList<>(registry.getOrDefault(CatalystKeys.TAB_FORMAT_CUSTOM));

                            for (int i3 = 0; i3 < customtabs.size(); i3++) {
                                GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + i3);

                                TabListEntry currentEntry = TabListEntry.builder().profile(tabProfile)
                                    .displayName(
                                        tabBuilder.formatTab(customtabs.get(i3), currentPlayerToProcess))
                                    .tabList(currentPlayerToProcess.getTabList()).build();

                                insertIntoTab(currentPlayerToProcess.getTabList(), currentEntry,
                                    toKeep);
                            }
                        }

                        for (TabListEntry current : currentPlayerToProcess.getTabList().getEntries()) {
                            if (!toKeep.contains(current.getProfile().getId()))
                                currentPlayerToProcess.getTabList().removeEntry(current.getProfile().getId());
                        }

                        currentPlayerToProcess.getTabList().setHeaderAndFooter(
                            tabBuilder.formatTab(registry.getOrDefault(CatalystKeys.TAB_HEADER),
                                currentPlayerToProcess),
                            tabBuilder.formatTab(registry.getOrDefault(CatalystKeys.TAB_FOOTER),
                                currentPlayerToProcess));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).repeat(10, TimeUnit.SECONDS).schedule();
    }
}
