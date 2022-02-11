/*
 *   Catalyst - AnvilPowered
 *   Copyright (C) 2021
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
import com.google.inject.TypeLiteral
import org.anvilpowered.anvil.api.misc.bind
import org.anvilpowered.anvil.api.misc.to
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.catalyst.api.discord.JDAService
import org.anvilpowered.catalyst.api.discord.WebhookSender
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.service.ChannelService
import org.anvilpowered.catalyst.api.service.ChatFilter
import org.anvilpowered.catalyst.api.service.ChatService
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
import org.anvilpowered.catalyst.common.service.CommonEventRegistrationService
import org.anvilpowered.catalyst.common.service.CommonLuckpermsService
import org.anvilpowered.catalyst.common.service.CommonPrivateMessageService
import org.anvilpowered.catalyst.common.service.CommonStaffListService
import org.anvilpowered.catalyst.common.service.CommonTabService
import org.anvilpowered.registry.ConfigurationService
import org.anvilpowered.registry.Registry
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.loader.ConfigurationLoader
import java.nio.file.Paths

@Suppress("UnstableApiUsage")
open class CommonModule<TPlayer, TCommandSource>(private val configDir: String) : AbstractModule() {

    override fun configure() {
        val configDirFull = Paths.get("$configDir/catalyst").toFile()
        if (!configDirFull.exists()) {
            check(configDirFull.mkdirs()) { "Unable to create config directory" }
        }
        bind(object : TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {}).toInstance(
            HoconConfigurationLoader.builder().path(Paths.get("$configDirFull/catalyst.conf")).build()
        )
        with(binder()) {
            bind<PluginInfo>().to<CatalystPluginInfo>()
            bind<PluginMessages>().to<CatalystPluginMessages>()
            bind<MemberManager>().to<CommonMemberManager<TPlayer>>()
            bind<ChannelService<TPlayer>>().to<CommonChannelService<TPlayer>>()
            bind<ChatService<TPlayer, TCommandSource>>().to<CommonChatService<TPlayer, TCommandSource>>()
            bind<PrivateMessageService>().to<CommonPrivateMessageService<TPlayer>>()
            bind<StaffListService>().to<CommonStaffListService<TPlayer>>()
            bind<TabService<TPlayer>>().to<CommonTabService<TPlayer>>()
            bind<LuckpermsService>().to<CommonLuckpermsService<TPlayer>>()
            bind<JDAService>().to<CommonJDAService<TPlayer>>()
            bind<WebhookSender>().to<CommonWebhookSender<TPlayer>>()
            bind<EventRegistrationService>().to<CommonEventRegistrationService<TPlayer, TCommandSource>>()
            bind<ChatFilter>().to<CommonChatFilter>()
            bind<ConfigurationService>().to<CommonConfigurationService>()
        }
        bind(ChatFilter::class.java).to(CommonChatFilter::class.java)
        bind(ConfigurationService::class.java).to(CommonConfigurationService::class.java)
        bind(Registry::class.java).to(CommonConfigurationService::class.java)
    }
}
