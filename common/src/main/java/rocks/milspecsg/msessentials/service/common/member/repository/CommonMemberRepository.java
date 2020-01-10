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

package rocks.milspecsg.msessentials.service.common.member.repository;

import rocks.milspecsg.msessentials.api.member.repository.MemberRepository;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.datastore.DataStoreConfig;
import rocks.milspecsg.msrepository.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.model.data.dbo.ObjectWithId;
import rocks.milspecsg.msrepository.service.common.repository.CommonRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class CommonMemberRepository<
        TKey,
        TDataStore,
        TDataStoreConfig extends DataStoreConfig>
        extends CommonRepository<TKey, Member<TKey>, CacheService<TKey, Member<TKey>, TDataStore, TDataStoreConfig>, TDataStore, TDataStoreConfig>
        implements MemberRepository<TKey, TDataStore, TDataStoreConfig> {

    protected CommonMemberRepository(DataStoreContext<TKey, TDataStore, TDataStoreConfig> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    public CompletableFuture<Optional<TKey>> getIdForUser(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> getOneForUser(userUUID).join().map(ObjectWithId::getId));
    }

    @Override
    public CompletableFuture<Optional<UUID>> getUUID(TKey id) {
        return CompletableFuture.supplyAsync(() -> getOne(id).join().map(Member::getUserUUID));
    }
}
