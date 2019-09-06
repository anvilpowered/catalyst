package essentials.modules.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import essentials.modules.Config.PlayerConfig;

import java.util.UUID;

public class PlayerJoin {

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event)
    {
        UUID playerUUID = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getUsername();
        PlayerConfig.getPlayerFromFile(playerUUID, name);

    }
}
