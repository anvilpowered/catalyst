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

package org.anvilpowered.catalyst.common.model.xodus

import jetbrains.exodus.entitystore.Entity
import jetbrains.exodus.entitystore.EntityId
import org.anvilpowered.anvil.api.datastore.XodusEntity
import org.anvilpowered.anvil.base.model.XodusDbo
import org.anvilpowered.catalyst.api.model.Member
import java.time.Instant
import java.util.UUID

@XodusEntity
open class XodusMember: XodusDbo(), Member<EntityId> {

    override var userUUID: UUID = UUID.randomUUID()
    override var userName: String = ""
    override var ipAddress: String = ""
    private var lastJoinedUtcSeconds: Long = 0
    private var lastJoinedUtcNanos = 0
    override var nickName: String = ""
    override var isBanned = false
    override var isMuted = false
    private var banEndUtcSeconds: Long = 0
    private var banEndUtcNanos = 0
    private var muteEndUtcSeconds: Long = 0
    private var muteEndUtcNanos = 0
    override var banReason: String = ""
    override var muteReason: String = ""

    override var lastJoinedUtc: Instant
        get() = Instant.ofEpochSecond(lastJoinedUtcSeconds, lastJoinedUtcNanos.toLong())
        set(lastJoinedUtc) {
            lastJoinedUtcSeconds = lastJoinedUtc.epochSecond
            lastJoinedUtcNanos = lastJoinedUtc.nano
        }
    override var banEndUtc: Instant
        get() = Instant.ofEpochSecond(banEndUtcSeconds, banEndUtcNanos.toLong())
        set(banEndUtc) {
            banEndUtcSeconds = banEndUtc.epochSecond
            banEndUtcNanos = banEndUtc.nano
        }
    override var muteEndUtc: Instant
        get() = Instant.ofEpochSecond(muteEndUtcSeconds, muteEndUtcNanos.toLong())
        set(muteEndUtc) {
            muteEndUtcSeconds = muteEndUtc.epochSecond
            muteEndUtcNanos = muteEndUtc.nano
        }

    override fun writeTo(entity: Entity): Entity {
        super.writeTo(entity)
        if (userUUID != null) {
            entity.setProperty("userUUID", userUUID.toString())
        }
        if (userName != null) {
            entity.setProperty("userName", userName)
        }
        if (ipAddress != null) {
            entity.setProperty("ipAddress", ipAddress)
        }
        entity.setProperty("lastJoinedUtcSeconds", lastJoinedUtcSeconds)
        entity.setProperty("lastJoinedUtcNanos", lastJoinedUtcNanos)
        if (nickName != null) {
            entity.setProperty("nickname", nickName)
        }
        entity.setProperty("banned", isBanned)
        entity.setProperty("muted", isMuted)
        entity.setProperty("banEndUtcSeconds", banEndUtcSeconds)
        entity.setProperty("banEndUtcNanos", banEndUtcNanos)
        entity.setProperty("muteEndUtcSeconds", muteEndUtcSeconds)
        entity.setProperty("muteEndUtcNanos", muteEndUtcNanos)
        if (banReason != null) {
            entity.setProperty("banReason", banReason!!)
        }
        if (muteReason != null) {
            entity.setProperty("muteReason", muteReason!!)
        }
        return entity
    }

    override fun readFrom(entity: Entity) {
        super.readFrom(entity)
        val userUUID = entity.getProperty("userUUID")
        if (userUUID is String) {
            this.userUUID = UUID.fromString(userUUID)
        }
        val userName = entity.getProperty("userName")
        if (userName is String) {
            this.userName = userName
        }
        val ipAddress = entity.getProperty("ipAddress")
        if (ipAddress is String) {
            this.ipAddress = ipAddress
        }
        val lastJoinedUtcSeconds = entity.getProperty("lastJoinedUtcSeconds")
        if (lastJoinedUtcSeconds is Long) {
            this.lastJoinedUtcSeconds = lastJoinedUtcSeconds
        }
        val lastJoinedUtcNanos = entity.getProperty("lastJoinedUtcNanos")
        if (lastJoinedUtcNanos is Int) {
            this.lastJoinedUtcNanos = lastJoinedUtcNanos
        }
        val nickName = entity.getProperty("nickName")
        if (nickName is String) {
            this.nickName = nickName
        }
        val banned = entity.getProperty("banned")
        if (banned is Boolean) {
            isBanned = banned
        }
        val muted = entity.getProperty("muted")
        if (muted is Boolean) {
            isMuted = muted
        }
        val banEndUtcSeconds = entity.getProperty("banEndUtcSeconds")
        if (banEndUtcSeconds is Long) {
            this.banEndUtcSeconds = banEndUtcSeconds
        }
        val banEndUtcNanos = entity.getProperty("banEndUtcNanos")
        if (banEndUtcNanos is Int) {
            this.banEndUtcNanos = banEndUtcNanos
        }
        val muteEndUtcSeconds = entity.getProperty("muteEndUtcSeconds")
        if (muteEndUtcSeconds is Long) {
            this.muteEndUtcSeconds = muteEndUtcSeconds
        }
        val muteEndUtcNanos = entity.getProperty("muteEndUtcNanos")
        if (muteEndUtcNanos is Int) {
            this.muteEndUtcNanos = muteEndUtcNanos
        }
        val banReason = entity.getProperty("banReason")
        if (banReason is String) {
            this.banReason = banReason
        }
        val muteReason = entity.getProperty("muteReason")
        if (muteReason is String) {
            this.muteReason = muteReason
        }
    }
}