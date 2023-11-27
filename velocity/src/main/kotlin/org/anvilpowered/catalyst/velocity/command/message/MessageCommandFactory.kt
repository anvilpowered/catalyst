package org.anvilpowered.catalyst.velocity.command.message

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
import org.anvilpowered.catalyst.velocity.chat.PrivateMessageService
import org.anvilpowered.catalyst.velocity.command.CommandDefaults
import org.anvilpowered.catalyst.velocity.command.playerArgument
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
            .requiresPermission(registry[catalystKeys.MESSAGE_PERMISSION])
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
                                            Component.text(context.get<String>("message"))
                                        )
                                    }
                                    0
                                }
                            }
                        }.build(),
                ).build(),
            ).build()
}
