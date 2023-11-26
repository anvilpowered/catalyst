/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.velocity

import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import org.anvilpowered.anvil.core.AnvilApi
import org.anvilpowered.anvil.velocity.createVelocity
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module

@Plugin(
    id = "catalyst",
    name = "Catalyst",
    version = "0.4.0-SNAPSHOT",
    authors = ["AnvilPowered"],
    dependencies = [Dependency(id = "luckperms")],
)
class CatalystVelocityBootstrap @Inject constructor(private val injector: Injector) {

    private lateinit var plugin: CatalystVelocityPlugin

    @Subscribe
    fun onInit(event: ProxyInitializeEvent) {
        plugin = koinApplication {
            modules(
                AnvilApi.createVelocity(injector).module,
                CatalystApi.create(injector).module,
                module { singleOf(::CatalystVelocityPlugin) },
            )
        }.koin.get()
        plugin.initialize()
    }
}
