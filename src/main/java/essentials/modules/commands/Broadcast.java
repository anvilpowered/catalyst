package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.proxychat.ProxyChat;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Broadcast implements Command {

    @Override
    public void execute(CommandSource source,@Nonnull String [] args)
    {
        if(args.length == 0)
        {
            source.sendMessage(PluginMessages.notEnoughArgs);
        }
        if(source instanceof Player)
        {
            Player player = (Player) source;
            if(player.hasPermission(PluginPermissions.BROADCAST))
            {
                String message = Stream.of(args).collect(Collectors.joining(" "));
                TextComponent.Builder messageBuilder = TextComponent.builder();

                String[] words = message.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    if (words[i].matches("[^\\s]+\\.[^.\\s/]{2,}[^\\s,]*")) {
                        // is url
                        messageBuilder.append(TextComponent.builder()
                                .append(words[i])
                                .color(TextColor.BLUE)
                                .decoration(TextDecoration.UNDERLINED, true)
                                .clickEvent(ClickEvent.openUrl(words[i]))
                        );
                    } else {
                        messageBuilder.append(ProxyChat.legacyColor(words[i]));
                    }

                    // add space between each word
                    if (i != words.length - 1) {
                        messageBuilder.append(" ");
                    }
                }
                TextComponent component = messageBuilder.build();
                for(Player p : MSEssentials.getServer().getAllPlayers())
                {
                    p.sendMessage(component);
                }
                return;
            }
            else
            {
                player.sendMessage(PluginMessages.noPermissions);
                return;
            }

        }
        String message = String.join(" ", args);
        for(Player p : MSEssentials.getServer().getAllPlayers())
        {
            p.sendMessage(PluginMessages.broadcastPrefix.append(PluginMessages.legacyColor(message)));
        }
    }
}
