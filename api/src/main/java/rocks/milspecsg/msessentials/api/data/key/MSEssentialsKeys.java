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

import rocks.milspecsg.msessentials.api.data.config.Channel;
import rocks.milspecsg.msrepository.api.data.key.Key;
import rocks.milspecsg.msrepository.api.data.key.Keys;

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
    }
}
