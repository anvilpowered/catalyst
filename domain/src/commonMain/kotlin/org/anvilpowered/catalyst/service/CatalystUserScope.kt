/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.service

import org.anvilpowered.catalyst.entity.CatalystUser
import org.sourcegrade.kontour.DomainEntity
import org.sourcegrade.kontour.UUID
import org.sourcegrade.kontour.scope.CrudScope

interface CatalystUserScope : CrudScope<CatalystUser, CatalystUser.CreateDto> {

    interface Nickname {
        suspend fun DomainEntity.Repository<CatalystUser>.getNickname(id: UUID): String?

        suspend fun DomainEntity.Repository<CatalystUser>.updateNickname(id: UUID, nickname: String): Boolean

        suspend fun DomainEntity.Repository<CatalystUser>.deleteNickname(id: UUID): Boolean
    }

    interface All : CatalystUserScope, Nickname
}
