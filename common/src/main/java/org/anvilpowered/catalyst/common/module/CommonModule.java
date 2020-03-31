/*
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

package org.anvilpowered.catalyst.common.module;

import com.google.common.reflect.TypeToken;
import com.google.inject.AbstractModule;
import org.anvilpowered.anvil.api.Anvil;
import org.anvilpowered.anvil.api.data.config.ConfigurationService;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.misc.BindingExtensions;
import org.anvilpowered.anvil.api.plugin.BasicPluginInfo;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.catalyst.api.discord.WebhookSender;
import org.anvilpowered.catalyst.api.event.ChatEvent;
import org.anvilpowered.catalyst.api.event.JoinEvent;
import org.anvilpowered.catalyst.api.event.LeaveEvent;
import org.anvilpowered.catalyst.api.event.StaffChatEvent;
import org.anvilpowered.catalyst.api.listener.ChatListener;
import org.anvilpowered.catalyst.api.listener.DiscordChatListener;
import org.anvilpowered.catalyst.api.listener.JoinListener;
import org.anvilpowered.catalyst.api.listener.LeaveListener;
import org.anvilpowered.catalyst.api.listener.StaffChatListener;
import org.anvilpowered.catalyst.api.member.MemberManager;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.ChatFilter;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.anvilpowered.catalyst.api.service.JDAService;
import org.anvilpowered.catalyst.api.service.LuckpermsService;
import org.anvilpowered.catalyst.api.service.PrivateMessageService;
import org.anvilpowered.catalyst.api.service.StaffChatService;
import org.anvilpowered.catalyst.api.service.StaffListService;
import org.anvilpowered.catalyst.api.service.TabService;
import org.anvilpowered.catalyst.common.data.config.CatalystConfigurationService;
import org.anvilpowered.catalyst.common.data.registry.CatalystRegistry;
import org.anvilpowered.catalyst.common.discord.CommonDiscordListener;
import org.anvilpowered.catalyst.common.discord.CommonJDAService;
import org.anvilpowered.catalyst.common.discord.CommonWebhookSender;
import org.anvilpowered.catalyst.common.event.CommonChatEvent;
import org.anvilpowered.catalyst.common.event.CommonJoinEvent;
import org.anvilpowered.catalyst.common.event.CommonLeaveEvent;
import org.anvilpowered.catalyst.common.event.CommonStaffChatEvent;
import org.anvilpowered.catalyst.common.listener.CommonChatListener;
import org.anvilpowered.catalyst.common.listener.CommonDiscordChatListener;
import org.anvilpowered.catalyst.common.listener.CommonJoinListener;
import org.anvilpowered.catalyst.common.listener.CommonLeaveListener;
import org.anvilpowered.catalyst.common.listener.CommonStaffChatListener;
import org.anvilpowered.catalyst.common.member.CommonMemberManager;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo;
import org.anvilpowered.catalyst.common.plugin.CatalystPluginMessages;
import org.anvilpowered.catalyst.common.plugin.CommonStaffListService;
import org.anvilpowered.catalyst.common.service.CommonChatFilter;
import org.anvilpowered.catalyst.common.service.CommonChatService;
import org.anvilpowered.catalyst.common.service.CommonLuckpermsService;
import org.anvilpowered.catalyst.common.service.CommonPrivateMessageService;
import org.anvilpowered.catalyst.common.service.CommonStaffChatService;
import org.anvilpowered.catalyst.common.service.CommonTabService;

@SuppressWarnings({"UnstableApiUsage"})
public class CommonModule<
    TUser,
    TPlayer extends TCommandSource,
    TString,
    TCommandSource,
    TSubject>
    extends AbstractModule {

    @Override
    protected void configure() {

        BindingExtensions be = Anvil.getBindingExtensions(binder());

        be.bind(new TypeToken<PluginInfo<TString>>(getClass()) {
        }, new TypeToken<CatalystPluginInfo<TString, TCommandSource>>(getClass()) {
        });

        be.bind(new TypeToken<BasicPluginInfo>(getClass()) {
        }, new TypeToken<CatalystPluginInfo<TString, TCommandSource>>(getClass()) {
        });

        be.bind(
            new TypeToken<PluginMessages<TString>>(getClass()) {
            },
            new TypeToken<CatalystPluginMessages<TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<MemberManager<TString>>(getClass()) {
            },
            new TypeToken<CommonMemberManager<TUser, TPlayer, TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<ChatService<TString, TPlayer, TCommandSource>>(getClass()) {
            },
            new TypeToken<CommonChatService<TPlayer, TString, TCommandSource, TSubject>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<PrivateMessageService<TString>>(getClass()) {
            },
            new TypeToken<CommonPrivateMessageService<TPlayer, TString, TCommandSource>>(getClass()) {
            }
        );

        be.bind(
            new TypeToken<StaffListService<TString>>(getClass()) {
            },
            new TypeToken<CommonStaffListService<TPlayer, TString, TCommandSource>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<TabService<TString>>(getClass()) {
            },
            new TypeToken<CommonTabService<TString, TCommandSource>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<LuckpermsService<TPlayer>>(getClass()) {
            },
            new TypeToken<CommonLuckpermsService<TString, TPlayer, TCommandSource>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<JDAService>(getClass()) {
            },
            new TypeToken<CommonJDAService<TString, TPlayer, TCommandSource, TSubject>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<WebhookSender<TPlayer>>(getClass()) {
            },
            new TypeToken<CommonWebhookSender<TPlayer>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<StaffChatEvent<TString, TPlayer>>(getClass()) {
            },
            new TypeToken<CommonStaffChatEvent<TString, TPlayer>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<ChatListener<TPlayer>>(getClass()) {
            },
            new TypeToken<CommonChatListener<TString, TPlayer, TCommandSource, TSubject>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<ChatEvent<TString, TPlayer>>(getClass()) {
            },
            new TypeToken<CommonChatEvent<TString, TPlayer>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<JoinListener<TPlayer>>(getClass()) {
            },
            new TypeToken<CommonJoinListener<TString, TPlayer, TCommandSource, TSubject>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<LeaveListener<TPlayer>>(getClass()) {
            },
            new TypeToken<CommonLeaveListener<TString, TPlayer, TCommandSource>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<JoinEvent<TPlayer>>(getClass()) {
            },
            new TypeToken<CommonJoinEvent<TPlayer>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<LeaveEvent<TPlayer>>(getClass()) {
            },
            new TypeToken<CommonLeaveEvent<TPlayer>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<DiscordChatListener<TString, TPlayer>>(getClass()) {
            },
            new TypeToken<CommonDiscordChatListener<TString, TPlayer>>(getClass()) {
            }
        );
        be.bind(
            new TypeToken<StaffChatListener<TPlayer>>(getClass()) {
            },
            new TypeToken<CommonStaffChatListener<TString, TPlayer, TCommandSource, TSubject>>(getClass()) {
            }
        );

        bind(ChatFilter.class).to(CommonChatFilter.class);
        bind(ConfigurationService.class).to(CatalystConfigurationService.class);
        bind(Registry.class).to(CatalystRegistry.class);
        bind(StaffChatService.class).to(CommonStaffChatService.class);
    }
}
