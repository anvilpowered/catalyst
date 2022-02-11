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

package org.anvilpowered.catalyst.velocity.module

import com.google.inject.Singleton
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import org.anvilpowered.anvil.api.misc.bind
import org.anvilpowered.anvil.api.misc.to
import org.anvilpowered.catalyst.api.discord.DiscordCommandService
import org.anvilpowered.catalyst.api.service.BroadcastService
import org.anvilpowered.catalyst.common.module.CommonModule
import org.anvilpowered.catalyst.velocity.service.VelocityBroadcastService
import org.anvilpowered.catalyst.velocity.service.VelocityDiscordCommandService

@Singleton
class VelocityModule : CommonModule<Player, CommandSource>("plugins") {
    override fun configure() {
        super.configure()
        with(binder()) {
            bind<BroadcastService>().to<VelocityBroadcastService>()
            bind<DiscordCommandService>().to<VelocityDiscordCommandService>()
        }
    }
}
