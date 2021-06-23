/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
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

package org.anvilpowered.catalyst.velocity.service

import com.google.inject.Inject
import com.google.inject.Singleton
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.player.TabList
import com.velocitypowered.api.proxy.player.TabListEntry
import com.velocitypowered.api.util.GameProfile
import net.kyori.adventure.text.TextComponent
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.catalyst.api.registry.CatalystKeys
import org.anvilpowered.catalyst.api.service.LuckpermsService
import org.anvilpowered.catalyst.api.service.TabService
import java.util.ArrayList
import java.util.HashMap
import java.util.UUID
import java.util.concurrent.TimeUnit

@Singleton
class GlobalTab @Inject constructor(
  private val registry: Registry,
  private val pluginContainer: PluginContainer,
  private val proxyServer: ProxyServer,
  private val tabService: TabService<TextComponent, Player>,
  private val luckpermsService: LuckpermsService
) {

  init {
    registry.whenLoaded { registryLoaded() }.register()
  }

  private fun registryLoaded() {
    if (registry.getOrDefault(CatalystKeys.TAB_ENABLED)) {
      schedule()
    }
  }

  private fun insertIntoTab(list: TabList, entry: TabListEntry) {
    val inUUID = entry.profile.id
    val contained: MutableList<UUID> = ArrayList()
    val cache: MutableMap<UUID, TabListEntry> = HashMap()
    for (e in list.entries) {
      contained.add(e.profile.id)
      cache[e.profile.id] = e
    }
    if (!contained.contains(inUUID)) {
      list.addEntry(entry)
    } else {
      val tabListEntry = cache[inUUID]
      if (tabListEntry?.displayNameComponent != entry.displayNameComponent) {
        list.removeEntry(inUUID)
        list.addEntry(entry)
      }
    }
  }

  private fun schedule() {
    proxyServer.scheduler.buildTask(pluginContainer) {
      val playerCount = proxyServer.playerCount
      if (playerCount == 0) {
        return@buildTask
      }
      val groupOrder = registry.getOrDefault(CatalystKeys.TAB_GROUP_ORDER)
      for (currentPlayerToProcess in proxyServer.allPlayers) {
        for (process in proxyServer.allPlayers) {
          var tempName = process.username
          if (registry.getOrDefault(CatalystKeys.TAB_ORDER).equals("group", ignoreCase = true)) {
            for (group in groupOrder) {
              if (luckpermsService.getGroupName(process).equals(group, ignoreCase = true)) {
                tempName = groupOrder.indexOf(group).toString()
              }
            }
          }
          val currentEntry = TabListEntry.builder()
            .profile(GameProfile(process.uniqueId, tempName, process.gameProfileProperties))
            .displayName(tabService.format(process, process.ping.toInt(), playerCount))
            .tabList(currentPlayerToProcess.tabList)
            .build()
          insertIntoTab(currentPlayerToProcess.tabList, currentEntry)
        }
        var x = 0
        for (custom in registry.getOrDefault(CatalystKeys.TAB_FORMAT_CUSTOM)) {
          x++
          val tabProfile = GameProfile.forOfflinePlayer("catalyst$x")
          val currentEntry = TabListEntry.builder()
            .profile(tabProfile)
            .displayName(
              tabService.formatCustom(
                custom,
                currentPlayerToProcess,
                currentPlayerToProcess.ping.toInt(),
                playerCount
              )
            )
            .tabList(currentPlayerToProcess.tabList)
            .build()
          insertIntoTab(currentPlayerToProcess.tabList, currentEntry)
        }
        currentPlayerToProcess.sendPlayerListHeaderAndFooter(
          tabService.formatHeader(
            currentPlayerToProcess,
            currentPlayerToProcess.ping.toInt(),
            playerCount
          ),
          tabService.formatFooter(
            currentPlayerToProcess,
            currentPlayerToProcess.ping.toInt(),
            playerCount
          )
        )
      }
    }.repeat(10, TimeUnit.SECONDS).schedule()
  }
}
