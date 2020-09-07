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

package org.anvilpowered.catalyst.api.data.key;

import com.google.common.collect.ImmutableList;
import org.anvilpowered.anvil.api.data.key.Key;
import org.anvilpowered.anvil.api.data.key.Keys;
import org.anvilpowered.catalyst.api.data.config.AdvancedServerInfo;
import org.anvilpowered.catalyst.api.data.config.ChatChannel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class CatalystKeys {

    public static final Key<Boolean> CHAT_FILTER_ENABLED = new Key<Boolean>("CHAT_FILTER_ENABLED",
        false) {
    };
    public static final Key<List<String>> CHAT_FILTER_SWEARS = new Key<List<String>>(
        "CHAT_FILTER_SWEARS", Arrays.asList("fuck", "shit", "ass")) {
    };
    public static final Key<List<String>> CHAT_FILTER_EXCEPTIONS = new Key<List<String>>(
        "CHAT_FILTER_EXCEPTIONS",
        Arrays.asList("assassin", "jkass")) {
    };
    public static final Key<String> FIRST_JOIN = new Key<String>("FIRST_JOIN",
        "Welcome to the server, %player%") {
    };
    public static final Key<String> JOIN_MESSAGE = new Key<String>("JOIN_MESSAGE",
        "%player% has joined the proxy") {
    };
    public static final Key<String> LEAVE_MESSAGE = new Key<String>("LEAVE_MESSAGE",
        "%player% has left the proxy") {
    };
    public static final Key<Boolean> PROXY_CHAT_ENABLED = new Key<Boolean>(
        "PROXY_CHAT_ENABLED", true) {
    };
    public static final Key<String> PROXY_CHAT_FORMAT_MESSAGE = new Key<String>(
        "PROXY_CHAT_FORMAT_MESSAGE", "%prefix% %player% %suffix%: %message% ") {
    };
    public static final Key<String> PROXY_CHAT_FORMAT_HOVER = new Key<String>(
        "PROXY_CHAT_FORMAT_HOVER", "%player% %server%") {
    };
    public static final Key<String> PROXY_CHAT_FORMAT_CLICK_COMMAND = new Key<String>(
        "PROXY_CHAT_FORMAT_CLICK_COMMAND", "/msg %player%") {
    };
    public static final Key<Boolean> TAB_ENABLED = new Key<Boolean>("TAB",
        true) {
    };
    public static final Key<String> TAB_HEADER = new Key<String>("TAB_HEADER",
        "Welcome to") {
    };
    public static final Key<String> TAB_FOOTER = new Key<String>("TAB_FOOTER",
        "A Velocity Server") {
    };
    public static final Key<String> TAB_FORMAT = new Key<String>("TAB_FORMAT",
        "%prefix% %player% %suffix%") {
    };
    public static final Key<List<String>> TAB_FORMAT_CUSTOM = new Key<List<String>>("TAB_FORMAT_CUSTOM",
        Arrays.asList(
            "&3Your Ping : &e%ping%",
            "&3Current Server : &e%server%",
            "&3Player Count : &e%playercount%")) {
    };
    public static final Key<Integer> TAB_UPDATE = new Key<Integer>("TAB_UPDATE", 1) {
    };
    public static final Key<String> CHAT_DEFAULT_CHANNEL = new Key<String>("CHAT_DEFAULT_CHANNEL",
        "global") {
    };
    public static final Key<String> BAN_PERMISSION = new Key<String>("BAN_PERMISSION",
        "catalyst.command.ban") {
    };
    public static final Key<String> TEMP_BAN_PERMISSION = new Key<String>("TEMP_BAN_PERMISSION",
        "catalyst.command.tempban") {
    };
    public static final Key<String> BAN_EXEMPT_PERMISSION = new Key<String>(
        "BAN_EXEMPT_PERMISSION", "catalyst.command.ban.exempt") {
    };
    public static final Key<String> BROADCAST_PERMISSION = new Key<String>("BROADCAST_PERMISSION",
        "catalyst.command.broadcast") {
    };
    public static final Key<String> CHAT_COLOR_PERMISSION = new Key<String>(
        "CHAT_COLOR_PERMISSION", "catalyst.chat.color") {
    };
    public static final Key<String> FIND_PERMISSION = new Key<String>("FIND_PERMISSION",
        "catalyst.command.find") {
    };
    public static final Key<String> GOOGLE_PERMISSION = new Key<String>("GOOGLE_PERMISSION",
        "catalyst.command.google") {
    };
    public static final Key<String> INFO_PERMISSION = new Key<String>("INFO_PERMISSION",
        "catalyst.command.info") {
    };
    public static final Key<String> INFO_IP_PERMISSION = new Key<String>("INFO_IP_PERMISSION",
        "catalyst.command.info.ip") {
    };
    public static final Key<String> INFO_BANNED_PERMISSION = new Key<String>(
        "INFO_BANNED_PERMISSION", "catalyst.command.info.banned") {
    };
    public static final Key<String> INFO_CHANNEL_PERMISSION = new Key<String>("INFO_CHANNEL_PERMISSION",
        "catalyst.command.info.channel") {
    };
    public static final Key<String> KICK_PERMISSION = new Key<String>(
        "KICK_PERMISSION", "catalyst.command.kick") {
    };
    public static final Key<String> KICK_EXEMPT_PERMISSION = new Key<String>(
        "KICK_EXEMPT_PERMISSION", "catalyst.command.kick.exempt") {
    };
    public static final Key<String> LANGUAGE_ADMIN_PERMISSION = new Key<String>(
        "LANGUAGE_ADMIN_PERMISSION", "catalyst.command.language.admin") {
    };
    public static final Key<String> LANGUAGE_LIST_PERMISSION = new Key<String>(
        "LANGUAGE_LIST_PERMISSION", "catalyst.command.language.list") {
    };
    public static final Key<String> LIST_PERMISSION = new Key<String>("LIST_PERMISSION",
        "catalyst.command.list") {
    };
    public static final Key<String> MESSAGE_PERMISSION = new Key<String>("MESSAGE_PERMISSION",
        "catalyst.command.message") {
    };
    public static final Key<String> MUTE_PERMISSION = new Key<String>("MUTE_PERMISSION",
        "catalyst.command.mute") {
    };
    public static final Key<String> MUTE_EXEMPT_PERMISSION = new Key<String>(
        "MUTE_EXEMPT_PERMISSION", "catalyst.command.mute.exempt") {
    };
    public static final Key<String> NICKNAME_PERMISSION = new Key<String>(
        "NICKNAME_PERMISSION", "catalyst.command.nickname") {
    };
    public static final Key<String> NICKNAME_COLOR_PERMISSION = new Key<String>(
        "NICKNAME_COLOR_PERMISSION", "catalyst.command.nickname.color") {
    };
    public static final Key<String> NICKNAME_MAGIC_PERMISSION = new Key<String>(
        "NICKNAME_MAGIC_PERMISSION", "catalyst.command.nickname.magic") {
    };
    public static final Key<String> NICKNAME_OTHER_PERMISSION = new Key<String>(
        "NICKNAME_OTHER_PERMISSION", "catalyst.command.nickname.other") {
    };
    public static final Key<String> NICKNAME_PREFIX = new Key<String>("NICKNAME_PREFIX",
        "~") {
    };
    public static final Key<String> SEND_PERMISSION = new Key<String>("SEND_PERMISSION",
        "catalyst.admin.command.send") {
    };
    public static final Key<String> SENDGOOGLE_PERMISSION = new Key<String>("SENDGOOGLE_PERMISSION",
        "catalyst.admin.command.sendgoogle") {
    };
    public static final Key<String> SOCIALSPY_PERMISSION = new Key<String>("SOCIALSPY_PERMISSION",
        "catalyst.admin.command.socialspy") {
    };
    public static final Key<String> SOCIALSPY_ONJOIN_PERMISSION = new Key<String>(
        "SOCIALSPY_ONJOIN_PERMISSION",
        "catalyst.admin.command.socialspy.onjoin") {
    };
    public static final Key<String> STAFFCHAT_PERMISSION = new Key<String>(
        "STAFFCHAT_PERMISSION", "catalyst.admin.command.staffchat") {
    };
    public static final Key<String> STAFFLIST_ADMIN_PERMISSION = new Key<String>(
        "STAFFLIST_ADMIN_PERMISSION", "catalyst.stafflist.admin") {
    };
    public static final Key<String> STAFFLIST_BASE_PERMISSION = new Key<String>(
        "STAFFLIST_BASE_PERMISSION", "catalyst.stafflist.base") {
    };
    public static final Key<String> STAFFLIST_OWNER_PERMISSION = new Key<String>(
        "STAFFLIST_OWNER_PERMISSION", "catalyst.stafflist.owner") {
    };
    public static final Key<String> STAFFLIST_STAFF_PERMISSION = new Key<String>(
        "STAFFLIST_STAFF_PERMISSION", "catalyst.stafflist.staff") {
    };
    public static final Key<String> ALL_CHAT_CHANNELS_PERMISSION = new Key<String>(
        "ALL_CHAT_CHANNELS_PERMISSION", "catalyst.channel.all") {
    };
    public static final Key<String> CHANNEL_BASE_PERMISSION = new Key<String>(
        "CHANNEL_BASE_PERMISSION", "catalyst.channel.") {
    };
    public static final Key<String> BOT_NAME = new Key<String>("BOT_NAME",
        "System") {
    };
    public static final Key<String> BOT_TOKEN = new Key<String>("BOT_TOKEN",
        "bot token") {
    };
    public static final Key<String> MAIN_CHANNEL = new Key<String>("MAIN_CHANNEL",
        "123456789") {
    };
    public static final Key<String> STAFF_CHANNEL = new Key<String>("STAFF_CHANNEL",
        "123456789") {
    };
    public static final Key<String> PLAYER_CHAT_FORMAT = new Key<String>("PLAYER_CHAT_FORMAT",
        "[%server%] %prefix% %player% %suffix%") {
    };
    public static final Key<String> JOIN_FORMAT = new Key<String>("JOIN_FORMAT",
        "%player% has joined the game.") {
    };
    public static final Key<String> DISCORD_LEAVE_FORMAT = new Key<String>("DISCORD_LEAVE_FORMAT",
        "%player% has left the game.") {
    };
    public static final Key<String> DISCORD_CHAT_FORMAT = new Key<String>("DISCORD_CHAT_FORMAT",
        "&6[Discord]&7 %name% : %message%") {
    };
    public static final Key<String> DISCORD_STAFF_FORMAT = new Key<String>("DISCORD_STAFF_FORMAT",
        "&b[STAFF] &7Discord %name%:&b %message%") {
    };
    public static final Key<String> TOPIC_FORMAT = new Key<String>("TOPIC_FORMAT",
        "Player Count : %players%") {
    };
    public static final Key<Boolean> TOPIC_UPDATE_ENABLED = new Key<Boolean>(
        "TOPIC_UPDATE_ENABLED", false) {
    };
    public static final Key<Integer> TOPIC_UPDATE_DELAY = new Key<Integer>("TOPIC_UPDATE_DELAY",
        5) {
    };
    public static final Key<String> TOPIC_NO_ONLINE_PLAYERS = new Key<String>(
        "TOPIC_NO_ONLINE_PLAYERS", "There are no players online!") {
    };
    public static final Key<String> NOW_PLAYING_MESSAGE = new Key<String>("NOW_PLAYING_MESSAGE",
        "A Minecraft Server!") {
    };
    public static final Key<String> WEBHOOK_URL = new Key<String>("WEBHOOK_URL",
        "https://crafatar.com/avatars/%uuid%?default=MHF_Alex") {
    };
    public static final Key<String> DISCORD_URL = new Key<String>("DISCORD_URL",
        "https://discord.gg/hZpQ5Sg") {
    };
    public static final Key<Boolean> DISCORD_ENABLE = new Key<Boolean>("DISCORD_ENABLE",
        false) {
    };
    public static final Key<String> DISCORD_HOVER_MESSAGE = new Key<String>(
        "DISCORD_HOVER_MESSAGE", "Click here to join our discord!") {
    };
    public static final Key<String> WEBSITE_URL = new Key<String>("WEBSITE_URL",
        "http://ci.anvilpowered.org") {
    };
    public static final Key<String> IGNORE_PERMISSION = new Key<String>("IGNORE_PERMISSION",
        "catalyst.command.ignore") {
    };
    public static final Key<String> IGNORE_EXEMPT_PERMISSION = new Key<String>(
        "IGNORE_EXEMPT_PERMISSION", "catalyst.command.ignore.exempt") {
    };
    public static final Key<String> SERVER_PING = new Key<String>("SERVER_PING",
        "PLAYERS") {
    };
    public static final Key<String> SERVER_PING_MESSAGE = new Key<String>("SERVER_PING_MESSAGE",
        "Change this message in the config!") {
    };
    public static final Key<String> SYNC_COMMAND = new Key<String>("SYNC_COMMAND",
        "catalyst.admin.command.sync") {
    };
    public static final Key<String> MOTD = new Key<String>("MOTD",
        "A Velocity Proxy!") {
    };
    public static final Key<Boolean> MOTD_ENABLED = new Key<Boolean>("MOTD_ENABLED",
        false){
    };
    public static final Key<Boolean> ADVANCED_SERVER_INFO_ENABLED = new Key<Boolean>(
        "ADVANCED_SERVER_INFO_ENABLED", false) {
    };
    private static final Map<String, Character> emojiMap = new HashMap<>();
    public static final Key<Boolean> EMOJI_ENABLE = new Key<Boolean>("EMOJI_ENABLE",
        false) {
    };
    public static final Key<Map<String, Character>> EMOJI_MAP = new Key<Map<String, Character>>("EMOJI_MAP",
        emojiMap) {
    };
    public static final Key<String> EMOJI_PERMISSION = new Key<String>("EMOJI_PERMISSION",
        "catalyst.chat.emoji.base") {
    };
    static List<ChatChannel> chatChannels = new LinkedList<>();
    public static final Key<List<ChatChannel>> CHAT_CHANNELS = new Key<List<ChatChannel>>("CHAT_CHANNELS",
        chatChannels) {
    };
    static List<AdvancedServerInfo> advancedServerInfo = new LinkedList<>();
    public static final Key<List<AdvancedServerInfo>> ADVANCED_SERVER_INFO = new Key<List<AdvancedServerInfo>>(
        "ADVANCED_SERVER_INFO", advancedServerInfo) {
    };
    public static final Key<Boolean> VIA_VERSION_ENABLED = new Key<Boolean>(
        "VIA_VERSION_ENABLED", false) {
    };
    static char emojiChar = '\uac00';
    public static final Key<Boolean> COMMAND_LOGGING_ENABLED = new Key<Boolean>(
        "COMMAND_LOGGING_ENABLED", true) {
    };
    public static final Key<List<String>> COMMAND_LOGGING_FILTER = new Key<List<String>>(
        "COMMAND_LOGGING_FILTER", ImmutableList.of("*")) {
    };
    public static final Key<Boolean> ENABLE_PER_SERVER_PERMS = new Key<Boolean>(
        "ENABLE_PER_SERVER_PERMS", false) {
    };
    //Keys for command toggling
    public static final Key<Boolean> BAN_COMMAND_ENABLED = new Key<Boolean>(
        "BAN_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> BROADCAST_COMMAND_ENABLED = new Key<Boolean>(
        "BROADCAST_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> NICKNAME_COMMAND_ENABLED = new Key<Boolean>(
        "NICKNAME_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> FIND_COMMAND_ENABLED = new Key<Boolean>(
        "FIND_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> INFO_COMMAND_ENABLED = new Key<Boolean>(
        "INFO_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> KICK_COMMAND_ENABLED = new Key<Boolean>(
        "KICK_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> LIST_COMMAND_ENABLED = new Key<Boolean>(
        "LIST_COMMAND_ENABLED", false) {
    };
    public static final Key<Boolean> MESSAGE_COMMAND_ENABLED = new Key<Boolean>(
        "MESSAGE_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> SEND_COMMAND_ENABLED = new Key<Boolean>(
        "SEND_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> SERVER_COMMAND_ENABLED = new Key<Boolean>(
        "SERVER_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> SOCIALSPY_COMMAND_ENABLED = new Key<Boolean>(
        "SOCIALSPY_COMMAND_ENABLED", true) {
    };
    public static final Key<Boolean> STAFFCHAT_COMMAND_ENABLED = new Key<Boolean>(
        "STAFF_CHAT_COMMAND", true) {
    };
    public static final Key<Boolean> MUTE_COMMAND_ENABLED = new Key<Boolean>(
        "MUTE_COMMAND_ENABLED", true) {
    };
    //Keys for root node comments
    public static final Key<String> ADVANCED_ROOT = new Key<String>(
        "ADVANCED_ROOT", null) {
    };
    public static final Key<String> COMMANDS_ROOT = new Key<String>(
        "COMMANDS_ROOT", null) {
    };
    public static final Key<String> CHAT_ROOT = new Key<String>(
        "CHAT_ROOT", null) {
    };
    public static final Key<String> DISCORD_ROOT = new Key<String>(
        "DISCORD_ROOT", null) {
    };
    public static final Key<String> JOIN_ROOT = new Key<String>(
        "JOIN_ROOT", null) {
    };
    public static final Key<String> LEAVE_ROOT = new Key<String>(
        "LEAVE_ROOT", null) {
    };
    public static final Key<String> MODULES_ROOT = new Key<String>(
        "MODULES_ROOT", null) {
    };
    public static final Key<String> MOTD_ROOT = new Key<String>(
        "MOTD_ROOT", null) {
    };
    public static final Key<String> PING_ROOT = new Key<String>(
        "PING_ROOT", null) {
    };
    public static final Key<String> TAB_ROOT = new Key<String>(
        "TAB_ROOT", null) {
    };

    static {
        ChatChannel global = new ChatChannel();
        global.id = "global";
        global.aliases = Arrays.asList("globalchat", "globalchannel");
        global.prefix = "[G]";
        global.servers = Arrays.asList("main", "wild");
        global.alwaysVisible = true;
        chatChannels.add(global);
        ChatChannel staff = new ChatChannel();
        staff.id = "staff";
        staff.aliases = Arrays.asList("staffchannel", "staffchat");
        staff.prefix = "[S]";
        staff.alwaysVisible = true;
        staff.servers = Arrays.asList("main", "wild");
        chatChannels.add(staff);
    }

    static {
        AdvancedServerInfo example = new AdvancedServerInfo();
        example.hostName = "lobby.hostname.com";
        example.motd = "&cA Velocity Proxy";
        example.prefix = "Lobby";
        example.port = 30066;
        example.modded = false;
        advancedServerInfo.add(example);
    }

    static {
        Keys.startRegistration("catalyst")
            .register(CHAT_FILTER_ENABLED)
            .register(CHAT_FILTER_EXCEPTIONS)
            .register(CHAT_FILTER_SWEARS)
            .register(FIRST_JOIN)
            .register(JOIN_MESSAGE)
            .register(LEAVE_MESSAGE)
            .register(PROXY_CHAT_ENABLED)
            .register(PROXY_CHAT_FORMAT_MESSAGE)
            .register(PROXY_CHAT_FORMAT_HOVER)
            .register(TAB_ENABLED)
            .register(TAB_HEADER)
            .register(TAB_FOOTER)
            .register(TAB_FORMAT)
            .register(TAB_FORMAT_CUSTOM)
            .register(TAB_UPDATE)
            .register(CHAT_CHANNELS)
            .register(CHAT_DEFAULT_CHANNEL)
            .register(BAN_PERMISSION)
            .register(TEMP_BAN_PERMISSION)
            .register(BAN_EXEMPT_PERMISSION)
            .register(BROADCAST_PERMISSION)
            .register(CHAT_COLOR_PERMISSION)
            .register(FIND_PERMISSION)
            .register(GOOGLE_PERMISSION)
            .register(INFO_PERMISSION)
            .register(INFO_IP_PERMISSION)
            .register(INFO_BANNED_PERMISSION)
            .register(INFO_CHANNEL_PERMISSION)
            .register(KICK_PERMISSION)
            .register(KICK_EXEMPT_PERMISSION)
            .register(LANGUAGE_ADMIN_PERMISSION)
            .register(LANGUAGE_LIST_PERMISSION)
            .register(LIST_PERMISSION)
            .register(MESSAGE_PERMISSION)
            .register(MUTE_PERMISSION)
            .register(MUTE_EXEMPT_PERMISSION)
            .register(NICKNAME_PERMISSION)
            .register(NICKNAME_COLOR_PERMISSION)
            .register(NICKNAME_MAGIC_PERMISSION)
            .register(NICKNAME_OTHER_PERMISSION)
            .register(NICKNAME_PREFIX)
            .register(SEND_PERMISSION)
            .register(SENDGOOGLE_PERMISSION)
            .register(SOCIALSPY_PERMISSION)
            .register(SOCIALSPY_ONJOIN_PERMISSION)
            .register(STAFFCHAT_PERMISSION)
            .register(STAFFLIST_ADMIN_PERMISSION)
            .register(STAFFLIST_BASE_PERMISSION)
            .register(STAFFLIST_OWNER_PERMISSION)
            .register(STAFFLIST_STAFF_PERMISSION)
            .register(ALL_CHAT_CHANNELS_PERMISSION)
            .register(CHANNEL_BASE_PERMISSION)
            .register(BOT_NAME)
            .register(BOT_TOKEN)
            .register(MAIN_CHANNEL)
            .register(STAFF_CHANNEL)
            .register(PLAYER_CHAT_FORMAT)
            .register(JOIN_FORMAT)
            .register(DISCORD_LEAVE_FORMAT)
            .register(DISCORD_CHAT_FORMAT)
            .register(DISCORD_STAFF_FORMAT)
            .register(TOPIC_FORMAT)
            .register(TOPIC_UPDATE_ENABLED)
            .register(TOPIC_UPDATE_DELAY)
            .register(NOW_PLAYING_MESSAGE)
            .register(WEBHOOK_URL)
            .register(DISCORD_URL)
            .register(DISCORD_ENABLE)
            .register(DISCORD_HOVER_MESSAGE)
            .register(WEBSITE_URL)
            .register(IGNORE_PERMISSION)
            .register(IGNORE_EXEMPT_PERMISSION)
            .register(SERVER_PING)
            .register(SERVER_PING_MESSAGE)
            .register(SYNC_COMMAND)
            .register(MOTD)
            .register(MOTD_ENABLED)
            .register(ADVANCED_SERVER_INFO_ENABLED)
            .register(ADVANCED_SERVER_INFO)
            .register(EMOJI_ENABLE)
            .register(EMOJI_MAP)
            .register(EMOJI_PERMISSION)
            .register(VIA_VERSION_ENABLED)
            .register(BAN_COMMAND_ENABLED)
            .register(BROADCAST_COMMAND_ENABLED)
            .register(NICKNAME_COMMAND_ENABLED)
            .register(FIND_COMMAND_ENABLED)
            .register(INFO_COMMAND_ENABLED)
            .register(KICK_COMMAND_ENABLED)
            .register(LIST_COMMAND_ENABLED)
            .register(MESSAGE_COMMAND_ENABLED)
            .register(SEND_COMMAND_ENABLED)
            .register(SERVER_COMMAND_ENABLED)
            .register(SOCIALSPY_COMMAND_ENABLED)
            .register(STAFFCHAT_COMMAND_ENABLED)
            .register(MUTE_COMMAND_ENABLED)
            .register(ADVANCED_ROOT)
            .register(CHAT_ROOT)
            .register(DISCORD_ROOT)
            .register(JOIN_ROOT)
            .register(LEAVE_ROOT)
            .register(MODULES_ROOT)
            .register(MOTD_ROOT)
            .register(PING_ROOT)
            .register(TAB_ROOT)
            .register(COMMAND_LOGGING_ENABLED)
            .register(COMMAND_LOGGING_FILTER)
            .register(ENABLE_PER_SERVER_PERMS);
    }

    static {
        emojiMap.put(":100:", emojiChar++);
        emojiMap.put(":1234:", emojiChar++);
        emojiMap.put(":grinning:", emojiChar++);
        emojiMap.put(":grimacing:", emojiChar++);
        emojiMap.put(":grin:", emojiChar++);
        emojiMap.put(":joy:", emojiChar++);
        emojiMap.put(":rofl:", emojiChar++);
        emojiMap.put(":smiley:", emojiChar++);
        emojiMap.put(":smile:", emojiChar++);
        emojiMap.put(":sweat_smile:", emojiChar++);
        emojiMap.put(":laughing:", emojiChar++);
        emojiMap.put(":innocent:", emojiChar++);
        emojiMap.put(":wink:", emojiChar++);
        emojiMap.put(":blush:", emojiChar++);
        emojiMap.put(":slightly_smiling_face:", emojiChar++);
        emojiMap.put(":upside_down_face:", emojiChar++);
        emojiMap.put(":relaxed:", emojiChar++);
        emojiMap.put(":yum:", emojiChar++);
        emojiMap.put(":relieved:", emojiChar++);
        emojiMap.put(":heart_eyes:", emojiChar++);
        emojiMap.put(":kissing_heart:", emojiChar++);
        emojiMap.put(":kissing:", emojiChar++);
        emojiMap.put(":kissing_smiling_eyes:", emojiChar++);
        emojiMap.put(":kissing_closed_eyes:", emojiChar++);
        emojiMap.put(":stuck_out_tongue_winking_eye:", emojiChar++);
        emojiMap.put(":stuck_out_tongue_closed_eyes:", emojiChar++);
        emojiMap.put(":stuck_out_tongue:", emojiChar++);
        emojiMap.put(":money_mouth_face:", emojiChar++);
        emojiMap.put(":nerd_face:", emojiChar++);
        emojiMap.put(":sunglasses:", emojiChar++);
        emojiMap.put(":clown_face:", emojiChar++);
        emojiMap.put(":cowboy_hat_face:", emojiChar++);
        emojiMap.put(":hugs:", emojiChar++);
        emojiMap.put(":smirk:", emojiChar++);
        emojiMap.put(":no_mouth:", emojiChar++);
        emojiMap.put(":neutral_face:", emojiChar++);
        emojiMap.put(":expressionless:", emojiChar++);
        emojiMap.put(":unamused:", emojiChar++);
        emojiMap.put(":roll_eyes:", emojiChar++);
        emojiMap.put(":thinking:", emojiChar++);
        emojiMap.put(":lying_face:", emojiChar++);
        emojiMap.put(":flushed:", emojiChar++);
        emojiMap.put(":disappointed:", emojiChar++);
        emojiMap.put(":worried:", emojiChar++);
        emojiMap.put(":angry:", emojiChar++);
        emojiMap.put(":rage:", emojiChar++);
        emojiMap.put(":pensive:", emojiChar++);
        emojiMap.put(":confused:", emojiChar++);
        emojiMap.put(":slightly_frowning_face:", emojiChar++);
        emojiMap.put(":frowning_face:", emojiChar++);
        emojiMap.put(":persevere:", emojiChar++);
        emojiMap.put(":confounded:", emojiChar++);
        emojiMap.put(":tired_face:", emojiChar++);
        emojiMap.put(":weary:", emojiChar++);
        emojiMap.put(":triumph:", emojiChar++);
        emojiMap.put(":open_mouth:", emojiChar++);
        emojiMap.put(":scream:", emojiChar++);
        emojiMap.put(":fearful:", emojiChar++);
        emojiMap.put(":cold_sweat:", emojiChar++);
        emojiMap.put(":hushed:", emojiChar++);
        emojiMap.put(":frowning:", emojiChar++);
        emojiMap.put(":anguished:", emojiChar++);
        emojiMap.put(":cry:", emojiChar++);
        emojiMap.put(":disappointed_relieved:", emojiChar++);
        emojiMap.put(":drooling_face:", emojiChar++);
        emojiMap.put(":sleepy:", emojiChar++);
        emojiMap.put(":sweat:", emojiChar++);
        emojiMap.put(":sob:", emojiChar++);
        emojiMap.put(":dizzy_face:", emojiChar++);
        emojiMap.put(":astonished:", emojiChar++);
        emojiMap.put(":zipper_mouth_face:", emojiChar++);
        emojiMap.put(":nauseated_face:", emojiChar++);
        emojiMap.put(":sneezing_face:", emojiChar++);
        emojiMap.put(":mask:", emojiChar++);
        emojiMap.put(":face_with_thermometer:", emojiChar++);
        emojiMap.put(":face_with_head_bandage:", emojiChar++);
        emojiMap.put(":sleeping:", emojiChar++);
        emojiMap.put(":zzz:", emojiChar++);
        emojiMap.put(":poop:", emojiChar++);
        emojiMap.put(":smiling_imp:", emojiChar++);
        emojiMap.put(":imp:", emojiChar++);
        emojiMap.put(":japanese_ogre:", emojiChar++);
        emojiMap.put(":japanese_goblin:", emojiChar++);
        emojiMap.put(":skull:", emojiChar++);
        emojiMap.put(":ghost:", emojiChar++);
        emojiMap.put(":alien:", emojiChar++);
        emojiMap.put(":robot:", emojiChar++);
        emojiMap.put(":smiley_cat:", emojiChar++);
        emojiMap.put(":smile_cat:", emojiChar++);
        emojiMap.put(":joy_cat:", emojiChar++);
        emojiMap.put(":heart_eyes_cat:", emojiChar++);
        emojiMap.put(":smirk_cat:", emojiChar++);
        emojiMap.put(":kissing_cat:", emojiChar++);
        emojiMap.put(":scream_cat:", emojiChar++);
        emojiMap.put(":crying_cat_face:", emojiChar++);
        emojiMap.put(":pouting_cat:", emojiChar++);
        emojiMap.put(":raised_hands:", emojiChar++);
        emojiMap.put(":clap:", emojiChar++);
        emojiMap.put(":wave:", emojiChar++);
        emojiMap.put(":call_me_hand:", emojiChar++);
        emojiMap.put(":+1:", emojiChar++);
        emojiMap.put(":-1:", emojiChar++);
        emojiMap.put(":facepunch:", emojiChar++);
        emojiMap.put(":fist:", emojiChar++);
        emojiMap.put(":fist_left:", emojiChar++);
        emojiMap.put(":fist_right:", emojiChar++);
        emojiMap.put(":v:", emojiChar++);
        emojiMap.put(":ok_hand:", emojiChar++);
        emojiMap.put(":raised_hand:", emojiChar++);
        emojiMap.put(":raised_back_of_hand:", emojiChar++);
        emojiMap.put(":open_hands:", emojiChar++);
        emojiMap.put(":muscle:", emojiChar++);
        emojiMap.put(":pray:", emojiChar++);
        emojiMap.put(":handshake:", emojiChar++);
        emojiMap.put(":point_up:", emojiChar++);
        emojiMap.put(":point_up_2:", emojiChar++);
        emojiMap.put(":point_down:", emojiChar++);
        emojiMap.put(":point_left:", emojiChar++);
        emojiMap.put(":point_right:", emojiChar++);
        emojiMap.put(":fu:", emojiChar++);
        emojiMap.put(":raised_hand_with_fingers_splayed:", emojiChar++);
        emojiMap.put(":metal:", emojiChar++);
        emojiMap.put(":crossed_fingers:", emojiChar++);
        emojiMap.put(":vulcan_salute:", emojiChar++);
        emojiMap.put(":writing_hand:", emojiChar++);
        emojiMap.put(":selfie:", emojiChar++);
        emojiMap.put(":lips:", emojiChar++);
        emojiMap.put(":tongue:", emojiChar++);
        emojiMap.put(":ear:", emojiChar++);
        emojiMap.put(":nose:", emojiChar++);
        emojiMap.put(":eye:", emojiChar++);
        emojiMap.put(":eyes:", emojiChar++);
        emojiMap.put(":womans_clothes:", emojiChar++);
        emojiMap.put(":tshirt:", emojiChar++);
        emojiMap.put(":jeans:", emojiChar++);
        emojiMap.put(":necktie:", emojiChar++);
        emojiMap.put(":dress:", emojiChar++);
        emojiMap.put(":bikini:", emojiChar++);
        emojiMap.put(":kimono:", emojiChar++);
        emojiMap.put(":lipstick:", emojiChar++);
        emojiMap.put(":kiss:", emojiChar++);
        emojiMap.put(":footprints:", emojiChar++);
        emojiMap.put(":high_heel:", emojiChar++);
        emojiMap.put(":sandal:", emojiChar++);
        emojiMap.put(":boot:", emojiChar++);
        emojiMap.put(":mans_shoe:", emojiChar++);
        emojiMap.put(":athletic_shoe:", emojiChar++);
        emojiMap.put(":womans_hat:", emojiChar++);
        emojiMap.put(":tophat:", emojiChar++);
        emojiMap.put(":rescue_worker_helmet:", emojiChar++);
        emojiMap.put(":mortar_board:", emojiChar++);
        emojiMap.put(":crown:", emojiChar++);
        emojiMap.put(":school_satchel:", emojiChar++);
        emojiMap.put(":pouch:", emojiChar++);
        emojiMap.put(":purse:", emojiChar++);
        emojiMap.put(":handbag:", emojiChar++);
        emojiMap.put(":briefcase:", emojiChar++);
        emojiMap.put(":eyeglasses:", emojiChar++);
        emojiMap.put(":dark_sunglasses:", emojiChar++);
        emojiMap.put(":ring:", emojiChar++);
        emojiMap.put(":closed_umbrella:", emojiChar++);
        emojiMap.put(":dog:", emojiChar++);
        emojiMap.put(":cat:", emojiChar++);
        emojiMap.put(":mouse:", emojiChar++);
        emojiMap.put(":hamster:", emojiChar++);
        emojiMap.put(":rabbit:", emojiChar++);
        emojiMap.put(":fox_face:", emojiChar++);
        emojiMap.put(":bear:", emojiChar++);
        emojiMap.put(":panda_face:", emojiChar++);
        emojiMap.put(":koala:", emojiChar++);
        emojiMap.put(":tiger:", emojiChar++);
        emojiMap.put(":lion:", emojiChar++);
        emojiMap.put(":cow:", emojiChar++);
        emojiMap.put(":pig:", emojiChar++);
        emojiMap.put(":pig_nose:", emojiChar++);
        emojiMap.put(":frog:", emojiChar++);
        emojiMap.put(":squid:", emojiChar++);
        emojiMap.put(":octopus:", emojiChar++);
        emojiMap.put(":shrimp:", emojiChar++);
        emojiMap.put(":monkey_face:", emojiChar++);
        emojiMap.put(":gorilla:", emojiChar++);
        emojiMap.put(":see_no_evil:", emojiChar++);
        emojiMap.put(":hear_no_evil:", emojiChar++);
        emojiMap.put(":speak_no_evil:", emojiChar++);
        emojiMap.put(":monkey:", emojiChar++);
        emojiMap.put(":chicken:", emojiChar++);
        emojiMap.put(":penguin:", emojiChar++);
        emojiMap.put(":bird:", emojiChar++);
        emojiMap.put(":baby_chick:", emojiChar++);
        emojiMap.put(":hatching_chick:", emojiChar++);
        emojiMap.put(":hatched_chick:", emojiChar++);
        emojiMap.put(":duck:", emojiChar++);
        emojiMap.put(":eagle:", emojiChar++);
        emojiMap.put(":owl:", emojiChar++);
        emojiMap.put(":bat:", emojiChar++);
        emojiMap.put(":wolf:", emojiChar++);
        emojiMap.put(":boar:", emojiChar++);
        emojiMap.put(":horse:", emojiChar++);
        emojiMap.put(":unicorn:", emojiChar++);
        emojiMap.put(":honeybee:", emojiChar++);
        emojiMap.put(":bug:", emojiChar++);
        emojiMap.put(":butterfly:", emojiChar++);
        emojiMap.put(":snail:", emojiChar++);
        emojiMap.put(":beetle:", emojiChar++);
        emojiMap.put(":ant:", emojiChar++);
        emojiMap.put(":spider:", emojiChar++);
        emojiMap.put(":scorpion:", emojiChar++);
        emojiMap.put(":crab:", emojiChar++);
        emojiMap.put(":snake:", emojiChar++);
        emojiMap.put(":lizard:", emojiChar++);
        emojiMap.put(":turtle:", emojiChar++);
        emojiMap.put(":tropical_fish:", emojiChar++);
        emojiMap.put(":fish:", emojiChar++);
        emojiMap.put(":blowfish:", emojiChar++);
        emojiMap.put(":dolphin:", emojiChar++);
        emojiMap.put(":shark:", emojiChar++);
        emojiMap.put(":whale:", emojiChar++);
        emojiMap.put(":whale2:", emojiChar++);
        emojiMap.put(":crocodile:", emojiChar++);
        emojiMap.put(":leopard:", emojiChar++);
        emojiMap.put(":tiger2:", emojiChar++);
        emojiMap.put(":water_buffalo:", emojiChar++);
        emojiMap.put(":ox:", emojiChar++);
        emojiMap.put(":cow2:", emojiChar++);
        emojiMap.put(":deer:", emojiChar++);
        emojiMap.put(":dromedary_camel:", emojiChar++);
        emojiMap.put(":camel:", emojiChar++);
        emojiMap.put(":elephant:", emojiChar++);
        emojiMap.put(":rhinoceros:", emojiChar++);
        emojiMap.put(":goat:", emojiChar++);
        emojiMap.put(":ram:", emojiChar++);
        emojiMap.put(":sheep:", emojiChar++);
        emojiMap.put(":racehorse:", emojiChar++);
        emojiMap.put(":pig2:", emojiChar++);
        emojiMap.put(":rat:", emojiChar++);
        emojiMap.put(":mouse2:", emojiChar++);
        emojiMap.put(":rooster:", emojiChar++);
        emojiMap.put(":turkey:", emojiChar++);
        emojiMap.put(":dove:", emojiChar++);
        emojiMap.put(":dog2:", emojiChar++);
        emojiMap.put(":poodle:", emojiChar++);
        emojiMap.put(":cat2:", emojiChar++);
        emojiMap.put(":rabbit2:", emojiChar++);
        emojiMap.put(":chipmunk:", emojiChar++);
        emojiMap.put(":paw_prints:", emojiChar++);
        emojiMap.put(":dragon:", emojiChar++);
        emojiMap.put(":dragon_face:", emojiChar++);
        emojiMap.put(":cactus:", emojiChar++);
        emojiMap.put(":christmas_tree:", emojiChar++);
        emojiMap.put(":evergreen_tree:", emojiChar++);
        emojiMap.put(":deciduous_tree:", emojiChar++);
        emojiMap.put(":palm_tree:", emojiChar++);
        emojiMap.put(":seedling:", emojiChar++);
        emojiMap.put(":herb:", emojiChar++);
        emojiMap.put(":shamrock:", emojiChar++);
        emojiMap.put(":four_leaf_clover:", emojiChar++);
        emojiMap.put(":bamboo:", emojiChar++);
        emojiMap.put(":tanabata_tree:", emojiChar++);
        emojiMap.put(":leaves:", emojiChar++);
        emojiMap.put(":fallen_leaf:", emojiChar++);
        emojiMap.put(":maple_leaf:", emojiChar++);
        emojiMap.put(":ear_of_rice:", emojiChar++);
        emojiMap.put(":hibiscus:", emojiChar++);
        emojiMap.put(":sunflower:", emojiChar++);
        emojiMap.put(":rose:", emojiChar++);
        emojiMap.put(":wilted_flower:", emojiChar++);
        emojiMap.put(":tulip:", emojiChar++);
        emojiMap.put(":blossom:", emojiChar++);
        emojiMap.put(":cherry_blossom:", emojiChar++);
        emojiMap.put(":bouquet:", emojiChar++);
        emojiMap.put(":mushroom:", emojiChar++);
        emojiMap.put(":chestnut:", emojiChar++);
        emojiMap.put(":jack_o_lantern:", emojiChar++);
        emojiMap.put(":shell:", emojiChar++);
        emojiMap.put(":spider_web:", emojiChar++);
        emojiMap.put(":earth_americas:", emojiChar++);
        emojiMap.put(":earth_africa:", emojiChar++);
        emojiMap.put(":earth_asia:", emojiChar++);
        emojiMap.put(":full_moon:", emojiChar++);
        emojiMap.put(":waning_gibbous_moon:", emojiChar++);
        emojiMap.put(":last_quarter_moon:", emojiChar++);
        emojiMap.put(":waning_crescent_moon:", emojiChar++);
        emojiMap.put(":new_moon:", emojiChar++);
        emojiMap.put(":waxing_crescent_moon:", emojiChar++);
        emojiMap.put(":first_quarter_moon:", emojiChar++);
        emojiMap.put(":waxing_gibbous_moon:", emojiChar++);
        emojiMap.put(":new_moon_with_face:", emojiChar++);
        emojiMap.put(":full_moon_with_face:", emojiChar++);
        emojiMap.put(":first_quarter_moon_with_face:", emojiChar++);
        emojiMap.put(":last_quarter_moon_with_face:", emojiChar++);
        emojiMap.put(":sun_with_face:", emojiChar++);
        emojiMap.put(":crescent_moon:", emojiChar++);
        emojiMap.put(":star:", emojiChar++);
        emojiMap.put(":star2:", emojiChar++);
        emojiMap.put(":dizzy:", emojiChar++);
        emojiMap.put(":sparkles:", emojiChar++);
        emojiMap.put(":comet:", emojiChar++);
        emojiMap.put(":sunny:", emojiChar++);
        emojiMap.put(":sun_behind_small_cloud:", emojiChar++);
        emojiMap.put(":partly_sunny:", emojiChar++);
        emojiMap.put(":sun_behind_large_cloud:", emojiChar++);
        emojiMap.put(":sun_behind_rain_cloud:", emojiChar++);
        emojiMap.put(":cloud:", emojiChar++);
        emojiMap.put(":cloud_with_rain:", emojiChar++);
        emojiMap.put(":cloud_with_lightning_and_rain:", emojiChar++);
        emojiMap.put(":cloud_with_lightning:", emojiChar++);
        emojiMap.put(":zap:", emojiChar++);
        emojiMap.put(":fire:", emojiChar++);
        emojiMap.put(":boom:", emojiChar++);
        emojiMap.put(":snowflake:", emojiChar++);
        emojiMap.put(":cloud_with_snow:", emojiChar++);
        emojiMap.put(":snowman:", emojiChar++);
        emojiMap.put(":snowman_with_snow:", emojiChar++);
        emojiMap.put(":wind_face:", emojiChar++);
        emojiMap.put(":dash:", emojiChar++);
        emojiMap.put(":tornado:", emojiChar++);
        emojiMap.put(":fog:", emojiChar++);
        emojiMap.put(":open_umbrella:", emojiChar++);
        emojiMap.put(":umbrella:", emojiChar++);
        emojiMap.put(":droplet:", emojiChar++);
        emojiMap.put(":sweat_drops:", emojiChar++);
        emojiMap.put(":ocean:", emojiChar++);
        emojiMap.put(":green_apple:", emojiChar++);
        emojiMap.put(":apple:", emojiChar++);
        emojiMap.put(":pear:", emojiChar++);
        emojiMap.put(":tangerine:", emojiChar++);
        emojiMap.put(":lemon:", emojiChar++);
        emojiMap.put(":banana:", emojiChar++);
        emojiMap.put(":watermelon:", emojiChar++);
        emojiMap.put(":grapes:", emojiChar++);
        emojiMap.put(":strawberry:", emojiChar++);
        emojiMap.put(":melon:", emojiChar++);
        emojiMap.put(":cherries:", emojiChar++);
        emojiMap.put(":peach:", emojiChar++);
        emojiMap.put(":pineapple:", emojiChar++);
        emojiMap.put(":kiwi_fruit:", emojiChar++);
        emojiMap.put(":avocado:", emojiChar++);
        emojiMap.put(":tomato:", emojiChar++);
        emojiMap.put(":eggplant:", emojiChar++);
        emojiMap.put(":cucumber:", emojiChar++);
        emojiMap.put(":carrot:", emojiChar++);
        emojiMap.put(":hot_pepper:", emojiChar++);
        emojiMap.put(":potato:", emojiChar++);
        emojiMap.put(":corn:", emojiChar++);
        emojiMap.put(":sweet_potato:", emojiChar++);
        emojiMap.put(":peanuts:", emojiChar++);
        emojiMap.put(":honey_pot:", emojiChar++);
        emojiMap.put(":croissant:", emojiChar++);
        emojiMap.put(":bread:", emojiChar++);
        emojiMap.put(":baguette_bread:", emojiChar++);
        emojiMap.put(":cheese:", emojiChar++);
        emojiMap.put(":egg:", emojiChar++);
        emojiMap.put(":bacon:", emojiChar++);
        emojiMap.put(":pancakes:", emojiChar++);
        emojiMap.put(":poultry_leg:", emojiChar++);
        emojiMap.put(":meat_on_bone:", emojiChar++);
        emojiMap.put(":fried_shrimp:", emojiChar++);
        emojiMap.put(":fried_egg:", emojiChar++);
        emojiMap.put(":hamburger:", emojiChar++);
        emojiMap.put(":fries:", emojiChar++);
        emojiMap.put(":stuffed_flatbread:", emojiChar++);
        emojiMap.put(":hotdog:", emojiChar++);
        emojiMap.put(":pizza:", emojiChar++);
        emojiMap.put(":spaghetti:", emojiChar++);
        emojiMap.put(":taco:", emojiChar++);
        emojiMap.put(":burrito:", emojiChar++);
        emojiMap.put(":green_salad:", emojiChar++);
        emojiMap.put(":shallow_pan_of_food:", emojiChar++);
        emojiMap.put(":ramen:", emojiChar++);
        emojiMap.put(":stew:", emojiChar++);
        emojiMap.put(":fish_cake:", emojiChar++);
        emojiMap.put(":sushi:", emojiChar++);
        emojiMap.put(":bento:", emojiChar++);
        emojiMap.put(":curry:", emojiChar++);
        emojiMap.put(":rice_ball:", emojiChar++);
        emojiMap.put(":rice:", emojiChar++);
        emojiMap.put(":rice_cracker:", emojiChar++);
        emojiMap.put(":oden:", emojiChar++);
        emojiMap.put(":dango:", emojiChar++);
        emojiMap.put(":shaved_ice:", emojiChar++);
        emojiMap.put(":ice_cream:", emojiChar++);
        emojiMap.put(":icecream:", emojiChar++);
        emojiMap.put(":cake:", emojiChar++);
        emojiMap.put(":birthday:", emojiChar++);
        emojiMap.put(":custard:", emojiChar++);
        emojiMap.put(":candy:", emojiChar++);
        emojiMap.put(":lollipop:", emojiChar++);
        emojiMap.put(":chocolate_bar:", emojiChar++);
        emojiMap.put(":popcorn:", emojiChar++);
        emojiMap.put(":doughnut:", emojiChar++);
        emojiMap.put(":cookie:", emojiChar++);
        emojiMap.put(":milk_glass:", emojiChar++);
        emojiMap.put(":beer:", emojiChar++);
        emojiMap.put(":beers:", emojiChar++);
        emojiMap.put(":clinking_glasses:", emojiChar++);
        emojiMap.put(":wine_glass:", emojiChar++);
        emojiMap.put(":tumbler_glass:", emojiChar++);
        emojiMap.put(":cocktail:", emojiChar++);
        emojiMap.put(":tropical_drink:", emojiChar++);
        emojiMap.put(":champagne:", emojiChar++);
        emojiMap.put(":sake:", emojiChar++);
        emojiMap.put(":tea:", emojiChar++);
        emojiMap.put(":coffee:", emojiChar++);
        emojiMap.put(":crazy_face:", emojiChar++);
        emojiMap.put(":face_with_monocle:", emojiChar++);
        emojiMap.put(":face_with_raised_eyebrow:", emojiChar++);
        emojiMap.put(":shushing_face:", emojiChar++);
        emojiMap.put(":face_with_hand_over_mouth:", emojiChar++);
        emojiMap.put(":face_with_symbols_over_mouth:", emojiChar++);
        emojiMap.put(":star_struck:", emojiChar++);
        emojiMap.put(":exploding_head:", emojiChar++);
        emojiMap.put(":face_vomiting:", emojiChar++);
        emojiMap.put(":soccer:", emojiChar++);
        emojiMap.put(":basketball:", emojiChar++);
        emojiMap.put(":football:", emojiChar++);
        emojiMap.put(":baseball:", emojiChar++);
        emojiMap.put(":tennis:", emojiChar++);
        emojiMap.put(":volleyball:", emojiChar++);
        emojiMap.put(":rugby_football:", emojiChar++);
        emojiMap.put(":8ball:", emojiChar++);
        emojiMap.put(":golf:", emojiChar++);
        emojiMap.put(":ping_pong:", emojiChar++);
        emojiMap.put(":badminton:", emojiChar++);
        emojiMap.put(":goal_net:", emojiChar++);
        emojiMap.put(":ice_hockey:", emojiChar++);
        emojiMap.put(":field_hockey:", emojiChar++);
        emojiMap.put(":cricket:", emojiChar++);
        emojiMap.put(":ice_skate:", emojiChar++);
        emojiMap.put(":bow_and_arrow:", emojiChar++);
        emojiMap.put(":boxing_glove:", emojiChar++);
        emojiMap.put(":martial_arts_uniform:", emojiChar++);
        emojiMap.put(":trophy:", emojiChar++);
        emojiMap.put(":medal_sports:", emojiChar++);
        emojiMap.put(":medal_military:", emojiChar++);
        emojiMap.put(":1st_place_medal:", emojiChar++);
        emojiMap.put(":2nd_place_medal:", emojiChar++);
        emojiMap.put(":3rd_place_medal:", emojiChar++);
        emojiMap.put(":reminder_ribbon:", emojiChar++);
        emojiMap.put(":rosette:", emojiChar++);
        emojiMap.put(":ticket:", emojiChar++);
        emojiMap.put(":tickets:", emojiChar++);
        emojiMap.put(":performing_arts:", emojiChar++);
        emojiMap.put(":art:", emojiChar++);
        emojiMap.put(":circus_tent:", emojiChar++);
        emojiMap.put(":microphone:", emojiChar++);
        emojiMap.put(":headphones:", emojiChar++);
        emojiMap.put(":musical_score:", emojiChar++);
        emojiMap.put(":musical_keyboard:", emojiChar++);
        emojiMap.put(":drum:", emojiChar++);
        emojiMap.put(":saxophone:", emojiChar++);
        emojiMap.put(":trumpet:", emojiChar++);
        emojiMap.put(":guitar:", emojiChar++);
        emojiMap.put(":violin:", emojiChar++);
        emojiMap.put(":clapper:", emojiChar++);
        emojiMap.put(":video_game:", emojiChar++);
        emojiMap.put(":space_invader:", emojiChar++);
        emojiMap.put(":dart:", emojiChar++);
        emojiMap.put(":game_die:", emojiChar++);
        emojiMap.put(":slot_machine:", emojiChar++);
        emojiMap.put(":bowling:", emojiChar++);
        emojiMap.put(":heart:", emojiChar++);
        emojiMap.put(":broken_heart:", emojiChar++);
        emojiMap.put(":ok:", emojiChar++);
        emojiMap.put(":up:", emojiChar++);
        emojiMap.put(":cool:", emojiChar++);
        emojiMap.put(":new:", emojiChar++);
        emojiMap.put(":free:", emojiChar++);
        emojiMap.put(":zero:", emojiChar++);
        emojiMap.put(":one:", emojiChar++);
        emojiMap.put(":two:", emojiChar++);
        emojiMap.put(":three:", emojiChar++);
        emojiMap.put(":four:", emojiChar++);
        emojiMap.put(":five:", emojiChar++);
        emojiMap.put(":six:", emojiChar++);
        emojiMap.put(":seven:", emojiChar++);
        emojiMap.put(":eight:", emojiChar++);
        emojiMap.put(":nine:", emojiChar++);
        emojiMap.put(":keycap_ten:", emojiChar++);
        emojiMap.put(":asterisk:", emojiChar++);
        emojiMap.put(":rotating_light:", emojiChar++);
        emojiMap.put(":airplane:", emojiChar++);
        emojiMap.put(":rocket:", emojiChar++);
        emojiMap.put(":artificial_satellite:", emojiChar++);
        emojiMap.put(":anchor:", emojiChar++);
        emojiMap.put(":construction:", emojiChar++);
        emojiMap.put(":vertical_traffic_light:", emojiChar++);
        emojiMap.put(":traffic_light:", emojiChar++);
        emojiMap.put(":checkered_flag:", emojiChar++);
        emojiMap.put(":tokyo_tower:", emojiChar++);
        emojiMap.put(":fountain:", emojiChar++);
        emojiMap.put(":mountain:", emojiChar++);
        emojiMap.put(":mountain_snow:", emojiChar++);
        emojiMap.put(":mount_fuji:", emojiChar++);
        emojiMap.put(":volcano:", emojiChar++);
        emojiMap.put(":tent:", emojiChar++);
        emojiMap.put(":railway_track:", emojiChar++);
        emojiMap.put(":sunrise:", emojiChar++);
        emojiMap.put(":sunrise_over_mountains:", emojiChar++);
        emojiMap.put(":desert:", emojiChar++);
        emojiMap.put(":beach_umbrella:", emojiChar++);
        emojiMap.put(":desert_island:", emojiChar++);
        emojiMap.put(":cityscape:", emojiChar++);
        emojiMap.put(":night_with_stars:", emojiChar++);
        emojiMap.put(":bridge_at_night:", emojiChar++);
        emojiMap.put(":city_sunrise:", emojiChar++);
        emojiMap.put(":city_sunset:", emojiChar++);
        emojiMap.put(":watch:", emojiChar++);
        emojiMap.put(":phone:", emojiChar++);
        emojiMap.put(":computer:", emojiChar++);
        emojiMap.put(":keyboard:", emojiChar++);
        emojiMap.put(":desktop_computer:", emojiChar++);
        emojiMap.put(":printer:", emojiChar++);
        emojiMap.put(":mc_planks:", emojiChar++);
        emojiMap.put(":mc_podzol:", emojiChar++);
        emojiMap.put(":mc_grass:", emojiChar++);
        emojiMap.put(":mc_snow_grass:", emojiChar++);
        emojiMap.put(":mc_dirt:", emojiChar++);
        emojiMap.put(":mc_mycellium:", emojiChar++);
        emojiMap.put(":mc_red_sand:", emojiChar++);
        emojiMap.put(":mc_sand:", emojiChar++);
        emojiMap.put(":mc_soulsand:", emojiChar++);
        emojiMap.put(":mc_netherrack:", emojiChar++);
        emojiMap.put(":mc_netherbrick:", emojiChar++);
        emojiMap.put(":mc_quartz_ore:", emojiChar++);
        emojiMap.put(":mc_ender_portal:", emojiChar++);
        emojiMap.put(":mc_ice:", emojiChar++);
        emojiMap.put(":mc_water:", emojiChar++);
        emojiMap.put(":mc_lava:", emojiChar++);
        emojiMap.put(":mc_emerald_block:", emojiChar++);
        emojiMap.put(":mc_diamond_block:", emojiChar++);
        emojiMap.put(":mc_gold_block:", emojiChar++);
        emojiMap.put(":mc_iron_block:", emojiChar++);
        emojiMap.put(":mc_emerald_ore:", emojiChar++);
        emojiMap.put(":mc_redstone_ore:", emojiChar++);
        emojiMap.put(":mc_diamond_ore:", emojiChar++);
        emojiMap.put(":mc_iron_ore:", emojiChar++);
        emojiMap.put(":mc_gold_ore:", emojiChar++);
        emojiMap.put(":mc_cobblestone:", emojiChar++);
        emojiMap.put(":mc_stone:", emojiChar++);
        emojiMap.put(":mc_stone_brick:", emojiChar++);
        emojiMap.put(":mc_bedrock:", emojiChar++);
        emojiMap.put(":mc_brick:", emojiChar++);
        emojiMap.put(":mc_quartz:", emojiChar++);
        emojiMap.put(":mc_redstone_lamp:", emojiChar++);
        emojiMap.put(":mc_piston:", emojiChar++);
        emojiMap.put(":mc_sticky_piston:", emojiChar++);
        emojiMap.put(":mc_dropper:", emojiChar++);
        emojiMap.put(":mc_redstone_block:", emojiChar++);
        emojiMap.put(":mc_tnt:", emojiChar++);
        emojiMap.put(":mc_jukebox:", emojiChar++);
        emojiMap.put(":mc_sponge:", emojiChar++);
        emojiMap.put(":mc_prismarine:", emojiChar++);
        emojiMap.put(":mc_command_block:", emojiChar++);
        emojiMap.put(":mc_hay:", emojiChar++);
        emojiMap.put(":mc_glowstone:", emojiChar++);
        emojiMap.put(":mc_gravel:", emojiChar++);
        emojiMap.put(":mc_lapis_lazuli_block:", emojiChar++);
        emojiMap.put(":mc_lapis_lazuli_ore:", emojiChar++);
        emojiMap.put(":mc_black_wool:", emojiChar++);
        emojiMap.put(":mc_blue_wool:", emojiChar++);
        emojiMap.put(":mc_brown_wool:", emojiChar++);
        emojiMap.put(":mc_cyan_wool:", emojiChar++);
        emojiMap.put(":mc_gray_wool:", emojiChar++);
        emojiMap.put(":mc_green_wool:", emojiChar++);
        emojiMap.put(":mc_light_blue_wool:", emojiChar++);
        emojiMap.put(":mc_lime_wool:", emojiChar++);
        emojiMap.put(":mc_magenta_wool:", emojiChar++);
        emojiMap.put(":mc_orange_wool:", emojiChar++);
        emojiMap.put(":mc_pink_wool:", emojiChar++);
        emojiMap.put(":mc_purple_wool:", emojiChar++);
        emojiMap.put(":mc_red_wool:", emojiChar++);
        emojiMap.put(":mc_light_gray_wool:", emojiChar++);
        emojiMap.put(":mc_white_wool:", emojiChar++);
        emojiMap.put(":mc_yellow_wool:", emojiChar++);
        emojiMap.put(":mc_snow_block:", emojiChar++);
        emojiMap.put(":mc_slime_block:", emojiChar++);
        emojiMap.put(":mc_obsidian:", emojiChar++);
        emojiMap.put(":mc_red_sandstone:", emojiChar++);
        emojiMap.put(":mc_portal:", emojiChar++);
        emojiMap.put(":mc_endstone:", emojiChar++);
        emojiMap.put(":mc_coal_ore:", emojiChar++);
        emojiMap.put(":mc_coal_block:", emojiChar++);
        emojiMap.put(":mc_beacon:", emojiChar++);
        emojiMap.put(":mc_bookshelf:", emojiChar++);
        emojiMap.put(":mc_chest:", emojiChar++);
        emojiMap.put(":mc_enderchest:", emojiChar++);
        emojiMap.put(":mc_blaze:", emojiChar++);
        emojiMap.put(":mc_cave_spider:", emojiChar++);
        emojiMap.put(":mc_ghast:", emojiChar++);
        emojiMap.put(":mc_zombie_pigman:", emojiChar++);
        emojiMap.put(":mc_enderman:", emojiChar++);
        emojiMap.put(":mc_magma_cube:", emojiChar++);
        emojiMap.put(":mc_slime:", emojiChar++);
        emojiMap.put(":mc_spider:", emojiChar++);
        emojiMap.put(":mc_chicken:", emojiChar++);
        emojiMap.put(":mc_pig:", emojiChar++);
        emojiMap.put(":mc_sheep:", emojiChar++);
        emojiMap.put(":mc_cow:", emojiChar++);
        emojiMap.put(":mc_squid:", emojiChar++);
        emojiMap.put(":mc_villager:", emojiChar++);
        emojiMap.put(":mc_iron_golem:", emojiChar++);
        emojiMap.put(":mc_mooshroom:", emojiChar++);
        emojiMap.put(":mc_ocelot:", emojiChar++);
        emojiMap.put(":mc_wither:", emojiChar++);
        emojiMap.put(":mc_wooden_sword:", emojiChar++);
        emojiMap.put(":mc_wooden_axe:", emojiChar++);
        emojiMap.put(":mc_wooden_pickaxe:", emojiChar++);
        emojiMap.put(":mc_wooden_shovel:", emojiChar++);
        emojiMap.put(":mc_wooden_hoe:", emojiChar++);
        emojiMap.put(":mc_bow:", emojiChar++);
        emojiMap.put(":mc_arrow:", emojiChar++);
        emojiMap.put(":mc_fishing_rod:", emojiChar++);
        emojiMap.put(":mc_flint_and_steel:", emojiChar++);
        emojiMap.put(":mc_shears:", emojiChar++);
    }

    private CatalystKeys() {
        throw new AssertionError("**boss music** No instance for you!");
    }
}
