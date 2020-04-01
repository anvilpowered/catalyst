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

package org.anvilpowered.catalyst.common.data.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.anvilpowered.anvil.base.data.config.BaseConfigurationService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;

@Singleton
public class CatalystConfigurationService extends BaseConfigurationService {

    @Inject
    public CatalystConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }

    @Override
    protected void initNodeNameMap() {
        setName(CatalystKeys.MOTD, "motd");
        setName(CatalystKeys.CHAT_FILTER_SWEARS, "chat.filter.swears");
        setName(CatalystKeys.CHAT_FILTER_EXCEPTIONS, "chat.filter.exceptions");
        setName(CatalystKeys.CHAT_FILTER_ENABLED, "chat.filter.enabled");
        setName(CatalystKeys.FIRST_JOIN, "message.firstJoin");
        setName(CatalystKeys.JOIN_MESSAGE, "message.join");
        setName(CatalystKeys.LEAVE_MESSAGE, "message.leave");
        setName(CatalystKeys.PROXY_CHAT_FORMAT_MESSAGE, "chat.format.message");
        setName(CatalystKeys.PROXY_CHAT_FORMAT_HOVER, "chat.format.hover");
        setName(CatalystKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND, "chat.format.click");
        setName(CatalystKeys.PROXY_CHAT_ENABLED, "chat.proxy.enabled");
        setName(CatalystKeys.SERVER_COMMAND, "command.server");
        setName(CatalystKeys.TAB_ENABLED, "tab.enabled");
        setName(CatalystKeys.TAB_HEADER, "tab.format.header");
        setName(CatalystKeys.TAB_FOOTER, "tab.format.footer");
        setName(CatalystKeys.TAB_FORMAT_CUSTOM, "tab.format.custom");
        setName(CatalystKeys.TAB_UPDATE, "tab.updateDelay");
        setName(CatalystKeys.CHAT_CHANNELS, "chat.channels");
        setName(CatalystKeys.CHAT_DEFAULT_CHANNEL, "chat.channelDefault");
        setName(CatalystKeys.BOT_TOKEN, "discord.bot.token");
        setName(CatalystKeys.MAIN_CHANNEL, "discord.channel.main");
        setName(CatalystKeys.STAFF_CHANNEL, "discord.channel.staff");
        setName(CatalystKeys.PLAYER_CHAT_FORMAT, "discord.format.proxy");
        setName(CatalystKeys.JOIN_FORMAT, "discord.format.join");
        setName(CatalystKeys.LEAVE_FORMAT, "discord.format.leave");
        setName(CatalystKeys.DISCORD_CHAT_FORMAT, "discord.format.discord");
        setName(CatalystKeys.DISCORD_STAFF_FORMAT, "discord.format.staff");
        setName(CatalystKeys.TOPIC_FORMAT, "discord.format.topic");
        setName(CatalystKeys.TOPIC_UPDATE_DELAY, "discord.time.topicUpdateFrequency");
        setName(CatalystKeys.NOW_PLAYING_MESSAGE, "discord.format.playing");
        setName(CatalystKeys.DISCORD_HOVER_MESSAGE, "discord.format.hover");
        setName(CatalystKeys.WEBHOOK_URL, "discord.url.webhook");
        setName(CatalystKeys.DISCORD_URL, "discord.url.invite");
        setName(CatalystKeys.DISCORD_ENABLE, "discord.enabled");
        setName(CatalystKeys.SERVER_PING, "ping.format");
    }

    @Override
    protected void initNodeDescriptionMap() {
        setDescription(CatalystKeys.MOTD, "\nServer MOTD that is displayed when the proxy is pinged.");
        setDescription(CatalystKeys.CHAT_FILTER_SWEARS, "\nList of words you would like filtered out of chat.");
        setDescription(CatalystKeys.CHAT_FILTER_EXCEPTIONS, "\nList of words that are caught by the swear detection, but shouldn't be. (ex. A player name that contains 'ass'");
        setDescription(CatalystKeys.CHAT_FILTER_ENABLED, "\nEnable or disable the chat filter. (true = enabled)");
        setDescription(CatalystKeys.FIRST_JOIN, "\nFormat for the message that is displayed when a player joins the proxy for the first time");
        setDescription(CatalystKeys.JOIN_MESSAGE, "\nFormat for the message that is displayed when a player joins the proxy");
        setDescription(CatalystKeys.LEAVE_MESSAGE, "\nFormat for the message that is displayed when a player leaves the proxy");
        setDescription(CatalystKeys.PROXY_CHAT_FORMAT_MESSAGE, "\nFormat for the proxy-wide chat");
        setDescription(CatalystKeys.PROXY_CHAT_FORMAT_HOVER, "\nFormat for the hover message");
        setDescription(CatalystKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND, "\nFormat for click event's");
        setDescription(CatalystKeys.PROXY_CHAT_ENABLED, "\nEnable or Disable the proxy-wide chat. (true = enabled)");
        setDescription(CatalystKeys.SERVER_COMMAND, "\nEnable or Disable the /(servername) command. (true = enabled)");
        setDescription(CatalystKeys.TAB_ENABLED, "\nEnable or Disable the global tab. (true = enabled)");
        setDescription(CatalystKeys.TAB_HEADER, "\nFormat for the tab header");
        setDescription(CatalystKeys.TAB_FOOTER, "\nFormat for the tab footer");
        setDescription(CatalystKeys.TAB_FORMAT, "\nFormat for how each player is displayed in the tab");
        setDescription(CatalystKeys.TAB_FORMAT_CUSTOM, "\nFormat for extra information that can be displayed in the tab.");
        setDescription(CatalystKeys.TAB_UPDATE, "\nTime setting for how often the tab updates in seconds");
        setDescription(CatalystKeys.CHAT_CHANNELS, "\nChat Channels");
        setDescription(CatalystKeys.CHAT_DEFAULT_CHANNEL, "\nDefault chat channel");
        setDescription(CatalystKeys.BOT_TOKEN, "\nToken for the discord bot that you would like to use when sending messages to discord \n For help creating a bot token, please see the wiki");
        setDescription(CatalystKeys.MAIN_CHANNEL, "\nChannel id of the main channel that you would like in-game chat sent to.");
        setDescription(CatalystKeys.STAFF_CHANNEL, "\nChannel id of the staff-chat channel that you would like staff chat messages sent to.");
        setDescription(CatalystKeys.PLAYER_CHAT_FORMAT, "\nFormat of the message to be sent to discord from in-game");
        setDescription(CatalystKeys.JOIN_FORMAT, "\nMessage to be sent to the discord each time a player joins.");
        setDescription(CatalystKeys.LEAVE_FORMAT, "\nMessage to be sent to the discord each time a player leaves.");
        setDescription(CatalystKeys.DISCORD_CHAT_FORMAT, "\nFormat of the message being sent from discord to in-game");
        setDescription(CatalystKeys.DISCORD_STAFF_FORMAT, "\nFormat of the message being sent from staff chat to in-game");
        setDescription(CatalystKeys.TOPIC_FORMAT, "\nFormat of the main channel topic updater");
        setDescription(CatalystKeys.TOPIC_UPDATE_DELAY, "\nHow often you would like the topic to be updated in seconds");
        setDescription(CatalystKeys.NOW_PLAYING_MESSAGE, "\nThe message being shown as the \"now playing\" for the discord bot.");
        setDescription(CatalystKeys.DISCORD_HOVER_MESSAGE, "\nThe message being shown when a player hovers over a message sent from discord");
        setDescription(CatalystKeys.WEBHOOK_URL, "\nURL that gets the player avatar when sending messages to discord");
        setDescription(CatalystKeys.DISCORD_URL, "\nDiscord invite link that allows players to join the discord");
        setDescription(CatalystKeys.DISCORD_ENABLE, "\nEnable or Disable the discord bot (false = disabled).");
        setDescription(CatalystKeys.SERVER_PING, "\nSpecify what you would like to be shown in the server list, either PLAYERS or MESSAGE");
    }
}
