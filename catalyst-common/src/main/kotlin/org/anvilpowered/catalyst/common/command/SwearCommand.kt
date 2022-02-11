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
package org.anvilpowered.catalyst.common.command

import com.google.inject.Inject
import com.mojang.brigadier.context.CommandContext
import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.registry.Registry

class SwearCommand<TCommandSource> @Inject constructor(
    private val registry: Registry
) {
    fun execute(context: CommandContext<TCommandSource>): Int {
        Component.text(java.lang.String.join(", ", registry.getOrDefault(CatalystKeys.CHAT_FILTER_SWEARS))).sendTo(context.source)
        return 1
    }
}
