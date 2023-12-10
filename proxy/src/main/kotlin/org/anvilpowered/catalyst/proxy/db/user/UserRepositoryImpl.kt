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

import org.anvilpowered.anvil.core.db.MutableRepository
import org.anvilpowered.anvil.core.db.Pagination
import org.anvilpowered.catalyst.api.user.User
import org.anvilpowered.catalyst.api.user.UserRepository
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.UUID

class UserRepositoryImpl(
    private val logger: Logger,
) : UserRepository {
    override suspend fun paginate(): Pagination<User> {
        TODO("Not yet implemented")
    }

    override suspend fun create(item: User.CreateDto): User = newSuspendedTransaction {
        val result = UserTable.insert {
            it[username] = item.username
            it[email] = item.email
            it[discordUserId] = item.discordUserId
            it[minecraftUserId] = item.minecraftUserId
        }.resultedValues

        val user = checkNotNull(result) { "Failed to create User ${item.username}" }
            .single().toUser()

        logger.info("Created new User ${user.id} with data $item")

        user
    }

    override suspend fun put(item: User.CreateDto): MutableRepository.PutResult<User> = newSuspendedTransaction {
        val existingUser = getByUsername(item.username)
        if (existingUser == null) {
            MutableRepository.PutResult(create(item), created = true)
        } else {
            // update the existing User
            logger.info("Found existing User ${existingUser.id} with username ${existingUser.username}")

            if (existingUser.email != item.email) {
                UserTable.update({ UserTable.id eq existingUser.id }) {
                    logger.info("Updating email for User ${existingUser.id} from ${existingUser.email} to ${item.email}")
                    it[email] = item.email
                }
            }

            if (existingUser.discordUserId != item.discordUserId) {
                UserTable.update({ UserTable.id eq existingUser.id }) {
                    logger.info("Updating discordUserId for User ${existingUser.id} from ${existingUser.discordUserId} to ${item.discordUserId}")
                    it[discordUserId] = item.discordUserId
                }
            }

            if (existingUser.minecraftUserId != item.minecraftUserId) {
                UserTable.update({ UserTable.id eq existingUser.id }) {
                    logger.info("Updating minecraftUserId for User ${existingUser.id} from ${existingUser.minecraftUserId} to ${item.minecraftUserId}")
                    it[minecraftUserId] = item.minecraftUserId
                }
            }

            MutableRepository.PutResult(existingUser, created = false)
        }
    }

    private suspend fun getOneWhere(condition: SqlExpressionBuilder.() -> Op<Boolean>): User? = newSuspendedTransaction {
        UserTable.select { condition() }.firstOrNull()?.toUser()
    }

    override suspend fun getById(id: UUID): User? = getOneWhere { UserTable.id eq id }
    override suspend fun getByUsername(username: String): User? = getOneWhere { UserTable.username eq username }
    override suspend fun getByEmail(email: String): User? = getOneWhere { UserTable.email eq email }
    override suspend fun getByDiscordUserId(id: Long): User? = getOneWhere { UserTable.discordUserId eq id }

    override suspend fun getByMinecraftUserId(id: UUID): User? = getOneWhere { UserTable.minecraftUserId eq id }

    override suspend fun countAll(): Long = newSuspendedTransaction { UserEntity.all().count() }

    override suspend fun exists(id: UUID): Boolean = newSuspendedTransaction {
        UserEntity.findById(id) != null
    }

    override suspend fun deleteById(id: UUID): Boolean = newSuspendedTransaction {
        UserTable.deleteWhere { UserTable.id eq id } > 0
    }
}
