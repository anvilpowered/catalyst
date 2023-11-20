/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
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

package org.anvilpowered.catalyst.velocity.chat

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.config.CatalystKeys

context(Registry.Scope, ProxyServerScope, LuckpermsService.Scope, LoggerScope)
class CommonTabService {

    suspend fun format(player: Player): Component = registry[CatalystKeys.TAB_FORMAT].resolvePlaceholders(player)
    suspend fun formatHeader(player: Player): Component = registry[CatalystKeys.TAB_HEADER].resolvePlaceholders(player)
    suspend fun formatFooter(player: Player): Component = registry[CatalystKeys.TAB_FOOTER].resolvePlaceholders(player)
}
