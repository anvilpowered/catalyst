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
        nodeNameMap.put(CatalystKeys.MOTD, "motd");
        nodeNameMap.put(CatalystKeys.CHAT_FILTER_SWEARS, "chat.filter.swears");
        nodeNameMap.put(CatalystKeys.CHAT_FILTER_EXCEPTIONS, "chat.filter.exceptions");
        nodeNameMap.put(CatalystKeys.CHAT_FILTER_ENABLED, "chat.filter.enabled");
        nodeNameMap.put(CatalystKeys.FIRST_JOIN, "message.firstJoin");
        nodeNameMap.put(CatalystKeys.JOIN_MESSAGE, "message.join");
        nodeNameMap.put(CatalystKeys.LEAVE_MESSAGE, "message.leave");
        nodeNameMap.put(CatalystKeys.PROXY_CHAT_FORMAT_MESSAGE, "chat.format.message");
        nodeNameMap.put(CatalystKeys.PROXY_CHAT_FORMAT_HOVER, "chat.format.hover");
        nodeNameMap.put(CatalystKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND, "chat.format.click");
        nodeNameMap.put(CatalystKeys.PROXY_CHAT_ENABLED, "chat.proxy.enabled");
        nodeNameMap.put(CatalystKeys.SERVER_COMMAND, "command.server");
        nodeNameMap.put(CatalystKeys.TAB_ENABLED, "tab.enabled");
        nodeNameMap.put(CatalystKeys.TAB_HEADER, "tab.format.header");
        nodeNameMap.put(CatalystKeys.TAB_FOOTER, "tab.format.footer");
        nodeNameMap.put(CatalystKeys.TAB_FORMAT_CUSTOM, "tab.format.custom");
        nodeNameMap.put(CatalystKeys.TAB_UPDATE, "tab.updateDelay");
        nodeNameMap.put(CatalystKeys.CHAT_CHANNELS, "chat.channels");
        nodeNameMap.put(CatalystKeys.CHAT_DEFAULT_CHANNEL, "chat.channelDefault");
        nodeNameMap.put(CatalystKeys.BOT_TOKEN, "discord.bot.token");
        nodeNameMap.put(CatalystKeys.MAIN_CHANNEL, "discord.channel.main");
        nodeNameMap.put(CatalystKeys.STAFF_CHANNEL, "discord.channel.staff");
        nodeNameMap.put(CatalystKeys.PLAYER_CHAT_FORMAT, "discord.format.proxy");
        nodeNameMap.put(CatalystKeys.JOIN_FORMAT, "discord.format.join");
        nodeNameMap.put(CatalystKeys.LEAVE_FORMAT, "discord.format.leave");
        nodeNameMap.put(CatalystKeys.DISCORD_CHAT_FORMAT, "discord.format.discord");
        nodeNameMap.put(CatalystKeys.DISCORD_STAFF_FORMAT, "discord.format.staff");
        nodeNameMap.put(CatalystKeys.TOPIC_FORMAT, "discord.format.topic");
        nodeNameMap.put(CatalystKeys.TOPIC_UPDATE_DELAY, "discord.time.topicUpdateFrequency");
        nodeNameMap.put(CatalystKeys.NOW_PLAYING_MESSAGE, "discord.format.playing");
        nodeNameMap.put(CatalystKeys.WEBHOOK_URL, "discord.url.webhook");
        nodeNameMap.put(CatalystKeys.DISCORD_URL, "discord.url.invite");
        nodeNameMap.put(CatalystKeys.DISCORD_ENABLE, "discord.enabled");
        ;
    }

    @Override
    protected void initNodeDescriptionMap() {
        nodeDescriptionMap.put(CatalystKeys.MOTD, "\nServer MOTD that is displayed when the proxy is pinged.");
        nodeDescriptionMap.put(CatalystKeys.CHAT_FILTER_SWEARS, "\nList of words you would like filtered out of chat.");
        nodeDescriptionMap.put(CatalystKeys.CHAT_FILTER_EXCEPTIONS, "\nList of words that are caught by the swear detection, but shouldn't be. (ex. A player name that contains 'ass'");
        nodeDescriptionMap.put(CatalystKeys.CHAT_FILTER_ENABLED, "\nEnable or disable the chat filter. (true = enabled)");
        nodeDescriptionMap.put(CatalystKeys.FIRST_JOIN, "\nFormat for the message that is displayed when a player joins the proxy for the first time");
        nodeDescriptionMap.put(CatalystKeys.JOIN_MESSAGE, "\nFormat for the message that is displayed when a player joins the proxy");
        nodeDescriptionMap.put(CatalystKeys.LEAVE_MESSAGE, "\nFormat for the message that is displayed when a player leaves the proxy");
        nodeDescriptionMap.put(CatalystKeys.PROXY_CHAT_FORMAT_MESSAGE, "\nFormat for the proxy-wide chat");
        nodeDescriptionMap.put(CatalystKeys.PROXY_CHAT_FORMAT_HOVER, "\nFormat for the hover message");
        nodeDescriptionMap.put(CatalystKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND, "\nFormat for click event's");
        nodeDescriptionMap.put(CatalystKeys.PROXY_CHAT_ENABLED, "\nEnable or Disable the proxy-wide chat. (true = enabled)");
        nodeDescriptionMap.put(CatalystKeys.SERVER_COMMAND, "\nEnable or Disable the /(servername) command. (true = enabled)");
        nodeDescriptionMap.put(CatalystKeys.TAB_ENABLED, "\nEnable or Disable the global tab. (true = enabled)");
        nodeDescriptionMap.put(CatalystKeys.TAB_HEADER, "\nFormat for the tab header");
        nodeDescriptionMap.put(CatalystKeys.TAB_FOOTER, "\nFormat for the tab footer");
        nodeDescriptionMap.put(CatalystKeys.TAB_FORMAT, "\nFormat for how each player is displayed in the tab");
        nodeDescriptionMap.put(CatalystKeys.TAB_FORMAT_CUSTOM, "\nFormat for extra information that can be displayed in the tab.");
        nodeDescriptionMap.put(CatalystKeys.TAB_UPDATE, "\nTime setting for how often the tab updates in seconds");
        nodeDescriptionMap.put(CatalystKeys.CHAT_CHANNELS, "\nChat Channels");
        nodeDescriptionMap.put(CatalystKeys.CHAT_DEFAULT_CHANNEL, "\nDefault chat channel");
        nodeDescriptionMap.put(CatalystKeys.BOT_TOKEN, "\nToken for the discord bot that you would like to use when sending messages to discord \n For help creating a bot token, please see the wiki");
        nodeDescriptionMap.put(CatalystKeys.MAIN_CHANNEL, "\nChannel id of the main channel that you would like in-game chat sent to.");
        nodeDescriptionMap.put(CatalystKeys.STAFF_CHANNEL, "\nChannel id of the staff-chat channel that you would like staff chat messages sent to.");
        nodeDescriptionMap.put(CatalystKeys.PLAYER_CHAT_FORMAT, "\nFormat of the message to be sent to discord from in-game");
        nodeDescriptionMap.put(CatalystKeys.JOIN_FORMAT, "\nMessage to be sent to the discord each time a player joins.");
        nodeDescriptionMap.put(CatalystKeys.LEAVE_FORMAT, "\nMessage to be sent to the discord each time a player leaves.");
        nodeDescriptionMap.put(CatalystKeys.DISCORD_CHAT_FORMAT, "\nFormat of the message being sent from discord to in-game");
        nodeDescriptionMap.put(CatalystKeys.DISCORD_STAFF_FORMAT, "\nFormat of the message being sent from staff chat to in-game");
        nodeDescriptionMap.put(CatalystKeys.TOPIC_FORMAT, "\nFormat of the main channel topic updater");
        nodeDescriptionMap.put(CatalystKeys.TOPIC_UPDATE_DELAY, "\nHow often you would like the topic to be updated in seconds");
        nodeDescriptionMap.put(CatalystKeys.NOW_PLAYING_MESSAGE, "\nThe message being shown as the \"now playing\" for the discord bot.");
        nodeDescriptionMap.put(CatalystKeys.WEBHOOK_URL, "\nURL that gets the player avatar when sending messages to discord");
        nodeDescriptionMap.put(CatalystKeys.DISCORD_URL, "\nDiscord invite link that allows players to join the discord");
        nodeDescriptionMap.put(CatalystKeys.DISCORD_ENABLE, "\nEnable or Disable the discord bot (false = disabled).");
    }
}
