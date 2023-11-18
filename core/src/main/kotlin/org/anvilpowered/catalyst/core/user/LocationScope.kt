package org.anvilpowered.catalyst.core.user

import org.anvilpowered.anvil.core.user.Player

interface LocationScope {

    val Player.serverName: String
}
