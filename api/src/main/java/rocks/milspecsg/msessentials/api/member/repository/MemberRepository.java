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
    CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID, String ipAddress, String username);

    CompletableFuture<Optional<Member<TKey>>> getOneForUser(String username);

    CompletableFuture<Optional<Member<TKey>>> getOneForUser(UUID userUUID);

    CompletableFuture<Optional<TKey>> getIdForUser(UUID userUUID);

    CompletableFuture<Optional<UUID>> getUUID(TKey id);

    CompletableFuture<Boolean> setBannedForUser(String username, boolean isBanned);

    CompletableFuture<Boolean> setNicknameForUser(UUID userUUID, String nickname);

    CompletableFuture<Boolean> setNicknameForUser(String username, String nickname);

    CompletableFuture<Boolean> setIPAddressForUser(String username, String ipAddress);

    CompletableFuture<Boolean> setJoinedUtcForUser(UUID userUUID, Date joinedUtc);

    CompletableFuture<Boolean> setLastSeenUtcForUser(UUID userUUID, Date lastSeenUtc);

    CompletableFuture<Boolean> setBanReasonForUser(String username, String banReason);

    CompletableFuture<Boolean> setMuteStatusForUser(String username, boolean muteStatus);


}
