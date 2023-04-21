/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvlipowered.catalyst.agent

import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.permission.Tristate
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.catalyst.agent.chat.BroadcastScope
import org.anvilpowered.catalyst.agent.command.CatalystCommand
import org.anvilpowered.catalyst.agent.command.createBroadcast
import org.anvilpowered.kbrig.builder.LiteralArgumentBuilder

@Plugin(
    id = "catalyst",
    name = "Catalyst",
    version = "0.4.0-SNAPSHOT",
    authors = ["AnvilPowered"],
    dependencies = [Dependency(id = "anvil"), Dependency(id = "luckperms")],
)
class CatalystVelocity @Inject constructor(
    injector: Injector,
    private val proxyServer: ProxyServer,
) {
    context(BroadcastScope)
    fun foo() {
        LiteralArgumentBuilder<String>("test")
        proxyServer.commandManager.register(
            BrigadierCommand(CatalystCommand.createBroadcast().mapSource(mapper)),
        )
    }
}

fun Tristate.toBoolean(): Boolean? {
    return when (this) {
        Tristate.TRUE -> true
        Tristate.FALSE -> false
        Tristate.UNDEFINED -> null
    }
}
