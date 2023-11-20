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

package org.anvilpowered.catalyst.velocity

import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.velocity.command.toVelocity
import org.anvilpowered.catalyst.core.command.nickname.NicknameCommand

@Plugin(
    id = "catalyst",
    name = "Catalyst",
    version = "0.4.0-SNAPSHOT",
    authors = ["AnvilPowered"],
)
class CatalystVelocity @Inject constructor(
    private val injector: Injector,
    private val proxyServer: ProxyServer,
) {

    @Subscribe
    fun onInit(event: ProxyInitializeEvent) {
        with(CatalystApi.createVelocity(injector)) {
            initApi()
        }
    }

    context(CatalystVelocityApi)
    private fun initApi() {
        proxyServer.commandManager.register(
            BrigadierCommand(NicknameCommand.create().toVelocity()),
        )
    }
}
