package essentials.discordbridge;

import essentials.MSEssentials;
import essentials.discordbridge.discord.CommandListener;
import essentials.discordbridge.discord.ConnectionListener;
import essentials.discordbridge.discord.DiscordStaffChat;
import essentials.discordbridge.discord.MSEssentialsChatListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Bridge {

    private static DiscordApi discordApi;
    private static DiscordConfig config;


    public static void enable() {
        MSEssentials.logger.info("Enabling Discord Bridge!");

        try{
            DiscordConfig.enable();
        }catch (Exception e)
        {
            throw new RuntimeException("Failed to load config", e);
        }


        startBot();


    }

    public static void onProxyShutdown() {
        MSEssentials.getServer().getScheduler().buildTask(MSEssentials.instance, () -> {
            MSEssentials.logger.info("Disconnecting the bridge");
            discordApi.disconnect();
            MSEssentials.logger.info("Successfully disconnected!");
        }).schedule();
    }

    private Bridge(DiscordApi dAPI, DiscordConfig configuration){
        config = configuration;
        discordApi = dAPI;
    }

    public static boolean reloadConfig(){
        final String oldToken = config.getToken();
        try
        {
            DiscordConfig.enable();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static void startBot() {
        if(discordApi != null)
        {
            discordApi.disconnect();
            discordApi = null;
        }

        ConnectionListener connectionListener = new ConnectionListener();
        DiscordStaffChat discordStaffChat = new DiscordStaffChat();
        MSEssentialsChatListener chatListener = new MSEssentialsChatListener();
        CommandListener commandListener = new CommandListener();
        new DiscordApiBuilder()
                .setToken(DiscordConfig.getToken())
                .addLostConnectionListener(connectionListener::onConnectionLost)
                .addReconnectListener(connectionListener::onReconnect)
                .addResumeListener(connectionListener::onResume)
                .addMessageCreateListener(discordStaffChat::onMessage)
                .addMessageCreateListener(chatListener::onMessage)
                .addMessageCreateListener(commandListener::onCmd)
                .login().thenAccept(discordApi1 ->
        {
            discordApi = discordApi1;
            MSEssentials.logger.info("Connected to discord");
        });
    }

    public static DiscordConfig getConfig()
    {
        return config;
    }

    public static DiscordApi getDiscordApi() {
        return discordApi;
    }
}
