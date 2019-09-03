package essentials.discordbridge.discord;

import essentials.MSEssentials;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;


public class Listener {

    public static MSEssentials plugin;

    public Listener(MSEssentials essentials){
        plugin = essentials;
    }

    public void onMessage(MessageCreateEvent event)
    {
        MessageAuthor author = event.getMessageAuthor();
        String message = "[Discord] " + author.getDisplayName() + " " + event.getReadableMessageContent();



        MSEssentials.server.getAllPlayers().stream()
                .forEach(player -> player.sendMessage(TextComponent.of(message)));

    }

    private static String replacePlaceholders(String format, MessageAuthor author, String message){
        return format == null ? null : format
                .replace("{name}", author.getName())
                .replace("{username}", author.getName())
                .replace("{display_name}", author.getDisplayName())
                .replace("{server_name}", "discord")
                .replace("{message}", message);
    }
}
