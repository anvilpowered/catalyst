package org.anvilpowered.catalyst.agent.chat

import org.anvilpowered.anvil.user.Component

interface BroadcastScope {

    fun broadcast(message: Component)
}
