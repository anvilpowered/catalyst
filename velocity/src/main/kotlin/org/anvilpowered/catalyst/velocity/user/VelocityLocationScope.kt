package org.anvilpowered.catalyst.velocity.user

import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.core.user.LocationScope
import com.velocitypowered.api.proxy.Player as VelocityPlayer

context(ProxyServerScope)
class VelocityLocationScope : LocationScope {
    override val Player.serverName: String
        get() = (platformDelegate as VelocityPlayer).currentServer.map { it.serverInfo.name }.orElse("null")
}
