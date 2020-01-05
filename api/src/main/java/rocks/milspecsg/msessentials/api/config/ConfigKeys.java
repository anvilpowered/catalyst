package rocks.milspecsg.msessentials.api.config;

public interface ConfigKeys extends rocks.milspecsg.msrepository.api.config.ConfigKeys{
    int BROADCAST_PREFIX = 10;
    int CHAT_FILTER_ENABLED = 11;
    int CHAT_FILTER_SWEARS = 12;
    int CHAT_FILTER_EXCEPTIONS = 13;
    int JOIN_MESSAGE = 14;
    int LEAVE_MESSAGE = 15;
    int WELCOME_MESSAGE = 16;
    int SERVER_COMMAND_ENABLED = 17;
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
    int MONGODB_USE_AUTH = 95;
}
