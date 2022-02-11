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

import com.google.inject.AbstractModule
import com.google.inject.TypeLiteral
import com.google.inject.name.Names
import dev.morphia.Datastore
import org.anvilpowered.anvil.api.misc.bind
import org.anvilpowered.anvil.api.misc.to
import org.anvilpowered.anvil.api.misc.withMongoDB
import org.anvilpowered.anvil.api.misc.withXodus
import org.anvilpowered.anvil.api.plugin.PluginInfo
import org.anvilpowered.catalyst.api.member.MemberManager
import org.anvilpowered.catalyst.api.member.MemberRepository
import org.anvilpowered.catalyst.api.plugin.PluginMessages
import org.anvilpowered.catalyst.api.service.ChatFilter
import org.anvilpowered.catalyst.common.member.CommonMemberManager
import org.anvilpowered.catalyst.common.member.CommonMongoMemberRepository
import org.anvilpowered.catalyst.common.member.CommonXodusMemberRepository
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo
import org.anvilpowered.catalyst.common.plugin.CatalystPluginMessages
import org.anvilpowered.catalyst.common.registry.CommonConfigurationService
import org.anvilpowered.catalyst.common.service.CommonChatFilter
import org.anvilpowered.registry.ConfigurationService
import org.anvilpowered.registry.Registry
import org.bson.types.ObjectId
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.loader.ConfigurationLoader
import java.nio.file.Paths

@Suppress("UnstableApiUsage")
open class CommonModule(private val configDir: String) : AbstractModule() {

    override fun configure() {
        val configDirFull = Paths.get("$configDir/catalyst").toFile()
        if (!configDirFull.exists()) {
            check(configDirFull.mkdirs()) { "Unable to create config directory" }
        }
        bind(object : TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {}).toInstance(
            HoconConfigurationLoader.builder().path(Paths.get("$configDirFull/catalyst.conf")).build()
        )
        with(binder()) {
            bind<MemberRepository<*, *>>()
                .annotatedWith(Names.named("mongodb"))
                .to<CommonMongoMemberRepository>()
            bind<MemberRepository<ObjectId, Datastore>>()
                .to<CommonMongoMemberRepository>()
            bind<MemberRepository<*, *>>()
                .annotatedWith(Names.named("xodus"))
                .to<CommonXodusMemberRepository>()
            withMongoDB()
            withXodus()

            bind<PluginInfo>().to<CatalystPluginInfo>()
            bind<PluginMessages>().to<CatalystPluginMessages>()
            bind<ChatFilter>().to<CommonChatFilter>()
            bind<ConfigurationService>().to<CommonConfigurationService>()
        }
        bind(ChatFilter::class.java).to(CommonChatFilter::class.java)
        bind(ConfigurationService::class.java).to(CommonConfigurationService::class.java)
        bind(Registry::class.java).to(CommonConfigurationService::class.java)
    }
}
