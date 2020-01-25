package rocks.milspecsg.msessentials.common.data.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msessentials.api.data.key.MSEssentialsKeys;
import rocks.milspecsg.msrepository.common.data.config.CommonConfigurationService;

@Singleton
public class MSEssentialsConfigurationService extends CommonConfigurationService {

    @Inject
    public MSEssentialsConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        super(configLoader);
    }

    @Override
    protected void initNodeNameMap() {
        nodeNameMap.put(MSEssentialsKeys.MOTD, "motd");
        nodeNameMap.put(MSEssentialsKeys.BROADCAST_PREFIX, "broadcast");
        nodeNameMap.put(MSEssentialsKeys.CHAT_FILTER_SWEARS, "chat.filter.swears");
        nodeNameMap.put(MSEssentialsKeys.CHAT_FILTER_EXCEPTIONS, "chat.filter.exceptions");
        nodeNameMap.put(MSEssentialsKeys.CHAT_FILTER_ENABLED, "chat.filter");
    }

    @Override
    protected void initNodeDescriptionMap() {
    }
}
