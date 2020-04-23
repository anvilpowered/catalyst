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

import org.anvilpowered.anvil.api.data.key.Key;
import org.anvilpowered.anvil.api.data.key.Keys;
import org.anvilpowered.catalyst.api.data.config.AdvancedServerInfo;
import org.anvilpowered.catalyst.api.data.config.ChatChannel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class CatalystKeys {

    public static final Key<Boolean> CHAT_FILTER_ENABLED = new Key<Boolean>("CHAT_FILTER_ENABLED",
        false) {
    };
    public static final Key<List<String>> CHAT_FILTER_SWEARS = new Key<List<String>>("CHAT_FILTER_SWEARS", Arrays.asList(
        "2g1c", "2girls1cup", "4r5e", "anal", "anus", "arse", "ass", "asses", "assfucker", "assfukka", "asshole",
        "arsehole", "asswhole", "assmunch", "autoerotic", "ballsack", "bastard", "beastial", "bestial",
        "bellend", "bdsm", "beastiality", "bestiality", "bitch", "bitches", "bitchin", "bitching", "bimbo", "bimbos",
        "blowjob", "blowjob", "blowjobs", "bluewaffle", "boob", "boobs", "booobs", "boooobs", "booooobs", "booooooobs",
        "breasts", "bootycall", "brownshower", "brownshowers", "boner", "bondage", "buceta", "bukake", "bukkake",
        "bullshit", "busty", "butthole", "carpetmuncher", "cawk", "chink", "cipa", "clit", "clits", "clitoris", "cnut",
        "cock", "cocks", "cockface", "cockhead", "cockmunch", "cockmuncher", "cocksuck", "cocksucked", "cocksucking",
        "cocksucks", "cocksucker", "cokmuncher", "coon", "cowgirl", "cowgirls", "cowgirl", "cowgirls", "crap", "crotch",
        "cum", "cummer", "cumming", "cuming", "cums", "cumshot", "cunilingus", "cunillingus", "cunnilingus", "cunt",
        "cuntlicker", "cuntlicking", "cunts", "damn", "dick", "dickhead", "dildo", "dildos", "dink", "dinks",
        "deepthroat", "deepthroat", "dogstyle", "doggiestyle", "doggiestyle", "doggystyle", "doggystyle",
        "donkeyribber", "doosh", "douche", "duche", "dyke", "ejaculate", "ejaculated", "ejaculates", "ejaculating",
        "ejaculatings", "ejaculation", "ejakulate", "erotic", "erotism", "fag", "faggot", "fagging", "faggit",
        "faggitt", "faggs", "fagot", "fagots", "fags", "fatass", "femdom", "fingering", "footjob", "footjob", "fuck",
        "fucks", "fucker", "fuckers", "fucked", "fuckhead", "fuckheads", "fuckin", "fucking", "fcuk", "fcuker",
        "fcuking", "felching", "fellate", "fellatio", "fingerfuck", "fingerfucked", "fingerfucker", "fingerfuckers",
        "fingerfucking", "fingerfucks", "fistfuck", "fistfucked", "fistfucker", "fistfuckers", "fistfucking",
        "fistfuckings", "fistfucks", "flange", "fook", "fooker", "fucka", "fuk", "fuks", "fuker", "fukker", "fukkin",
        "fukking", "futanari", "futanary", "gangbang", "gangbanged", "gangbang", "gokkun", "goldenshower",
        "goldenshower", "gaysex", "goatse", "handjob", "handjob", "hentai", "hooker", "hoer", "homo", "horny", "incest",
        "jackoff", "jackoff", "jerkoff", "jerkoff", "jizz", "knob", "kinbaku", "labia", "masturbate", "masochist",
        "mofo", "mothafuck", "motherfuck", "motherfucker", "mothafucka", "mothafuckas", "mothafuckaz", "mothafucked",
        "mothafucker", "mothafuckers", "mothafuckin", "mothafucking", "mothafuckings", "mothafucks", "motherfucker",
        "motherfucked", "motherfucker", "motherfuckers", "motherfuckin", "motherfucking", "motherfuckings",
        "motherfuckka", "motherfucks", "milf", "muff", "nigga", "nigger", "nigg", "nipple", "nipples", "nob",
        "nobjokey", "nobhead", "nobjocky", "nobjokey", "numbnuts", "nutsack", "nude", "nudes", "orgy", "orgasm",
        "orgasms", "panty", "panties", "penis", "playboy", "porn", "porno", "pornography", "pron", "pussy", "pussies",
        "rape", "raping", "rapist", "rectum", "retard", "rimming", "sadist", "sadism", "schlong", "scrotum", "sex",
        "semen", "shemale", "shemale", "shibari", "shibary", "shit", "shitdick", "shitfuck", "shitfull", "shithead",
        "shiting", "shitings", "shits", "shitted", "shitters", "shitting", "shittings", "shitty", "shota", "skank",
        "slut", "sluts", "smut", "smegma", "spunk", "stripclub", "stripclub", "tit", "tits", "titties", "titty",
        "titfuck", "tittiefucker", "titties", "tittyfuck", "tittywank", "titwank", "threesome", "threesome",
        "throating", "twat", "twathead", "twatty", "twunt", "viagra", "vagina", "vulva", "wank", "wanker",
        "wanky", "whore", "whoar", "xxx", "xx", "yaoi", "yury")) {
    };
    public static final Key<List<String>> CHAT_FILTER_EXCEPTIONS = new Key<List<String>>("CHAT_FILTER_EXCEPTIONS",
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
    public static final Key<Boolean> PROXY_CHAT_ENABLED = new Key<Boolean>("PROXY_CHAT_ENABLED",
        true) {
    };
    public static final Key<String> PROXY_CHAT_FORMAT_MESSAGE = new Key<String>("PROXY_CHAT_FORMAT_MESSAGE",
        "%prefix% %player% %suffix%: %message% ") {
    };
    public static final Key<String> PROXY_CHAT_FORMAT_HOVER = new Key<String>("PROXY_CHAT_FORMAT_HOVER",
        "%player% %server%") {
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
            "&3Your Ping : &e%ping%", "&3Current Server : &e%server%", "&3Player Count : &e%playercount%")) {
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
    public static final Key<String> BAN_EXEMPT_PERMISSION = new Key<String>("BAN_EXEMPT_PERMISSION",
        "catalyst.command.ban.exempt") {
    };
    public static final Key<String> BROADCAST_PERMISSION = new Key<String>("BROADCAST_PERMISSION",
        "catalyst.command.broadcast") {
    };
    public static final Key<String> CHAT_COLOR_PERMISSION = new Key<String>("CHAT_COLOR_PERMISSION",
        "catalyst.chat.color") {
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
    public static final Key<String> KICK_PERMISSION = new Key<String>("KICK_PERMISSION",
        "catalyst.command.kick") {
    };
    public static final Key<String> KICK_EXEMPT_PERMISSION = new Key<String>("KICK_EXEMPT_PERMISSION",
        "catalyst.command.kick.exempt") {
    };
    public static final Key<String> LANGUAGE_ADMIN_PERMISSION = new Key<String>("LANGUAGE_ADMIN_PERMISSION",
        "catalyst.command.language.admin") {
    };
    public static final Key<String> LANGUAGE_LIST_PERMISSION = new Key<String>("LANGUAGE_LIST_PERMISSION",
        "catalyst.command.language.list") {
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
    public static final Key<String> MUTE_EXEMPT_PERMISSION = new Key<String>("MUTE_EXEMPT_PERMISSION",
        "catalyst.command.mute.exempt") {
    };
    public static final Key<String> NICKNAME_PERMISSION = new Key<String>("NICKNAME_PERMISSION",
        "catalyst.command.nickname") {
    };
    public static final Key<String> NICKNAME_COLOR_PERMISSION = new Key<String>("NICKNAME_COLOR_PERMISSION",
        "catalyst.command.nickname.color") {
    };
    public static final Key<String> NICKNAME_MAGIC_PERMISSION = new Key<String>("NICKNAME_COLOR_PERMISSION",
        "catalyst.command.nickname.magic") {
    };
    public static final Key<String> NICKNAME_OTHER_PERMISSION = new Key<String>("NICKNAME_OTHER_PERMISSION",
        "catalyst.command.nickname.other") {
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
    public static final Key<String> SOCIALSPY_ONJOIN_PERMISSION = new Key<String>("SOCIALSPY_ONJOIN_PERMISSION",
        "catalyst.admin.command.socialspy.onjoin") {
    };
    public static final Key<String> STAFFCHAT_PERMISSION = new Key<String>("STAFFCHAT_PERMISSION",
        "catalyst.admin.command.staffchat") {
    };
    public static final Key<String> STAFFLIST_ADMIN_PERMISSION = new Key<String>("STAFFLIST_ADMIN_PERMISSION",
        "catalyst.stafflist.admin") {
    };
    public static final Key<String> STAFFLIST_BASE_PERMISSION = new Key<String>("STAFFLIST_BASE_PERMISSION",
        "catalyst.stafflist.base") {
    };
    public static final Key<String> STAFFLIST_OWNER_PERMISSION = new Key<String>("STAFFLIST_OWNER_PERMISSION",
        "catalyst.stafflist.owner") {
    };
    public static final Key<String> STAFFLIST_STAFF_PERMISSION = new Key<String>("STAFFLIST_STAFF_PERMISSION",
        "catalyst.stafflist.staff") {
    };
    public static final Key<String> ALL_CHAT_CHANNELS_PERMISSION = new Key<String>("ALL_CHAT_CHANNELS_PERMISSION",
        "catalyst.channel.all") {
    };
    public static final Key<String> CHANNEL_BASE_PERMISSION = new Key<String>("CHANNEL_BASE_PERMISSION",
        "catalyst.channel.") {
    };
    public static final Key<String> BOT_NAME = new Key<String>("BOT_NAME",
        "System") {
    };
    public static final Key<String> BOT_TOKEN = new Key<String>("BOT_TOKEN",
        "bot token") {
    };
    public static final Key<String> MAIN_CHANNEL = new Key<String>("MAIN_CHANNEL",
        "mainchannelid") {
    };
    public static final Key<String> STAFF_CHANNEL = new Key<String>("STAFF_CHANNEL",
        "staffchannelid") {
    };
    public static final Key<String> PLAYER_CHAT_FORMAT = new Key<String>("PLAYER_CHAT_FORMAT",
        "[%server%] %prefix% %player% %suffix%") {
    };
    public static final Key<String> JOIN_FORMAT = new Key<String>("JOIN_FORMAT",
        "%player% has joined the game.") {
    };
    public static final Key<String> LEAVE_FORMAT = new Key<String>("LEAVE_FORMAT",
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
    public static final Key<Integer> TOPIC_UPDATE_DELAY = new Key<Integer>("TOPIC_UPDATE_DELAY",
        10) {
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
    public static final Key<String> DISCORD_HOVER_MESSAGE = new Key<String>("DISCORD_HOVER_MESSAGE",
        "Click here to join our discord!") {
    };
    public static final Key<String> WEBSITE_URL = new Key<String>("WEBSITE_URL",
        "http://ci.anvilpowered.org") {
    };
    public static final Key<String> IGNORE_PERMISSION = new Key<String>("IGNORE_PERMISSION",
        "catalyst.command.ignore") {
    };
    public static final Key<String> IGNORE_EXEMPT_PERMISSION = new Key<String>("IGNORE_EXEMPT_PERMISSION",
        "catalyst.command.ignore.exempt") {
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
    public static final Key<Boolean> ADVANCED_SERVER_INFO_ENABLED = new Key<Boolean>("ADVANCED_SERVER_INFO_ENABLED",
        false) {
    };
    static List<ChatChannel> chatChannels = new LinkedList<>();
    public static final Key<List<ChatChannel>> CHAT_CHANNELS = new Key<List<ChatChannel>>("CHAT_CHANNELS",
        chatChannels) {
    };
    static List<AdvancedServerInfo> advancedServerInfo = new LinkedList<>();
    public static final Key<List<AdvancedServerInfo>> ADVANCED_SERVER_INFO = new Key<List<AdvancedServerInfo>>("ADVANCED_SERVER_INFO",
        advancedServerInfo) {
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
        Keys.registerKey(CHAT_FILTER_ENABLED);
        Keys.registerKey(CHAT_FILTER_EXCEPTIONS);
        Keys.registerKey(CHAT_FILTER_SWEARS);
        Keys.registerKey(FIRST_JOIN);
        Keys.registerKey(JOIN_MESSAGE);
        Keys.registerKey(LEAVE_MESSAGE);
        Keys.registerKey(PROXY_CHAT_ENABLED);
        Keys.registerKey(PROXY_CHAT_FORMAT_MESSAGE);
        Keys.registerKey(PROXY_CHAT_FORMAT_HOVER);
        Keys.registerKey(TAB_ENABLED);
        Keys.registerKey(TAB_HEADER);
        Keys.registerKey(TAB_FOOTER);
        Keys.registerKey(TAB_FORMAT);
        Keys.registerKey(TAB_FORMAT_CUSTOM);
        Keys.registerKey(TAB_UPDATE);
        Keys.registerKey(CHAT_CHANNELS);
        Keys.registerKey(CHAT_DEFAULT_CHANNEL);
        Keys.registerKey(BAN_PERMISSION);
        Keys.registerKey(TEMP_BAN_PERMISSION);
        Keys.registerKey(BAN_EXEMPT_PERMISSION);
        Keys.registerKey(BROADCAST_PERMISSION);
        Keys.registerKey(CHAT_COLOR_PERMISSION);
        Keys.registerKey(FIND_PERMISSION);
        Keys.registerKey(GOOGLE_PERMISSION);
        Keys.registerKey(INFO_PERMISSION);
        Keys.registerKey(KICK_PERMISSION);
        Keys.registerKey(KICK_EXEMPT_PERMISSION);
        Keys.registerKey(LANGUAGE_ADMIN_PERMISSION);
        Keys.registerKey(LANGUAGE_LIST_PERMISSION);
        Keys.registerKey(LIST_PERMISSION);
        Keys.registerKey(MESSAGE_PERMISSION);
        Keys.registerKey(MUTE_PERMISSION);
        Keys.registerKey(MUTE_EXEMPT_PERMISSION);
        Keys.registerKey(NICKNAME_PERMISSION);
        Keys.registerKey(NICKNAME_COLOR_PERMISSION);
        Keys.registerKey(NICKNAME_MAGIC_PERMISSION);
        Keys.registerKey(NICKNAME_OTHER_PERMISSION);
        Keys.registerKey(NICKNAME_PREFIX);
        Keys.registerKey(SEND_PERMISSION);
        Keys.registerKey(SENDGOOGLE_PERMISSION);
        Keys.registerKey(SOCIALSPY_PERMISSION);
        Keys.registerKey(SOCIALSPY_ONJOIN_PERMISSION);
        Keys.registerKey(STAFFCHAT_PERMISSION);
        Keys.registerKey(STAFFLIST_ADMIN_PERMISSION);
        Keys.registerKey(STAFFLIST_BASE_PERMISSION);
        Keys.registerKey(STAFFLIST_OWNER_PERMISSION);
        Keys.registerKey(STAFFLIST_STAFF_PERMISSION);
        Keys.registerKey(ALL_CHAT_CHANNELS_PERMISSION);
        Keys.registerKey(CHANNEL_BASE_PERMISSION);
        Keys.registerKey(BOT_NAME);
        Keys.registerKey(BOT_TOKEN);
        Keys.registerKey(MAIN_CHANNEL);
        Keys.registerKey(STAFF_CHANNEL);
        Keys.registerKey(PLAYER_CHAT_FORMAT);
        Keys.registerKey(JOIN_FORMAT);
        Keys.registerKey(LEAVE_FORMAT);
        Keys.registerKey(DISCORD_CHAT_FORMAT);
        Keys.registerKey(DISCORD_STAFF_FORMAT);
        Keys.registerKey(TOPIC_FORMAT);
        Keys.registerKey(TOPIC_FORMAT);
        Keys.registerKey(TOPIC_UPDATE_DELAY);
        Keys.registerKey(NOW_PLAYING_MESSAGE);
        Keys.registerKey(WEBHOOK_URL);
        Keys.registerKey(DISCORD_URL);
        Keys.registerKey(DISCORD_ENABLE);
        Keys.registerKey(DISCORD_HOVER_MESSAGE);
        Keys.registerKey(WEBSITE_URL);
        Keys.registerKey(IGNORE_PERMISSION);
        Keys.registerKey(IGNORE_EXEMPT_PERMISSION);
        Keys.registerKey(SERVER_PING);
        Keys.registerKey(SERVER_PING_MESSAGE);
        Keys.registerKey(SYNC_COMMAND);
        Keys.registerKey(MOTD);
        Keys.registerKey(ADVANCED_SERVER_INFO_ENABLED);
        Keys.registerKey(ADVANCED_SERVER_INFO);
    }

    private CatalystKeys() {
        throw new AssertionError("**boss music** No instance for you!");
    }
}
