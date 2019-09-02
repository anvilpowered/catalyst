package essentials.discordbridge;

import essentials.MSEssentials;
import essentials.discordbridge.discord.ConnectionListener;
import essentials.discordbridge.velocity.MSEssentialsChatListener;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static java.lang.System.in;

public class Bridge {

    private static DiscordApi discordApi;
    private static MSDBConfig config;


    public static void enable(){
        MSEssentials.logger.info("Enabling Discord Bridge!");

        try{
            config = loadConfig();
        }catch (Exception e)
        {
            throw new RuntimeException("Failed to load config", e);
        }


        startBot();


    }

    private Bridge(DiscordApi dAPI, MSDBConfig configuration){
        config = configuration;
        discordApi = dAPI;
    }

    public static boolean reloadConfig(){
        final String oldToken = config.getToken();
        try
        {
            config = loadConfig();
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static MSDBConfig loadConfig() throws Exception
    {
        ConfigurationNode config = YAMLConfigurationLoader.builder()
                .setFile(getBundledFile("config.yml"))
                .build()
                .load();
        return new MSDBConfig(config);
    }

    private static File getBundledFile(String name)
    {
        File file = new File(MSEssentials.defaultConfigPath.toFile(), name);

        if(!file.exists())
        {
            MSEssentials.defaultConfigPath.toFile().mkdir();
            try(InputStream n = MSEssentials.class.getResourceAsStream("/" + name))
            {
                Files.copy(in, file.toPath());
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return file;
    }

    private static void startBot() {
        if(discordApi != null)
        {
            discordApi.disconnect();
            discordApi = null;
        }

        ConnectionListener connectionListener = new ConnectionListener();
        MSEssentialsChatListener chatListener = new MSEssentialsChatListener();

        new DiscordApiBuilder()
                .setToken(config.getToken())
                .addLostConnectionListener(connectionListener::onConnectionLost)
                .addReconnectListener(connectionListener::onReconnect)
                .addResumeListener(connectionListener::onResume)
                .addMessageCreateListener(chatListener::onMessage)
                .login().thenAccept(discordApi1 ->
        {
            discordApi = discordApi1;
            MSEssentials.logger.info("Connected to discord");
        });
    }

    public static MSDBConfig getConfig()
    {
        return config;
    }

    public static DiscordApi getDiscordApi() {
        return discordApi;
    }
}
