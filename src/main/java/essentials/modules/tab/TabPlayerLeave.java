package essentials.modules.tab;

/*
Code used from this module was provieded by Aang23's Globaltab
can be found at : https://github.com/Aang23/GlobalTab/
*/
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;

public class TabPlayerLeave {
    @Subscribe
    public void onLeave(DisconnectEvent e)
    {
        if(MSEssentials.server.getPlayerCount() > 0)
        {
            for(int i = 0; i< MSEssentials.server.getPlayerCount(); i++)
            {
                Player p = (Player) MSEssentials.server.getAllPlayers().toArray()[i];
                p.getTabList().removeEntry(e.getPlayer().getUniqueId());
            }
        }
    }
}
