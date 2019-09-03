package essentials.modules.Config;

import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import net.kyori.text.TextComponent;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerConfig {

    static MSEssentials plugin;
    static List<String> playerUUID;
    static Path configPath;

    public static SimpleDateFormat formatter = new SimpleDateFormat("mm-dd-yyyy HH:mm:ss");
    public static Date date = new Date(System.currentTimeMillis());

    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static CommentedConfigurationNode config;

    public PlayerConfig(MSEssentials main)
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

            String message = config.getNode("Welcome", "message").getString();

            if(message == null)
            {
                config.getNode("Welcome", "message").setValue("Welcome to the server, ");
                config.getNode("Welcome", "name-color").setValue("&r");
                save();
                load();
            }
            config.setComment("Changes made in this file can cause problems. \nDon't change any values if you aren't sure of what you are doing");

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

    public static List<String> getPlayerUUID() {
        Set<Object> objectSet = config.getNode("players").getChildrenMap().keySet();

        List<String> uuids = new ArrayList();

        for(Object object : objectSet)
        {
            String uu = (String) object;

            uuids.add(uu);
        }

        return uuids;
    }

    public static void setMessage(String text) throws IOException
    {
        config.getNode("Welcome", "message").setValue(text);
        save();
        load();
    }

    public static String getMessage()
    {
        String text = config.getNode("Welcome", "message").getString();
        return text;
    }

    public static String getPlayerColor(String name)
    {
        String color = config.getNode("Welcome", "name-color").getString();
        name = color+name;
        return name;
    }

    public static void addPlayer(String playerid, String name)
    {
        config.getNode("players", playerid, "name").setValue(name);
        config.getNode("players", playerid, "joined").setValue(formatter.format(date));
        save();
        load();
    }

    public static void setLastSeen(String uuid)
    {
        config.getNode("players", uuid, "last-seen").setValue(formatter.format(date));
        save();
        load();
    }

    public static void getPlayerFromFile(UUID uuid, String name)
    {
        if(getPlayerUUID().contains(uuid.toString()))
        {
            return;
        }
        MSEssentials.server.broadcast(PluginMessages.legacyColor(getMessage()).append(TextComponent.of(getPlayerColor(name))));
        String playerid = uuid.toString();
        addPlayer(playerid, name);
    }
}
