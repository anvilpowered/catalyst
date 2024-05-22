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

package org.anvilpowered.catalyst.proxy.db.user

import org.anvilpowered.catalyst.api.user.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

internal object UserTable : UUIDTable("catalyst_users") {
    val username = varchar("username", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex().nullable()
    val discordUserId = long("discord_user_id").uniqueIndex().nullable()
    val minecraftUserId = optReference("minecraft_user_id", MinecraftUserTable)
}

internal class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var username: String by UserTable.username
    var email: String? by UserTable.email
    var discordUserId: Long? by UserTable.discordUserId
    var minecraftUser: MinecraftUserEntity? by MinecraftUserEntity optionalReferencedOn UserTable.minecraftUserId

    companion object : UUIDEntityClass<UserEntity>(UserTable)
}

internal fun ResultRow.toUser() = User(
    uuid = this[UserTable.id].value,
    username = this[UserTable.username],
    email = this[UserTable.email],
)
