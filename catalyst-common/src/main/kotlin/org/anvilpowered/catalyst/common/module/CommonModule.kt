/*
 * Catalyst - AnvilPowered
 *   Copyright (C) 2020
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package org.anvilpowered.catalyst.common.module

import com.google.common.reflect.TypeToken
import com.google.inject.AbstractModule
import org.anvilpowered.anvil.api.Anvil
import org.anvilpowered.anvil.api.plugin.BasicPluginInfo
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.anvil.api.registry.ConfigurationService
import org.anvilpowered.anvil.api.registry.Registry
import org.anvilpowered.catalyst.api.discord.JDAService
import org.anvilpowered.catalyst.api.discord.WebhookSender
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.ChatFilter
import org.anvilpowered.catalyst.api.service.ChatService
import org.anvilpowered.catalyst.api.service.EmojiService
import org.anvilpowered.catalyst.api.service.EventRegistrationService
import org.anvilpowered.catalyst.api.service.LuckpermsService
import org.anvilpowered.catalyst.api.service.PrivateMessageService
import org.anvilpowered.catalyst.api.service.StaffListService
import org.anvilpowered.catalyst.api.service.TabService
import org.anvilpowered.catalyst.common.discord.CommonJDAService
import org.anvilpowered.catalyst.common.discord.CommonWebhookSender
import org.anvilpowered.catalyst.common.member.CommonMemberManager
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo
import org.anvilpowered.catalyst.common.plugin.CatalystPluginMessages
import org.anvilpowered.catalyst.common.registry.CommonConfigurationService
import org.anvilpowered.catalyst.common.service.CommonChannelService
import org.anvilpowered.catalyst.common.service.CommonChatFilter
import org.anvilpowered.catalyst.common.service.CommonChatService
import org.anvilpowered.catalyst.common.service.CommonEmojiService
import org.anvilpowered.catalyst.common.service.CommonEventRegistrationService
import org.anvilpowered.catalyst.common.service.CommonLuckpermsService
import org.anvilpowered.catalyst.common.service.CommonPrivateMessageService
import org.anvilpowered.catalyst.common.service.CommonStaffListService
import org.anvilpowered.catalyst.common.service.CommonTabService

@Suppress("UnstableApiUsage")
open class CommonModule<TPlayer, TString, TCommandSource> : AbstractModule() {

  override fun configure() {
    val be = Anvil.getBindingExtensions(binder())
    be.bind(
      object : TypeToken<PluginInfo<TString>>(javaClass) {
      },
      object : TypeToken<CatalystPluginInfo<TString, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<BasicPluginInfo>(javaClass) {
      },
      object : TypeToken<CatalystPluginInfo<TString, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<PluginMessages<TString>>(javaClass) {
      },
      object : TypeToken<CatalystPluginMessages<TString, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<MemberManager<TString>>(javaClass) {
      },
      object : TypeToken<CommonMemberManager<TPlayer, TString, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<ChannelService<TPlayer>>(javaClass) {
      },
      object : TypeToken<CommonChannelService<TPlayer>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<ChatService<TString, TPlayer, TCommandSource>>(javaClass) {
      },
      object : TypeToken<CommonChatService<TPlayer, TString, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<PrivateMessageService<TString>>(javaClass) {
      },
      object : TypeToken<CommonPrivateMessageService<TPlayer, TString, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<StaffListService<TString>>(javaClass) {
      },
      object : TypeToken<CommonStaffListService<TString, TPlayer, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<TabService<TString, TPlayer>>(javaClass) {
      },
      object : TypeToken<CommonTabService<TString, TPlayer, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<LuckpermsService>(javaClass) {
      },
      object : TypeToken<CommonLuckpermsService<TPlayer>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<JDAService>(javaClass) {
      },
      object : TypeToken<CommonJDAService<TPlayer, TString, TCommandSource>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<WebhookSender>(javaClass) {
      },
      object : TypeToken<CommonWebhookSender<TPlayer>>(javaClass) {
      }
    )
    be.bind(
      object : TypeToken<EventRegistrationService>(javaClass) {
      },
      object : TypeToken<CommonEventRegistrationService<TString, TPlayer, TCommandSource>>(javaClass) {
      }
    )
    bind(ChatFilter::class.java).to(CommonChatFilter::class.java)
    bind(ConfigurationService::class.java).to(CommonConfigurationService::class.java)
    bind(Registry::class.java).to(CommonConfigurationService::class.java)
    bind(EmojiService::class.java).to(CommonEmojiService::class.java)
  }
}
