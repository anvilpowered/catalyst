package rocks.milspecsg.msessentials.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;

import java.util.Arrays;

public class PluginMessageListener {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        System.out.println("Plugin messaged");
        System.out.println(event.getIdentifier());
        if(event.getIdentifier().equals("MSE-Starting")) {
            System.out.println(Arrays.toString(event.getData()));
        }
    }
}
