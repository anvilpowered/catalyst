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
        nodeTypeMap.put(ConfigKeys.DISCORD_IN_CHANNELS, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_OUT_CHANNELS, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_JOIN_FORMAT, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_QUIT_FORMAT, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_STAFF_CHANNEL, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.DISCORD_BOT_TOKEN, stringTypeToken);
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
        defaultStringMap.put(ConfigKeys.DISCORD_IN_CHANNELS, "replace");
        defaultStringMap.put(ConfigKeys.DISCORD_OUT_CHANNELS, "replace");
        defaultStringMap.put(ConfigKeys.DISCORD_STAFF_CHANNEL, "replace");
        defaultStringMap.put(ConfigKeys.DISCORD_JOIN_FORMAT, "**{player} joined the proxy");
        defaultStringMap.put(ConfigKeys.DISCORD_QUIT_FORMAT, "**{player} left the proxy");
        defaultStringMap.put(ConfigKeys.DISCORD_BOT_TOKEN, "replace");
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
        nodeNameMap.put(ConfigKeys.DISCORD_BRIDGE_ENABLED, "discord.config.toggle");
        nodeNameMap.put(ConfigKeys.JOIN_MESSAGE, "join.message");
        nodeNameMap.put(ConfigKeys.PROXY_CHAT_ENABLED, "proxy.chat");
        nodeNameMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, "server.command");
        nodeNameMap.put(ConfigKeys.DISCORD_IN_CHANNELS, "discord.config.channel.in");
        nodeNameMap.put(ConfigKeys.DISCORD_OUT_CHANNELS, "discord.config.channel.out");
        nodeNameMap.put(ConfigKeys.DISCORD_JOIN_FORMAT, "discord.config.format.join");
        nodeNameMap.put(ConfigKeys.DISCORD_QUIT_FORMAT, "discord.config.format.quit");
        nodeNameMap.put(ConfigKeys.DISCORD_STAFF_CHANNEL, "discord.config.channel.staff");
        nodeNameMap.put(ConfigKeys.DISCORD_BOT_TOKEN, "discord.config.token");
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
        nodeDescriptionMap.put(ConfigKeys.DISCORD_IN_CHANNELS, "\nChannel to recieve discord chat from");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_OUT_CHANNELS, "\nChannel to recieve proxy chat from");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_JOIN_FORMAT, "\nFormat to be displayed to discord when a player joins the proxy");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_QUIT_FORMAT, "\nFormat to be displayed to discord for when a player leaves the proxy");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_STAFF_CHANNEL, "\nChannels used for sending and recieving messages from /staffchat and discord.");
        nodeDescriptionMap.put(ConfigKeys.DISCORD_BOT_TOKEN, "\nBot Token required for this module to work. The discord bot token can be retrieved after you create a bot. See the wiki for more information");
        nodeDescriptionMap.put(ConfigKeys.DATA_STORE_NAME, "Data store name");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_HOSTNAME, "\nMongoDB hostname");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_PORT, "\nMongoDB port");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_DBNAME, "\nMongoDB database name");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_USERNAME, "\nMongoDB username");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_PASSWORD, "\nMongoDB password");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_USE_AUTH, "\nWhether to use authentication (username/password) for MongoDB connection");
    }
}
