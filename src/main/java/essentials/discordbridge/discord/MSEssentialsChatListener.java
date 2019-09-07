package essentials.discordbridge.discord;

import essentials.MSEssentials;
import essentials.discordbridge.DiscordConfig;
import essentials.modules.PluginMessages;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;


public class MSEssentialsChatListener {

    public void onMessage(MessageCreateEvent event)
    {
        if(!DiscordConfig.getInChannels(event.getApi()).contains(event.getChannel())) return;
        if(event.getMessageAuthor().isYourself()) return;


        String message = event.getReadableMessageContent();

        Message commandMsg = event.getMessage();
        Server server = event.getServer().get();
        String author = event.getMessageAuthor().getDisplayName();
        MessageAuthor author1 = event.getMessageAuthor();
        MSEssentials.logger.info(new DiscordCommandSource().toString());
       // MSEssentials.logger.info(event.getMessage().getUserAuthor().get().getRoles(server).toString());
        if (author1.isServerAdmin()) {
            if (!commandMsg.getReadableContent().toLowerCase().contains("!cmd")) {
                return;
            }
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
/*
        if(Bridge.getConfig().getStaffChannel(event.getApi()).contains(event.getChannel()))
        {
            MSEssentials.server.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                    .forEach(target -> {target.sendMessage(component);});

            return;

        }*/

        MSEssentials.getServer().getAllPlayers().stream()
                .forEach(player -> player.sendMessage(component));
    }


}
