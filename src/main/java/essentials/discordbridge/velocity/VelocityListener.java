package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.ConnectionHandshakeEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import essentials.discordbridge.Bridge;
import essentials.modules.Config.PlayerConfig;

import java.util.UUID;

public class VelocityListener {


    @Subscribe
    public void onPlayerJoin(PostLoginEvent event)
    {
        String message = Bridge.getConfig().getJoinFormat()
                .replaceAll("\\{player}", event.getPlayer().getUsername());
        UUID playerUUID = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getGameProfile().getName();
        PlayerConfig.getPlayerFromFile(playerUUID, name);

        Bridge.getConfig().getOutChannels(Bridge.getDiscordApi()).forEach(chan -> chan.sendMessage(message));
}
    @Subscribe
    public void onPlayerQuit(DisconnectEvent event)
    {
        String message = Bridge.getConfig().getQuitFormat()
                .replace("{player}", event.getPlayer().getUsername());
        String playerid = event.getPlayer().getUniqueId().toString();
        PlayerConfig.setLastSeen(playerid);

        Bridge.getConfig().getOutChannels(Bridge.getDiscordApi()).forEach(chan -> chan.sendMessage(message));
    }


}
