package essentials.discordbridge.discord;

import essentials.MSEssentials;
import essentials.discordbridge.DiscordConfig;
import essentials.modules.PluginMessages;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;


public class DiscordChatListener {

    public void onMessage(MessageCreateEvent event) {
        System.out.println(event.getMessage().toString());
        if (!DiscordConfig.getInChannels(event.getApi()).contains(event.getChannel())) return;
        if (event.getMessageAuthor().isYourself()) return;


        String message = event.getReadableMessageContent();

        Message commandMsg = event.getMessage();
        String author = event.getMessageAuthor().getDisplayName();
        MessageAuthor author1 = event.getMessageAuthor();

        if (author1.isServerAdmin() && commandMsg.getReadableContent().toLowerCase().contains("!cmd")) {
            String command = commandMsg.getReadableContent();
            command = command.replace("!cmd ", "");
            MSEssentials.server.getCommandManager().execute(new DiscordCommandSource(), command);

            return;

        }

        message = "&6[Discord] &7" + author + " " + message;

        TextComponent component = TextComponent.builder()
                .content("")
                .append(PluginMessages.legacyColor(message))
                .hoverEvent(HoverEvent.showText(TextComponent.of("Click here to join the discord!")))
                .clickEvent(ClickEvent.openUrl(DiscordConfig.url))
                .build();

        MSEssentials.getServer().getAllPlayers()
                .forEach(player -> player.sendMessage(component));
    }
}
