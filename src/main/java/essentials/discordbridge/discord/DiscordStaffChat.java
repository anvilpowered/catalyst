package essentials.discordbridge.discord;

import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.DiscordConfig;
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
        if(!DiscordConfig.getStaffChannel(event.getApi()).contains(event.getChannel())) return;
        if(event.getMessageAuthor().isYourself()) return;


        String message = event.getReadableMessageContent();

        String author = event.getMessageAuthor().getDisplayName();
        message = "&b[STAFF] &6[Discord] &7" + author + " " + message;


        TextComponent component = TextComponent.builder()
                .content("")
                .append(PluginMessages.legacyColor(message))
                .build();


            MSEssentials.server.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                    .forEach(target -> {target.sendMessage(component);});



        }

    }

