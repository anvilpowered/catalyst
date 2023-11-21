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

package org.anvilpowered.catalyst.velocity.tab

import com.velocitypowered.api.proxy.player.TabList
import com.velocitypowered.api.proxy.player.TabListEntry
import com.velocitypowered.api.util.GameProfile
import kotlinx.coroutines.runBlocking
import org.anvilpowered.anvil.core.LoggerScope
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.velocity.ProxyServerScope
import org.anvilpowered.anvil.velocity.platform.PluginContainerScope
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.config.CatalystKeys
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

context(Registry.Scope, PluginContainerScope, ProxyServerScope, LoggerScope, LuckpermsService.Scope)
class GlobalTab {

    private fun registryLoaded() {
        if (registry[CatalystKeys.TAB_ENABLED]) {
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
        proxyServer.scheduler.buildTask(pluginContainer, Consumer { runBlocking { action() } }).repeat(10, TimeUnit.SECONDS).schedule()
    }

    private suspend fun action() {
        val playerCount = proxyServer.playerCount
        if (playerCount == 0) {
            return
        }
        for (displayPlayer in proxyServer.allPlayers) {
            for (entry in displayPlayer.tabList.entries) {
                displayPlayer.tabList.removeEntry(entry.profile.id)
            }
            for (processPlayer in proxyServer.allPlayers) {
                val currentEntry = TabListEntry.builder()
                    .profile(GameProfile(processPlayer.uniqueId, processPlayer.username, processPlayer.gameProfileProperties))
                    .displayName(registry[CatalystKeys.TAB_FORMAT].resolvePlaceholders(processPlayer))
                    .tabList(displayPlayer.tabList)
                    .build()
                insertIntoTab(displayPlayer.tabList, currentEntry)
            }
            var x = 0
            for (customFormat in registry[CatalystKeys.TAB_FORMAT_CUSTOM]) {
                x++
                val tabProfile = GameProfile.forOfflinePlayer("catalyst$x")
                val currentEntry = TabListEntry.builder()
                    .profile(tabProfile)
                    .displayName(
                        customFormat.resolvePlaceholders(displayPlayer),
                    )
                    .tabList(displayPlayer.tabList)
                    .build()
                insertIntoTab(displayPlayer.tabList, currentEntry)
            }
            displayPlayer.sendPlayerListHeaderAndFooter(
                registry[CatalystKeys.TAB_HEADER].resolvePlaceholders(displayPlayer),
                registry[CatalystKeys.TAB_FOOTER].resolvePlaceholders(displayPlayer),
            )
        }
    }
}
