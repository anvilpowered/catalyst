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

package rocks.milspecsg.msessentials.common.member.repository;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msessentials.api.model.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.datastore.*;
import rocks.milspecsg.msrepository.common.repository.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMongoMemberRepository
    extends CommonMemberRepository<ObjectId, Datastore>
    implements CommonMongoRepository<Member<ObjectId>, CacheService<ObjectId, Member<ObjectId>, Datastore>> {

    @Inject
    public CommonMongoMemberRepository(DataStoreContext<ObjectId, Datastore> dataStoreContext) {
        super(dataStoreContext);
    }

    public Optional<Query<Member<ObjectId>>> asQueryForUser(UUID userUUID) {
        return asQuery().map(q -> q.field("userUUID").equal(userUUID));
    }

    public Optional<Query<Member<ObjectId>>> asQueryForUser(String username) {
        return asQuery().map(q -> q.field("userName").equal(username));
    }

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneForUser(String userName) {
        return CompletableFuture.supplyAsync(() -> asQueryForUser(userName).map(QueryResults::get));
    }

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneForUser(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> asQueryForUser(userUUID).map(QueryResults::get));
    }

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneOrGenerateForUser(UUID userUUID, String ipAddress, String userName, boolean[] flags) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Member<ObjectId>> optionalMember = getOneForUser(userUUID).join();
                if (optionalMember.isPresent()) {
                    Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations();
                    boolean updateName = false;
                    boolean updateIpAddress = false;
                    if (!userName.equals(optionalMember.get().getUserName())) {
                        updateOperations.map(u -> u.set("userName", userName));
                        updateName = true;
                    }
                    if (!optionalMember.get().getIPAddress().equals(ipAddress)) {
                        updateOperations.map(u -> u.set("ipAddress", ipAddress));
                        updateIpAddress = true;
                    }
                    Optional<Query<Member<ObjectId>>> optionalQuery = asQuery(optionalMember.get().getId());
                    if (optionalQuery.isPresent() && updateOperations
                        .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                            .map(datastore -> datastore.update(optionalQuery.get(), memberUpdateOperations).getUpdatedCount() > 0).orElse(false)
                        ).orElse(false)) {
                        if (updateName) {
                            optionalMember.get().setUserName(userName);
                        }
                        if (updateIpAddress) {
                            optionalMember.get().setIPAddress(ipAddress);
                        }
                    }
                    flags[0] = false;
                    return optionalMember;
                }
                // if there isn't one already, create a new one
                Member<ObjectId> member = generateEmpty();
                member.setUserUUID(userUUID);
                member.setJoinedUtc(new Date());
                member.setIPAddress(ipAddress);
                member.setUserName(userName);
                flags[0] = true;
                return insertOne(member).join();
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    @Override
    public CompletableFuture<Optional<UUID>> setBannedForUser(String userName, boolean isBanned, String reason) {
        return asQueryForUser(userName).map(q -> setBanned(q, isBanned, reason)).orElse(CompletableFuture.completedFuture(Optional.empty()));
    }

    public CompletableFuture<Optional<UUID>> setBanned(Query<Member<ObjectId>> query, boolean isBanned, String reason) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("isBanned", isBanned).set("banReason", reason));
            if (updateOperations
                .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                    .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false)
                ).orElse(false)) {
                return Optional.of(query.get().getUserUUID());
            } else {
                return Optional.empty();
            }
        });
    }

    @Override
    public CompletableFuture<Optional<UUID>> setMuteStatusForUser(String userName, boolean muteStatus) {
        return asQueryForUser(userName).map(q -> setMuteStatus(q, muteStatus)).orElse(CompletableFuture.completedFuture(Optional.empty()));
    }

    public CompletableFuture<Optional<UUID>> setMuteStatus(Query<Member<ObjectId>> query, boolean muteStatus) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("muteStatus", muteStatus));
            if (updateOperations
                .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                    .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false)
                ).orElse(false)) {
                return Optional.of(query.get().getUserUUID());
            } else {
                return Optional.empty();
            }
        });
    }

    public CompletableFuture<Boolean> setNickname(Query<Member<ObjectId>> query, String nickname) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("nickname", nickname));
            return updateOperations
                .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                    .map(dataStore -> dataStore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false)
                ).orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> setNickNameForUser(String userName, String nickName) {
        return asQueryForUser(userName).map(q -> setNickname(q, nickName).exceptionally(e -> false)).orElse(CompletableFuture.completedFuture(false));
    }

    public CompletableFuture<Boolean> setJoinedUtc(Query<Member<ObjectId>> query, Date joinedUtc) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("joinedUtc", joinedUtc));
            return updateOperations
                .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                    .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false))
                .orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> setJoinedUtcForUser(UUID userUUID, Date joinedUtc) {
        return getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return false;
            }
            return asQueryForUser(userUUID).map(q -> setJoinedUtc(q, joinedUtc).join()).orElse(false);
        });
    }

    public CompletableFuture<Boolean> setLastSeenUtc(Query<Member<ObjectId>> query, Date lastSeenUtc) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("lastSeenUtc", lastSeenUtc));
            return updateOperations
                .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                    .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false))
                .orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> setLastSeenUtcForUser(UUID userUUID, Date lastSeenUtc) {
        return getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return false;
            }
            return asQueryForUser(userUUID).map(q -> setLastSeenUtc(q, lastSeenUtc).join()).orElse(false);
        });
    }

    public CompletableFuture<Boolean> setIPAddress(Query<Member<ObjectId>> query, String ipAddress) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("ipAddress", ipAddress));
            return updateOperations
                .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                    .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false))
                .orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> setIPAddressForUser(String userName, String ipAddress) {
        return getOneForUser(userName).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                System.out.println("isn't present");
                return false;
            }
            return asQueryForUser(userName).map(q -> setIPAddress(q, ipAddress).join()).orElse(false);
        });
    }
}
