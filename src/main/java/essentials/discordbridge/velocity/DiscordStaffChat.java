package essentials.discordbridge.velocity;

import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.plain.PlainComponentSerializer;
import org.javacord.api.event.message.MessageCreateEvent;

public class DiscordStaffChat {

    public void onMessage(MessageCreateEvent event)
    {

        if(!Bridge.getConfig().getStaffChannel(event.getApi()).contains(event.getChannel())) return;
        if(event.getMessageAuthor().isYourself()) return;


        String message = event.getReadableMessageContent();

        String author = event.getMessageAuthor().getDisplayName();
        message = "&b[STAFF] &6[Discord] &7" + author + " " + message;


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
