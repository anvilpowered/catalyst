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

package rocks.milspecsg.msessentials;

import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.api.member.repository.MemberRepository;
import rocks.milspecsg.msessentials.model.core.member.Member;
import rocks.milspecsg.msessentials.service.common.member.CommonMemberManager;
import rocks.milspecsg.msessentials.service.common.member.repository.CommonMongoMemberRepository;
import rocks.milspecsg.msrepository.BindingExtensions;
import rocks.milspecsg.msrepository.CommonBindingExtensions;
import rocks.milspecsg.msrepository.api.manager.annotation.MongoDBComponent;
import rocks.milspecsg.msrepository.datastore.DataStoreContext;
import rocks.milspecsg.msrepository.datastore.mongodb.MongoContext;

@SuppressWarnings({"unchecked", "UnstableApiUsage"})
public class CommonModule<
        TString,
        TCommandSource>
        extends AbstractModule {

    @Override
    protected void configure() {

        BindingExtensions be = new CommonBindingExtensions(binder());

        be.bind(
                new TypeToken<MemberRepository<?, ?>>(getClass()) {
                },
                new TypeToken<MemberRepository<?, Member<?>>>(getClass()) {
                },
                new TypeToken<MemberRepository<ObjectId, Datastore>>(getClass()) {
                },
                new TypeToken<CommonMongoMemberRepository>(getClass()) {
                },
                MongoDBComponent.class
        );
        be.bind(
                new TypeToken<MemberManager<TString>>(getClass()) {
                },
                new TypeToken<CommonMemberManager<TCommandSource, TString, TCommandSource>>(getClass()) {
                }
        );
        bind(new TypeLiteral<DataStoreContext<ObjectId, Datastore>>() {
        }).to(new TypeLiteral<MongoContext>() {
        });
    }
}
