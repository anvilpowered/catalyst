package essentials.modules.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import essentials.MSEssentials;
import essentials.modules.Config.MSEssentialsConfig;
import essentials.modules.StaffChat.StaffChat;
import net.kyori.text.TextComponent;

public class PlayerLeave {

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event)
    {
        StaffChat.toggledSet.remove(event.getPlayer());
        MSEssentials.getServer().broadcast(
                TextComponent.of(MSEssentialsConfig.getLeaveMessage().replace("{Player}", event.getPlayer().getUsername())));
    }
}
