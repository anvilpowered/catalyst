package org.anvilpowered.catalyst.agent.command

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.user.CommandSource
import org.anvilpowered.kbrig.context.CommandContext

object CatalystCommand {
    internal fun notEnoughArgs(context: CommandContext<CommandSource>): Int {
        context.source.sendMessage(
            Component.text()
                .append(Component.text("Invalid command usage!\n").color(NamedTextColor.RED))
                .append(
                    Component.text("Not enough arguments in: ").color(NamedTextColor.RED)
                        .append(Component.text(context.input).color(NamedTextColor.YELLOW)),
                ),
        )
        return 1
    }
}
