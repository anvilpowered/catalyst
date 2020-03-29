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

package org.anvilpowered.catalyst.velocity.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.Plugin;
import org.anvilpowered.catalyst.velocity.plugin.Catalyst;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class LuckPermsUtils {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    Plugin<?> catalyst;

    private Registry registry;

    private static Map<Player, CachedMetaData> cachedPlayers = new HashMap<>();

    @Inject
    public LuckPermsUtils(Registry registry) {
        this.registry = registry;
        this.registry.addRegistryLoadedListener(this::syncPlayerCache);
    }


    public void syncPlayerCache() {
        proxyServer.getScheduler().buildTask(catalyst, () -> {
            for (Player player : proxyServer.getAllPlayers()) {
                addPlayerToCache(player);
            }
        }).repeat(5, TimeUnit.MINUTES).schedule();
    }

    public void addPlayerToCache(Player player) {
        UUID playerUUID = player.getUniqueId();
        User temp = Catalyst.api.getUserManager().getUser(playerUUID);
        if (temp != null) {
            if (!cachedPlayers.containsKey(player)) {
                cachedPlayers.put(player, temp.getCachedData().getMetaData(getQueryOptions(temp)));
            }
        } else {
            throw new IllegalStateException("Failed to find the user " + player.getUsername() + " inside luckperms. Please report this on github");
        }
    }

    public void removePlayerFromCache(Player player) {
        cachedPlayers.remove(player);
    }

    public Optional<CachedMetaData> getCachedPlayerData(Player player) {
        return Optional.of(cachedPlayers.get(player));
    }

    public String getPrefix(Player player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getPrefix() != null) {
                return getCachedPlayerData(player).get().getPrefix();
            }
            return "";
        }
        return "";
    }

    public String getSuffix(Player player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getSuffix() != null) {
                return getCachedPlayerData(player).get().getSuffix();
            }
            return "";
        }
        return "";
    }

    private QueryOptions getQueryOptions(User user) {
        final ContextManager contextManager = Catalyst.api.getContextManager();
        return contextManager.getQueryOptions(user)
            .orElseGet(contextManager::getStaticQueryOptions);
    }

    public String getChatColor(Player player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getMetaValue("chat-color") != null) {
                return getCachedPlayerData(player).get().getMetaValue("chat-color");
            }
        }
        return "";
    }

    public String getNameColor(Player player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getMetaValue("name-color") != null) {
                return getCachedPlayerData(player).get().getMetaValue("name-color");
            }
        }
        return "";
    }
}
