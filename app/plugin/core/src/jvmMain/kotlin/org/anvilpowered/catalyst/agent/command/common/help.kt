package org.anvilpowered.catalyst.agent.command.common

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.anvilpowered.anvil.domain.command.CommandSource
import org.anvilpowered.catalyst.agent.PluginMessages
import org.anvilpowered.kbrig.Command
import org.anvilpowered.kbrig.builder.ArgumentBuilder

fun <B : ArgumentBuilder<CommandSource, B>> B.addHelpChild(usage: String): B = then(
    ArgumentBuilder.literal<CommandSource>("help")
        .executes(usage(usage)),
)

private fun usage(usage: String): Command<CommandSource> = Command { context ->
    context.source.audience.sendMessage(
        Component.text()
            .append(PluginMessages.pluginPrefix)
            .append(Component.text("Command Usage", NamedTextColor.RED, TextDecoration.BOLD))
            .append(Component.newline())
            .append(Component.text(usage, NamedTextColor.AQUA)),
    )
    Command.SINGLE_SUCCESS
}
