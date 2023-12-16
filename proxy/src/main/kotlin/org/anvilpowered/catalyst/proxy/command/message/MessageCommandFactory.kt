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

package org.anvilpowered.catalyst.proxy.command.message

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.user.requiresPermission
import org.anvilpowered.catalyst.api.PluginMessages
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.anvilpowered.catalyst.proxy.chat.PrivateMessageService
import org.anvilpowered.catalyst.proxy.command.CommandDefaults
import org.anvilpowered.catalyst.proxy.command.playerArgument
import org.anvilpowered.kbrig.argument.StringArgumentType
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.context.get
import org.anvilpowered.kbrig.tree.LiteralCommandNode

class MessageCommandFactory(
    val minecraftUserRepository: MinecraftUserRepository,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val proxyServer: ProxyServer,
    private val privateMessageService: PrivateMessageService,
) {

    fun create(): LiteralCommandNode<CommandSource> =
        ArgumentBuilder.literal<CommandSource>("msg").executes(CommandDefaults::notEnoughArgs)
            .requiresPermission(registry[catalystKeys.PERMISSION_MESSAGE])
            .then(
                proxyServer.playerArgument { context, _ ->
                    context.source.sendMessage(PluginMessages.notEnoughArgs)
                    0
                }.then(
                    ArgumentBuilder.required<CommandSource, String>("message", StringArgumentType.GreedyPhrase)
                        .executes { context ->
                            if (ConsoleCommandSource::class.java.isAssignableFrom(context.source::class.java)) {
                                0
                            } else {
                                if (context.source == context.get<Player>("player")) {
                                    context.source.sendMessage(PluginMessages.messageSelf)
                                    0
                                } else {
                                    suspend {
                                        privateMessageService.sendMessage(
                                            context.source as Player,
                                            context.get<Player>("player"),
                                            Component.text(context.get<String>("message")),
                                        )
                                    }
                                    0
                                }
                            }
                        }.build(),
                ).build(),
            ).build()
}
