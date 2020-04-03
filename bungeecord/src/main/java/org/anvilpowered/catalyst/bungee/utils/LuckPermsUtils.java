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

package org.anvilpowered.catalyst.bungee.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.bungee.plugin.CatalystBungee;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class LuckPermsUtils {

    private Registry registry;

    private static Map<UUID, CachedMetaData> cachedPlayers = new HashMap<>();

    @Inject
    private java.util.logging.Logger logger;

    public static LuckPerms api;

    @Inject
    public LuckPermsUtils(Registry registry) {
        api = LuckPermsProvider.get();
        this.registry = registry;
        this.registry.addRegistryLoadedListener(this::syncPlayerCache);
    }

    public void syncPlayerCache() {
        System.out.println("Running SyncPlayerCache");
        CatalystBungee.plugin.getProxy().getScheduler().schedule(CatalystBungee.plugin, () -> {
            System.out.println("SyncPlayerCache");
            System.out.println(CatalystBungee.plugin.getProxy().getPlayers() );
            for (ProxiedPlayer player : CatalystBungee.plugin.getProxy().getPlayers()) {
                System.out.println("Inside for loop???");
                addPlayerToCache(player);
            }
        }, 5, TimeUnit.SECONDS).getTask().run();
    }

    public void addPlayerToCache(ProxiedPlayer player) {
        UUID playerUUID = player.getUniqueId();
        User temp = api.getUserManager().getUser(playerUUID);
        if (temp != null) {
            if (cachedPlayers.containsKey(playerUUID)) {
                if (temp.getCachedData().getMetaData(getQueryOptions(temp)) != cachedPlayers.get(playerUUID)) {
                    cachedPlayers.replace(playerUUID, temp.getCachedData().getMetaData(getQueryOptions(temp)));
                    logger.info("Updated luckperms data for " + player.getDisplayName());
                }
            } else {
                cachedPlayers.put(playerUUID, temp.getCachedData().getMetaData(getQueryOptions(temp)));
                logger.info("Added " + player.getDisplayName() + " to the luckperms cache!");
            }
        } else {
            throw new IllegalStateException("Failed to find the user " + player.getDisplayName() + " inside luckperms. Please report this on github");
        }
    }

    public void removePlayerFromCache(ProxiedPlayer player) {
        cachedPlayers.remove(player.getUniqueId());
    }

    public Optional<CachedMetaData> getCachedPlayerData(ProxiedPlayer player) {
        return Optional.of(cachedPlayers.get(player.getUniqueId()));
    }

    public String getPrefix(ProxiedPlayer player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getPrefix() != null) {
                return getCachedPlayerData(player).get().getPrefix();
            }
            return "";
        }
        return "Unspecified";
    }

    public String getSuffix(ProxiedPlayer player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getSuffix() != null) {
                return getCachedPlayerData(player).get().getSuffix();
            }
            return "";
        }
        return "Unspecified";
    }

    private QueryOptions getQueryOptions(User user) {
        final ContextManager contextManager = api.getContextManager();
        return contextManager.getQueryOptions(user)
            .orElseGet(contextManager::getStaticQueryOptions);
    }

    public String getChatColor(ProxiedPlayer player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getMetaValue("chat-color") != null) {
                return getCachedPlayerData(player).get().getMetaValue("chat-color");
            }
        }
        return "";
    }

    public String getNameColor(ProxiedPlayer player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getMetaValue("name-color") != null) {
                return getCachedPlayerData(player).get().getMetaValue("name-color");
            }
        }
        return "";
    }
}
