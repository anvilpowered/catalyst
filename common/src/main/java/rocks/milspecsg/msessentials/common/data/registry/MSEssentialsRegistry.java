package rocks.milspecsg.msessentials.common.data.registry;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import rocks.milspecsg.msrepository.api.data.key.Keys;
import rocks.milspecsg.msrepository.common.data.registry.CommonExtendedRegistry;

@Singleton
public class MSEssentialsRegistry extends CommonExtendedRegistry {

    @Inject
    public MSEssentialsRegistry() {
        defaultMap.put(Keys.BASE_SCAN_PACKAGE, "rocks.milspecsg.msessentials.common.model");
    }
}
