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
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.anvil.api.environment.Environment
import org.anvilpowered.catalyst.api.CatalystImpl
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo
import org.anvilpowered.catalyst.velocity.listener.VelocityListener
import org.anvilpowered.catalyst.velocity.module.VelocityModule
import org.anvilpowered.catalyst.velocity.service.GlobalTab

@Plugin(
    id = CatalystPluginInfo.id,
    name = CatalystPluginInfo.name,
    version = CatalystPluginInfo.version,
    authors = ["STG_Allen", "Cableguy20"],
    description = CatalystPluginInfo.description,
    url = CatalystPluginInfo.url,
    dependencies = [Dependency(id = "anvil"), Dependency(id = "luckperms")]
)
class CatalystVelocity @Inject constructor(
    injector: Injector,
    private val proxyServer: ProxyServer,
) : CatalystImpl(injector, VelocityModule()) {

    override fun applyToBuilder(builder: Environment.Builder) {
        super.applyToBuilder(builder)
        builder.addEarlyServices(GlobalTab::class.java)
    }

    override fun whenReady(environment: Environment) {
        super.whenReady(environment)
        proxyServer.eventManager.register(
            this,
            environment.injector.getInstance(VelocityListener::class.java)
        )
    }
}
