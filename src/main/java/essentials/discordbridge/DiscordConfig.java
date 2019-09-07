package essentials.discordbridge;

import com.google.common.reflect.TypeToken;
import essentials.MSEssentials;
import ninja.leaping.configurate.ConfigurationNode;
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

    private static String token;
    public static String url;
    private static List<Long> inChannels;
    private static List<Long> outChannels;
    private static List<Long> staffChannel;
    private static boolean playerlistEnabled;
    private static String playerlistFormat;
    private static String playerlistSeperator;
    private static int playerlistCommandRemoveDelay;
    private static int playerlistResponseRemoveDelay;

    private static String joinFormat;
    private static String quitFormat;

    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static CommentedConfigurationNode config;

    public static CommentedConfigurationNode getConfig() {
        return config;
    }

    public DiscordConfig(MSEssentials main) {
        plugin = main;
    }

    public static class InvalidConfigException extends Exception {
        InvalidConfigException(String message) {
            super(message);
        }
    }

    public static void startup() throws Exception {
        if(config.getNode("discord", "token").getString() == null)
            MSEssentials.logger.info("token == null");
        token = config.getNode("discord", "token").getString();
        MSEssentials.logger.info(token);

        inChannels = config.getNode("discord", "in-channels").getList(TypeToken.of(Long.class));
        outChannels = config.getNode("discord", "out-channels").getList(TypeToken.of(Long.class));

        staffChannel = config.getNode("discord", "staff-channel").getList(TypeToken.of(Long.class));

        playerlistEnabled = config.getNode("discord", "playerlist", "enabled").getBoolean(true);
        playerlistFormat = config.getNode("discord", "playerlist", "format").getString("**{count} players online:** ```\n{players}\n```");
        playerlistSeperator = config.getNode("discord", "playerlist", "seperator").getString(", ");
        playerlistCommandRemoveDelay = config.getNode("discord", "playerlist", "command-remove-delay").getInt(0);
        playerlistResponseRemoveDelay = config.getNode("discord", "playerlist", "response-remove-delay").getInt(10);
        url = config.getNode("discord", "url").getString();

        if (token == null || token.isEmpty()) {
            throw new DiscordConfig.InvalidConfigException("You need to set a bot token!");
        }

        joinFormat = config.getNode("velocity", "join-format").getString("**{player} joined the game");
        quitFormat = config.getNode("velocity", "quit-format").getString("**{player} left the game**");


    }

    public static void enable() throws Exception {
        try {
            configPath = Paths.get(MSEssentials.defaultConfigPath + "/discordconfig.json");
            if (!Files.exists(configPath)) {
                Files.createFile(configPath);
                loader = HoconConfigurationLoader.builder().setPath(configPath).build();
                config = loader.load();
                setup();
                return;
            }

            loader = HoconConfigurationLoader.builder().setPath(configPath).build();
            config = loader.load();
            startup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setup() throws Exception
    {
        config.getNode("discord", "token").setValue("").setComment("The discord bot token");
        config.getNode("discord", "out-channels").setValue("").setComment("Channels you'd like to send discord messages to");
        config.getNode("discord", "in-channels").setValue("").setComment("Channels you'd like to receive discord messages from");
        config.getNode("discord", "staff-channel").setValue("").setComment("Your staff discord channel for /staffchat");
        config.getNode("discord", "playerlist", "enabled").setValue(true);
        config.getNode("discord", "playerlist", "format").setValue("").setComment("Format for playerlist, this is not yet implemented");
        config.getNode("discord", "playerlist", "seperator").setValue("").setComment("player name seperator");
        config.getNode("discord", "join-format").setValue("**{player} joined the game");
        config.getNode("discord", "quit-format").setValue("**{player} left the game");
        config.getNode("discord", "url").setValue("https://www.milspecsg.rocks");
        save();
        load();
        startup();
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

    public static List<TextChannel> getInChannels(DiscordApi api)
    {
        return inChannels.stream()
                .map(api::getTextChannelById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public static List<TextChannel> getOutChannels(DiscordApi api)
    {
        return outChannels.stream()
                .map(api::getTextChannelById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public static List<TextChannel> getStaffChannel(DiscordApi api)
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

    public static String getJoinFormat() {
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

    public static String getToken() {
        return token;
    }

    public static String getQuitFormat() {
        return quitFormat;
    }





}
