/*
 *     MSEssentials - MilSpecSG
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

package rocks.milspecsg.msessentials.common.data.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msessentials.api.data.key.MSEssentialsKeys;
import rocks.milspecsg.anvil.common.data.config.CommonConfigurationService;

@Singleton
public class MSEssentialsConfigurationService extends CommonConfigurationService {

    @Inject
    public MSEssentialsConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }

    @Override
    protected void initNodeNameMap() {
        nodeNameMap.put(MSEssentialsKeys.MOTD, "motd");
        nodeNameMap.put(MSEssentialsKeys.CHAT_FILTER_SWEARS, "chat.filter.swears");
        nodeNameMap.put(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS, "chat.filter.exceptions");
        nodeNameMap.put(MSEssentialsKeys.CHAT_FILTER_ENABLED, "chat.filter.enabled");
        nodeNameMap.put(MSEssentialsKeys.FIRST_JOIN, "message.firstJoin");
        nodeNameMap.put(MSEssentialsKeys.JOIN_MESSAGE, "message.join");
        nodeNameMap.put(MSEssentialsKeys.LEAVE_MESSAGE, "message.leave");
        nodeNameMap.put(MSEssentialsKeys.PROXY_CHAT_FORMAT_MESSAGE, "chat.format.message");
        nodeNameMap.put(MSEssentialsKeys.PROXY_CHAT_FORMAT_HOVER, "chat.format.hover");
        nodeNameMap.put(MSEssentialsKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND, "chat.format.click");
        nodeNameMap.put(MSEssentialsKeys.PROXY_CHAT_ENABLED, "chat.proxy.enabled");
        nodeNameMap.put(MSEssentialsKeys.SERVER_COMMAND, "command.server");
        nodeNameMap.put(MSEssentialsKeys.TAB_ENABLED, "tab.enabled");
        nodeNameMap.put(MSEssentialsKeys.TAB_HEADER, "tab.format.header");
        nodeNameMap.put(MSEssentialsKeys.TAB_FOOTER, "tab.format.footer");
        nodeNameMap.put(MSEssentialsKeys.TAB_FORMAT_CUSTOM, "tab.format.custom");
        nodeNameMap.put(MSEssentialsKeys.TAB_UPDATE, "tab.updateDelay");
        nodeNameMap.put(MSEssentialsKeys.CHAT_CHANNELS, "chat.channels");
        nodeNameMap.put(MSEssentialsKeys.CHAT_DEFAULT_CHANNEL, "chat.channelDefault");
    }

    @Override
    protected void initNodeDescriptionMap() {
        nodeDescriptionMap.put(MSEssentialsKeys.MOTD, "\nServer MOTD that is displayed when the proxy is pinged.");
        nodeDescriptionMap.put(MSEssentialsKeys.CHAT_FILTER_SWEARS, "\nList of words you would like filtered out of chat.");
        nodeDescriptionMap.put(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS, "\nList of words that are caught by the swear detection, but shouldn't be. (ex. A player name that contains 'ass'");
        nodeDescriptionMap.put(MSEssentialsKeys.CHAT_FILTER_ENABLED, "\nEnable or disable the chat filter. (true = enabled)");
        nodeDescriptionMap.put(MSEssentialsKeys.FIRST_JOIN, "\nFormat for the message that is displayed when a player joins the proxy for the first time");
        nodeDescriptionMap.put(MSEssentialsKeys.JOIN_MESSAGE, "\nFormat for the message that is displayed when a player joins the proxy");
        nodeDescriptionMap.put(MSEssentialsKeys.LEAVE_MESSAGE, "\nFormat for the message that is displayed when a player leaves the proxy");
        nodeDescriptionMap.put(MSEssentialsKeys.PROXY_CHAT_FORMAT_MESSAGE, "\nFormat for the proxy-wide chat");
        nodeDescriptionMap.put(MSEssentialsKeys.PROXY_CHAT_FORMAT_HOVER, "\nFormat for the hover message");
        nodeDescriptionMap.put(MSEssentialsKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND, "\nFormat for click event's");
        nodeDescriptionMap.put(MSEssentialsKeys.PROXY_CHAT_ENABLED, "\nEnable or Disable the proxy-wide chat. (true = enabled)");
        nodeDescriptionMap.put(MSEssentialsKeys.SERVER_COMMAND, "\nEnable or Disable the /(servername) command. (true = enabled)");
        nodeDescriptionMap.put(MSEssentialsKeys.TAB_ENABLED, "\nEnable or Disable the global tab. (true = enabled)");
        nodeDescriptionMap.put(MSEssentialsKeys.TAB_HEADER, "\nFormat for the tab header");
        nodeDescriptionMap.put(MSEssentialsKeys.TAB_FOOTER, "\nFormat for the tab footer");
        nodeDescriptionMap.put(MSEssentialsKeys.TAB_FORMAT, "\nFormat for how each player is displayed in the tab");
        nodeDescriptionMap.put(MSEssentialsKeys.TAB_FORMAT_CUSTOM, "\nFormat for extra information that can be displayed in the tab.");
        nodeDescriptionMap.put(MSEssentialsKeys.TAB_UPDATE, "\nTime setting for how often the tab updates in seconds");
        nodeDescriptionMap.put(MSEssentialsKeys.CHAT_CHANNELS, "\nChat Channels");
        nodeDescriptionMap.put(MSEssentialsKeys.CHAT_DEFAULT_CHANNEL, "\nDefault chat channel");
    }
}
