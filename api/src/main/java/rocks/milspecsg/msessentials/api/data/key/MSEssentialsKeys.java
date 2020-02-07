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

package rocks.milspecsg.msessentials.api.data.key;

import rocks.milspecsg.anvil.api.data.key.Key;
import rocks.milspecsg.anvil.api.data.key.Keys;
import rocks.milspecsg.msessentials.api.data.config.Channel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class MSEssentialsKeys {

    private MSEssentialsKeys() {
        throw new AssertionError("**boss music** No instance for you!");
    }

    static List<Channel> channels = new LinkedList<>();

    static {
        Channel global = new Channel();
        global.id = "global";
        global.aliases = Arrays.asList("globalchat", "globalchannel");
        global.prefix = "[G]";
        global.servers = Arrays.asList("main", "wild");
        global.alwaysVisible = true;
        channels.add(global);
        Channel staff = new Channel();
        staff.id = "staff";
        staff.aliases = Arrays.asList("staffchannel", "staffchat");
        staff.prefix = "[S]";
        staff.alwaysVisible = true;
        staff.servers = Arrays.asList("main", "wild");
        channels.add(staff);
    }

    public static final Key<String> MOTD = new Key<String>("MOTD", "&eA &eVelocity &eServer") {
    };
    public static final Key<Boolean> CHAT_FILTER_ENABLED = new Key<Boolean>("CHAT_FILTER_ENABLED", false) {
    };
    public static final Key<List<String>> CHAT_FILTER_SWEARS = new Key<List<String>>("CHAT_FILTER_SWEARS", Arrays.asList("fuck", "shit", "ass")) {
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
    public static final Key<Boolean> SERVER_COMMAND = new Key<Boolean>("SERVER_COMMAND", true) {
    };
    public static final Key<Boolean> TAB_ENABLED = new Key<Boolean>("TAB", true) {
    };
    public static final Key<String> TAB_HEADER = new Key<String>("TAB_HEADER", "Welcome to") {
    };
    public static final Key<String> TAB_FOOTER = new Key<String>("TAB_FOOTER", "A Velocity Server") {
    };
    public static final Key<String> TAB_FORMAT = new Key<String>("TAB_FORMAT", "%prefix% %player% %suffix%") {
    };
    public static final Key<List<String>> TAB_FORMAT_CUSTOM = new Key<List<String>>("TAB_FORMAT_CUSTOM", Arrays.asList("&3Your Ping : &e%ping%", "&3Current Server : &e%server%", "&eBalance : &e%balance%")) {
    };
    public static final Key<Integer> TAB_UPDATE = new Key<Integer>("TAB_UPDATE", 1) {
    };
    public static final Key<List<Channel>> CHAT_CHANNELS = new Key<List<Channel>>("CHAT_CHANNELS", channels) {
    };
    public static final Key<String> CHAT_DEFAULT_CHANNEL = new Key<String>("CHAT_DEFAULT_CHANNEL", "global") {
    };
    public static final Key<String> BAN = new Key<String>("BAN", "msessentials.command.ban") {
    };
    public static final Key<String> TEMP_BAN = new Key<String>("TEMP_BAN", "msessentials.command.tempban") {
    };
    public static final Key<String> BAN_EXEMPT = new Key<String>("BAN_EXEMPT", "msessentials.command.ban.exempt") {
    };
    public static final Key<String> BROADCAST = new Key<String>("BROADCAST", "msessentials.command.broadcast") {
    };
    public static final Key<String> CHAT_COLOR = new Key<String>("CHAT_COLOR", "msessentials.chat.color") {
    };
    public static final Key<String> FIND = new Key<String>("FIND", "msessentials.command.find") {
    };
    public static final Key<String> GOOGLE = new Key<String>("GOOGLE", "msessentials.command.google") {
    };
    public static final Key<String> INFO = new Key<String>("INFO", "msessentials.command.info") {
    };
    public static final Key<String> KICK = new Key<String>("KICK", "msessentials.command.kick") {
    };
    public static final Key<String> KICK_EXEMPT = new Key<String>("KICK_EXEMPT", "msessentials.command.kick.exempt") {
    };
    public static final Key<String> LANGUAGE_ADMIN = new Key<String>("LANGUAGE_ADMIN", "msessentials.command.language.admin") {
    };
    public static final Key<String> LANGUAGE_LIST = new Key<String>("LANGUAGE_LIST", "msessentials.command.language.list") {
    };
    public static final Key<String> LIST = new Key<String>("LIST", "msessentials.command.list") {
    };
    public static final Key<String> MESSAGE = new Key<String>("MESSAGE", "msessentials.command.message") {
    };
    public static final Key<String> MUTE = new Key<String>("MUTE", "msessentials.command.mute") {
    };
    public static final Key<String> MUTE_EXEMPT = new Key<String>("MUTE_EXEMPT", "msessentials.command.mute.exempt") {
    };
    public static final Key<String> NICKNAME = new Key<String>("NICKNAME", "msessentials.command.nickname") {
    };
    public static final Key<String> NICKNAME_COLOR = new Key<String>("NICKNAME_COLOR", "msessentials.command.nickname.color") {
    };
    public static final Key<String> NICKNAME_OTHER = new Key<String>("NICKNAME_OTHER", "msessentials.command.nickname.other"){
    };
    public static final Key<String> RELOAD = new Key<String>("RELOAD", "msessentials.command.reload"){
    };
    public static final Key<String> SEND = new Key<String>("SEND", "msessentials.admin.command.send"){
    };
    public static final Key<String> SERVER_BASE = new Key<String>("SERVER_BASE", "msessentials.server."){
    };
    public static final Key<String> SENDGOOGLE = new Key<String>("SENDGOOGLE", "msessentials.admin.command.sendgoogle"){
    };
    public static final Key<String> SOCIALSPY = new Key<String>("SOCIALSPY", "msessentials.admin.command.socialspy"){
    };
    public static final Key<String> SOCIALSPY_ONJOIN = new Key<String>("SOCIALSPY_ONJOIN", "msessentials.admin.command.socialspy.onjoin"){
    };
    public static final Key<String> STAFFCHAT = new Key<String>("STAFFCHAT", "msessentials.admin.command.staffchat"){
    };
    public static final Key<String> STAFFLIST_ADMIN = new Key<String>("STAFFLIST_ADMIN", "msessentials.stafflist.admin") {
    };
    public static final Key<String> STAFFLIST_BASE = new Key<String>("STAFFLIST_BASE", "msessentials.stafflist.base"){
    };
    public static final Key<String> STAFFLIST_OWNER = new Key<String>("STAFFLIST_OWNER", "msessentials.stafflist.owner"){
    };
    public static final Key<String> STAFFLIST_STAFF = new Key<String>("STAFFLIST_STAFF", "msessentials.stafflist.staff") {
    };
    public static final Key<String> ALL_CHAT_CHANNELS = new Key<String>("ALL_CHAT_CHANNELS", "msessentials.channel.all"){
    };
    public static final Key<String> CHANNEL_BASE = new Key<String>("CHANNEL_BASE","msessentials.channel."){
    };

    static {
        Keys.registerKey(MOTD);
        Keys.registerKey(CHAT_FILTER_ENABLED);
        Keys.registerKey(CHAT_FILTER_EXCEPTIONS);
        Keys.registerKey(CHAT_FILTER_SWEARS);
        Keys.registerKey(FIRST_JOIN);
        Keys.registerKey(JOIN_MESSAGE);
        Keys.registerKey(LEAVE_MESSAGE);
        Keys.registerKey(PROXY_CHAT_ENABLED);
        Keys.registerKey(PROXY_CHAT_FORMAT_MESSAGE);
        Keys.registerKey(PROXY_CHAT_FORMAT_HOVER);
        Keys.registerKey(SERVER_COMMAND);
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
        Keys.registerKey(NICKNAME_OTHER);
        Keys.registerKey(RELOAD);
        Keys.registerKey(SEND);
        Keys.registerKey(SERVER_BASE);
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
    }
}
