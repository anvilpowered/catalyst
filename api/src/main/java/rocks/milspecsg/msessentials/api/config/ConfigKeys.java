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

package rocks.milspecsg.msessentials.api.config;

public interface ConfigKeys extends rocks.milspecsg.msrepository.api.config.ConfigKeys{
    int BROADCAST_PREFIX = 10;
    int CHAT_FILTER_ENABLED = 11;
    int CHAT_FILTER_SWEARS = 12;
    int CHAT_FILTER_EXCEPTIONS = 13;
    int JOIN_MESSAGE = 14;
    int LEAVE_MESSAGE = 15;
    int WELCOME_MESSAGE = 16;
    int MOTD = 17;
    int SERVER_COMMAND_ENABLED = 18;
    int PROXY_CHAT_ENABLED = 50;
    int GLOBAL_TAB_ENABLED = 70;
    int GLOBAL_TAB_HEADER = 71;
    int GLOBAL_TAB_FOOTER = 72;
    int GLOBAL_TAB_PLAYER_FORMAT = 73;
    int GLOBAL_TAB_CUSTOM = 74;
    int GLOBAL_TAB_UPDATE_DELAY = 75;
    int GLOBAL_TAB_DISABLED_SERVERS = 76;
    int MONGODB_HOSTNAME = 90;
    int MONGODB_PORT = 91;
    int MONGODB_DBNAME = 92;
    int MONGODB_USERNAME = 93;
    int MONGODB_PASSWORD = 94;
    int MONGODB_AUTH_DB = 95;
    int MONGODB_USE_AUTH = 96;
}
