package rocks.milspecsg.msessentials.api.member.repository;

import io.jsondb.JsonDBOperations;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msrepository.api.cache.CacheService;
import rocks.milspecsg.msrepository.api.repository.JsonRepository;
import rocks.milspecsg.msrepository.datastore.json.JsonConfig;

import java.util.UUID;

public interface JsonMemberRepository
extends MemberRepository<UUID, JsonDBOperations, JsonConfig>, JsonRepository<Member<UUID>, CacheService<UUID, Member<UUID>, JsonDBOperations, JsonConfig>> {

    String asQueryForUser(UUID userUUID);

}
