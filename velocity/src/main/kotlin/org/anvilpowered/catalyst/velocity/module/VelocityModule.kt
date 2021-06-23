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

package org.anvilpowered.catalyst.velocity.module

import com.google.inject.Singleton
import com.google.inject.TypeLiteral
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.TextComponent
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.anvilpowered.anvil.api.command.CommandNode
import org.anvilpowered.anvil.api.registry.ConfigurationService
import org.anvilpowered.catalyst.api.discord.DiscordCommandService
import org.anvilpowered.catalyst.api.service.BroadcastService
import org.anvilpowered.catalyst.common.module.CommonModule
import org.anvilpowered.catalyst.common.plugin.CatalystPluginInfo
import org.anvilpowered.catalyst.common.registry.CommonConfigurationService
import org.anvilpowered.catalyst.velocity.command.VelocityCommandNode
import org.anvilpowered.catalyst.velocity.service.VelocityBroadcastService
import org.anvilpowered.catalyst.velocity.service.VelocityDiscordCommandService
import java.nio.file.Paths

@Singleton
class VelocityModule : CommonModule<Player, TextComponent, CommandSource>() {
  override fun configure() {
    super.configure()
    bind(object : TypeLiteral<CommandNode<CommandSource>>() {}).to(VelocityCommandNode::class.java)
    val configFilesLocation = Paths.get("plugins/" + CatalystPluginInfo.id).toFile()
    if (!configFilesLocation.exists()) {
      check(configFilesLocation.mkdirs()) { "Unable to create config directory" }
    }
    bind(object : TypeLiteral<ConfigurationLoader<CommentedConfigurationNode>>() {
    }).toInstance(
      HoconConfigurationLoader.builder()
        .setPath(Paths.get("$configFilesLocation/catalyst.conf"))
        .build()
    )
    bind(object : TypeLiteral<BroadcastService<TextComponent>>() {
    }).to(VelocityBroadcastService::class.java)
    bind(DiscordCommandService::class.java).to(VelocityDiscordCommandService::class.java)
  }
}
