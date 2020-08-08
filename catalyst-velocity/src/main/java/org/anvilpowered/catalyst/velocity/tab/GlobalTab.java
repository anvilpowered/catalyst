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
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.player.TabListEntry;
import com.velocitypowered.api.util.GameProfile;
import net.kyori.text.TextComponent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.api.service.TabService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class GlobalTab {

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    private ProxyServer proxyServer;
    private Registry registry;

    @Inject
    private TabService<TextComponent> tabService;

    @Inject
    private LuckpermsService luckPermsUtils;

    @Inject
    public GlobalTab(Registry registry) {
        this.registry = registry;
        this.registry.whenLoaded(this::registryLoaded).register();
    }

    private void registryLoaded() {
        if (registry.getOrDefault(CatalystKeys.TAB_ENABLED)) {
            schedule();
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
        } else {
            TabListEntry tabListEntry = cache.get(inUUID);
            if (!tabListEntry.getDisplayName().equals(entry.getDisplayName())) {
                list.removeEntry(inUUID);
                list.addEntry(entry);
            }
        }
        toKeep.add(inUUID);
    }

    public void schedule() {
        proxyServer.getScheduler().buildTask(pluginContainer, () -> {
            if (!(proxyServer.getPlayerCount() > 0)) {
                return;
            }
            for (Player currentPlayerToProcess : proxyServer.getAllPlayers()) {
                List<UUID> toKeep = new ArrayList<>();
                for (int i2 = 0; i2 < proxyServer.getPlayerCount(); i2++) {
                    Player currentPlayer = (Player) proxyServer
                        .getAllPlayers().toArray()[i2];
                    TabListEntry currentEntry = TabListEntry.builder().
                        profile(currentPlayer.getGameProfile())
                        .displayName(tabService.formatTab(
                            registry.getOrDefault(CatalystKeys.TAB_FORMAT),
                            currentPlayer.getUsername(),
                            luckPermsUtils.getPrefix(currentPlayer),
                            luckPermsUtils.getSuffix(currentPlayer)
                            )
                        )
                        .tabList(currentPlayerToProcess.getTabList()).build();
                    insertIntoTab(currentPlayerToProcess.getTabList(), currentEntry, toKeep);
                }
                List<String> customTabs = new ArrayList<>(
                    registry.getOrDefault(CatalystKeys.TAB_FORMAT_CUSTOM));
                for (int i3 = 0; i3 < customTabs.size(); i3++) {
                    GameProfile tabProfile = GameProfile.forOfflinePlayer("customTab" + i3);
                    TabListEntry currentEntry = TabListEntry.builder().profile(tabProfile)
                        .displayName(
                            tabService.formatPlayerSpecificTab(customTabs.get(i3),
                                currentPlayerToProcess.getUsername(),
                                luckPermsUtils.getPrefix(currentPlayerToProcess),
                                luckPermsUtils.getSuffix(currentPlayerToProcess),
                                currentPlayerToProcess.getPing(),
                                proxyServer.getPlayerCount(),
                                registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)))
                        .tabList(currentPlayerToProcess.getTabList())
                        .build();
                    insertIntoTab(currentPlayerToProcess.getTabList(), currentEntry, toKeep);
                }
                for (TabListEntry current : currentPlayerToProcess.getTabList().getEntries()) {
                    if (!toKeep.contains(current.getProfile().getId()))
                        currentPlayerToProcess.getTabList().
                            removeEntry(current.getProfile().getId());
                }
                currentPlayerToProcess.getTabList().setHeaderAndFooter(
                    tabService.formatPlayerSpecificTab(
                        registry.getOrDefault(CatalystKeys.TAB_HEADER),
                        currentPlayerToProcess.getUsername(),
                        luckPermsUtils.getPrefix(currentPlayerToProcess),
                        luckPermsUtils.getSuffix(currentPlayerToProcess),
                        (int) currentPlayerToProcess.getPing(),
                        proxyServer.getPlayerCount(),
                        registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
                    ),
                    tabService.formatPlayerSpecificTab(
                        registry.getOrDefault(CatalystKeys.TAB_FOOTER),
                        currentPlayerToProcess.getUsername(),
                        luckPermsUtils.getPrefix(currentPlayerToProcess),
                        luckPermsUtils.getSuffix(currentPlayerToProcess),
                        (int) currentPlayerToProcess.getPing(),
                        proxyServer.getPlayerCount(),
                        registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED)
                    )
                );
            }
        }).repeat(10, TimeUnit.SECONDS).schedule();
    }
}
