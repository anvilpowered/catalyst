package rocks.milspecsg.msessentials.service.common.config;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msessentials.api.config.ConfigKeys;
import rocks.milspecsg.msessentials.api.config.ConfigTypes;
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
        nodeTypeMap.put(ConfigKeys.CHAT_FILTER_SWEARS, ConfigTypes.STRINGLIST);
        nodeTypeMap.put(ConfigKeys.CHAT_FILTER_EXCEPTIONS, ConfigTypes.STRINGLIST);
        nodeTypeMap.put(ConfigKeys.JOIN_MESSAGE, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.LEAVE_MESSAGE, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MOTD, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.PROXY_CHAT_ENABLED, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.DATA_STORE_NAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_HOSTNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_PORT, integerTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_DBNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_USERNAME, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_PASSWORD, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.MONGODB_USE_AUTH, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.GLOBAL_TAB_ENABLED, booleanTypeToken);
        nodeTypeMap.put(ConfigKeys.GLOBAL_TAB_HEADER, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.GLOBAL_TAB_FOOTER, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.GLOBAL_TAB_PLAYER_FORMAT, stringTypeToken);
        nodeTypeMap.put(ConfigKeys.GLOBAL_TAB_CUSTOM, stringListTypeToken);
        nodeTypeMap.put(ConfigKeys.GLOBAL_TAB_UPDATE_DELAY, integerTypeToken);
        nodeTypeMap.put(ConfigKeys.GLOBAL_TAB_DISABLED_SERVERS, stringListTypeToken);
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
        defaultStringMap.put(ConfigKeys.JOIN_MESSAGE, "%player% has joined the proxy");
        defaultStringMap.put(ConfigKeys.LEAVE_MESSAGE, "%player% has left the proxy");
        defaultBooleanMap.put(ConfigKeys.PROXY_CHAT_ENABLED, true);
        defaultStringMap.put(ConfigKeys.MOTD, "A MSEssentials Proxy Server!");
        defaultBooleanMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, true);
        defaultStringMap.put(ConfigKeys.DATA_STORE_NAME, "mongodb");
        defaultStringMap.put(ConfigKeys.MONGODB_HOSTNAME, "hostname");
        defaultIntegerMap.put(ConfigKeys.MONGODB_PORT, 27017);
        defaultStringMap.put(ConfigKeys.MONGODB_USERNAME, "username");
        defaultStringMap.put(ConfigKeys.MONGODB_PASSWORD, "password");
        defaultStringMap.put(ConfigKeys.MONGODB_DBNAME, "msessentials");
        defaultBooleanMap.put(ConfigKeys.MONGODB_USE_AUTH, false);
        defaultBooleanMap.put(ConfigKeys.GLOBAL_TAB_ENABLED, true);
        defaultStringMap.put(ConfigKeys.GLOBAL_TAB_HEADER, "Welcome to");
        defaultStringMap.put(ConfigKeys.GLOBAL_TAB_FOOTER, "A Great Server");
        defaultStringMap.put(ConfigKeys.GLOBAL_TAB_PLAYER_FORMAT, "%prefix% %player% %suffix%");
        defaultListMap.put(ConfigKeys.GLOBAL_TAB_CUSTOM, Arrays.asList("&3Your Ping : &e%ping%", "&3Current Server : &e%server%", "&3Balance : &e%balance%"));
        defaultIntegerMap.put(ConfigKeys.GLOBAL_TAB_UPDATE_DELAY, 1);
        defaultListMap.put(ConfigKeys.GLOBAL_TAB_DISABLED_SERVERS, Arrays.asList("none"));
    }

    @Override
    protected void initNodeNameMap() {
        nodeNameMap.put(ConfigKeys.BROADCAST_PREFIX, "message.broadcast.prefix");
        nodeNameMap.put(ConfigKeys.CHAT_FILTER_ENABLED, "chat.filter");
        nodeNameMap.put(ConfigKeys.CHAT_FILTER_SWEARS, "chat.filter.swears");
        nodeNameMap.put(ConfigKeys.CHAT_FILTER_EXCEPTIONS, "chat.filter.exceptions");
        nodeNameMap.put(ConfigKeys.JOIN_MESSAGE, "message.join");
        nodeNameMap.put(ConfigKeys.LEAVE_MESSAGE, "message.leave");
        nodeNameMap.put(ConfigKeys.PROXY_CHAT_ENABLED, "proxy.chat");
        nodeNameMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, "server.command");
        nodeNameMap.put(ConfigKeys.MOTD, "message.motd");
        nodeNameMap.put(ConfigKeys.DATA_STORE_NAME, "datastore.dataStoreName");
        nodeNameMap.put(ConfigKeys.MONGODB_HOSTNAME, "mongodb.hostname");
        nodeNameMap.put(ConfigKeys.MONGODB_PORT, "mongodb.port");
        nodeNameMap.put(ConfigKeys.MONGODB_USERNAME, "mongodb.username");
        nodeNameMap.put(ConfigKeys.MONGODB_PASSWORD, "mongodb.password");
        nodeNameMap.put(ConfigKeys.MONGODB_DBNAME, "mongodb.dbname");
        nodeNameMap.put(ConfigKeys.MONGODB_USE_AUTH, "mongodb.auth");
        nodeNameMap.put(ConfigKeys.GLOBAL_TAB_ENABLED, "globaltab.enabled");
        nodeNameMap.put(ConfigKeys.GLOBAL_TAB_HEADER, "globaltab.format.header");
        nodeNameMap.put(ConfigKeys.GLOBAL_TAB_FOOTER, "globaltab.format.footer");
        nodeNameMap.put(ConfigKeys.GLOBAL_TAB_PLAYER_FORMAT, "globaltab.format.player");
        nodeNameMap.put(ConfigKeys.GLOBAL_TAB_CUSTOM, "globaltab.format.custom");
        nodeNameMap.put(ConfigKeys.GLOBAL_TAB_UPDATE_DELAY, "globaltab.updatedelay");
        nodeNameMap.put(ConfigKeys.GLOBAL_TAB_DISABLED_SERVERS, "globaltab.disabledservers");
    }

    @Override
    protected void initNodeDescriptionMap() {
        nodeDescriptionMap.put(ConfigKeys.BROADCAST_PREFIX, "\nPrefix for the /broadcast command");
        nodeDescriptionMap.put(ConfigKeys.CHAT_FILTER_ENABLED, "\nEnable or disable the chat filter");
        nodeDescriptionMap.put(ConfigKeys.CHAT_FILTER_SWEARS, "\nList of words you would like filtered out of chat.");
        nodeDescriptionMap.put(ConfigKeys.CHAT_FILTER_EXCEPTIONS, "\nList of words that are caught by the swear detection, but shouldn't be (ex. player's name that includes 'ass')");
        nodeDescriptionMap.put(ConfigKeys.JOIN_MESSAGE, "\nFormat for proxy connection messages. Current placeholders: %player%");
        nodeDescriptionMap.put(ConfigKeys.LEAVE_MESSAGE, "\nFormat for proxy disconnect messages. Current placeholders: %player%");
        nodeDescriptionMap.put(ConfigKeys.PROXY_CHAT_ENABLED, "\nEnable or disable proxy-wide chat.");
        nodeDescriptionMap.put(ConfigKeys.MOTD, "\nServer MOTD that is displayed when the proxy is pinged.");
        nodeDescriptionMap.put(ConfigKeys.SERVER_COMMAND_ENABLED, "\nEnable or disable the /(server) command");
        nodeDescriptionMap.put(ConfigKeys.DATA_STORE_NAME, "Data store name");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_HOSTNAME, "\nMongoDB hostname");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_PORT, "\nMongoDB port");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_DBNAME, "\nMongoDB database name");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_USERNAME, "\nMongoDB username");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_PASSWORD, "\nMongoDB password");
        nodeDescriptionMap.put(ConfigKeys.MONGODB_USE_AUTH, "\nWhether to use authentication (username/password) for MongoDB connection");
        nodeDescriptionMap.put(ConfigKeys.GLOBAL_TAB_ENABLED, "\nEnable or disable the global player tab");
        nodeDescriptionMap.put(ConfigKeys.GLOBAL_TAB_HEADER, "\nFormat for the tab header");
        nodeDescriptionMap.put(ConfigKeys.GLOBAL_TAB_FOOTER, "\nFormat for the tab footer");
        nodeDescriptionMap.put(ConfigKeys.GLOBAL_TAB_PLAYER_FORMAT, "\nFormat for the how players are displayed in the tab \nDefault example '[admin] STG_Allen [founder] '(%prefix% %player% %suffix%");
        nodeDescriptionMap.put(ConfigKeys.GLOBAL_TAB_CUSTOM, "\nFormat for extra information that can be displayed in the tab");
        nodeDescriptionMap.put(ConfigKeys.GLOBAL_TAB_UPDATE_DELAY, "\nTime setting for how often the tab updates in seconds");
        nodeDescriptionMap.put(ConfigKeys.GLOBAL_TAB_DISABLED_SERVERS, "List of servers you wish to disable the global tab in");
    }
}
