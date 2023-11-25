/*
 *   Catalyst - AnvilPowered.org
 *   Copyright (C) 2020-2023 Contributors
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

package org.anvilpowered.catalyst.velocity

import com.google.inject.Injector
import org.anvilpowered.anvil.core.config.EnvironmentRegistry
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.catalyst.api.chat.ChannelMessage
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.config.CatalystKeys
import org.anvilpowered.catalyst.api.config.ChatChannel
import org.anvilpowered.catalyst.api.user.MinecraftUserRepository
import org.anvilpowered.catalyst.api.user.UserRepository
import org.anvilpowered.catalyst.velocity.chat.ChannelServiceImpl
import org.anvilpowered.catalyst.velocity.chat.ChatFilter
import org.anvilpowered.catalyst.velocity.chat.ChatService
import org.anvilpowered.catalyst.velocity.chat.ChatServiceImpl
import org.anvilpowered.catalyst.velocity.chat.StaffListService
import org.anvilpowered.catalyst.velocity.chat.builder.ChannelMessageBuilderImpl
import org.anvilpowered.catalyst.velocity.chat.builder.ChatChannelBuilderImpl
import org.anvilpowered.catalyst.velocity.command.nickname.NicknameCommandFactory
import org.anvilpowered.catalyst.velocity.db.user.MinecraftUserRepositoryImpl
import org.anvilpowered.catalyst.velocity.db.user.UserRepositoryImpl
import org.anvilpowered.catalyst.velocity.discord.JDAService
import org.anvilpowered.catalyst.velocity.listener.ChatListener
import org.anvilpowered.catalyst.velocity.listener.CommandListener
import org.anvilpowered.catalyst.velocity.listener.DiscordListener
import org.anvilpowered.catalyst.velocity.listener.JoinListener
import org.anvilpowered.catalyst.velocity.listener.LeaveListener
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

interface CatalystApi {
    val module: Module

    companion object
}

fun CatalystApi.Companion.create(injector: Injector): CatalystApi {
    val velocityModule = module {
        single<Registry> { EnvironmentRegistry(prefix = "CATALYST") }
        singleOf(::LuckpermsService)
        singleOf(::ChatFilter)
        singleOf(ChatChannelBuilderImpl::Factory) { bind<ChatChannel.Builder.Factory>() }
        singleOf(ChannelMessageBuilderImpl::Factory) { bind<ChannelMessage.Builder.Factory>() }
        singleOf(::ChannelServiceImpl) { bind<ChannelService>() }
        singleOf(::ChatServiceImpl) { bind<ChatService>() }
        singleOf(::NicknameCommandFactory)
        singleOf(::ChatListener)
        singleOf(::CatalystKeys)
        singleOf(::CommandListener)
        singleOf(::JoinListener)
        singleOf(::LeaveListener)
        singleOf(::DiscordListener)
        singleOf(::JDAService)
        singleOf(::StaffListService)
        singleOf(::MinecraftUserRepositoryImpl) { bind<MinecraftUserRepository>() }
        singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    }

    return object : CatalystApi {
        override val module: Module = velocityModule
    }
}
