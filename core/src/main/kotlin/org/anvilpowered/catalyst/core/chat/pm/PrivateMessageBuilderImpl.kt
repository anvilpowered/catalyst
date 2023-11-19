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

package org.anvilpowered.catalyst.core.chat.pm

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.user.Player
import org.anvilpowered.catalyst.api.config.CatalystKeys

context(Registry.Scope)
internal class PrivateMessageBuilderImpl : PrivateMessage.Builder {

    private var source: Player? = null
    private var recipient: Player? = null
    private var message: Component? = null

    override fun source(source: Player): PrivateMessage.Builder {
        this.source = source
        return this
    }

    override fun recipient(recipient: Player): PrivateMessage.Builder {
        this.recipient = recipient
        return this
    }

    override fun message(message: Component): PrivateMessage.Builder {
        this.message = message
        return this
    }

    private fun formatMessage(source: Player, recipient: Player, message: Component): Component {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(
            registry[CatalystKeys.PRIVATE_MESSAGE_FORMAT]
                .replace("%sender%", source)
                .replace("%recipient%", recipient)
                .replace("%message%", message.trim { it <= ' ' }),
        )
    }

    private fun formatSocialSpy(source: String, recipient: String, rawMessage: String): Component {
        return Component.text()
            .append(Component.text("[SocialSpy] ").color(NamedTextColor.GRAY))
            .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
            .append(Component.text(source).color(NamedTextColor.BLUE))
            .append(Component.text(" -> ").color(NamedTextColor.GOLD))
            .append(Component.text(recipient).color(NamedTextColor.BLUE))
            .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
            .append(Component.text(rawMessage.trim { it <= ' ' }).color(NamedTextColor.GRAY))
            .build()
    }

    override fun build(): PrivateMessage {
        return PrivateMessage(
            formatMessage("Me", recipient, message),
            formatMessage(source, "Me", message),
            formatSocialSpy(source, recipient, message),
        )
    }
}
