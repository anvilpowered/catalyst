package essentials.discordbridge.discord;

import essentials.MSEssentials;
import org.javacord.api.event.connection.LostConnectionEvent;
import org.javacord.api.event.connection.ReconnectEvent;
import org.javacord.api.event.connection.ResumeEvent;

public class ConnectionListener {

    public void onConnectionLost(LostConnectionEvent event){
        MSEssentials.getLogger().info("Lost connection to discord");
    }

    public void onReconnect(ReconnectEvent event)
    {
        MSEssentials.getLogger().info("Reconnected to discord");
    }

    public void onResume(ResumeEvent event)
    {
        MSEssentials.getLogger().info("Resumed connection to discord");
    }
}
