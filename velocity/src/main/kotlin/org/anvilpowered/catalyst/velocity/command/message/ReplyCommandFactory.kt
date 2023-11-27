package org.anvilpowered.catalyst.velocity.command.message

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.user.requiresPermission
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.anvilpowered.catalyst.velocity.chat.PrivateMessageService
import org.anvilpowered.catalyst.velocity.command.CommandDefaults
import org.anvilpowered.kbrig.argument.StringArgumentType
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.context.get
import org.anvilpowered.kbrig.tree.LiteralCommandNode

class ReplyCommandFactory(
    val minecraftUserRepository: MinecraftUserRepository,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
    private val proxyServer: ProxyServer,
    private val privateMessageService: PrivateMessageService,
) {

    fun create(): LiteralCommandNode<CommandSource> =
        ArgumentBuilder.literal<CommandSource>("reply").executes(CommandDefaults::notEnoughArgs)
            .requiresPermission(registry[catalystKeys.MESSAGE_PERMISSION])
            .then(
                ArgumentBuilder.required<CommandSource, String>("message", StringArgumentType.GreedyPhrase)
                    .executes { context ->
                        suspend {
                            privateMessageService.reply(
                                context.source as Player,
                                Component.text(context.get<String>("message")),
                            )
                        }
                        0
                    }.build(),
            ).build()
}
