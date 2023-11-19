package org.anvilpowered.catalyst.core.chat.pm

import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.Player

class PrivateMessage(
    val sourceMessage: Component,
    val recipientMessage: Component,
    val socialSpyMessage: Component,
) {

    interface Builder {
        fun source(source: Player): Builder
        fun recipient(recipient: Player): Builder
        fun message(message: Component): Builder
        fun build(): PrivateMessage
    }

    companion object {
        context(Registry.Scope)
        fun builder(): Builder = PrivateMessageBuilderImpl()
    }
}
