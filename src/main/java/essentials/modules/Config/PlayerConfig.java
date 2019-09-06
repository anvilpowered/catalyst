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
            configPath = Paths.get(MSEssentials.defaultConfigPath + "/players.json");
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
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteNick(String name)
    {
      if(hasNickName(name) == true)
      {
          config.getNode("players", name, "nickname").setValue(null);
          save();
          load();
      }
    }
    public static void addNick(String nick, String name){
        MSEssentials.logger.info("addNick 92");
        config.getNode("players",name, "nickname").setValue(nick);
        save();
        load();
    }
    public static String getNickName(String name){
       String nick = config.getNode("players", name, "nickname").getString();
       MSEssentials.logger.info(nick);
       return nick;
    }

    public static boolean hasNickName(String name) {
        if (config.getNode("players", name, "nickname").getValue() != null) {
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
        config.getNode("players", name, "uuid").setValue(playerid);
        config.getNode("players", name, "joined").setValue(formatter.format(date));
        config.getNode("players", name, "banned", "value").setValue(false);
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
        MSEssentials.server.broadcast(PluginMessages.legacyColor(getMessage()).append(PluginMessages.legacyColor(getPlayerColor(name))));
        String playerid = uuid.toString();
        addPlayer(playerid, name);
    }

    public static void addBan(String name, String reason)
    {
            config.getNode("players", name, "banned", "value").setValue(true);
            if(reason == null) reason = "The ban hammer has spoken!";
            config.getNode("players", name, "banned", "reason").setValue(reason);
            save();
            load();
    }

    public static boolean checkBan(String name)
    {
           boolean x =  config.getNode("players", name, "banned", "value").getBoolean();
           return x;
    }
    public static String getBanReason(String name)
    {
        String reason = config.getNode("players", name, "banned", "reason").getString();

        return reason = "&4&lYou are banned! &r" + reason;
    }

    public static void unBan(String name)
    {
        config.getNode("players", name, "banned", "value").setValue(false);
        save();
        load();
    }
}
