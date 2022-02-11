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

package org.anvilpowered.catalyst.velocity.service

import com.google.inject.Inject
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.ProxyServer
import org.anvilpowered.catalyst.api.discord.DiscordCommandService
import org.anvilpowered.catalyst.velocity.discord.DiscordCommandSource
import org.slf4j.Logger

class VelocityDiscordCommandService @Inject constructor(
  private val proxyServer: ProxyServer,
  private val discordCommandSource: DiscordCommandSource,
  private val pluginContainer: PluginContainer,
  private val logger: Logger
) : DiscordCommandService {

  private var channelId: String? = null

  override fun executeDiscordCommand(command: String) {
    proxyServer.scheduler.buildTask(pluginContainer) {
      proxyServer.commandManager.executeAsync(discordCommandSource, command)
      logger.info("Discord: $command")
    }.schedule()
  }

  override fun setChannelId(channelId: String) {
    this.channelId = channelId
  }

  override fun getChannelId(): String {
    return channelId!!
  }
}
