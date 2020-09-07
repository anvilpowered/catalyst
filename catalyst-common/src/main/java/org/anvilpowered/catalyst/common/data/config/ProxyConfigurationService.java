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
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Singleton
public class ProxyConfigurationService extends BaseConfigurationService {

    @Inject
    private Logger logger;

    @Inject
    public ProxyConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
        setName(CatalystKeys.CHAT_FILTER_SWEARS, "chat.filter.swears");
        setName(CatalystKeys.CHAT_FILTER_EXCEPTIONS, "chat.filter.exceptions");
        setName(CatalystKeys.CHAT_FILTER_ENABLED, "chat.filter.enabled");
        setName(CatalystKeys.FIRST_JOIN, "join.firstJoin");
        setName(CatalystKeys.JOIN_MESSAGE, "join.message");
        setName(CatalystKeys.LEAVE_MESSAGE, "leave.message");
        setName(CatalystKeys.PROXY_CHAT_FORMAT_MESSAGE, "chat.format.message");
        setName(CatalystKeys.PROXY_CHAT_FORMAT_HOVER, "chat.format.hover");
        setName(CatalystKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND, "chat.format.click");
        setName(CatalystKeys.PROXY_CHAT_ENABLED, "modules.chat");
        setName(CatalystKeys.TAB_ENABLED, "modules.tab");
        setName(CatalystKeys.TAB_FORMAT, "tab.format.player");
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
        setName(CatalystKeys.DISCORD_LEAVE_FORMAT, "discord.format.leave");
        setName(CatalystKeys.DISCORD_CHAT_FORMAT, "discord.format.discord");
        setName(CatalystKeys.DISCORD_STAFF_FORMAT, "discord.format.staff");
        setName(CatalystKeys.TOPIC_FORMAT, "discord.topic.format");
        setName(CatalystKeys.TOPIC_UPDATE_ENABLED, "discord.topic.enabled");
        setName(CatalystKeys.TOPIC_UPDATE_DELAY, "discord.topic.updateInterval");
        setName(CatalystKeys.TOPIC_NO_ONLINE_PLAYERS, "discord.topic.noPlayers");
        setName(CatalystKeys.NOW_PLAYING_MESSAGE, "discord.format.playing");
        setName(CatalystKeys.DISCORD_HOVER_MESSAGE, "discord.format.hover");
        setName(CatalystKeys.WEBHOOK_URL, "discord.url.webhook");
        setName(CatalystKeys.DISCORD_URL, "discord.url.invite");
        setName(CatalystKeys.DISCORD_ENABLE, "modules.discord");
        setName(CatalystKeys.SERVER_PING, "ping.mode");
        setName(CatalystKeys.SERVER_PING_MESSAGE, "ping.message");
        setName(CatalystKeys.MOTD, "motd.message");
        setName(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED, "modules.advancedServerInfo");
        setName(CatalystKeys.ADVANCED_SERVER_INFO, "advanced.server.info");
        setName(CatalystKeys.EMOJI_ENABLE, "chat.emoji.enabled");
        setName(CatalystKeys.VIA_VERSION_ENABLED, "advanced.viaversion");
        setName(CatalystKeys.BAN_COMMAND_ENABLED, "commands.ban");
        setName(CatalystKeys.BROADCAST_COMMAND_ENABLED, "commands.broadcast");
        setName(CatalystKeys.NICKNAME_COMMAND_ENABLED, "commands.nickname");
        setName(CatalystKeys.FIND_COMMAND_ENABLED, "commands.find");
        setName(CatalystKeys.INFO_COMMAND_ENABLED, "commands.info");
        setName(CatalystKeys.KICK_COMMAND_ENABLED, "commands.kick");
        setName(CatalystKeys.LIST_COMMAND_ENABLED, "commands.list");
        setName(CatalystKeys.MESSAGE_COMMAND_ENABLED, "commands.message");
        setName(CatalystKeys.SEND_COMMAND_ENABLED, "commands.send");
        setName(CatalystKeys.SERVER_COMMAND_ENABLED, "commands.server.enabled");
        setName(CatalystKeys.SOCIALSPY_COMMAND_ENABLED, "commands.socialspy");
        setName(CatalystKeys.STAFFCHAT_COMMAND_ENABLED, "commands.staffchat");
        setName(CatalystKeys.MUTE_COMMAND_ENABLED, "commands.mute");
        setName(CatalystKeys.ADVANCED_ROOT, "advanced");
        setName(CatalystKeys.COMMANDS_ROOT, "commands");
        setName(CatalystKeys.CHAT_ROOT, "chat");
        setName(CatalystKeys.DISCORD_ROOT, "discord");
        setName(CatalystKeys.JOIN_ROOT, "join");
        setName(CatalystKeys.LEAVE_ROOT, "leave");
        setName(CatalystKeys.MODULES_ROOT, "modules");
        setName(CatalystKeys.MOTD_ROOT, "motd");
        setName(CatalystKeys.PING_ROOT, "ping");
        setName(CatalystKeys.TAB_ROOT, "tab");
        setName(CatalystKeys.COMMAND_LOGGING_ENABLED, "modules.logging.command.enabled");
        setName(CatalystKeys.COMMAND_LOGGING_FILTER, "modules.logging.command.filter");
        setName(CatalystKeys.ENABLE_PER_SERVER_PERMS, "commands.server.permissions");
        setName(CatalystKeys.MOTD_ENABLED, "modules.motd");
        setDescription(CatalystKeys.CHAT_FILTER_SWEARS,
            "\nList of words you would like filtered out of chat.");
        setDescription(CatalystKeys.CHAT_FILTER_EXCEPTIONS,
            "\nList of words that are caught by the swear detection, " +
                "but shouldn't be. (ex. A player name that contains 'ass'");
        setDescription(CatalystKeys.CHAT_FILTER_ENABLED,
            "\nEnable or disable the chat filter. (true = enabled)");
        setDescription(CatalystKeys.FIRST_JOIN,
            "\nFormat for the message that is displayed when a player joins the proxy for the first time");
        setDescription(CatalystKeys.JOIN_MESSAGE,
            "\nFormat for the message that is displayed when a player joins the proxy");
        setDescription(CatalystKeys.LEAVE_MESSAGE,
            "\nFormat for the message that is displayed when a player leaves the proxy");
        setDescription(CatalystKeys.PROXY_CHAT_FORMAT_MESSAGE,
            "\nFormat for the proxy-wide chat");
        setDescription(CatalystKeys.PROXY_CHAT_FORMAT_HOVER,
            "\nFormat for the hover message");
        setDescription(CatalystKeys.PROXY_CHAT_FORMAT_CLICK_COMMAND,
            "\nFormat for click event's");
        setDescription(CatalystKeys.PROXY_CHAT_ENABLED,
            "\nEnable or Disable the proxy-wide chat. (true = enabled)");
        setDescription(CatalystKeys.TAB_ENABLED,
            "\nEnable or Disable the global tab. (true = enabled)");
        setDescription(CatalystKeys.TAB_HEADER,
            "\nFormat for the tab header");
        setDescription(CatalystKeys.TAB_FOOTER,
            "\nFormat for the tab footer");
        setDescription(CatalystKeys.TAB_FORMAT,
            "\nFormat for how each player is displayed in the tab");
        setDescription(CatalystKeys.TAB_FORMAT_CUSTOM,
            "\nFormat for extra information that can be displayed in the tab.");
        setDescription(CatalystKeys.TAB_UPDATE,
            "\nTime setting for how often the tab updates in seconds");
        setDescription(CatalystKeys.CHAT_CHANNELS,
            "\nChat Channels");
        setDescription(CatalystKeys.CHAT_DEFAULT_CHANNEL,
            "\nDefault chat channel");
        setDescription(CatalystKeys.BOT_TOKEN,
            "\nToken for the discord bot that you would like to use when sending messages to discord " +
                "\n For help creating a bot token, please see the wiki");
        setDescription(CatalystKeys.MAIN_CHANNEL,
            "\nChannel id of the main channel that you would like in-game chat sent to.");
        setDescription(CatalystKeys.STAFF_CHANNEL,
            "\nChannel id of the staff-chat channel that you would like staff chat messages sent to.");
        setDescription(CatalystKeys.PLAYER_CHAT_FORMAT,
            "\nFormat of the message to be sent to discord from in-game");
        setDescription(CatalystKeys.JOIN_FORMAT,
            "\nMessage to be sent to the discord each time a player joins.");
        setDescription(CatalystKeys.DISCORD_LEAVE_FORMAT,
            "\nMessage to be sent to the discord each time a player leaves.");
        setDescription(CatalystKeys.DISCORD_CHAT_FORMAT,
            "\nFormat of the message being sent from discord to in-game");
        setDescription(CatalystKeys.DISCORD_STAFF_FORMAT,
            "\nFormat of the message being sent from staff chat to in-game");
        setDescription(CatalystKeys.TOPIC_FORMAT,
            "\nFormat of the main channel topic updater");
        setDescription(CatalystKeys.TOPIC_UPDATE_DELAY,
            "\nHow often you would like the topic to be updated in minutes. Setting this below " +
                "the default will result in constant RateLimit issues. If you experience issues " +
                "with 5 minutes, set this to 10 or higher.");
        setDescription(CatalystKeys.TOPIC_NO_ONLINE_PLAYERS,
            "\nThis message will be shown when there are no players online.");
        setDescription(CatalystKeys.NOW_PLAYING_MESSAGE,
            "\nThe message being shown as the \"now playing\" for the discord bot.");
        setDescription(CatalystKeys.DISCORD_HOVER_MESSAGE,
            "\nThe message being shown when a player hovers over a message sent from discord");
        setDescription(CatalystKeys.WEBHOOK_URL,
            "\nURL that gets the player avatar when sending messages to discord");
        setDescription(CatalystKeys.DISCORD_URL,
            "\nDiscord invite link that allows players to join the discord");
        setDescription(CatalystKeys.DISCORD_ENABLE,
            "\nEnable or Disable the discord bot (false = disabled).");
        setDescription(CatalystKeys.SERVER_PING,
            "\nSpecify what you would like to be shown in the server list, either PLAYERS or MESSAGE");
        setDescription(CatalystKeys.SERVER_PING_MESSAGE,
            "\nThis message is shown when players hover over the player count. " +
                "\nThis message will only appear when the mode is set to MESSAGE");
        setDescription(CatalystKeys.MOTD,
            "\nDefault MOTD that catalyst uses if the advanced server information is disabled.");
        setDescription(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED,
            "\nEnable to disable advanced server information settings");
        setDescription(CatalystKeys.ADVANCED_SERVER_INFO,
            "\nThis config option is for users that would like to have multiple servers," +
                " \neach with differing MOTD and/or Mods \n" +
                "Note: To do this, the IP specified MUST correlate to the IP defined in the forced host section\n" +
                "of the velocity config. Also, your players must use the IP defined in this config to connect");
        setDescription(CatalystKeys.EMOJI_ENABLE,
            "\nEnable or disable emojis in chat" +
                "\nEnabling emojis means that you will have to have your players download a select texture pack, found on the github page." +
                "\n(true = enabled)");
        setDescription(CatalystKeys.VIA_VERSION_ENABLED,
            "\nIf you are running servers with ViaVersion, this will disable the version checking done in the /server command." +
                "\n(true = enabled)");
        setDescription(CatalystKeys.BAN_COMMAND_ENABLED,
            "\nToggle ban command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.BAN_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.BROADCAST_COMMAND_ENABLED,
            "\nToggle broadcast command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.BROADCAST_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.NICKNAME_COMMAND_ENABLED,
            "\nToggle nickname command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nBase Permission : " + CatalystKeys.NICKNAME_PERMISSION.getFallbackValue() +
                "\nColor Permission : " + CatalystKeys.NICKNAME_COLOR_PERMISSION.getFallbackValue() +
                "\nMagic Permission : " + CatalystKeys.NICKNAME_MAGIC_PERMISSION.getFallbackValue() +
                "\nOther permission : " + CatalystKeys.NICKNAME_OTHER_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.FIND_COMMAND_ENABLED,
            "\nToggle find command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.FIND_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.INFO_COMMAND_ENABLED,
            "\nToggle info command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.INFO_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.KICK_COMMAND_ENABLED,
            "\nToggle kick command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.KICK_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.LIST_COMMAND_ENABLED,
            "\nToggle list command handling " +
                "\n(true = enabled | false = disabled)");
        setDescription(CatalystKeys.MESSAGE_COMMAND_ENABLED,
            "\nToggle message command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.MESSAGE_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.SEND_COMMAND_ENABLED,
            "\nToggle send command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.SEND_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.SERVER_COMMAND_ENABLED,
            "\nToggle server command handling " +
                "\n(true = enabled | false = disabled)");
        setDescription(CatalystKeys.SOCIALSPY_COMMAND_ENABLED,
            "\nToggle SocialSpy command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.SOCIALSPY_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.STAFFCHAT_COMMAND_ENABLED,
            "\nToggle staffchat command handling" +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.STAFFCHAT_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.MUTE_COMMAND_ENABLED,
            "\nToggle mute command handling " +
                "\n(true = enabled | false = disabled)" +
                "\nPermission : " + CatalystKeys.MUTE_PERMISSION.getFallbackValue());
        setDescription(CatalystKeys.COMMAND_LOGGING_ENABLED,
            "Toggle command logging " +
                "\n(true = enabled | false = disabled)");
        setDescription(CatalystKeys.COMMAND_LOGGING_FILTER,
            "Filter commands that you want logged to the console." +
                "\nBy default, catalyst will log all commands.");
        setDescription(CatalystKeys.ENABLE_PER_SERVER_PERMS,
            "Enforces the following permission check" +
                "\ncatalyst.server.<name> when a player uses the /server command." +
                "\nFor example, if the target server is called lobby, catalyst will test" +
                "\nthe permission catalyst.server.lobby");
        setDescription(CatalystKeys.MOTD_ENABLED,
            "Toggle MOTD handling" +
                "\nBy default, Catalyst will not control the MOTD");
        setDescription(CatalystKeys.ADVANCED_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Advanced                         |\n" +
                " |------------------------------------------------------------| "
        );
        setDescription(CatalystKeys.COMMANDS_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Commands                         |\n" +
                " |------------------------------------------------------------| "
        );
        setDescription(CatalystKeys.CHAT_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Chat                             |\n" +
                " |------------------------------------------------------------| "
        );
        setDescription(CatalystKeys.DISCORD_ROOT,
            " |------------------------------------------------------------|\n"
                + " |                           Discord                          |\n"
                + " |------------------------------------------------------------|"
        );
        setDescription(CatalystKeys.JOIN_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Join                             |\n" +
                " |------------------------------------------------------------| "
        );
        setDescription(CatalystKeys.LEAVE_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Leave                            |\n" +
                " |------------------------------------------------------------| "
        );
        setDescription(CatalystKeys.MODULES_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Modules                          |\n" +
                " |------------------------------------------------------------| "
        );
        setDescription(CatalystKeys.MOTD_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Motd                             |\n" +
                " |------------------------------------------------------------| "
        );
        setDescription(CatalystKeys.PING_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Ping                             |\n" +
                " |------------------------------------------------------------| "
        );
        setDescription(CatalystKeys.TAB_ROOT,
            " |------------------------------------------------------------|\n" +
                " |                           Tab                              |\n" +
                " |------------------------------------------------------------| "
        );
        Map<Predicate<String>, Function<String, String>> pingMessageMap = new HashMap<>();
        pingMessageMap.put(message -> message.contains("&"),
            message -> {
                logger.warn("Replacing all '&' with '\u00a7' in the config for the hover message.");
                return message.replaceAll("&", "\u00a7");
            });
        setVerification(CatalystKeys.SERVER_PING_MESSAGE, pingMessageMap);
    }
}
