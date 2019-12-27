package rocks.milspecsg.msessentials.discord.chat;

import com.google.inject.Inject;
import net.dv8tion.jda.api.entities.TextChannel;
import rocks.milspecsg.msessentials.discord.DiscordBridge;

public class DiscordChat {

    @Inject
    private DiscordBridge discordBridge;

    public void sendToChannel(String channel, String message) {
        TextChannel textChannel = discordBridge.jda.getTextChannelById(channel);
        if (textChannel != null) {
            textChannel.sendMessage(message).queue();
        }
    }
}

