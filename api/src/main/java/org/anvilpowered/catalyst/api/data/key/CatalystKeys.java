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

    public static final Key<Boolean> CHAT_FILTER_ENABLED = new Key<Boolean>("CHAT_FILTER_ENABLED", false) {
    };
    public static final Key<List<String>> CHAT_FILTER_SWEARS = new Key<List<String>>("CHAT_FILTER_SWEARS", Arrays.asList("2g1c", "2girls1cup", "4r5e", "anal", "anus", "arse", "ass", "asses", "assfucker", "assfukka", "asshole", "arsehole", "asswhole", "assmunch", "autoerotic", "autoerotic", "ballsack", "bastard", "beastial", "bestial", "bellend", "bdsm", "beastiality", "bestiality", "bitch", "bitches", "bitchin", "bitching", "bimbo", "bimbos", "blowjob", "blowjob", "blowjobs", "bluewaffle", "boob", "boobs", "booobs", "boooobs", "booooobs", "booooooobs", "breasts", "bootycall", "brownshower", "brownshowers", "boner", "bondage", "buceta", "bukake", "bukkake", "bullshit", "bullshit", "busty", "butthole", "carpetmuncher", "cawk", "chink", "cipa", "clit", "clits", "clitoris", "cnut", "cock", "cocks", "cockface", "cockhead", "cockmunch", "cockmuncher", "cocksuck", "cocksucked", "cocksucking", "cocksucks", "cocksucker", "cokmuncher", "coon", "cowgirl", "cowgirls", "cowgirl", "cowgirls", "crap", "crotch", "cum", "cummer", "cumming", "cuming", "cums", "cumshot", "cunilingus", "cunillingus", "cunnilingus", "cunt", "cuntlicker", "cuntlicking", "cunts", "damn", "dick", "dickhead", "dildo", "dildos", "dink", "dinks", "deepthroat", "deepthroat", "dogstyle", "doggiestyle", "doggiestyle", "doggystyle", "doggystyle", "donkeyribber", "doosh", "douche", "duche", "dyke", "ejaculate", "ejaculated", "ejaculates", "ejaculating", "ejaculatings", "ejaculation", "ejakulate", "erotic", "erotism", "fag", "faggot", "fagging", "faggit", "faggitt", "faggs", "fagot", "fagots", "fags", "fatass", "femdom", "fingering", "footjob", "footjob", "fuck", "fucks", "fucker", "fuckers", "fucked", "fuckhead", "fuckheads", "fuckin", "fucking", "fcuk", "fcuker", "fcuking", "felching", "fellate", "fellatio", "fingerfuck", "fingerfucked", "fingerfucker", "fingerfuckers", "fingerfucking", "fingerfucks", "fistfuck", "fistfucked", "fistfucker", "fistfuckers", "fistfucking", "fistfuckings", "fistfucks", "flange", "fook", "fooker", "fucka", "fuk", "fuks", "fuker", "fukker", "fukkin", "fukking", "futanari", "futanary", "gangbang", "gangbanged", "gangbang", "gokkun", "goldenshower", "goldenshower", "gaysex", "goatse", "handjob", "handjob", "hentai", "hooker", "hoer", "homo", "horny", "incest", "jackoff", "jackoff", "jerkoff", "jerkoff", "jizz", "knob", "kinbaku", "labia", "masturbate", "masochist", "mofo", "mothafuck", "motherfuck", "motherfucker", "mothafucka", "mothafuckas", "mothafuckaz", "mothafucked", "mothafucker", "mothafuckers", "mothafuckin", "mothafucking", "mothafuckings", "mothafucks", "motherfucker", "motherfucked", "motherfucker", "motherfuckers", "motherfuckin", "motherfucking", "motherfuckings", "motherfuckka", "motherfucks", "milf", "muff", "nigga", "nigger", "nigg", "nipple", "nipples", "nob", "nobjokey", "nobhead", "nobjocky", "nobjokey", "numbnuts", "nutsack", "nude", "nudes", "orgy", "orgasm", "orgasms", "panty", "panties", "penis", "playboy", "porn", "porno", "pornography", "pron", "pussy", "pussies", "rape", "raping", "rapist", "rectum", "retard", "rimming", "sadist", "sadism", "schlong", "scrotum", "sex", "semen", "shemale", "shemale", "shibari", "shibary", "shit", "shitdick", "shitfuck", "shitfull", "shithead", "shiting", "shitings", "shits", "shitted", "shitters", "shitting", "shittings", "shitty", "shota", "skank", "slut", "sluts", "smut", "smegma", "spunk", "stripclub", "stripclub", "tit", "tits", "titties", "titty", "titfuck", "tittiefucker", "titties", "tittyfuck", "tittywank", "titwank", "threesome", "threesome", "throating", "twat", "twathead", "twatty", "twunt", "viagra", "vagina", "vulva", "wank", "wanker", "wanky", "whore", "whoar", "xxx", "xx", "yaoi", "yury")) {
    };
    public static final Key<List<String>> CHAT_FILTER_EXCEPTIONS = new Key<List<String>>("CHAT_FILTER_EXCEPTIONS", Arrays.asList("assassin", "jkass")) {
    };
    public static final Key<String> FIRST_JOIN = new Key<String>("FIRST_JOIN", "Welcome to the server, %player%") {
    };
    public static final Key<String> JOIN_MESSAGE = new Key<String>("JOIN_MESSAGE", "%player% has joined the proxy") {
    };
    public static final Key<String> LEAVE_MESSAGE = new Key<String>("LEAVE_MESSAGE", "%player% has left the proxy") {
    };
    public static final Key<Boolean> PROXY_CHAT_ENABLED = new Key<Boolean>("PROXY_CHAT_ENABLED", true) {
    };
    public static final Key<String> PROXY_CHAT_FORMAT_MESSAGE = new Key<String>("PROXY_CHAT_FORMAT_MESSAGE", "%prefix% %username% %suffix%: %message% ") {
    };
    public static final Key<String> PROXY_CHAT_FORMAT_HOVER = new Key<String>("PROXY_CHAT_FORMAT_HOVER", "%player% %server%") {
    };
    public static final Key<String> PROXY_CHAT_FORMAT_CLICK_COMMAND = new Key<String>("PROXY_CHAT_FORMAT_CLICK_COMMAND", "/msg %player%") {
    };
    public static final Key<Boolean> TAB_ENABLED = new Key<Boolean>("TAB", true) {
    };
    public static final Key<String> TAB_HEADER = new Key<String>("TAB_HEADER", "Welcome to") {
    };
    public static final Key<String> TAB_FOOTER = new Key<String>("TAB_FOOTER", "A Velocity AdvancedServerInfo") {
    };
    public static final Key<String> TAB_FORMAT = new Key<String>("TAB_FORMAT", "%prefix% %player% %suffix%") {
    };
    public static final Key<List<String>> TAB_FORMAT_CUSTOM = new Key<List<String>>("TAB_FORMAT_CUSTOM", Arrays.asList("&3Your Ping : &e%ping%", "&3Current AdvancedServerInfo : &e%server%", "&eBalance : &e%balance%")) {
    };
    public static final Key<Integer> TAB_UPDATE = new Key<Integer>("TAB_UPDATE", 1) {
    };
    public static final Key<String> CHAT_DEFAULT_CHANNEL = new Key<String>("CHAT_DEFAULT_CHANNEL", "global") {
    };
    public static final Key<String> BAN = new Key<String>("BAN", "catalyst.command.ban") {
    };
    public static final Key<String> TEMP_BAN = new Key<String>("TEMP_BAN", "catalyst.command.tempban") {
    };
    public static final Key<String> BAN_EXEMPT = new Key<String>("BAN_EXEMPT", "catalyst.command.ban.exempt") {
    };
    public static final Key<String> BROADCAST = new Key<String>("BROADCAST", "catalyst.command.broadcast") {
    };
    public static final Key<String> CHAT_COLOR = new Key<String>("CHAT_COLOR", "catalyst.chat.color") {
    };
    public static final Key<String> FIND = new Key<String>("FIND", "catalyst.command.find") {
    };
    public static final Key<String> GOOGLE = new Key<String>("GOOGLE", "catalyst.command.google") {
    };
    public static final Key<String> INFO = new Key<String>("INFO", "catalyst.command.info") {
    };
    public static final Key<String> KICK = new Key<String>("KICK", "catalyst.command.kick") {
    };
    public static final Key<String> KICK_EXEMPT = new Key<String>("KICK_EXEMPT", "catalyst.command.kick.exempt") {
    };
    public static final Key<String> LANGUAGE_ADMIN = new Key<String>("LANGUAGE_ADMIN", "catalyst.command.language.admin") {
    };
    public static final Key<String> LANGUAGE_LIST = new Key<String>("LANGUAGE_LIST", "catalyst.command.language.list") {
    };
    public static final Key<String> LIST = new Key<String>("LIST", "catalyst.command.list") {
    };
    public static final Key<String> MESSAGE = new Key<String>("MESSAGE", "catalyst.command.message") {
    };
    public static final Key<String> MUTE = new Key<String>("MUTE", "catalyst.command.mute") {
    };
    public static final Key<String> MUTE_EXEMPT = new Key<String>("MUTE_EXEMPT", "catalyst.command.mute.exempt") {
    };
    public static final Key<String> NICKNAME = new Key<String>("NICKNAME", "catalyst.command.nickname") {
    };
    public static final Key<String> NICKNAME_COLOR = new Key<String>("NICKNAME_COLOR", "catalyst.command.nickname.color") {
    };
    public static final Key<String> NICKNAME_MAGIC = new Key<String>("NICKNAME_COLOR", "catalyst.command.nickname.magic") {
    };
    public static final Key<String> NICKNAME_OTHER = new Key<String>("NICKNAME_OTHER", "catalyst.command.nickname.other") {
    };
    public static final Key<String> NICKNAME_PREFIX = new Key<String>("NICKNAME_PREFIX", "~") {
    };
    public static final Key<String> RELOAD = new Key<String>("RELOAD", "catalyst.command.reload") {
    };
    public static final Key<String> SEND = new Key<String>("SEND", "catalyst.admin.command.send") {
    };
    public static final Key<String> SENDGOOGLE = new Key<String>("SENDGOOGLE", "catalyst.admin.command.sendgoogle") {
    };
    public static final Key<String> SOCIALSPY = new Key<String>("SOCIALSPY", "catalyst.admin.command.socialspy") {
    };
    public static final Key<String> SOCIALSPY_ONJOIN = new Key<String>("SOCIALSPY_ONJOIN", "catalyst.admin.command.socialspy.onjoin") {
    };
    public static final Key<String> STAFFCHAT = new Key<String>("STAFFCHAT", "catalyst.admin.command.staffchat") {
    };
    public static final Key<String> STAFFLIST_ADMIN = new Key<String>("STAFFLIST_ADMIN", "catalyst.stafflist.admin") {
    };
    public static final Key<String> STAFFLIST_BASE = new Key<String>("STAFFLIST_BASE", "catalyst.stafflist.base") {
    };
    public static final Key<String> STAFFLIST_OWNER = new Key<String>("STAFFLIST_OWNER", "catalyst.stafflist.owner") {
    };
    public static final Key<String> STAFFLIST_STAFF = new Key<String>("STAFFLIST_STAFF", "catalyst.stafflist.staff") {
    };
    public static final Key<String> ALL_CHAT_CHANNELS = new Key<String>("ALL_CHAT_CHANNELS", "catalyst.channel.all") {
    };
    public static final Key<String> CHANNEL_BASE = new Key<String>("CHANNEL_BASE", "catalyst.channel.") {
    };
    public static final Key<String> BOT_NAME = new Key<String>("BOT_NAME", "System") {
    };
    public static final Key<String> BOT_TOKEN = new Key<String>("BOT_TOKEN", "bot token") {
    };
    public static final Key<String> MAIN_CHANNEL = new Key<String>("MAIN_CHANNEL", "mainchannelid") {
    };
    public static final Key<String> STAFF_CHANNEL = new Key<String>("STAFF_CHANNEL", "staffchannelid") {
    };
    public static final Key<String> PLAYER_CHAT_FORMAT = new Key<String>("PLAYER_CHAT_FORMAT", "[%server%] %prefix% %player% %suffix%") {
    };
    public static final Key<String> JOIN_FORMAT = new Key<String>("JOIN_FORMAT", "%player% has joined the game.") {
    };
    public static final Key<String> LEAVE_FORMAT = new Key<String>("LEAVE_FORMAT", "%player% has left the game.") {
    };
    public static final Key<String> DISCORD_CHAT_FORMAT = new Key<String>("DISCORD_CHAT_FORMAT", "&6[Discord]&7 %name% : %message%") {
    };
    public static final Key<String> DISCORD_STAFF_FORMAT = new Key<String>("DISCORD_STAFF_FORMAT", "&b[STAFF] &7Discord %name%:&b %message%") {
    };
    public static final Key<String> TOPIC_FORMAT = new Key<String>("TOPIC_FORMAT", "Player Count : %players%") {
    };
    public static final Key<Integer> TOPIC_UPDATE_DELAY = new Key<Integer>("TOPIC_UPDATE_DELAY", 10) {
    };
    public static final Key<String> NOW_PLAYING_MESSAGE = new Key<String>("NOW_PLAYING_MESSAGE", "A Minecraft AdvancedServerInfo!") {
    };
    public static final Key<String> WEBHOOK_URL = new Key<String>("WEBHOOK_URL", "https://crafatar.com/avatars/%uuid%?default=MHF_Alex") {
    };
    public static final Key<String> DISCORD_URL = new Key<String>("DISCORD_URL", "https://discord.gg/hZpQ5Sg") {
    };
    public static final Key<Boolean> DISCORD_ENABLE = new Key<Boolean>("DISCORD_ENABLE", false) {
    };
    public static final Key<String> DISCORD_HOVER_MESSAGE = new Key<String>("DISCORD_HOVER_MESSAGE", "Click here to join our discord!") {
    };
    public static final Key<String> WEBSITE_URL = new Key<String>("WEBSITE_URL", "http://ci.anvilpowered.org") {
    };
    public static final Key<String> IGNORE = new Key<String>("IGNORE", "catalyst.command.ignore") {
    };
    public static final Key<String> IGNORE_EXEMPT = new Key<String>("IGNORE_EXEMPT", "catalyst.command.ignore.exempt") {
    };
    public static final Key<String> SERVER_PING = new Key<String>("SERVER_PING", "PLAYERS") {
    };
    public static final Key<String> SERVER_PING_MESSAGE = new Key<String>("SERVER_PING_MESSAGE", "Change this message in the config!") {
    };
    public static final Key<String> SYNC_COMMAND = new Key<String>("SYNC_COMMAND", "catalyst.admin.command.sync") {
    };
    public static final Key<String> MOTD = new Key<String>("MOTD", "A Velocity Proxy!") {
    };
    public static final Key<Boolean> ADVANCED_SERVER_INFO_ENABLED = new Key<Boolean>("ADVANCED_SERVER_INFO_ENABLED", false) {
    };
    static List<ChatChannel> chatChannels = new LinkedList<>();
    public static final Key<List<ChatChannel>> CHAT_CHANNELS = new Key<List<ChatChannel>>("CHAT_CHANNELS", chatChannels) {
    };
    static List<AdvancedServerInfo> advancedServerInfo = new LinkedList<>();
    public static final Key<List<AdvancedServerInfo>> ADVANCED_SERVER_INFO = new Key<List<AdvancedServerInfo>>("ADVANCED_SERVER_INFO", advancedServerInfo) {
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
        Keys.registerKey(BAN);
        Keys.registerKey(TEMP_BAN);
        Keys.registerKey(BAN_EXEMPT);
        Keys.registerKey(BROADCAST);
        Keys.registerKey(CHAT_COLOR);
        Keys.registerKey(FIND);
        Keys.registerKey(GOOGLE);
        Keys.registerKey(INFO);
        Keys.registerKey(KICK);
        Keys.registerKey(KICK_EXEMPT);
        Keys.registerKey(LANGUAGE_ADMIN);
        Keys.registerKey(LANGUAGE_LIST);
        Keys.registerKey(LIST);
        Keys.registerKey(MESSAGE);
        Keys.registerKey(MUTE);
        Keys.registerKey(MUTE_EXEMPT);
        Keys.registerKey(NICKNAME);
        Keys.registerKey(NICKNAME_COLOR);
        Keys.registerKey(NICKNAME_MAGIC);
        Keys.registerKey(NICKNAME_OTHER);
        Keys.registerKey(NICKNAME_PREFIX);
        Keys.registerKey(RELOAD);
        Keys.registerKey(SEND);
        Keys.registerKey(SENDGOOGLE);
        Keys.registerKey(SOCIALSPY);
        Keys.registerKey(SOCIALSPY_ONJOIN);
        Keys.registerKey(STAFFCHAT);
        Keys.registerKey(STAFFLIST_ADMIN);
        Keys.registerKey(STAFFLIST_BASE);
        Keys.registerKey(STAFFLIST_OWNER);
        Keys.registerKey(STAFFLIST_STAFF);
        Keys.registerKey(ALL_CHAT_CHANNELS);
        Keys.registerKey(CHANNEL_BASE);
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
        Keys.registerKey(IGNORE);
        Keys.registerKey(IGNORE_EXEMPT);
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
