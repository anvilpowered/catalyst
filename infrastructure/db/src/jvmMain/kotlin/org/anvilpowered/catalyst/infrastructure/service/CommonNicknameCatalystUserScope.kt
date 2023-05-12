package org.anvilpowered.catalyst.infrastructure.service

import org.anvilpowered.catalyst.domain.entity.CatalystUser
import org.anvilpowered.catalyst.domain.service.CatalystUserScope
import org.sourcegrade.kontour.DomainEntity
import org.sourcegrade.kontour.UUID

class CommonNicknameCatalystUserScope : CatalystUserScope.Nickname {
    override suspend fun DomainEntity.Repository<CatalystUser>.getNickname(id: UUID): String? {
        TODO("Not yet implemented")
    }

    override suspend fun DomainEntity.Repository<CatalystUser>.updateNickname(id: UUID, nickname: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun DomainEntity.Repository<CatalystUser>.deleteNickname(id: UUID): Boolean {
        TODO("Not yet implemented")
    }
}
