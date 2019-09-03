package essentials.discordbridge.velocity;

import com.velocitypowered.api.event.Subscribe;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.MSDBConfig;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.text.serializer.plain.PlainComponentSerializer;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.MessageEvent;

import java.util.Optional;


public class MSEssentialsChatListener {


    public void onMessage(MessageCreateEvent event)
    {

        if(!Bridge.getConfig().getInChannels(event.getApi()).contains(event.getChannel())) return;
        if(event.getMessageAuthor().isYourself()) return;


        String message = event.getReadableMessageContent();

        String author = event.getMessageAuthor().getDisplayName();
        message = "&6[Discord] &7" + author + " " + message;


        TextComponent component = TextComponent.builder()
               .content("")
               .append(PluginMessages.legacyColor(message))
               .hoverEvent(HoverEvent.showText(TextComponent.of("Click here to join the discord!")))
               .clickEvent(ClickEvent.openUrl("https://www.google.com/"))
               .build();

        if(Bridge.getConfig().getStaffChannel(event.getApi()).contains(event.getChannel()))
        {
            MSEssentials.server.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                    .forEach(target -> {target.sendMessage(component);});

            return;

        }

        MSEssentials.getServer().getAllPlayers().stream()
                .forEach(player -> player.sendMessage(component));

        MSEssentials.getLogger().info(PlainComponentSerializer.INSTANCE.serialize(component));
    }


}
