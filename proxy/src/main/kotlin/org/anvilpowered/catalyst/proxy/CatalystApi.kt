/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.catalyst.proxy

import org.anvilpowered.anvil.core.AnvilApi
import org.anvilpowered.anvil.core.config.KeyNamespace
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.anvil.core.config.configureDefaults
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.chat.placeholder.BackendFormat
import org.anvilpowered.catalyst.api.chat.placeholder.ChannelMessageFormat
import org.anvilpowered.catalyst.api.chat.placeholder.ChatChannelFormat
import org.anvilpowered.catalyst.api.chat.placeholder.MessageContentFormat
import org.anvilpowered.catalyst.api.chat.placeholder.MiniMessageSerializer
import org.anvilpowered.catalyst.api.chat.placeholder.OnlineUserFormat
import org.anvilpowered.catalyst.api.chat.placeholder.PlayerFormat
import org.anvilpowered.catalyst.api.chat.placeholder.PrivateMessageFormat
import org.anvilpowered.catalyst.api.chat.placeholder.ProxyFormat
import org.anvilpowered.catalyst.api.chat.placeholder.register
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.config.ChatChannel
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.anvilpowered.catalyst.api.user.UserRepository
import org.anvilpowered.catalyst.proxy.chat.ChannelServiceImpl
import org.anvilpowered.catalyst.proxy.chat.ChannelSwitchFrontend
import org.anvilpowered.catalyst.proxy.chat.ChatFilter
import org.anvilpowered.catalyst.proxy.chat.ChatService
import org.anvilpowered.catalyst.proxy.chat.ChatServiceImpl
import org.anvilpowered.catalyst.proxy.chat.PrivateMessageService
import org.anvilpowered.catalyst.proxy.chat.StaffListService
import org.anvilpowered.catalyst.proxy.chat.builder.ChannelMessageBuilderImpl
import org.anvilpowered.catalyst.proxy.chat.builder.ChatChannelBuilderImpl
import org.anvilpowered.catalyst.proxy.command.CatalystCommandFactory
import org.anvilpowered.catalyst.proxy.command.broadcast.BroadcastCommandFactory
import org.anvilpowered.catalyst.proxy.command.channel.ChannelAliasCommandRegistrar
import org.anvilpowered.catalyst.proxy.command.channel.ChannelCommandFactory
import org.anvilpowered.catalyst.proxy.command.message.MessageCommandFactory
import org.anvilpowered.catalyst.proxy.command.message.ReplyCommandFactory
import org.anvilpowered.catalyst.proxy.command.nickname.NicknameCommandFactory
import org.anvilpowered.catalyst.proxy.db.user.MinecraftUserRepositoryImpl
import org.anvilpowered.catalyst.proxy.db.user.UserRepositoryImpl
import org.anvilpowered.catalyst.proxy.discord.JDAService
import org.anvilpowered.catalyst.proxy.discord.WebhookSender
import org.anvilpowered.catalyst.proxy.listener.ChatListener
import org.anvilpowered.catalyst.proxy.listener.CommandListener
import org.anvilpowered.catalyst.proxy.listener.DiscordListener
import org.anvilpowered.catalyst.proxy.listener.JoinListener
import org.anvilpowered.catalyst.proxy.listener.LeaveListener
import org.anvilpowered.catalyst.proxy.registrar.CommandRegistrar
import org.anvilpowered.catalyst.proxy.registrar.ListenerRegistrar
import org.anvilpowered.catalyst.proxy.registrar.Registrar
import org.anvilpowered.catalyst.proxy.tab.GlobalTab
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import org.spongepowered.configurate.serialize.TypeSerializerCollection

interface CatalystApi {
    val module: Module

    companion object
}

fun CatalystApi.Companion.create(anvilApi: AnvilApi): CatalystApi {
    val velocityModule = module {
        val serializers = TypeSerializerCollection.defaults().childBuilder()
            .register(BackendFormat.Serializer)
            .register(ChannelMessageFormat.Serializer)
            .register(ChatChannelFormat.Serializer)
            .register(MessageContentFormat.Serializer)
            .register(OnlineUserFormat.Serializer)
            .register(PlayerFormat.Serializer)
            .register(PrivateMessageFormat.Serializer)
            .register(ProxyFormat.Serializer)
            .register(MiniMessageSerializer)
            .build()

        Registry.configureDefaults(anvilApi, serializers)
        singleOf(::LuckpermsService)
        singleOf(::WebhookSender)
        singleOf(::ChatFilter)
        singleOf(ChatChannelBuilderImpl::Factory) { bind<ChatChannel.Builder.Factory>() }
        singleOf(ChannelMessageBuilderImpl::Factory) { bind<ChannelMessage.Builder.Factory>() }
        singleOf(::ChannelServiceImpl) { bind<ChannelService>() }
        singleOf(::ChannelSwitchFrontend)
        singleOf(::ChatServiceImpl) { bind<ChatService>() }
        singleOf(::PrivateMessageService)
        singleOf(::ChatListener)
        singleOf(::CatalystKeys).withOptions {
            bind<KeyNamespace>()
        }
        singleOf(::CommandListener)
        singleOf(::JoinListener)
        singleOf(::LeaveListener)
        singleOf(::DiscordListener)
        singleOf(::JDAService)
        singleOf(::StaffListService)
        singleOf(::MinecraftUserRepositoryImpl) { bind<MinecraftUserRepository>() }
        singleOf(::UserRepositoryImpl) { bind<UserRepository>() }

        singleOf(BackendFormat::Resolver)
        singleOf(ChannelMessageFormat::Resolver)
        singleOf(ChatChannelFormat::Resolver)
        singleOf(MessageContentFormat::Resolver)
        singleOf(OnlineUserFormat::Resolver)
        singleOf(PlayerFormat::Resolver)
        singleOf(PrivateMessageFormat::Resolver)
        singleOf(ProxyFormat::Resolver)

        singleOf(::GlobalTab).withOptions {
            createdAtStart()
        }

        singleOf(::BroadcastCommandFactory)
        singleOf(::CatalystCommandFactory)
        singleOf(::ChannelCommandFactory)
        singleOf(::MessageCommandFactory)
        singleOf(::NicknameCommandFactory)
        singleOf(::ReplyCommandFactory)

        singleOf(::CommandRegistrar) { bind<Registrar>() }
        singleOf(::ListenerRegistrar) { bind<Registrar>() }
        singleOf(::ChannelAliasCommandRegistrar) { bind<Registrar>() }
    }

    return object : CatalystApi {
        override val module: Module = velocityModule
    }
}
