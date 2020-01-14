/*
 *     MSEssentials - MilSpecSG
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rocks.milspecsg.msessentials.api.member.repository;

import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.repository.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberRepository<
        TKey,
        TDataStore>
    extends Repository<TKey, Member<TKey>, CacheService<TKey, Member<TKey>, TDataStore>, TDataStore> {


    CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID, String ipAddress, String userName, boolean[] flags);

    CompletableFuture<Optional<Member<TKey>>> getOneForUser(String userName);

    CompletableFuture<Optional<Member<TKey>>> getOneForUser(UUID userUUID);

    CompletableFuture<Optional<UUID>> getUUID(TKey id);

    CompletableFuture<Optional<UUID>> setBannedForUser(String userName, boolean isBanned, String reason);

    CompletableFuture<Optional<UUID>> setMuteStatusForUser(String userName, boolean muteStatus);

    CompletableFuture<Boolean> setNickNameForUser(String userName, String nickName);

    CompletableFuture<Boolean> setIPAddressForUser(String userName, String ipAddress);

    CompletableFuture<Boolean> setJoinedUtcForUser(UUID userUUID, Date joinedUtc);

    CompletableFuture<Boolean> setLastSeenUtcForUser(UUID userUUID, Date lastSeenUtc);
}
