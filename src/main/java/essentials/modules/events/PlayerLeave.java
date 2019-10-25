package essentials.modules.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.MainConfig;
import essentials.modules.StaffChat.StaffChat;
import net.kyori.text.TextComponent;

public class PlayerLeave {

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event)
    {
        StaffChat.toggledSet.remove(event.getPlayer());
        MSEssentials.getServer().broadcast(
                TextComponent.of(MainConfig.getLeaveMessage().replace("{Player}", event.getPlayer().getUsername())));
        if(MSEssentials.server.getPlayerCount() > 0)
        {
            for(int i = 0; i< MSEssentials.server.getPlayerCount(); i++)
            {
                Player p = (Player) MSEssentials.server.getAllPlayers().toArray()[i];
                p.getTabList().removeEntry(event.getPlayer().getUniqueId());
            }
        }
    }
}
