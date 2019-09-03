package essentials.modules.Welcome;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import essentials.modules.Config.PlayerConfig;

import java.util.UUID;

public class PlayerJoin {

    @Subscribe
    public void onPlayerJoin(ServerConnectedEvent event)
    {
        UUID playerUUID = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getGameProfile().getName();
        PlayerConfig.getPlayerFromFile(playerUUID, name);
    }
}
