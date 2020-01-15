package rocks.milspecsg.msessentials.service.velocity.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import rocks.milspecsg.msessentials.service.common.config.MSEssentialsConfigurationService;

@Singleton
public class MSEssentialsVelocityConfigurationService extends MSEssentialsConfigurationService {

    @Inject
    public MSEssentialsVelocityConfigurationService(ConfigurationLoader<CommentedConfigurationNode> configLoader){
        super(configLoader);
    }
}
