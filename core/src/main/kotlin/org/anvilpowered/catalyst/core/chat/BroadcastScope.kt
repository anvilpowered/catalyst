package org.anvilpowered.catalyst.core.chat

import net.kyori.adventure.text.Component

interface BroadcastScope {

    fun broadcast(message: Component)
}
