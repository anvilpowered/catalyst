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
package org.anvilpowered.catalyst.common.plugin

import com.google.inject.Singleton
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.anvilpowered.anvil.api.plugin.PluginInfo

@Singleton
class CatalystPluginInfo : PluginInfo {
    override var prefix: Component = Component.text().append(Component.text("[${Companion.name}] ").color(NamedTextColor.GOLD)).build()
    override val id: String = Companion.id
    override val name: String = Companion.name
    override val version: String = Companion.version
    override val description: String get() = Companion.description
    override val url: String get() = Companion.url
    override val authors: Array<String> get() = Companion.authors
    override val organizationName: String get() = Companion.organizationName
    override val buildDate: String get() = Companion.buildDate

    companion object {
        const val id = "catalyst"
        const val name = "Catalyst"
        const val version = "{modVersion}"
        const val description = "An essentials plugin for velocity"
        const val url = "https://github.com/AnvilPowered/Catalyst"
        val authors = arrayOf("STG_Allen", "Cableguy20")
        const val organizationName = "AnvilPowered"
        const val buildDate = "\$buildDate"
    }
}
