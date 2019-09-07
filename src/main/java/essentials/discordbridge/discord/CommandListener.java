package essentials.discordbridge.discord;

import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.DiscordConfig;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.stream.Collectors;

public class CommandListener {

    public void onCmd(MessageCreateEvent event) {
        if (event.getMessageAuthor().isYourself()) return;

        Message commandMsg = event.getMessage();
        Server server = event.getServer().get();

        if (event.getMessage().getUserAuthor().get().getRoles(server).contains("admin")) {
            if (!commandMsg.getReadableContent().toLowerCase().startsWith("!cmd")) {
                return;
            }
            String command = commandMsg.getReadableContent().toString();
            command = command.replace("!cmd", "");
            MSEssentials.server.getCommandManager().execute(MSEssentials.getServer().getConsoleCommandSource(), command);

        }
        else
        {
            return;
        }
    }
}
