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
package org.anvilpowered.catalyst.api.member

import net.kyori.adventure.text.Component
import org.anvilpowered.anvil.api.datastore.Manager
import java.util.concurrent.CompletableFuture

interface MemberManager : Manager<MemberRepository<*, *>> {
    fun info(userName: String, isActive: Boolean, permissions: BooleanArray): CompletableFuture<Component>
    fun setNickName(userName: String, nickName: String): CompletableFuture<Component>
    fun setNickNameForUser(userName: String, nickName: String): CompletableFuture<Component>
    fun deleteNickName(userName: String): CompletableFuture<Component>
    fun deleteNickNameForUser(userName: String): CompletableFuture<Component>
    fun ban(userName: String, reason: String): CompletableFuture<Component>
    fun ban(userName: String): CompletableFuture<Component>
    fun tempBan(userName: String, duration: String, reason: String): CompletableFuture<Component>
    fun tempBan(userName: String, duration: String): CompletableFuture<Component>
    fun unBan(userName: String): CompletableFuture<Component>
    fun mute(userName: String, reason: String): CompletableFuture<Component>
    fun mute(userName: String): CompletableFuture<Component>
    fun tempMute(userName: String, duration: String, reason: String): CompletableFuture<Component>
    fun tempMute(userName: String, duration: String): CompletableFuture<Component>
    fun unMute(userName: String): CompletableFuture<Component>
}
