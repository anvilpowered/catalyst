package rocks.milspecsg.msessentials.api.member.repository;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.repository.MongoRepository;
import rocks.milspecsg.msrepository.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.datastore.mongodb.MongoConfig;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface MongoMemberRepository
        extends MemberRepository<ObjectId,
        Datastore,
        MongoConfig>, MongoRepository<Member<ObjectId>, CacheService<ObjectId, Member<ObjectId>, Datastore, MongoConfig>> {

    Optional<Query<Member<ObjectId>>> asQueryForUser(UUID userUUID);

    Optional<Query<Member<ObjectId>>> asQueryForUser(String username);

    CompletableFuture<Boolean> setNickname(Query<Member<ObjectId>> query, String username);

    CompletableFuture<Boolean> setJoinedUtcForUser(UUID userUUID, Date joinedUtc);

    CompletableFuture<Boolean> setLastSeenUtc(Query<Member<ObjectId>> query, Date lastSeenUtc);

    CompletableFuture<Boolean> setLastSeenUtcForUser(UUID userUUID, Date lastSeenUtc);

    CompletableFuture<Boolean> setJoinedUtc(Query<Member<ObjectId>> query, Date joinedUtc);

    CompletableFuture<Boolean> setIPAddressForUser(String username, String ipAddress);

    CompletableFuture<Boolean> setIPAddress(Query<Member<ObjectId>> query, String ipAddress);

    CompletableFuture<Boolean> setBanned(Query<Member<ObjectId>> query, boolean isBanned);

    CompletableFuture<Boolean> setBannedForUser(String username, boolean isbanned);

    CompletableFuture<Boolean> setBanReasonForUser(String username, String banReason);

    CompletableFuture<Boolean> setBanReason(Query<Member<ObjectId>> query, String banReason);
}
