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

package org.anvilpowered.catalyst.common.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
public class CommonLuckpermsService<TString, TPlayer extends TCommandSource, TCommandSource> implements LuckpermsService<TPlayer> {

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    @Inject
    private PluginInfo<TString> pluginInfo;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Logger logger;

    private static Map<UUID, CachedMetaData> cachedPlayers = new HashMap<>();

    @Override
    public Runnable syncPlayerCache() {
        return () -> {
            userService.getOnlinePlayers().forEach(this::addPlayerToCache);
        };
    }

    @Override
    public void addPlayerToCache(TPlayer player) {
        UUID playerUUID = userService.getUUID(player);
        User tempUser = LuckPermsProvider.get().getUserManager().getUser(playerUUID);

        if (tempUser != null) {
            if (cachedPlayers.containsKey(playerUUID)) {
                if (tempUser.getCachedData().getMetaData(getQueryOptions(tempUser)) != cachedPlayers.get(playerUUID)) {
                    cachedPlayers.replace(playerUUID, tempUser.getCachedData().getMetaData(getQueryOptions(tempUser)));
                }
            } else {
                cachedPlayers.put(playerUUID, tempUser.getCachedData().getMetaData(getQueryOptions(tempUser)));
            }
        } else {
            throw new IllegalStateException("Failed to find the user " + userService.getUserName(player) + " inside luckperms.");
        }
    }

    @Override
    public void removePlayerFromCache(TPlayer player) {
        cachedPlayers.remove(userService.getUUID(player));
    }

    @Override
    public Optional<CachedMetaData> getCachedPlayerData(TPlayer player) {
        return Optional.of(cachedPlayers.get(userService.getUUID(player)));
    }

    @Override
    public QueryOptions getQueryOptions(User user) {
        final ContextManager contextManager = LuckPermsProvider.get().getContextManager();
        return contextManager.getQueryOptions(user)
            .orElseGet(contextManager::getStaticQueryOptions);
    }

    @Override
    public String getPrefix(TPlayer player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getPrefix() != null) {
                return getCachedPlayerData(player).get().getPrefix();
            }
            return "";
        }
        return "";
    }

    @Override
    public String getSuffix(TPlayer player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getSuffix() != null) {
                return getCachedPlayerData(player).get().getSuffix();
            }
            return "";
        }
        return "";
    }

    @Override
    public String getChatColor(TPlayer player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getMetaValue("chat-color") != null) {
                return getCachedPlayerData(player).get().getMetaValue("chat-color");
            }
        }
        return "";
    }

    @Override
    public String getNameColor(TPlayer player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getMetaValue("name-color") != null) {
                return getCachedPlayerData(player).get().getMetaValue("name-color");
            }
        }
        return "";
    }
}
