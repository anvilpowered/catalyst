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

import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.velocity.command.nickname.NicknameCommandFactory
import org.anvilpowered.catalyst.velocity.db.user.MinecraftUserTable
import org.anvilpowered.catalyst.velocity.db.user.UserTable
import org.anvilpowered.catalyst.velocity.listener.ChatListener
import org.anvilpowered.catalyst.velocity.listener.CommandListener
import org.anvilpowered.catalyst.velocity.listener.DiscordListener
import org.anvilpowered.catalyst.velocity.listener.JoinListener
import org.anvilpowered.catalyst.velocity.listener.LeaveListener
import org.anvilpowered.kbrig.brigadier.toBrigadier
import org.anvilpowered.kbrig.tree.LiteralCommandNode
import org.apache.logging.log4j.Logger
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class CatalystVelocityPlugin(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val pluginContainer: PluginContainer,
    private val logger: Logger,
    private val nicknameCommandFactory: NicknameCommandFactory,
    private val chatListener: ChatListener,
    private val commandListener: CommandListener,
    private val discordListener: DiscordListener,
    private val joinListener: JoinListener,
    private val leaveListener: LeaveListener,
) {

    private fun LiteralCommandNode<CommandSource>.register() = proxyServer.commandManager.register(BrigadierCommand(toBrigadier()))

    fun registerCommands() {
        logger.info("Building command trees and registering commands...")
        nicknameCommandFactory.create().register()
        logger.info("Finished registering commands.")
    }

    fun registerListeners() {
        logger.info("Registering listeners...")
        proxyServer.eventManager.register(pluginContainer, chatListener)
        proxyServer.eventManager.register(pluginContainer, commandListener)
        proxyServer.eventManager.register(pluginContainer, discordListener)
        proxyServer.eventManager.register(pluginContainer, joinListener)
        proxyServer.eventManager.register(pluginContainer, leaveListener)
        logger.info("Finished registering listeners.")
    }

    fun connectDatabase() {
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
