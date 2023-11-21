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

package org.anvilpowered.catalyst.velocity.discord

import com.google.inject.Inject
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.permission.Tristate
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.util.Objects

context(JDAService.Scope)
internal class DiscordCommandSource @Inject constructor(
    private val jdaHook: JDAService,
    private val discordCommandService: DiscordCommandService,
) : CommandSource {

    override fun hasPermission(permission: String): Boolean = true
    override fun getPermissionValue(permission: String): Tristate = Tristate.TRUE

    override fun sendMessage(identity: Identity, message: Component, type: MessageType) {
        Objects.requireNonNull(jdaHook.jda.getTextChannelById(discordCommandService.channelId))
            ?.sendMessage("```" + PlainTextComponentSerializer.plainText().serialize(message) + "```")
            ?.queue()
    }
}
