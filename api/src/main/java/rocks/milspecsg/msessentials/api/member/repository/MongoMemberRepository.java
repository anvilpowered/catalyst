package rocks.milspecsg.msessentials.api.member.repository;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.repository.MongoRepository;
import rocks.milspecsg.msrepository.datastore.mongodb.MongoConfig;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MongoMemberRepository
        extends MemberRepository<ObjectId,
        Datastore,
        MongoConfig>, MongoRepository<Member<ObjectId>, CacheService<ObjectId, Member<ObjectId>, Datastore, MongoConfig>> {

    Optional<Query<Member<ObjectId>>> asQueryForUser(UUID userUUID);

    Optional<Query<Member<ObjectId>>> asQueryForUser(String username);

    CompletableFuture<Boolean> setNickname(Query<Member<ObjectId>> query, String username);

    CompletableFuture<Boolean> setLastSeenUtc(Query<Member<ObjectId>> query, Date lastSeenUtc);

    CompletableFuture<Boolean> setJoinedUtc(Query<Member<ObjectId>> query, Date joinedUtc);

    CompletableFuture<Boolean> setIPAddress(Query<Member<ObjectId>> query, String ipAddress);

    CompletableFuture<Optional<UUID>> setBanned(Query<Member<ObjectId>> query, boolean isBanned, String reason);

    CompletableFuture<Optional<UUID>> setMuteStatus(Query<Member<ObjectId>> query, boolean muteStatus);
}
