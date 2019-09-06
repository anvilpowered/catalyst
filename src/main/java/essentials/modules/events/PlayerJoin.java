package essentials.modules.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;

import java.net.InetSocketAddress;
import java.util.UUID;

public class PlayerJoin {

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event)
    {
        InetSocketAddress ipAddress = event.getPlayer().getRemoteAddress();
        UUID playerUUID = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getUsername();
        PlayerConfig.getPlayerFromFile(playerUUID, name, ipAddress);
    }
}
