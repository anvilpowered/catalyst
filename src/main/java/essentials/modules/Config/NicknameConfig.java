package essentials.modules.Config;

import essentials.MSEssentials;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NicknameConfig {

    static MSEssentials plugin;
    static List<String> playerUUID;
    static Path configPath;

    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static CommentedConfigurationNode config;

    public NicknameConfig(MSEssentials main)
    {
        plugin = main;
    }

    public static CommentedConfigurationNode getConfig(){return config;}

    public static void enable(){
        try
        {
            configPath = Paths.get(MSEssentials.defaultConfigPath + "/nicknames.json");
            if(!Files.exists(configPath))
            {
                Files.createDirectories(MSEssentials.defaultConfigPath);
                Files.createFile(configPath);
            }

            loader = HoconConfigurationLoader.builder().setPath(configPath).build();
            config = loader.load();

            config.getNode("Nicknames").setComment("Do not change anything in this file.");
            save();
            load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void save(){
        try
        {
            loader.save(getConfig());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public static void load(){
        try
        {
            config = loader.load();
            playerUUID = config.getNode("players").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void addNick(String nick, UUID uuid){
        MSEssentials.logger.info("addNick 81");
        if(!playerUUID.contains(uuid.toString()))
        {
            MSEssentials.logger.info("addNick 84");
            if(!playerUUID.isEmpty())
            {
                if(config.getNode("players", uuid).equals(true))
                {
                    MSEssentials.logger.info("nick existing");
                }else
                {
                    MSEssentials.logger.info("addNick 92");
                    playerUUID.add(uuid.toString());
                    config.getNode("players",uuid.toString(), "nickname").setValue(nick);
                    save();
                    load();
                }
            }
            else
                {
                    MSEssentials.logger.info("addNick 99");
                    playerUUID.add(uuid.toString());
                    config.getNode("players", uuid.toString(), "nickname").setValue(nick);
                    save();
                    load();
                    return;
                }
        }
    }
    public static String getNickName(UUID uuid){
       String nick = config.getNode("players", uuid.toString(), "nickname").getString();
       MSEssentials.logger.info(nick);
       return nick;
    }

    public static boolean hasNickName(UUID uuid) {
        if (config.getNode("players", uuid.toString(), "nickname").getValue() != null) {
            return true;
        } else
        {
            return false;
        }

    }
}
