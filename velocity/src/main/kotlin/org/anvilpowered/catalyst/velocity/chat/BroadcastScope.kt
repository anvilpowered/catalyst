package org.anvilpowered.catalyst.velocity.chat

import net.kyori.adventure.text.Component

interface BroadcastScope {

    fun broadcast(message: Component)
}
