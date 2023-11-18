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

package org.anvilpowered.catalyst.core.chat.builder

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.core.chat.PrivateMessageService

context(Registry.Scope)
internal class PrivateMessageBuilderImpl : PrivateMessageService.Message.Builder {

    private var source: String = ""
    private var recipient: String = ""
    private var rawMessage: String = ""

    override fun source(source: String): PrivateMessageService.Message.Builder {
        this.source = source
        return this
    }

    override fun recipient(recipient: String): PrivateMessageService.Message.Builder {
        this.recipient = recipient
        return this
    }

    override fun rawMessage(rawMessage: String): PrivateMessageService.Message.Builder {
        this.rawMessage = rawMessage
        return this
    }

    private fun formatMessage(source: String, recipient: String, rawMessage: String): Component {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(
            registry[CatalystKeys.PRIVATE_MESSAGE_FORMAT]
                .replace("%sender%", source)
                .replace("%recipient%", recipient)
                .replace("%message%", rawMessage.trim { it <= ' ' }),
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

    override fun build(): PrivateMessageService.Message {
        return PrivateMessageService.Message(
            formatMessage("Me", recipient, rawMessage),
            formatMessage(source, "Me", rawMessage),
            formatSocialSpy(source, recipient, rawMessage),
        )
    }
}
