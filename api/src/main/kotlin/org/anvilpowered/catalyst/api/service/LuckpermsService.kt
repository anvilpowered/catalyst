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

package org.anvilpowered.catalyst.api.service

import net.luckperms.api.cacheddata.CachedMetaData
import net.luckperms.api.model.user.User
import net.luckperms.api.query.QueryOptions
import org.anvilpowered.anvil.core.user.Player

interface LuckpermsService {

    fun cachedPlayerData(player: Any): CachedMetaData?

    fun queryOptions(user: User): QueryOptions

    fun prefix(player: Player): String

    fun suffix(player: Player): String

    fun chatColor(player: Player): String

    fun nameColor(player: Player): String

    fun groupName(player: Player): String

    interface Scope {
        val luckpermsService: LuckpermsService
    }
}
