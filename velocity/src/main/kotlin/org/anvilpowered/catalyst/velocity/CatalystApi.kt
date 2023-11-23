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
import org.anvilpowered.catalyst.api.chat.ChannelService
import org.anvilpowered.catalyst.api.chat.LuckpermsService
import org.anvilpowered.catalyst.api.user.DiscordUserRepository
import org.anvilpowered.catalyst.api.user.GameUserRepository
import org.anvilpowered.catalyst.api.user.UserRepository
import org.anvilpowered.catalyst.velocity.chat.ChannelServiceImpl
import org.anvilpowered.catalyst.velocity.chat.ChatFilter
import org.anvilpowered.catalyst.velocity.chat.ChatService
import org.anvilpowered.catalyst.velocity.chat.ChatServiceImpl
import org.anvilpowered.catalyst.velocity.chat.builder.ChatChannelBuilderImpl
import org.anvilpowered.catalyst.velocity.chat.builder.ChatMessageBuilderImpl
import org.anvilpowered.catalyst.velocity.db.user.DiscordUserRepositoryImpl
import org.anvilpowered.catalyst.velocity.db.user.GameUserRepositoryImpl
import org.anvilpowered.catalyst.velocity.db.user.UserRepositoryImpl
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
        single<Registry> { EnvironmentRegistry() }
        singleOf(::LuckpermsService)
        singleOf(::ChatFilter)
        singleOf(ChatChannelBuilderImpl::Factory)
        singleOf(ChatMessageBuilderImpl::Factory)
        singleOf(::ChannelServiceImpl) {
            bind<ChannelService>()
        }
        singleOf(::ChatServiceImpl) {
            bind<ChatService>()
        }
        single<DiscordUserRepository> { DiscordUserRepositoryImpl }
        single<GameUserRepository> { GameUserRepositoryImpl }
        single<UserRepository> { UserRepositoryImpl }
    }

    return object : CatalystApi {
        override val module: Module = velocityModule
    }
}
