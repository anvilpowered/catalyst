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

package org.anvilpowered.catalyst.api.service

import net.kyori.adventure.text.Component
import org.anvilpowered.catalyst.api.builder.PrivateMessageBuilderImpl
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface PrivateMessageService {

    var socialSpySet: MutableSet<UUID>
    var replyMap: MutableMap<UUID, UUID>

    fun sendMessage(source: String, recipient: String, rawMessage: String): CompletableFuture<Void>
    fun sendMessageFromConsole(recipient: String, rawMessage: String): CompletableFuture<Void>
    fun socialSpy(source: String, recipient: String, component: Component): CompletableFuture<Void>

    class Message(
        val sourceMessage: Component,
        val recipientMessage: Component,
        val socialSpyMessage: Component
        ) {

        interface Builder {
            fun source(source: String): Builder
            fun recipient(recipient: String): Builder
            fun rawMessage(rawMessage: String): Builder
            fun build(): Message
        }

        companion object {
            fun builder(): Builder {
                return PrivateMessageBuilderImpl()
            }
        }
    }
}
