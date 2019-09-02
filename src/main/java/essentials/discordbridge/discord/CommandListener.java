package essentials.discordbridge.discord;

import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.stream.Collectors;

public class CommandListener {

    public void onPlayerList(MessageCreateEvent event){
        if(event.getMessageAuthor().isYourself()) return;

        Message commandMsg = event.getMessage();
        final int count = MSEssentials.getServer().getPlayerCount();
        final String players = MSEssentials.getServer().getAllPlayers().stream()
                .map(Player::getUsername)
                .collect(Collectors.joining());


    }
}
