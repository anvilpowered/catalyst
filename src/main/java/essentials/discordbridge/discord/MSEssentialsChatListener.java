package essentials.discordbridge.discord;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.permission.Tristate;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.discordbridge.DiscordConfig;
import essentials.discordbridge.MSDBConfig;
import essentials.discordbridge.discord.TextUtil;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.text.serializer.plain.PlainComponentSerializer;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.MessageEvent;

import java.util.Optional;


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
