package org.anvilpowered.catalyst.api.user

import org.anvilpowered.anvil.core.user.Player

interface LocationScope {

    val Player.serverName: String
}
