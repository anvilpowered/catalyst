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

package org.anvilpowered.catalyst.common.service

import com.google.inject.Inject
import com.google.inject.Singleton
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.registry.CatalystKeys.TAB_FOOTER
import org.anvilpowered.catalyst.api.registry.CatalystKeys.TAB_FORMAT
import org.anvilpowered.catalyst.api.registry.CatalystKeys.TAB_HEADER
import org.anvilpowered.catalyst.api.service.LuckpermsService
import org.anvilpowered.catalyst.api.service.TabService

@Singleton
class CommonTabService<TPlayer> @Inject constructor(
    private val registry: Registry,
    private val locationService: LocationService,
    private val userService: UserService<TPlayer, TPlayer>,
    private val luckpermsService: LuckpermsService
) : TabService<TPlayer> {

    override fun format(player: TPlayer, ping: Int, playerCount: Int): Component {
        return LegacyComponentSerializer.legacyAmpersand()
            .deserialize(replacePlaceholders(registry.getOrDefault(TAB_FORMAT), player, ping, playerCount))
    }

    override fun formatCustom(format: String, player: TPlayer, ping: Int, playerCount: Int): Component {
        return LegacyComponentSerializer.legacyAmpersand()
            .deserialize(replacePlaceholders(format, player, ping, playerCount))
    }

    override fun formatHeader(player: TPlayer, ping: Int, playerCount: Int): Component {
        return LegacyComponentSerializer.legacyAmpersand()
            .deserialize(replacePlaceholders(registry.getOrDefault(TAB_HEADER), player, ping, playerCount))
    }

    override fun formatFooter(player: TPlayer, ping: Int, playerCount: Int): Component {
        return LegacyComponentSerializer.legacyAmpersand()
            .deserialize(replacePlaceholders(registry.getOrDefault(TAB_FOOTER), player, ping, playerCount))
    }

    private fun replacePlaceholders(format: String, player: TPlayer, ping: Int, playerCount: Int): String {
        val userName = userService.getUserName(player)
        return format.replace("%player%", userName)
            .replace("%prefix%", luckpermsService.prefix(player!!))
            .replace("%suffix%", luckpermsService.suffix(player))
            .replace("%server%", locationService.getServer(userName)?.name ?: "null")
            .replace("%ping%", ping.toString())
            .replace("%playercount%", playerCount.toString())
    }
}
