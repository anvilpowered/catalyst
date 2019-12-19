package rocks.milspecsg.msessentials.service.common.member.repository;

import com.google.inject.Inject;
import io.jsondb.JsonDBOperations;
import io.jsondb.query.Update;
import rocks.milspecsg.msessentials.api.member.repository.JsonMemberRepository;
import rocks.milspecsg.msessentials.model.core.member.JsonMember;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.datastore.json.JsonConfig;
import rocks.milspecsg.msrepository.service.common.repository.CommonJsonRepository;


import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonJsonMemberRepository
        extends CommonMemberRepository<UUID, JsonDBOperations, JsonConfig>
        implements CommonJsonRepository<Member<UUID>, CacheService<UUID, Member<UUID>, JsonDBOperations, JsonConfig>>,
        JsonMemberRepository {

    @Inject
    public CommonJsonMemberRepository(DataStoreContext<UUID, JsonDBOperations, JsonConfig> dataStoreContext) {
        super(dataStoreContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Member<UUID>> getTClass() {
        return (Class<Member<UUID>>) getDataStoreContext().getEntityClassUnsafe("member");
    }


    @Override
    public String asQueryForUser(UUID userUUID) {
        return String.format("/.[userUUID='%s']", userUUID);
    }

    @Override
    public CompletableFuture<Optional<Member<UUID>>> getOneForUser(UUID userUUID) {
        return CompletableFuture.supplyAsync(() -> getDataStoreContext().getDataStore().map(j -> j.findOne(asQueryForUser(userUUID), getTClass())));
    }

    @Override
    public CompletableFuture<Boolean> setBannedForUser(UUID userUUID, boolean isBanned) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> setNicknameForUser(UUID userUUID, String nickname) {
        getDataStoreContext().getDataStore().map(j -> j.savr)
       return CompletableFuture.supplyAsync(() -> getDataStoreContext().getDataStore().map(j -> j.findAndModify(asQueryForUser(userUUID), Update.update("nickname", nickname), getTClass())).isPresent());
    }
}
