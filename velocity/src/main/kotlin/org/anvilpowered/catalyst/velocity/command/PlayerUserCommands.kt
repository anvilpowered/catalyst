package org.anvilpowered.catalyst.velocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import kotlin.jvm.optionals.getOrNull
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.kbrig.argument.StringArgumentType
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.builder.ArgumentBuilder.Companion.required
import org.anvilpowered.kbrig.builder.RequiredArgumentBuilder
import org.anvilpowered.kbrig.context.CommandContext
import org.anvilpowered.kbrig.context.get


fun ProxyServer.playerArgument(
    argumentName: String = "player",
    command: (context: CommandContext<CommandSource>, player: Player) -> Int,
): RequiredArgumentBuilder<CommandSource, String> =
    required<CommandSource, String>(argumentName, StringArgumentType.SingleWord)
        .suggests { _, builder ->
            allPlayers.forEach { player -> builder.suggest(player.username) }
            builder.build()
        }
        .executes { context ->
            val playerName = context.get<String>(argumentName)
            getPlayer(playerName).getOrNull()?.let { velocityPlayer ->
                command(context, velocityPlayer)
            } ?: run {
                context.source.sendMessage(
                    Component.text()
                        .append(Component.text("Player with name ", NamedTextColor.RED))
                        .append(Component.text(playerName, NamedTextColor.GOLD))
                        .append(Component.text(" not found!", NamedTextColor.RED)),
                )
                0
            }
        }


