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

package org.anvilpowered.catalyst.common.model.mongo

import dev.morphia.annotations.Entity
import org.anvilpowered.anvil.base.model.MongoDbo
import org.anvilpowered.catalyst.api.model.Member
import org.bson.types.ObjectId
import java.time.Instant
import java.util.UUID

@Entity("members")
open class MongoMember : MongoDbo(), Member<ObjectId> {
    override var userUUID: UUID = UUID.randomUUID()
    override var userName: String = ""
    override var ipAddress: String = ""
    override var lastJoinedUtc: Instant = Instant.now()
    override var nickName: String = ""
    override var isBanned = false
    override var isMuted = false
    override var banEndUtc: Instant = Instant.now()
    override var muteEndUtc: Instant = Instant.now()
    override var banReason: String = ""
    override var muteReason: String = ""
}
