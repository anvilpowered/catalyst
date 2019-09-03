package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import essentials.MSEssentials;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.PluginMessages;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.text.serializer.plain.PlainComponentSerializer;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.MessageEvent;


public class MSEssentialsChatListener {


    public void onMessage(MessageCreateEvent event)
    {
        if(event.getMessageAuthor().isYourself()) return;

        String message = event.getReadableMessageContent();

        String author = event.getMessageAuthor().getDisplayName();
        message = "&6[Discord] &r" + author + " " + message;

       TextComponent component = TextComponent.builder()
               .content("")
               .append(PluginMessages.legacyColor(message))
               .hoverEvent(HoverEvent.showText(TextComponent.of("Click here to join the discord!")))
               .clickEvent(ClickEvent.openUrl("https://www.google.com/"))
               .build();

        MSEssentials.getServer().getAllPlayers().stream()
                .forEach(player -> player.sendMessage(component));

        MSEssentials.getLogger().info(PlainComponentSerializer.INSTANCE.serialize(component));
    }


}
