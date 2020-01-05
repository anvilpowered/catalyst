package rocks.milspecsg.msessentials.api.member.repository;

import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.repository.Repository;
import rocks.milspecsg.msrepository.datastore.DataStoreConfig;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MemberRepository<
        TKey,
        TDataStore,
        TDataStoreConfig extends DataStoreConfig>
        extends Repository<TKey, Member<TKey>, CacheService<TKey, Member<TKey>, TDataStore, TDataStoreConfig>, TDataStore, TDataStoreConfig> {
    CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID, String ipAddress, String userName, boolean[] flags);

    CompletableFuture<Optional<Member<TKey>>> getOneForUser(String userName);

    CompletableFuture<Optional<Member<TKey>>> getOneForUser(UUID userUUID);

    CompletableFuture<Optional<TKey>> getIdForUser(UUID userUUID);

    CompletableFuture<Optional<UUID>> getUUID(TKey id);

    CompletableFuture<Optional<UUID>> setBannedForUser(String userName, boolean isBanned, String reason);

    CompletableFuture<Boolean> setNickNameForUser(String userName, String nickName);

    CompletableFuture<Boolean> setIPAddressForUser(String userName, String ipAddress);

    CompletableFuture<Boolean> setJoinedUtcForUser(UUID userUUID, Date joinedUtc);

    CompletableFuture<Boolean> setLastSeenUtcForUser(UUID userUUID, Date lastSeenUtc);

    CompletableFuture<Boolean> setMuteStatusForUser(String userName, boolean muteStatus);
}
