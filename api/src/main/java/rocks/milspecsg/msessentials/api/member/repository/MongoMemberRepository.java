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

package rocks.milspecsg.msessentials.api.member.repository;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import rocks.milspecsg.msessentials.model.core.member.Member;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MongoMemberRepository<
        TMember extends Member<ObjectId>,
        TUser>
        extends MemberRepository<ObjectId, Datastore> {

    Optional<Query<Member<ObjectId>>> asQueryForUser(UUID userUUID);

    Optional<Query<Member<ObjectId>>> asQueryForUser(String username);

    CompletableFuture<Boolean> setNickname(Query<Member<ObjectId>> query, String username);

    CompletableFuture<Boolean> setLastSeenUtc(Query<Member<ObjectId>> query, Date lastSeenUtc);

    CompletableFuture<Boolean> setJoinedUtc(Query<Member<ObjectId>> query, Date joinedUtc);

    CompletableFuture<Boolean> setIPAddress(Query<Member<ObjectId>> query, String ipAddress);

    CompletableFuture<Optional<UUID>> setBanned(Query<Member<ObjectId>> query, boolean isBanned, String reason);

    CompletableFuture<Optional<UUID>> setMuteStatus(Query<Member<ObjectId>> query, boolean muteStatus);
}
