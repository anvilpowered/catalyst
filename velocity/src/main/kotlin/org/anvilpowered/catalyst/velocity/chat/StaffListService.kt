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

import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.config.CatalystKeys
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class StaffListService(
    private val proxyServer: ProxyServer,
    private val registry: Registry,
    private val catalystKeys: CatalystKeys,
) {

    init {
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.schedule<Unit>({ update() }, 10, TimeUnit.MINUTES)
    }

    private var staffNames: MutableList<Component> = mutableListOf()
    private var adminNames: MutableList<Component> = mutableListOf()
    private var ownerNames: MutableList<Component> = mutableListOf()

    fun staffNames(): List<Component> = staffNames
    fun adminNames(): List<Component> = adminNames
    fun ownerNames(): List<Component> = ownerNames

    fun getStaffNames(player: String, adminPermission: Boolean, staffPermission: Boolean, ownerPermission: Boolean) {
        if (ownerPermission) {
            ownerNames.add(Component.text(player))
        } else if (staffPermission) {
            if (adminPermission) {
                adminNames.add(Component.text(player))
            } else {
                staffNames.add(Component.text(player))
            }
        }
    }

    private fun update() {
        for (player in proxyServer.allPlayers) {
            getStaffNames(
                player.username,
                player.hasPermission(registry[catalystKeys.STAFFLIST_ADMIN_PERMISSION]),
                player.hasPermission(registry[catalystKeys.STAFFLIST_STAFF_PERMISSION]),
                player.hasPermission(registry[catalystKeys.STAFFLIST_OWNER_PERMISSION]),
            )
        }
    }

    fun removeStaffNames(player: String) {
        staffNames.remove(Component.text(player))
        adminNames.remove(Component.text(player))
        ownerNames.remove(Component.text(player))
    }

    interface Scope {
        val staffListService: StaffListService
    }
}
