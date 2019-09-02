import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import modules.PluginMessages;
import org.slf4j.Logger;

@Plugin(description = "your one stop velocity plugin!", authors = "STG_Allen", version = "1.0", id = "msessentials")
public class MSEssentials {

    public static ProxyServer server;
    public static Logger logger;

    @Subscribe
    public void onInit(ProxyInitializeEvent e){
        logger.info(PluginMessages.prefix + "is now starting!");
    }


    public static Logger getLogger() {
        return logger;
    }

    public static ProxyServer getServer() {
        return server;
    }
}
