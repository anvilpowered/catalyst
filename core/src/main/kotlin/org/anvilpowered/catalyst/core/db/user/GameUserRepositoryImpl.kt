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

package org.anvilpowered.catalyst.core.db.user

import org.anvilpowered.catalyst.api.user.GameUser
import org.anvilpowered.catalyst.api.user.GameUserRepository
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

object GameUserRepositoryImpl : GameUserRepository {
    override suspend fun getOneOrCreate(
        id: UUID,
        userId: UUID,
        username: String,
        ipAddress: String,
    ): GameUser = newSuspendedTransaction {
        getById(id) ?: create(
            GameUser(
                id,
                userId,
                username,
                "minecraft",
                null,
            ),
        )
    }

    override suspend fun getNickname(id: UUID): String? = newSuspendedTransaction {
        GameUserTable.select { GameUserTable.id eq id }
            .firstOrNull()
            ?.getOrNull(GameUserTable.nickname)
    }

    override suspend fun updateNickname(id: UUID, nickname: String): Boolean = newSuspendedTransaction {
        GameUserTable.update({ GameUserTable.id eq id }) {
            it[this.nickname] = nickname
        } > 0
    }

    override suspend fun deleteNickname(id: UUID): Boolean = newSuspendedTransaction {
        GameUserTable.update({ GameUserTable.id eq id }) {
            it[nickname] = null
        } > 0
    }

    override suspend fun getAllUsernames(startWith: String): SizedIterable<String> = newSuspendedTransaction {
        GameUserEntity.find { GameUserTable.username like "$startWith%" }.mapLazy { it.username }
    }

    override suspend fun getByUsername(username: String): GameUser? = newSuspendedTransaction {
        GameUserTable.select { GameUserTable.username eq username }
            .firstOrNull()
            ?.toGameUser()
    }

    override suspend fun countAll(): Long = newSuspendedTransaction {
        GameUserEntity.all().count()
    }

    override suspend fun create(item: GameUser): GameUser = newSuspendedTransaction {
        GameUserEntity.new(item.id) {
            user = UserEntity[item.userId]
            gameType = item.gameType
            username = item.username
        }
        item
    }

    override suspend fun exists(id: UUID): Boolean =
        newSuspendedTransaction { GameUserEntity.findById(id) != null }

    override suspend fun getById(id: UUID): GameUser? = newSuspendedTransaction {
        GameUserTable.select { GameUserTable.id eq id }
            .firstOrNull()
            ?.toGameUser()
    }

    override suspend fun deleteById(id: UUID): Boolean = newSuspendedTransaction {
        GameUserTable.deleteWhere { GameUserTable.id eq id } > 0
    }
}
