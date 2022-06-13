/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import com.google.inject.Singleton
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.cacheddata.CachedMetaData
import net.luckperms.api.context.ContextManager
import net.luckperms.api.model.user.User
import net.luckperms.api.model.user.UserManager
import net.luckperms.api.query.QueryOptions
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.service.LuckpermsService

@Singleton
class CommonLuckpermsService<TPlayer> @Inject constructor(
    private val userService: UserService<TPlayer, TPlayer>
) : LuckpermsService {

    private val userManager: UserManager = LuckPermsProvider.get().userManager
    private val contextManager: ContextManager = LuckPermsProvider.get().contextManager

    override fun cachedPlayerData(player: Any): CachedMetaData? {
        val user = userManager.getUser(userService.getUUID(player as TPlayer)!!)
        return user?.cachedData?.getMetaData(queryOptions(user))
    }

    override fun queryOptions(user: User): QueryOptions {
        return contextManager.getQueryOptions(user).orElseGet { contextManager.staticQueryOptions }
    }

    override fun prefix(player: Any): String = cachedPlayerData(player)?.prefix ?: ""
    override fun suffix(player: Any): String = cachedPlayerData(player)?.suffix ?: ""
    override fun chatColor(player: Any): String = cachedPlayerData(player)?.getMetaValue("chat-color") ?: ""
    override fun nameColor(player: Any): String = cachedPlayerData(player)?.getMetaValue("name-color") ?: ""

    override fun groupName(player: Any): String {
        return userManager.getUser(userService.getUserName(player as TPlayer))?.primaryGroup ?: ""
    }
}

