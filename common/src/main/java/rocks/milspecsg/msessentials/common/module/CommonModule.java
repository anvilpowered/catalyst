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

package rocks.milspecsg.msessentials.common.module;

import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import rocks.milspecsg.msessentials.api.chat.ChatService;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.api.plugin.PluginMessages;
import rocks.milspecsg.msessentials.common.chat.CommonChatService;
import rocks.milspecsg.msessentials.common.data.config.MSEssentialsConfigurationService;
import rocks.milspecsg.msessentials.common.data.registry.MSEssentialsRegistry;
import rocks.milspecsg.msessentials.common.member.CommonMemberManager;
import rocks.milspecsg.msessentials.common.plugin.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.common.plugin.MSEssentialsPluginMessages;
import rocks.milspecsg.msrepository.api.data.config.ConfigurationService;
import rocks.milspecsg.msrepository.api.data.registry.Registry;
import rocks.milspecsg.msrepository.api.misc.BindingExtensions;
import rocks.milspecsg.msrepository.api.plugin.BasicPluginInfo;
import rocks.milspecsg.msrepository.api.plugin.PluginInfo;
import rocks.milspecsg.msrepository.common.misc.CommonBindingExtensions;

@SuppressWarnings({"UnstableApiUsage"})
public class CommonModule<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource>
    extends AbstractModule {

    @Override
    protected void configure() {

        BindingExtensions be = new CommonBindingExtensions(binder());

        be.bind(new TypeToken<PluginInfo<TString>>(getClass()) {
        }, new TypeToken<MSEssentialsPluginInfo<TString, TCommandSource>>(getClass()) {
        });

        be.bind(new TypeToken<BasicPluginInfo>(getClass()) {
        }, new TypeToken<MSEssentialsPluginInfo<TString, TCommandSource>>(getClass()) {
        });

        be.bind(
            new TypeToken<PluginMessages<TString>>(getClass()) {
            },
            new TypeToken<MSEssentialsPluginMessages<TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<MemberManager<TString>>(getClass()) {
            },
            new TypeToken<CommonMemberManager<TUser, TPlayer, TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<ChatService<TString>>(getClass()) {
            },
            new TypeToken<CommonChatService<TPlayer, TString, TCommandSource>>(getClass()) {
            }
        );

        bind(ConfigurationService.class).to(MSEssentialsConfigurationService.class);
        bind(Registry.class).to(MSEssentialsRegistry.class);
    }
}
