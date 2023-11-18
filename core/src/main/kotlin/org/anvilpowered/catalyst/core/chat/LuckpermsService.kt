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

package org.anvilpowered.catalyst.core.chat

import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.cacheddata.CachedMetaData
import net.luckperms.api.context.ContextManager
import net.luckperms.api.model.user.User
import net.luckperms.api.model.user.UserManager
import net.luckperms.api.query.QueryOptions
import org.anvilpowered.anvil.core.user.PlayerService
import java.util.UUID

context(PlayerService.Scope)
class LuckpermsService {

    private val userManager: UserManager = LuckPermsProvider.get().userManager
    private val contextManager: ContextManager = LuckPermsProvider.get().contextManager

    private fun cachedPlayerData(userId: UUID): CachedMetaData? {
        val user = userManager.getUser(userId)
        return user?.cachedData?.getMetaData(queryOptions(user))
    }

    private fun queryOptions(user: User): QueryOptions {
        return contextManager.getQueryOptions(user).orElseGet { contextManager.staticQueryOptions }
    }

    fun prefix(userId: UUID): String = cachedPlayerData(userId)?.prefix ?: ""
    fun suffix(userId: UUID): String = cachedPlayerData(userId)?.suffix ?: ""
    fun chatColor(userId: UUID): String = cachedPlayerData(userId)?.getMetaValue("chat-color") ?: ""
    fun nameColor(userId: UUID): String = cachedPlayerData(userId)?.getMetaValue("name-color") ?: ""
    fun groupName(userId: UUID): String = userManager.getUser(userId)?.primaryGroup ?: ""
}
