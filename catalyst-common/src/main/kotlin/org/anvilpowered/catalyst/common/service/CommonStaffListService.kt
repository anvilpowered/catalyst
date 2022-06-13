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
import org.anvilpowered.anvil.api.util.PermissionService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.catalyst.api.registry.CatalystKeys.STAFFLIST_ADMIN_PERMISSION
import org.anvilpowered.catalyst.api.registry.CatalystKeys.STAFFLIST_OWNER_PERMISSION
import org.anvilpowered.catalyst.api.registry.CatalystKeys.STAFFLIST_STAFF_PERMISSION
import org.anvilpowered.catalyst.api.service.StaffListService
import org.anvilpowered.anvil.api.registry.Registry
import java.util.ArrayList
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Singleton
class CommonStaffListService<TPlayer> @Inject constructor(
    private val registry: Registry,
    private val userService: UserService<TPlayer, TPlayer>,
    private val permissionService: PermissionService
) : StaffListService {

  init {
    val executor = Executors.newSingleThreadScheduledExecutor()
    executor.schedule<Runnable>({ update() }, 10, TimeUnit.MINUTES)
  }

  var staffNames: MutableList<Component> = ArrayList()
  var adminNames: MutableList<Component> = ArrayList()
  var ownerNames: MutableList<Component> = ArrayList()

  override fun staffNames(): List<Component> = staffNames
  override fun adminNames(): List<Component> = adminNames
  override fun ownerNames(): List<Component> = ownerNames

  override fun getStaffNames(player: String, adminPermission: Boolean, staffPermission: Boolean, ownerPermission: Boolean) {
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

  private fun update(): Runnable {
    return Runnable {
      CompletableFuture.runAsync {
        for (player in userService.onlinePlayers()) {
          val userName = userService.getUserName(player)
          getStaffNames(
            userName,
            permissionService.hasPermission(player, registry.getOrDefault(STAFFLIST_ADMIN_PERMISSION)),
            permissionService.hasPermission(player, registry.getOrDefault(STAFFLIST_STAFF_PERMISSION)),
            permissionService.hasPermission(player, registry.getOrDefault(STAFFLIST_OWNER_PERMISSION))
          )
        }
      }
    }
  }

  override fun removeStaffNames(player: String) {
    staffNames.remove(Component.text(player))
    adminNames.remove(Component.text(player))
    ownerNames.remove(Component.text(player))
  }
}
