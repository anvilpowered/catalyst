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
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.service.LoggerService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class CommonLuckpermsService<TUser, TPlayer> implements LuckpermsService {

    private static Map<UUID, CachedMetaData> cachedPlayers = new HashMap<>();

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    private LoggerService loggerService;

    @Inject
    public CommonLuckpermsService(Registry registry) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this.syncPlayerCache(), 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public Runnable syncPlayerCache() {
        return () -> {
            userService.getOnlinePlayers().forEach(this::addPlayerToCache);
        };
    }

    @Override
    public void addPlayerToCache(Object player) {
        UUID playerUUID = userService.getUUID((TUser) player);
        User tempUser = LuckPermsProvider.get().getUserManager().getUser(playerUUID);

        if (tempUser != null) {
            if (cachedPlayers.containsKey(playerUUID)) {
                if (tempUser.getCachedData().getMetaData(getQueryOptions(tempUser))
                    != cachedPlayers.get(playerUUID)) {
                    cachedPlayers.replace(
                        playerUUID,
                        tempUser.getCachedData().getMetaData(getQueryOptions(tempUser))
                    );
                }
            } else {
                cachedPlayers.put(
                    playerUUID,
                    tempUser.getCachedData().getMetaData(getQueryOptions(tempUser))
                );
                loggerService.info("Adding " + tempUser.getUsername() + " to the LuckPerms cache!");
            }
        } else {
            throw new IllegalStateException("Failed to find user matching UUID " +
                playerUUID.toString() + " inside luckperms.");
        }
    }

    @Override
    public void removePlayerFromCache(Object player) {
        cachedPlayers.remove(userService.getUUID((TUser) player));
    }

    @Override
    public Optional<CachedMetaData> getCachedPlayerData(Object player) {
        return Optional.ofNullable(cachedPlayers.get(userService.getUUID((TUser) player)));
    }

    @Override
    public QueryOptions getQueryOptions(User user) {
        final ContextManager contextManager = LuckPermsProvider.get().getContextManager();
        return contextManager.getQueryOptions(user)
            .orElseGet(contextManager::getStaticQueryOptions);
    }

    @Override
    public @NotNull String getPrefix(Object player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getPrefix() != null) {
                return Objects.requireNonNull(getCachedPlayerData(player).get().getPrefix());
            }
            return "";
        }
        return "";
    }

    @Override
    public @NotNull String getSuffix(Object player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getSuffix() != null) {
                return Objects.requireNonNull(getCachedPlayerData(player).get().getSuffix());
            }
            return "";
        }
        return "";
    }

    @Override
    public @NotNull String getChatColor(Object player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getMetaValue("chat-color") != null) {
                return Objects.requireNonNull(getCachedPlayerData(player)
                    .get().getMetaValue("chat-color"));
            }
        }
        return "";
    }

    @Override
    public @NotNull String getNameColor(Object player) {
        if (getCachedPlayerData(player).isPresent()) {
            if (getCachedPlayerData(player).get().getMetaValue("name-color") != null) {
                return Objects.requireNonNull(getCachedPlayerData(player)
                    .get().getMetaValue("name-color"));
            }
        }
        return "";
    }
}
