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

package org.anvilpowered.catalyst.velocity

import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.velocity.db.user.MinecraftUserTable
import org.anvilpowered.catalyst.velocity.db.user.UserTable
import org.anvilpowered.catalyst.velocity.registrar.Registrar
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class CatalystVelocityPlugin(
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val logger: Logger,
    private val registrars: List<Registrar>,
) {

    fun initialize() {
        connectDatabase()
        registrars.forEach(Registrar::register)
    }

    private fun connectDatabase() {
        logger.info("Connecting to database...")
        Database.connect(
            registry[catalystKeys.DB_URL],
            driver = "org.postgresql.Driver",
            user = registry[catalystKeys.DB_USER],
            password = registry[catalystKeys.DB_PASSWORD],
        )
        logger.info("Finished connecting to database.")
        logger.info("Creating tables...")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                MinecraftUserTable,
                UserTable,
            )
        }
        logger.info("Finished creating tables.")
    }
}
