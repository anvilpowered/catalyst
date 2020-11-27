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
import net.kyori.adventure.text.TextComponent;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.catalyst.api.registry.CatalystKeys;
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

    final private Registry registry;

    @Inject
    private TabService<TextComponent, Player> tabService;

    @Inject
    private LuckpermsService luckpermsService;

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

    public void insertIntoTab(TabList list, TabListEntry entry) {
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
            if (!tabListEntry.getDisplayNameComponent().equals(entry.getDisplayNameComponent())) {
                list.removeEntry(inUUID);
                list.addEntry(entry);
            }
        }
    }

    public void schedule() {
        proxyServer.getScheduler().buildTask(pluginContainer, () -> {
            final int playerCount = proxyServer.getPlayerCount();
            if (playerCount == 0) {
                return;
            }
            List<String> groupOrder = registry.getOrDefault(CatalystKeys.TAB_GROUP_ORDER);
            for (Player currentPlayerToProcess : proxyServer.getAllPlayers()) {
                for (Player process : proxyServer.getAllPlayers()) {
                    String tempName = process.getUsername();
                    if (registry.getOrDefault(CatalystKeys.TAB_ORDER).equalsIgnoreCase("group")) {
                        for (String group : groupOrder) {
                            if (luckpermsService.getGroupName(process).equalsIgnoreCase(group)) {
                                    tempName = String.valueOf(groupOrder.indexOf(group));
                            }
                        }
                    }
                    TabListEntry currentEntry = TabListEntry.builder().
                        profile(new GameProfile(process.getUniqueId(), tempName, process.getGameProfileProperties()))
                        .displayName(tabService.format(process, (int) process.getPing(), playerCount))
                        .tabList(currentPlayerToProcess.getTabList())
                        .build();
                    insertIntoTab(currentPlayerToProcess.getTabList(), currentEntry);
                }
                int x = 0;
                for (String custom : registry.getOrDefault(CatalystKeys.TAB_FORMAT_CUSTOM)) {
                    x++;
                    GameProfile tabProfile = GameProfile.forOfflinePlayer("catalyst" + x);
                    TabListEntry currentEntry = TabListEntry.builder()
                        .profile(tabProfile)
                        .displayName(
                            tabService.formatCustom(
                                custom,
                                currentPlayerToProcess,
                                (int) currentPlayerToProcess.getPing(),
                                playerCount))
                        .tabList(currentPlayerToProcess.getTabList())
                        .build();
                    insertIntoTab(currentPlayerToProcess.getTabList(), currentEntry);
                }
                currentPlayerToProcess.getTabList().setHeaderAndFooter(
                    tabService.formatHeader(
                        currentPlayerToProcess,
                        (int) currentPlayerToProcess.getPing(),
                        playerCount
                    ),
                    tabService.formatFooter(
                        currentPlayerToProcess,
                        (int) currentPlayerToProcess.getPing(),
                        playerCount
                    ));
            }
        }).repeat(10, TimeUnit.SECONDS).schedule();
    }
}
