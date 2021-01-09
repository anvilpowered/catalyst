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

package org.anvilpowered.catalyst.velocity.service;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.anvilpowered.catalyst.api.discord.DiscordCommandService;
import org.anvilpowered.catalyst.velocity.discord.DiscordCommandSource;
import org.slf4j.Logger;

public class VelocityDiscordCommandService implements DiscordCommandService {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private DiscordCommandSource discordCommandSource;

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    private Logger logger;

    private String channelId;

    @Override
    public void executeDiscordCommand(String command) {
        proxyServer.getScheduler().buildTask(pluginContainer, () -> {
            proxyServer.getCommandManager().executeAsync(discordCommandSource, command);
            logger.info("Discord: " + command);
        }).schedule();
    }

    @Override
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String getChannelId() {
        return channelId;
    }
}
