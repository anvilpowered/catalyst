package rocks.milspecsg.msessentials.api.config;

public interface ConfigKeys extends rocks.milspecsg.msrepository.api.config.ConfigKeys{
    int BROADCAST_PREFIX = 1;
    int CHAT_FILTER_ENABLED = 2;
    int CHAT_FILTER_SWEARS = 3;
    int CHAT_FILTER_EXCEPTIONS = 4;
    int JOIN_MESSAGE = 5;
    int SERVER_COMMAND_ENABLED = 6;
    int DISCORD_BRIDGE_ENABLED = 30;
    int PROXY_CHAT_ENABLED = 50;
    int GLOBAL_TAB_ENABLED = 70;
    int MONGODB_HOSTNAME = 90;
    int MONGODB_PORT = 91;
    int MONGODB_DBNAME = 92;
    int MONGODB_USERNAME = 93;
    int MONGODB_PASSWORD = 94;
    int MONGODB_USE_AUTH = 95;

    int DISCORD_IN_CHANNELS = 31;
    int DISCORD_OUT_CHANNELS = 32;
    int DISCORD_STAFF_CHANNEL = 33;
    int DISCORD_JOIN_FORMAT = 34;
    int DISCORD_QUIT_FORMAT = 35;
    int DISCORD_BOT_TOKEN = 36;


}
