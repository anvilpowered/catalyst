package essentials;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import essentials.modules.PluginMessages;
import essentials.modules.commands.GoogleCommand;
import essentials.modules.commands.SendGoogleCommand;
import essentials.modules.commands.StaffChatCommand;
import org.slf4j.Logger;

import javax.inject.Inject;


@Plugin(description = "your one stop velocity plugin!", authors = "STG_Allen", version = "1.0", id = "msessentials")
public class MSEssentials {

    public static ProxyServer server;
    public static Logger logger;
    private static SendGoogleCommand sendGoogleCommand;

    @Subscribe
    public void onInit(ProxyInitializeEvent event){
        logger.info(PluginMessages.prefix + "is now starting!");
        server.getCommandManager().register(new SendGoogleCommand(),"sendgoogle");
        server.getCommandManager().register(new GoogleCommand(), "google");
        server.getCommandManager().register(new StaffChatCommand(), "staffchat");

    }

    @Inject
    public MSEssentials(ProxyServer pserver, Logger log){
        logger = log;
        server = pserver;
    }

    public static Logger getLogger() {
        return logger;

    }


    public static ProxyServer getServer() {
        return server;
    }
}
