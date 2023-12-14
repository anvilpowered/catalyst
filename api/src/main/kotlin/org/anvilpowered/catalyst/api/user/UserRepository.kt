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

package org.anvilpowered.catalyst.api.user

import org.anvilpowered.anvil.core.db.MutableRepository
import org.anvilpowered.anvil.core.db.Pagination
import java.util.UUID

interface UserRepository : MutableRepository<User, User.CreateDto> {

    /**
     * For listing use.
     */
    suspend fun paginate(): Pagination<User>

    suspend fun getByUsername(username: String): User?
    suspend fun getByEmail(email: String): User?
    suspend fun getByDiscordUserId(id: Long): User?
    suspend fun getByMinecraftUserId(id: UUID): User?
}