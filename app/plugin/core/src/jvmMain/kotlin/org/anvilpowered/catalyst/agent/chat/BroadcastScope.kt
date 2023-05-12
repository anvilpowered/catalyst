package org.anvilpowered.catalyst.agent.chat

import org.anvilpowered.anvil.domain.user.Component

interface BroadcastScope {

    fun broadcast(message: Component)
}
