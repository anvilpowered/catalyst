package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import essentials.MSEssentials;
import essentials.discordbridge.discord.TextUtil;
import net.kyori.text.TextComponent;
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

       TextComponent component = LegacyComponentSerializer.INSTANCE.deserialize(message, '&');

        MSEssentials.getServer().getAllPlayers().stream()
                .forEach(player -> player.sendMessage(component));

        MSEssentials.getLogger().info(PlainComponentSerializer.INSTANCE.serialize(component));
    }


}
