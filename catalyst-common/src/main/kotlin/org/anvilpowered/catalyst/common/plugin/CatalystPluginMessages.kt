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

import com.google.inject.Inject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.util.TimeFormatService
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class CatalystPluginMessages @Inject constructor(
    private val timeFormatService: TimeFormatService,
    private val pluginInfo: CatalystPluginInfo
) : PluginMessages {

    override fun getBroadcast(message: Component): Component {
        return Component.text("[Broadcast] ").append(message).color(NamedTextColor.GREEN)
    }

    override fun getBroadcast(message: String): Component = getBroadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(message))

    override fun getNotEnoughArgs(): Component {
        return Component.text("Not enough arguments!").color(NamedTextColor.RED)
    }

    override fun getNoPermission(): Component {
        return Component.text("Insufficient Permissions!").color(NamedTextColor.RED)
    }

    override fun getNoServerPermission(serverName: String): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("You do not have permission to enter ").color(NamedTextColor.RED))
            .append(Component.text(serverName).color(NamedTextColor.GOLD))
            .build()
    }

    override fun getNoNickColorPermission(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("You do not have permission for a colored nickname!").color(NamedTextColor.RED))
            .build()
    }

    override fun getNoNickMagicPermission(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("You do not have permission for a magical nickname!").color(NamedTextColor.RED))
            .build()
    }

    override fun getCurrentServer(userName: String, serverName: String): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text(userName).color(NamedTextColor.GOLD))
            .append(Component.text(" is connected to ").color(NamedTextColor.GREEN))
            .append(Component.text(serverName).color(NamedTextColor.GOLD))
            .build()
    }

    override fun getMuted(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("You are muted!").color(NamedTextColor.RED))
            .build()
    }

    override fun getMuteExempt(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("This user is exempt from being muted.").color(NamedTextColor.RED))
            .build()
    }

    override fun getBanExempt(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("This user is exempt from being banned.").color(NamedTextColor.RED))
            .build()
    }

    override fun getKickExempt(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("This user is exempt from being muted.").color(NamedTextColor.RED))
            .build()
    }

    override fun getSocialSpy(enabled: Boolean): Component {
        val builder = Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("SocialSpy ").color(NamedTextColor.YELLOW))
        return if (enabled) {
            builder.append(Component.text("enabled").color(NamedTextColor.GREEN)).build()
        } else builder.append(Component.text("disabled").color(NamedTextColor.RED)).build()
    }

    override fun getStaffChat(enabled: Boolean): Component {
        val builder = Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("Staff Chat ").color(NamedTextColor.YELLOW))
        return if (enabled) {
            builder.append(Component.text("enabled").color(NamedTextColor.GREEN)).build()
        } else builder.append(Component.text("disabled").color(NamedTextColor.RED)).build()
    }

    override fun getStaffChatMessageFormatted(userName: String, message: Component): Component {
        return Component.text()
            .append(Component.text("[STAFF] ").color(NamedTextColor.AQUA))
            .append(Component.text("$userName: ").append(message).color(NamedTextColor.LIGHT_PURPLE))
            .build()
    }

    override fun getStaffChatMessageFormattedConsole(message: Component): Component {
        return getStaffChatMessageFormatted("CONSOLE", message)
    }

    override fun getIncompatibleServerVersion(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("The server you are attempting to connect to is running a different Minecraft version!").color(NamedTextColor.RED))
            .build()
    }

    private fun getForList(a: Boolean, b: Boolean, c: Boolean, value: String): Component {
        val _a = if (a) "swear" else "exception"
        val _b = if (b) if (c) "is already in" else "is not in" else if (c) "was added to" else "was removed from"
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("The $_a ").color(NamedTextColor.RED))
            .append(Component.text(value).color(NamedTextColor.YELLOW))
            .append(Component.text(" $_b the $_a list.").color(NamedTextColor.RED))
            .build()
    }

    override fun getExistingSwear(swear: String): Component = getForList(true, true, true, swear)
    override fun getExistingException(exception: String): Component = getForList(false, true, true, exception)
    override fun getMissingSwear(swear: String): Component = getForList(true, true, false, swear)
    override fun getMissingException(exception: String): Component = getForList(false, true, false, exception)
    override fun getNewSwear(swear: String): Component = getForList(true, false, true, swear)
    override fun getNewException(exception: String): Component = getForList(false, false, true, exception)
    override fun getRemoveSwear(swear: String): Component = getForList(true, false, false, swear)
    override fun getRemoveException(exception: String): Component = getForList(false, false, false, exception)

    override fun ignoreExempt(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("This user is exempt from being ignored.").color(NamedTextColor.RED))
            .build()
    }

    override fun offlineOrInvalidPlayer(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("Invalid or offline player!").color(NamedTextColor.YELLOW))
            .build()
    }

    override fun messageSelf(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("You cannot send private messages to yourself!"))
            .build()
    }

    override fun getBanMessage(reason: String, endUtc: Instant): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("You have been banned for: $reason")
                .color(NamedTextColor.RED))
            .append(Component.text("\n\nFor another ${timeFormatService.format(Duration.between(OffsetDateTime.now(ZoneOffset.UTC).toInstant(), endUtc))}")
                .color(NamedTextColor.YELLOW))
            .append(Component.text("\n\nUntil ${timeFormatService.format(endUtc)}"))
            .build()
    }

    override fun getMuteMessage(reason: String, endUtc: Instant): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("You have been muted for: $reason")
                .color(NamedTextColor.RED))
            .append(Component.text("\nFor another ${timeFormatService.format(Duration.between(OffsetDateTime.now(ZoneOffset.UTC).toInstant(), endUtc))}")
                .color(NamedTextColor.YELLOW)
            )
            .build()
    }

    override fun getInvalidServer(): Component {
        return Component.text()
            .append(pluginInfo.prefix)
            .append(Component.text("Invalid Server!").color(NamedTextColor.YELLOW))
            .build()
    }
}

