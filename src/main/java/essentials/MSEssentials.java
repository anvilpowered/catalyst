package essentials;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.velocity.*;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.StaffChat.StaffChat;
import essentials.modules.StaffChat.StaffChatEvent;
import essentials.modules.Welcome.PlayerJoin;
import essentials.modules.Welcome.PlayerQuit;
import essentials.modules.commands.*;
import essentials.modules.proxychat.ProxyChatEvent;
import org.slf4j.Logger;
import me.lucko.luckperms.*;
import me.lucko.luckperms.api.*;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.UUID;


@Plugin(description = "your one stop velocity plugin!",
        authors = "STG_Allen",
        version = "1.0",
        id = "msessentials",
        dependencies = {
        @Dependency(id = "luckperms")
        })
public class MSEssentials {

    public static ProxyServer server;
    public static Logger logger;
    public static Path defaultConfigPath;

    public static LuckPermsApi api;

    @Subscribe
    public void onInit(ProxyInitializeEvent event){
        logger.info("is now starting!");

        logger.info("Loading commands");
        server.getCommandManager().register(new SendGoogleCommand(),"sendgoogle");
        server.getCommandManager().register(new GoogleCommand(), "google");
        server.getCommandManager().register(new StaffChatCommand(), "staffchat", "sc");
        server.getCommandManager().register(new MessageCommand(), "msg", "message", "pm");
        server.getCommandManager().register(new NickNameCommand(), "nick", "nickname");
        server.getCommandManager().register(new StaffList(this), "stafflist");

        logger.info("initializing listeners");
        initListeners();

        Bridge.enable();

        StaffChat.toggledSet = new HashSet<UUID>();

        PlayerConfig.enable();
        if(server.getPluginManager().isLoaded("luckperms")) {
            reload();
        }
    }

    public void reload(){
        api = LuckPerms.getApi();
        logger.info("luckperms api connected successfully.");
    }

    @Inject
    public MSEssentials(ProxyServer pserver, Logger log, @DataDirectory Path path){
        defaultConfigPath = path;
        logger = log;
        server = pserver;
    }

    public static Logger getLogger() {
        return logger;

    }


    public static ProxyServer getServer() {
        return server;
    }


    public  void initListeners(){
        server.getEventManager().register(this,new MSEssentialsChatListener());
        server.getEventManager().register(this, new VelocityListener());
        server.getEventManager().register(this, new ProxyChatListener());
        server.getEventManager().register(this, new StaffChatListener());
        server.getEventManager().register(this, new DiscordStaffChat());

        server.getEventManager().register(this, new StaffChatEvent());
        server.getEventManager().register(this, new ProxyChatEvent());

        server.getEventManager().register(this, new PlayerJoin());
        server.getEventManager().register(this, new PlayerQuit());
    }
}
