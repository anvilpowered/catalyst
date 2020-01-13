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

import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.EntityId;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.StoreTransaction;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.repository.XodusRepository;
import rocks.milspecsg.msrepository.datastore.xodus.XodusConfig;
import rocks.milspecsg.msrepository.model.data.dbo.Mappable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface XodusMemberRepository<
        TMember extends Member<EntityId> & Mappable<Entity>,
        TUser>
        extends MemberRepository<EntityId, TMember, TUser, PersistentEntityStore, XodusConfig>,
        XodusRepository<TMember, CacheService<EntityId, TMember, PersistentEntityStore, XodusConfig>> {

    CompletableFuture<Boolean> ban(Function<? super StoreTransaction, ? extends Iterable<Entity>> query);

    CompletableFuture<Boolean> setNickname(Function<? super StoreTransaction, ? extends Iterable<Entity>> query);

    CompletableFuture<Boolean> setIPAddress(Function<? super StoreTransaction, ? extends Iterable<Entity>> query);

    CompletableFuture<Boolean> setLastSeenUtc(Function<? super StoreTransaction, ? extends Iterable<EntityId>> query);

    CompletableFuture<Boolean> setJoinedUtc(Function<? super StoreTransaction, ? extends Iterable<EntityId>> query);

    CompletableFuture<Optional<UUID>> setBanned(Function<? super StoreTransaction, ? extends Iterable<EntityId>> query);

    CompletableFuture<Optional<UUID>> setMuted(Function<? super StoreTransaction, ? extends Iterable<EntityId>>query);


}
