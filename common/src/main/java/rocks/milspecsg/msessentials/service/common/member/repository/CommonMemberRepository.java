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
    public CompletableFuture<Optional<Member<TKey>>> getOneOrGenerateForUser(UUID userUUID, String ipAddress, String username) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Member<TKey>> optionalMember = getOneForUser(userUUID).join();
                if (optionalMember.isPresent()) {
                    optionalMember.get().setIPAddress(ipAddress);
                    if (!optionalMember.get().getUserName().equals(username)) {
                        optionalMember.get().setUserName(username);
                    }
                    if (!optionalMember.get().getIPAddress().equals(ipAddress)) {
                        optionalMember.get().setIPAddress(ipAddress);
                    }
                    return optionalMember;
                }
                // if there isn't one already, create a new one
                Member<TKey> member = generateEmpty();
                member.setUserUUID(userUUID);
                member.setJoinDateUtc(new Date());
                member.setIPAddress(ipAddress);
                member.setUserName(username);
                return insertOne(member).join();
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
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
