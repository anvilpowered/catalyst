package rocks.milspecsg.msessentials.service.common.config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msrepository.service.common.config.CommonConfigurationService;

import java.util.Arrays;
import java.util.List;

@Singleton
public class MSEssentialsConfigurationService extends CommonConfigurationService {

    @Inject
    public MSEssentialsConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configurationLoader) {
        super(configurationLoader);
    }

    private static TypeToken<String> stringTypeToken = new TypeToken<String>() {
    };
    private static TypeToken<Integer> integerTypeToken = new TypeToken<Integer>() {
    };
    private static TypeToken<Boolean> booleanTypeToken = new TypeToken<Boolean>() {
    };
    private static TypeToken<List<String>> stringListTypeToken = new TypeToken<List<String>>() {
    };


    @Override
    protected void initNodeTypeMap() {
        nodeTypeMap.put(ConfigKeys.BROADCAST_PREFIX, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.CHAT_FILTER_ENABLED, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.CHAT_FILTER_SWEARS, stringListTypeToken);
        nodeTypeMap.put(ConfigKeys.CHAT_FILTER_EXCEPTIONS, stringListTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.JOIN_MESSAGE, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.PROXY_CHAT_ENABLED, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_CHANNEL_ID, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_JOIN_FORMAT, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_QUIT_FORMAT, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_STAFF_CHANNEL, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_BOT_TOKEN, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_PLAYING_MESSAGE, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_CHAT_TO_GAME_FORMAT, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_GAME_TO_CHAT_FORMAT, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_WEBHOOK_URL, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_STARTING_MESSAGE, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_TOPIC_FORMAT, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_CONSOLE_COMMAND_PREFIX, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DATA_STORE_NAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_HOSTNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_PORT, integerTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_DBNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_USERNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_PASSWORD, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_USE_AUTH, booleanTypeToken);
    }

    @Override
    protected void initVerificationMaps() {

    }

    @Override
    protected void initDefaultMaps() {
        defaultStringMap.put(ConfigKeys.BROADCAST_PREFIX, "[Broadcast] ");
        defaultBooleanMap.put(ConfigKeys.CHAT_FILTER_ENABLED, true);
        defaultListMap.put(ConfigKeys.CHAT_FILTER_SWEARS, Arrays.asList("ass", "fuck"));
        defaultListMap.put(ConfigKeys.CHAT_FILTER_EXCEPTIONS, Arrays.asList("assassin", "jkass"));
        defaultBooleanMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, true);
        defaultStringMap.put(ConfigKeys.JOIN_MESSAGE, "{Player} has joined the proxy");
        defaultBooleanMap.put(ConfigKeys.PROXY_CHAT_ENABLED, true);
        defaultBooleanMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, true);
        defaultStringMap.put(ConfigKeys.DISCORD_CHANNEL_ID, "replace");
        defaultStringMap.put(ConfigKeys.DISCORD_STAFF_CHANNEL, "replace");
        defaultStringMap.put(ConfigKeys.DISCORD_JOIN_FORMAT, "**{player} joined the proxy");
        defaultStringMap.put(ConfigKeys.DISCORD_QUIT_FORMAT, "**{player} left the proxy");
        defaultStringMap.put(ConfigKeys.DISCORD_BOT_TOKEN, "replace");
        defaultStringMap.put(ConfigKeys.DISCORD_PLAYING_MESSAGE, "On the best server ever!");
        defaultStringMap.put(ConfigKeys.DISCORD_CHAT_TO_GAME_FORMAT, "[Discord] %name% : ");
        defaultStringMap.put(ConfigKeys.DISCORD_GAME_TO_CHAT_FORMAT, "%prefix% %player% : ");
        defaultStringMap.put(ConfigKeys.DISCORD_WEBHOOK_URL, "replace");
        defaultStringMap.put(ConfigKeys.DISCORD_STARTING_MESSAGE, "The proxy is now starting!");
        defaultStringMap.put(ConfigKeys.DISCORD_TOPIC_FORMAT, "Total Players : %players%");
        defaultStringMap.put(ConfigKeys.DISCORD_CONSOLE_COMMAND_PREFIX, "!cmd");
        defaultStringMap.put(ConfigKeys.DATA_STORE_NAME, "mongodb");
        defaultStringMap.put(ConfigKeys.MONGODB_HOSTNAME, "hostname");
        defaultIntegerMap.put(ConfigKeys.MONGODB_PORT, 27017);
        defaultStringMap.put(ConfigKeys.MONGODB_USERNAME, "username");
        defaultStringMap.put(ConfigKeys.MONGODB_PASSWORD, "password");
        defaultStringMap.put(ConfigKeys.MONGODB_DBNAME, "msessentials");
        defaultBooleanMap.put(ConfigKeys.MONGODB_USE_AUTH, false);

    }

    @Override
    protected void initNodeNameMap() {
        nodeNameMap.put(ConfigKeys.BROADCAST_PREFIX, "broadcast.prefix");
        nodeNameMap.put(ConfigKeys.CHAT_FILTER_ENABLED, "chat.filter");
        nodeNameMap.put(ConfigKeys.CHAT_FILTER_SWEARS, "chat.filter.swears");
        nodeNameMap.put(ConfigKeys.CHAT_FILTER_EXCEPTIONS, "chat.filter.exceptions");
        nodeNameMap.put(ConfigKeys.JOIN_MESSAGE, "join.message");
        nodeNameMap.put(ConfigKeys.PROXY_CHAT_ENABLED, "proxy.chat");
        nodeNameMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, "server.command");
        nodeNameMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, "discord.toggle");
        nodeNameMap.put(ConfigKeys.DISCORD_CHANNEL_ID, "discord.channel.main");
        nodeNameMap.put(ConfigKeys.DISCORD_JOIN_FORMAT, "discord.format.join");
        nodeNameMap.put(ConfigKeys.DISCORD_QUIT_FORMAT, "discord.format.quit");
        nodeNameMap.put(ConfigKeys.DISCORD_STAFF_CHANNEL, "discord.channel.staff");
        nodeNameMap.put(ConfigKeys.DISCORD_BOT_TOKEN, "discord.token");
        nodeNameMap.put(ConfigKeys.DISCORD_PLAYING_MESSAGE, "discord.messages.playing");
        nodeNameMap.put(ConfigKeys.DISCORD_CHAT_TO_GAME_FORMAT, "discord.messages.discord");
        nodeNameMap.put(ConfigKeys.DISCORD_GAME_TO_CHAT_FORMAT, "discord.messages.server");
        nodeNameMap.put(ConfigKeys.DISCORD_WEBHOOK_URL, "discord.webhook");
        nodeNameMap.put(ConfigKeys.DISCORD_STARTING_MESSAGE, "discord.messages.startup");
        nodeNameMap.put(ConfigKeys.DISCORD_TOPIC_FORMAT, "discord.messages.topic");
        nodeNameMap.put(ConfigKeys.DATA_STORE_NAME, "datastore.dataStoreName");
        nodeNameMap.put(ConfigKeys.MONGODB_HOSTNAME, "mongodb.hostname");
        nodeNameMap.put(ConfigKeys.MONGODB_PORT, "mongodb.port");
        nodeNameMap.put(ConfigKeys.MONGODB_USERNAME, "mongodb.username");
        nodeNameMap.put(ConfigKeys.MONGODB_PASSWORD, "mongodb.password");
        nodeNameMap.put(ConfigKeys.MONGODB_DBNAME, "mongodb.dbname");
        nodeNameMap.put(ConfigKeys.MONGODB_USE_AUTH, "mongodb.auth");
    }

    @Override
    protected void initNodeDescriptionMap() {
        nodeDescriptionMap.put(ConfigKeys.BROADCAST_PREFIX, "\nPrefix for the /broadcast command");
        nodeDescriptionMap.put(ConfigKeys.CHAT_FILTER_ENABLED, "\nEnable or disable the chat filter");
        nodeDescriptionMap.put(ConfigKeys.CHAT_FILTER_SWEARS, "\nList of words you would like filtered out of chat.");
        nodeDescriptionMap.put(ConfigKeys.CHAT_FILTER_EXCEPTIONS, "\nList of words that are caught by the swear detection, but shouldn't be (ex. player's name that includes 'ass')");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, "\nEnable or disable the discord chat bridge");
        nodeDescriptionMap.put(ConfigKeys.JOIN_MESSAGE, "\nFormat for proxy connection messages. Current placeholders: {Player}");
        nodeDescriptionMap.put(ConfigKeys.PROXY_CHAT_ENABLED, "\nEnable or disable proxy-wide chat.");
        nodeDescriptionMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, "\nEnable or disable the /(server) command");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_CHANNEL_ID, "\nChannel for relaying chat between discord and the proxy");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_JOIN_FORMAT, "\nFormat to be displayed to discord when a player joins the proxy");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_QUIT_FORMAT, "\nFormat to be displayed to discord for when a player leaves the proxy");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_STAFF_CHANNEL, "\nChannels used for sending and recieving messages from /staffchat and discord.");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_BOT_TOKEN, "\nBot Token required for this module to work. The discord bot token can be retrieved after you create a bot. See the wiki for more information");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_PLAYING_MESSAGE, "\nMessage to be displayed as the 'now playing' for the discord bot!");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_CHAT_TO_GAME_FORMAT, "\nThe format in which you would like discord messages to appear in-game");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_GAME_TO_CHAT_FORMAT, "\nThe format in which you would like in-game messages to appear in discord");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_WEBHOOK_URL, "\nWebhook url that retrieves player images for discord");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_STARTING_MESSAGE, "\nThe message that is displayed when the proxy is booting");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_TOPIC_FORMAT, "\nThe format in which the topic of the discord channel is updated");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_CONSOLE_COMMAND_PREFIX, "\nThe prefix you would like to be used to run commands from discord");
        nodeDescriptionMap.put(ConfigKeys.DATA_STORE_NAME, "Data store name");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_HOSTNAME, "\nMongoDB hostname");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_PORT, "\nMongoDB port");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_DBNAME, "\nMongoDB database name");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_USERNAME, "\nMongoDB username");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_PASSWORD, "\nMongoDB password");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_USE_AUTH, "\nWhether to use authentication (username/password) for MongoDB connection");
    }
}
