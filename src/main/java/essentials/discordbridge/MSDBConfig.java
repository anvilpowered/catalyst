package essentials.discordbridge;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MSDBConfig {

    private final ConfigurationNode node;

    private final String token;
    private final List<Long> inChannels;
    private final List<Long> outChannels;
    private final boolean playerlistEnabled;
    private final String playerlistFormat;
    private final String playerlistSeperator;
    private final int playerlistCommandRemoveDelay;
    private final int playerlistResponseRemoveDelay;

    private final String joinFormat;
    private final String quitFormat;


    public MSDBConfig(ConfigurationNode node) throws Exception{

        this.node = node;

        token = node.getNode("discord", "token").getString();

        inChannels = node.getNode("discord", "in-channels").getList(TypeToken.of(Long.class));
        outChannels = node.getNode("discord", "out-channels").getList(TypeToken.of(Long.class));

        playerlistEnabled = node.getNode("discord", "playerlist", "enabled").getBoolean(true);
        playerlistFormat = node.getNode("discord", "playerlist", "format").getString("**{count} players online:** ```\n{players}\n```");
        playerlistSeperator = node.getNode("discord", "playerlist", "seperator").getString(", ");
        playerlistCommandRemoveDelay = node.getNode("discord", "playerlist", "command-remove-delay").getInt(0);
        playerlistResponseRemoveDelay = node.getNode("discord", "playerlist", "response-remove-delay").getInt(10);

        if(token == null || token.isEmpty())
        {
            throw new InvalidConfigException("You need to set a bot token!");
        }

        joinFormat = node.getNode("velocity", "join-format").getString("**{player} joined the game");
        quitFormat = node.getNode("velocity", "quit-format").getString("**{player} left the game**");


    }

    public class InvalidConfigException extends Exception
    {
        InvalidConfigException(String message){
            super (message);
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

    public List<TextChannel> getOutChannels(DiscordApi api)
    {
        return outChannels.stream()
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
