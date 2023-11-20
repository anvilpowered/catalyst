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

package org.anvilpowered.catalyst.core.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.anvil.core.user.PlayerService
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.service.TabService
import org.anvilpowered.catalyst.api.user.LocationScope

context(Registry.Scope, org.anvilpowered.catalyst.api.user.LocationScope, PlayerService.Scope, LuckpermsService.Scope)
class CommonTabService : TabService {

    override fun format(player: Player, ping: Int, playerCount: Int): Component {
        return LegacyComponentSerializer.legacyAmpersand()
            .deserialize(replacePlaceholders(registry[CatalystKeys.TAB_FORMAT], player, ping, playerCount))
    }

    override fun formatCustom(format: Component, player: Player, ping: Int, playerCount: Int): Component {
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
