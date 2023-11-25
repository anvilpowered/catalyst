/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
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

package org.anvilpowered.catalyst.velocity.db.user

import org.anvilpowered.catalyst.api.user.MinecraftUser
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

internal object MinecraftUserTable : UUIDTable("catalyst_minecraft_users") {
    val username = varchar("username", 255).uniqueIndex()
    val ipAddress = varchar("ip_address", 255)
    val nickname = varchar("nickname", 255).nullable()
}

internal class MinecraftUserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var username: String by MinecraftUserTable.username
    var ipAddress: String by MinecraftUserTable.ipAddress
    var nickname: String? by MinecraftUserTable.nickname

    companion object : UUIDEntityClass<MinecraftUserEntity>(MinecraftUserTable)
}

internal fun ResultRow.toMinecraftUser() = MinecraftUser(
    id = this[MinecraftUserTable.id].value,
    username = this[MinecraftUserTable.username],
    ipAddress = this[MinecraftUserTable.ipAddress],
    nickname = this[MinecraftUserTable.nickname],
)
