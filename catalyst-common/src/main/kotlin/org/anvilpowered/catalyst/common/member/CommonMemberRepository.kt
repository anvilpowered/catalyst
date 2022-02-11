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

import org.anvilpowered.anvil.base.datastore.BaseRepository
import org.anvilpowered.catalyst.api.member.MemberRepository
import org.anvilpowered.catalyst.api.model.Member
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID
import java.util.concurrent.CompletableFuture

abstract class CommonMemberRepository<TKey, TDataStore> : BaseRepository<TKey, Member<TKey>, TDataStore>(), MemberRepository<TKey, TDataStore> {

    override val tClass: Class<Member<TKey>> by lazy { dataStoreContext.getEntityClassUnsafe("member") as Class<Member<TKey>> }

    override fun getOneOrGenerateForUser(userUUID: UUID, userName: String, ipAddress: String): CompletableFuture<Member<TKey>?> {
        return getOneOrGenerateForUser(userUUID, userName, ipAddress, booleanArrayOf(false, false, false, false, false, false, false, false))
    }

    override fun checkBanned(member: Member<*>): Boolean {
        if (member.isBanned && member.banEndUtc.isAfter(OffsetDateTime.now(ZoneOffset.UTC).toInstant())) {
            return true
        } else if (member.isBanned) {
            unBanUser(member.userUUID)
        }
        return false
    }

    override fun checkBanned(id: TKey): CompletableFuture<Boolean> = getOne(id).thenApplyAsync {
        checkBanned(it ?: return@thenApplyAsync false)
    }

    override fun checkBannedForUser(userUUID: UUID): CompletableFuture<Boolean> {
        return getOneForUser(userUUID).thenApply {
            if (it == null) {
                return@thenApply false
            }
            checkBanned(it)
        }
    }

    override fun checkBannedForUser(userName: String): CompletableFuture<Boolean> {
        return getOneForUser(userName).thenApply {
            if (it == null) {
                return@thenApply false
            }
            checkBanned(it)
        }
    }

    override fun checkMuted(member: Member<*>): Boolean {
        if (member.isMuted && member.muteEndUtc.isAfter(OffsetDateTime.now(ZoneOffset.UTC).toInstant())) {
            return true
        } else if (member.isMuted) {
            unMuteUser(member.userUUID)
        }
        return false
    }

    override fun checkMuted(id: TKey): CompletableFuture<Boolean> = getOne(id).thenApplyAsync {
        checkMuted(it ?: return@thenApplyAsync false)
    }

    override fun checkMutedForUser(userUUID: UUID): CompletableFuture<Boolean> {
        return getOneForUser(userUUID).thenApplyAsync {
            if (it == null) {
                return@thenApplyAsync false
            }
            checkMuted(it)
        }
    }

    override fun checkMutedForUser(userName: String): CompletableFuture<Boolean> {
        return getOneForUser(userName).thenApplyAsync {
            if (it == null) {
                return@thenApplyAsync false
            }
            checkMuted(it)
        }
    }
}
