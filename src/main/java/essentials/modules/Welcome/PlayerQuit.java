package essentials.modules.Welcome;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import essentials.modules.Config.PlayerConfig;

public class PlayerQuit {

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event)
    {
        String playerid = event.getPlayer().getUniqueId().toString();
        PlayerConfig.setLastSeen(playerid);

    }
}
