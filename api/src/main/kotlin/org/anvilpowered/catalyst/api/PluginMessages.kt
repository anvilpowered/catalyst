/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
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

package org.anvilpowered.catalyst.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.time.Instant

object PluginMessages {
    val pluginPrefix = Component.text("[Catalyst] ").color(NamedTextColor.GOLD)
    val notEnoughArgs = Component.text("Not enough arguments!").color(NamedTextColor.RED)
    val noPermission = Component.text("Insufficient Permissions!").color(NamedTextColor.RED)
    fun noServerPermission(serverName: String) = Component.text()
        .append(pluginPrefix)
        .append(Component.text("You do not have permission to enter ").color(NamedTextColor.RED))
        .append(Component.text(serverName).color(NamedTextColor.GOLD))
        .build()

    val noNickColorPermission = Component.text()
        .append(pluginPrefix)
        .append(Component.text("You do not have permission for a colored nickname!").color(NamedTextColor.RED))
        .build()
    val noNickMagicPermission = Component.text()
        .append(pluginPrefix)
        .append(Component.text("You do not have permission for a magical nickname!").color(NamedTextColor.RED))
        .build()
    val muted = Component.text()
        .append(pluginPrefix)
        .append(Component.text("You are muted!").color(NamedTextColor.RED))
        .build()
    val muteExempt = Component.text()
        .append(pluginPrefix)
        .append(Component.text("This user is exempt from being muted!").color(NamedTextColor.RED))
        .build()
    val ignoreExempt = Component.text()
        .append(pluginPrefix)
        .append(Component.text("This user is exempt from being ignored!").color(NamedTextColor.RED))
        .build()
    val banExempt = Component.text()
        .append(pluginPrefix)
        .append(Component.text("This user is exempt from being banned!").color(NamedTextColor.RED))
        .build()
    val kickExempt = Component.text()
        .append(pluginPrefix)
        .append(Component.text("This user is exempt from being kicked!").color(NamedTextColor.RED))
        .build()
    val incompatibleServerVersion = Component.text()
        .append(pluginPrefix)
        .append(Component.text("This server is running an incompatible version of Minecraft!").color(NamedTextColor.RED))
        .build()
    val invalidPlayer = Component.text()
        .append(pluginPrefix)
        .append(Component.text("Invalid or offline player!").color(NamedTextColor.RED))
        .build()
    val invalidServer = Component.text()
        .append(pluginPrefix)
        .append(Component.text("Invalid server!").color(NamedTextColor.RED))
        .build()
    val messageSelf = Component.text()
        .append(pluginPrefix)
        .append(Component.text("You can't message yourself!").color(NamedTextColor.RED))
        .build()

    fun getBroadcast(message: Component) = Component.text("[Broadcast] ").append(message).color(NamedTextColor.GREEN)

    fun getCurrentServer(userName: String, serverName: String) = Component.text()
        .append(pluginPrefix)
        .append(Component.text(userName).color(NamedTextColor.GOLD))
        .append(Component.text(" is connected to ").color(NamedTextColor.GREEN))
        .append(Component.text(serverName).color(NamedTextColor.GOLD))
        .build()

    private fun getEnabledDisabledText(enabled: Boolean) = if (enabled) {
        Component.text("enabled").color(NamedTextColor.GREEN)
    } else {
        Component.text("disabled").color(NamedTextColor.RED)
    }

    fun getSocialSpy(enabled: Boolean) = Component.text()
        .append(pluginPrefix)
        .append(Component.text("SocialSpy is now ").color(NamedTextColor.GREEN))
        .append(getEnabledDisabledText(enabled))
        .build()

    fun getStaffChat(enabled: Boolean) = Component.text()
        .append(pluginPrefix)
        .append(Component.text("StaffChat is now ").color(NamedTextColor.GREEN))
        .append(getEnabledDisabledText(enabled)).build()

    fun getStaffChatMessageFormatted(message: Component, sender: String = "CONSOLE") = Component.text()
        .append(pluginPrefix)
        .append(Component.text("[STAFF]: ").color(NamedTextColor.AQUA))
        .append(Component.text("$sender: ").append(message).color(NamedTextColor.LIGHT_PURPLE))
        .build()

    fun getBanMessage(reason: String, endUtc: Instant) = Component.text()
        .append(pluginPrefix)
        .append(Component.text("You have been banned for ").color(NamedTextColor.RED))
        .append(Component.text(reason).color(NamedTextColor.GOLD))
        .append(Component.text(" until ").color(NamedTextColor.RED))
        .append(Component.text(endUtc.toString()).color(NamedTextColor.GOLD))
        .build()
}
