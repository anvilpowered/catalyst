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

package org.anvilpowered.catalyst.api

import com.google.inject.Injector
import com.google.inject.Module
import org.anvilpowered.anvil.api.environment.Environment
import org.anvilpowered.catalyst.api.discord.JDAService
import org.anvilpowered.catalyst.api.service.EventRegistrationService
import org.anvilpowered.catalyst.api.service.LuckpermsService
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo

open class CatalystImpl(
    rootInjector: Injector,
    module: Module
) : Catalyst(CatalystPluginInfo.id, rootInjector, module) {

    override fun applyToBuilder(builder: Environment.Builder) {
        super.applyToBuilder(builder)
        builder.addEarlyServices(
            LuckpermsService::class.java,
            JDAService::class.java,
            EventRegistrationService::class.java
        )
    }
}
