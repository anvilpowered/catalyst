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

package org.anvilpowered.catalyst.common.member

import com.google.inject.Inject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.api.Anvil
import org.anvilpowered.anvil.api.misc.appendIf
import org.anvilpowered.anvil.api.misc.sendTo
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.KickService
import org.anvilpowered.anvil.api.util.TimeFormatService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.anvil.base.datastore.BaseManager
import org.anvilpowered.catalyst.api.Catalyst
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.member.MemberRepository
import org.anvilpowered.catalyst.api.registry.CatalystKeys.NICKNAME_PREFIX
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.common.plugin.CatalystPluginMessages
import org.anvilpowered.anvil.api.registry.Registry
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.CompletableFuture

class CommonMemberManager<TPlayer> @Inject constructor(
    registry: Registry,
    private val locationService: LocationService,
    private val kickService: KickService,
    private val pluginInfo: PluginInfo,
    private val pluginMessages: CatalystPluginMessages,
    private val timeFormatService: TimeFormatService,
    private val userService: UserService<TPlayer, TPlayer>,
    private val channelService: ChannelService<TPlayer>
) : BaseManager<MemberRepository<*, *>>(registry), MemberManager {

    override fun info(
        userName: String, isOnline: Boolean,
        permissions: BooleanArray
    ): CompletableFuture<Component> {
        return primaryComponent.getOneForUser(userName).thenApplyAsync { member ->
            if (member == null) {
                return@thenApplyAsync Component.text("Could not get user data").color(NamedTextColor.RED)
            }
            val nick: String = if (member.nickName != "") {
                member.nickName
            } else {
                "No Nickname."
            }
            val lastSeen: String = if (isOnline) {
                "Currently Online."
            } else {
                timeFormatService.format(member.lastJoinedUtc).toString()
            }
            val banReason: String = if (member.isBanned) {
                member.banReason
            } else {
                "This user is not banned."
            }
            return@thenApplyAsync Component.text()
                .append(Component.text("----------------Player Info----------------").color(NamedTextColor.BLUE))
                .append(Component.text("\nUUID: ").color(NamedTextColor.BLUE))
                .append(Component.text(member.userUUID.toString()).color(NamedTextColor.GREEN))
                .append(Component.text("\nUsername: ").color(NamedTextColor.BLUE))
                .append(Component.text(member.userName).color(NamedTextColor.GREEN))
                .append(Component.text("\nNickName: ").color(NamedTextColor.BLUE))
                .append(LegacyComponentSerializer.legacyAmpersand().deserialize(nick).color(NamedTextColor.GREEN))
                .appendIf(
                    permissions[0], Component.text()
                        .append(Component.text("\nIP: ").color(NamedTextColor.BLUE))
                        .append(Component.text(member.ipAddress).color(NamedTextColor.GREEN))
                        .build()
                )
                .append(Component.text("\nJoined Date: ").color(NamedTextColor.BLUE))
                .append(Component.text(member.createdUtc.toString()).color(NamedTextColor.GREEN))
                .append(Component.text("\nLast Seen: ").color(NamedTextColor.BLUE))
                .append(Component.text(lastSeen).color(NamedTextColor.GREEN))
                .appendIf(
                    permissions[1], Component.text()
                        .append(Component.text("\nBanned: ").color(NamedTextColor.BLUE))
                        .append(Component.text(banReason).color(NamedTextColor.GREEN))
                        .build()
                )
                .appendIf(
                    permissions[2], Component.text()
                        .append(Component.text("\nChannel: ").color(NamedTextColor.BLUE))
                        .append(Component.text(channelService.getChannelIdForUser(member.userUUID)).color(NamedTextColor.GREEN))
                        .build()
                )
                .append(Component.text("\nCurrent Server: ").color(NamedTextColor.BLUE))
                .append(Component.text(locationService.getServer(member.userUUID)?.name ?: "Offline").color(NamedTextColor.GREEN))
                .build() as Component
        }.exceptionally { e: Throwable ->
            e.printStackTrace()
            Component.text("")
        }
    }

    override fun setNickName(userName: String, nickName: String): CompletableFuture<Component> {
        return primaryComponent.setNickNameForUser(userName, registry.getOrDefault(NICKNAME_PREFIX).toString() + nickName)
            .thenApplyAsync { result: Boolean ->
                if (result) {
                    Component.text("Set nickname to $nickName").color(NamedTextColor.GREEN)
                } else {
                    Component.text("Failed to set the nickname $nickName").color(NamedTextColor.RED)
                }
            }
    }

    override fun setNickNameForUser(userName: String, nickName: String): CompletableFuture<Component> {

        return primaryComponent.setNickNameForUser(userName, registry.getOrDefault(NICKNAME_PREFIX).toString() + nickName)
            .thenApplyAsync { result: Boolean ->
                if (result) {
                    userService.getPlayer(userName)
                        ?.also { Component.text("Your nickname was set to $nickName").color(NamedTextColor.GREEN).sendTo(it) }
                    Component.text("Set $userName's nickname to $nickName").color(NamedTextColor.GREEN)
                } else {
                    Component.text("Failed to set the nickname for $nickName").color(NamedTextColor.RED)
                }
            }
    }

    override fun deleteNickNameForUser(userName: String): CompletableFuture<Component> {
        return primaryComponent.deleteNickNameForUser(userName).thenApplyAsync { result: Boolean ->
            if (result) {
                userService.getPlayer(userName)?.also { Component.text("Your nickname was deleted").color(NamedTextColor.GREEN).sendTo(it) }
                Component.text("Successfully deleted $userName's nickname.")
            } else {
                Component.text("Failed to delete $userName's nickname.")
            }
        }
    }

    override fun deleteNickName(userName: String): CompletableFuture<Component> {
        return primaryComponent.deleteNickNameForUser(userName).thenApplyAsync {
            if (it) {
                Component.text("Successfully deleted your nickname").color(NamedTextColor.GREEN)
            } else {
                Component.text("Failed to delete your nickname.").color(NamedTextColor.RED)
            }
        }
    }

    override fun ban(userName: String, reason: String): CompletableFuture<Component> {
        val endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(Duration.ofDays(3600))
        return primaryComponent.banUser(userName, endUtc, reason).thenApplyAsync {
            if (it) {
                kickService.kick(userName, pluginMessages.getBanMessage(reason, endUtc))
                Component.text("Banned $userName for $reason").color(NamedTextColor.GREEN)
            }
            Component.text("Invalid user.").color(NamedTextColor.RED)
        }
    }

    override fun ban(userName: String): CompletableFuture<Component> = ban(userName, "The ban hammer has spoken.")
    override fun tempBan(userName: String, duration: String): CompletableFuture<Component> = tempBan(userName, duration, "The ban hammer has spoken.")

    override fun tempBan(userName: String, duration: String, reason: String): CompletableFuture<Component> {
        val formatted = timeFormatService.parseDuration(duration)
            ?: return CompletableFuture.completedFuture(Component.text("Invalid input for duration").color(NamedTextColor.RED))

        val endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(formatted)
        return primaryComponent.banUser(userName, endUtc, reason).thenApplyAsync { b: Boolean ->
            if (b) {
                kickService.kick(userName, pluginMessages.getBanMessage(reason, endUtc))
                Component.text("Banned $userName for $reason for ${timeFormatService.format(formatted).toString()}")
            }
            Component.text("Invalid user.").color(NamedTextColor.RED)
        }
    }

    override fun unBan(userName: String): CompletableFuture<Component> {
        return primaryComponent.unBanUser(userName).thenApplyAsync {
            if (it) {
                Component.text("Unbanned $userName").color(NamedTextColor.GREEN)
            }
            Component.text("Invalid user.").color(NamedTextColor.RED)
        }
    }

    override fun mute(userName: String): CompletableFuture<Component> = mute(userName, "You have been muted.")

    override fun mute(userName: String, reason: String): CompletableFuture<Component> {
        val endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(Duration.ofDays(3600))
        return primaryComponent.muteUser(userName, endUtc, reason).thenApplyAsync {
            if (it) {
                userService.getPlayer(userName)?.also { user -> pluginMessages.getMuteMessage(reason, endUtc).sendTo(user) }
                Component.text("Muted $userName").color(NamedTextColor.GREEN)
            }
            Component.text("Invalid user.").color(NamedTextColor.RED)
        }
    }

    override fun tempMute(userName: String, duration: String, reason: String): CompletableFuture<Component> {
        val formatted = timeFormatService.parseDuration(duration)
            ?: return CompletableFuture.completedFuture(Component.text("Invalid input for duration").color(NamedTextColor.RED))
        val endUtc = OffsetDateTime.now(ZoneOffset.UTC).toInstant().plus(formatted)
        return primaryComponent.muteUser(userName, endUtc, reason).thenApplyAsync { b: Boolean ->
            if (b) {
                userService.getPlayer(userName)?.also { pluginMessages.getMuteMessage(reason, endUtc).sendTo(it) }
                Component.text("Muted $userName for $reason for ${timeFormatService.format(formatted).toString()}")
            }
            Component.text("Invalid user.").color(NamedTextColor.RED)
        }
    }

    override fun tempMute(userName: String, duration: String): CompletableFuture<Component> = tempMute(userName, duration, "You have been muted.")

    override fun unMute(userName: String): CompletableFuture<Component> {
        return primaryComponent.unMuteUser(userName).thenApplyAsync { b: Boolean ->
            if (b) {
                userService.getPlayer(userName)?.also {
                    Component.text()
                        .append(pluginInfo.prefix)
                        .append(Component.text("You have been muted").color(NamedTextColor.YELLOW))
                        .sendTo(it)
                }
                Component.text("UnMuted $userName").color(NamedTextColor.GREEN)
            }
            Component.text("Invalid user.").color(NamedTextColor.RED)
        }
    }
}
