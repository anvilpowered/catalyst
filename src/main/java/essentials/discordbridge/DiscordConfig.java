package essentials.discordbridge;

import com.google.common.reflect.TypeToken;
import essentials.MSEssentials;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiscordConfig {

    static MSEssentials plugin;
    static Path configPath;

    private final String token;
    private final List<Long> inChannels;
    private final List<Long> outChannels;
    private final List<Long> staffChannel;
    private final boolean playerlistEnabled;
    private final String playerlistFormat;
    private final String playerlistSeperator;
    private final int playerlistCommandRemoveDelay;
    private final int playerlistResponseRemoveDelay;

    private final String joinFormat;
    private final String quitFormat;

    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static CommentedConfigurationNode config;
    public static CommentedConfigurationNode getConfig(){return config;}

    public DiscordConfig(MSEssentials main) throws Exception {
        plugin = main;
        token = config.getNode("discord", "token").getString();

        inChannels = config.getNode("discord", "in-channels").getList(TypeToken.of(Long.class));
        outChannels = config.getNode("discord", "out-channels").getList(TypeToken.of(Long.class));

        staffChannel = config.getNode("discord", "staff-channel").getList(TypeToken.of(Long.class));

        playerlistEnabled = config.getNode("discord", "playerlist", "enabled").getBoolean(true);
        playerlistFormat = config.getNode("discord", "playerlist", "format").getString("**{count} players online:** ```\n{players}\n```");
        playerlistSeperator = config.getNode("discord", "playerlist", "seperator").getString(", ");
        playerlistCommandRemoveDelay = config.getNode("discord", "playerlist", "command-remove-delay").getInt(0);
        playerlistResponseRemoveDelay = config.getNode("discord", "playerlist", "response-remove-delay").getInt(10);

        if(token == null || token.isEmpty())
        {
            throw new DiscordConfig.InvalidConfigException("You need to set a bot token!");
        }

        joinFormat = config.getNode("velocity", "join-format").getString("**{player} joined the game");
        quitFormat = config.getNode("velocity", "quit-format").getString("**{player} left the game**");


    }
    public class InvalidConfigException extends Exception
    {
        InvalidConfigException(String message){super(message);}
    }

    public static void enable()
    {
        try
        {
            configPath = Paths.get(MSEssentials.defaultConfigPath + "/discordconfig.json");
            if(!Files.exists(configPath))
            {
                Files.createFile(configPath);
            }
            loader = HoconConfigurationLoader.builder().setPath(configPath).build();
            config = loader.load();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void save()
    {
        try
        {
            loader.save(getConfig());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void load()
    {
        try
        {
            config = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<TextChannel> getInChannels(DiscordApi api)
    {
        return inChannels.stream()
                .map(api::getTextChannelById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public  List<TextChannel> getOutChannels(DiscordApi api)
    {
        return outChannels.stream()
                .map(api::getTextChannelById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<TextChannel> getStaffChannel(DiscordApi api)
    {
        return staffChannel.stream()
                .map(api::getTextChannelById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public boolean isPlayerlistEnabled() {
        return playerlistEnabled;
    }

    public String getJoinFormat() {
        return joinFormat;
    }

    public String getPlayerlistFormat() {
        return playerlistFormat;
    }

    public String getPlayerlistSeperator() {
        return playerlistSeperator;
    }

    public int getPlayerlistCommandRemoveDelay() {
        return playerlistCommandRemoveDelay;
    }

    public int getPlayerlistResponseRemoveDelay() {
        return playerlistResponseRemoveDelay;
    }

    public String getToken() {
        return token;
    }

    public String getQuitFormat() {
        return quitFormat;
    }





}
