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
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.query.QueryOptions;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

@Singleton
public class CommonLuckpermsService<TUser, TPlayer> implements LuckpermsService {

    private final UserManager userManager;
    private final ContextManager contextManager;

    @Inject
    private UserService<TUser, TPlayer> userService;

    @Inject
    public CommonLuckpermsService(Registry registry) {
        this.userManager = LuckPermsProvider.get().getUserManager();
        this.contextManager = LuckPermsProvider.get().getContextManager();
    }

    @Override
    public Optional<CachedMetaData> getCachedPlayerData(Object player) {
        User lpUser = userManager.getUser(userService.getUUID((TUser) player));
        if (lpUser == null) return Optional.empty();
        return Optional.of(lpUser.getCachedData().getMetaData(getQueryOptions(lpUser)));
    }

    @Override
    public QueryOptions getQueryOptions(User user) {
        return contextManager.getQueryOptions(user)
            .orElseGet(contextManager::getStaticQueryOptions);
    }

    @Override
    public @NotNull String getPrefix(Object player) {
        Optional<CachedMetaData> playerData = getCachedPlayerData(player);
        if (playerData.isPresent()) {
            if (playerData.get().getPrefix() != null) {
                return Objects.requireNonNull(playerData.get().getPrefix());
            }
        }
        return "";
    }

    @Override
    public @NotNull String getSuffix(Object player) {
        Optional<CachedMetaData> playerData = getCachedPlayerData(player);
        if (playerData.isPresent()) {
            if (playerData.get().getSuffix() != null) {
                return Objects.requireNonNull(playerData.get().getSuffix());
            }
        }
        return "";
    }

    @Override
    public @NotNull String getChatColor(Object player) {
        Optional<CachedMetaData> playerData = getCachedPlayerData(player);
        if (playerData.isPresent()) {
            if (playerData.get().getMetaValue("chat-color") != null) {
                return Objects.requireNonNull(playerData.get().getMetaValue("chat-color"));
            }
        }
        return "";
    }

    @Override
    public @NotNull String getNameColor(Object player) {
        Optional<CachedMetaData> playerData = getCachedPlayerData(player);
        if (playerData.isPresent()) {
            if (playerData.get().getMetaValue("name-color") != null) {
                return Objects.requireNonNull(playerData.get().getMetaValue("name-color"));
            }
        }
        return "";
    }
}
