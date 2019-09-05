package essentials.modules.Config;

import essentials.MSEssentials;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Main essentials.Config, Will be used to toggle essentials.Config.modules on and off
public class MSEssentialsConfig {

    static Path configPath;

    public static ConfigurationLoader<CommentedConfigurationNode> loader;
    public static CommentedConfigurationNode mainNode;

    public static CommentedConfigurationNode getConfig(){return mainNode;}

    public static void load()
    {
        try
        {
            mainNode = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void save()
    {
        try {
            loader.save(getConfig());
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void enable()
    {
        try
        {
            configPath = Paths.get(MSEssentials.defaultConfigPath + "/msessentials.json");
            if(!Files.exists(configPath))
            {
                Files.createFile(configPath);
            }
            loader = HoconConfigurationLoader.builder().setPath(configPath).build();
            mainNode = loader.load();

            mainNode.getNode("ProxyChat").setComment("To disable proxy-wide chat, change true to false");
            mainNode.getNode("Chat-Filter").setComment("To disable the proxy-wide chat filter, change true to false");
            mainNode.getNode("Discord-Bridge").setComment("To disable the discord bridge, change true to false");

            if(mainNode.getNode("Discord-Bridge", "enabled:").getValue() == null)
            {
                setupDefaultConfig();
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void setupDefaultConfig(){
        mainNode.getNode("ProxyChat", "enabled:").setValue(true);
        mainNode.getNode("Chat-Filter", "enabled:").setValue(true);
        mainNode.getNode("Discord-Bridge", "enabled:").setValue(true);
        save();
        load();

    }

    public static boolean getProxyChatBoolean()
    {
        boolean v = mainNode.getNode("ProxyChat", "enabled:").getBoolean();
        return v;
    }

    public static boolean getChatFilterBoolean()
    {
        boolean v = mainNode.getNode("Chat-Filter", "enabled:").getBoolean();
        return v;
    }

    public static boolean getDiscordBoolean()
    {
        boolean v = mainNode.getNode("Discord-Bridge", "enabled:").getBoolean();
        return v;
    }
}
