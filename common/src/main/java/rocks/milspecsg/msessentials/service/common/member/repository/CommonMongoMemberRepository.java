package rocks.milspecsg.msessentials.service.common.member.repository;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msessentials.api.member.repository.MongoMemberRepository;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.datastore.mongodb.MongoConfig;
import rocks.milspecsg.msrepository.service.common.repository.CommonMongoRepository;


import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonMongoMemberRepository
        extends CommonMemberRepository<ObjectId, Datastore, MongoConfig>
        implements CommonMongoRepository<Member<ObjectId>, CacheService<ObjectId, Member<ObjectId>, Datastore, MongoConfig>>,
        MongoMemberRepository {

    @Inject
    public CommonMongoMemberRepository(DataStoreContext<ObjectId, Datastore, MongoConfig> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Member<ObjectId>> getTClass() {
        return (Class<Member<ObjectId>>) getDataStoreContext().getEntityClassUnsafe("member");
    }


    @Override
    public Optional<Query<Member<ObjectId>>> asQueryForUser(UUID userUUID) {
        return asQuery().map(q -> q.field("userUUID").equal(userUUID));
    }

    @Override
    public Optional<Query<Member<ObjectId>>> asQueryForUser(String username) {
        return asQuery().map(q -> q.field("username").equal(username));
    }

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneForUser(String username) {
        return CompletableFuture.supplyAsync(() -> asQueryForUser(username).map(QueryResults::get));
    }

    @Override
    public CompletableFuture<Optional<Member<ObjectId>>> getOneForUser(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> asQueryForUser(userUUID).map(QueryResults::get));
    }

    @Override
    public CompletableFuture<Boolean> setBannedForUser(String username, boolean isBanned) {
        return getOneForUser(username).thenApplyAsync(optionalMember -> {
            if(!optionalMember.isPresent()) {
                return false;
            }
            return asQueryForUser(username).map(q -> setBanned(q, isBanned).join()).orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> setBanned(Query<Member<ObjectId>> query, boolean isBanned) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("isBanned", isBanned));
            return updateOperations
                    .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                            .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false)
                    ).orElse(false);
        });
    }

    @Override
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
    public CompletableFuture<Boolean> setNicknameForUser(UUID userUUID, String nickname) {
        return getOneForUser(userUUID).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return false;
            }
            return asQueryForUser(userUUID).map(q -> setNickname(q, nickname).join()).orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> setNicknameForUser(String username, String nickname) {
        return asQueryForUser(username).map(q -> setNickname(q, nickname).exceptionally(e -> false)).orElse(CompletableFuture.completedFuture(false));
    }

    @Override
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

    @Override
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

    @Override
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
    public CompletableFuture<Boolean> setIPAddressForUser(String username, String ipAddress) {
        return getOneForUser(username).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                System.out.println("isn't present");
                return false;
            }
            return asQueryForUser(username).map(q -> setIPAddress(q, ipAddress).join()).orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> setBanReason(Query<Member<ObjectId>> query, String banReason) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<UpdateOperations<Member<ObjectId>>> updateOperations = createUpdateOperations().map(u -> u.set("banReason", banReason));
            return updateOperations
                    .map(memberUpdateOperations -> getDataStoreContext().getDataStore()
                            .map(datastore -> datastore.update(query, memberUpdateOperations).getUpdatedCount() > 0).orElse(false))
                    .orElse(false);
        });
    }

    @Override
    public CompletableFuture<Boolean> setBanReasonForUser(String username, String banReason) {
        return getOneForUser(username).thenApplyAsync(optionalMember -> {
            if (!optionalMember.isPresent()) {
                return false;
            }
            return asQueryForUser(username).map(q -> setBanReason(q, banReason).join()).orElse(false);
        });
    }
}
