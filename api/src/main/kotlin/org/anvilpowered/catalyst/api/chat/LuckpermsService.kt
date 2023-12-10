/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.api.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.cacheddata.CachedMetaData
import net.luckperms.api.context.ContextManager
import net.luckperms.api.model.user.User
import net.luckperms.api.model.user.UserManager
import net.luckperms.api.query.QueryOptions
import org.anvilpowered.catalyst.api.chat.placeholder.MessageContentFormat
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import java.util.UUID

class LuckpermsService {

    private fun String.toMiniComponent(): Component = MiniMessage.miniMessage().deserialize(this)

    private val userManager: UserManager = LuckPermsProvider.get().userManager
    private val contextManager: ContextManager = LuckPermsProvider.get().contextManager

    private fun cachedPlayerData(userId: UUID): CachedMetaData? {
        val user = userManager.getUser(userId)
        return user?.cachedData?.getMetaData(queryOptions(user))
    }

    private fun queryOptions(user: User): QueryOptions =
        contextManager.getQueryOptions(user).orElseGet { contextManager.staticQueryOptions }

    fun prefix(userId: UUID): Component = cachedPlayerData(userId)?.prefix?.toMiniComponent() ?: Component.empty()
    fun suffix(userId: UUID): Component = cachedPlayerData(userId)?.suffix?.toMiniComponent() ?: Component.empty()
    fun group(userId: UUID): Component = cachedPlayerData(userId)?.primaryGroup?.toMiniComponent() ?: Component.empty()

    fun nameFormat(userId: UUID, channelId: String): OnlineUserFormat? =
        cachedPlayerData(userId)?.getMetaValue("channel.$channelId.name-format")
            ?.let { OnlineUserFormat(MiniMessage.miniMessage().deserialize(it)) }

    fun getMessageContentFormat(userId: UUID, channelId: String): MessageContentFormat? =
        cachedPlayerData(userId)?.getMetaValue("channel.$channelId.message-content-format")
            ?.let { MessageContentFormat(MiniMessage.miniMessage().deserialize(it)) }
}
