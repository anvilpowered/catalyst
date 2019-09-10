package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;

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
                for(Player p : MSEssentials.getServer().getAllPlayers())
                {
                    p.sendMessage(PluginMessages.prefix.append(PluginMessages.legacyColor(message)));
                }
                return;
            }
            else
            {
                player.sendMessage(PluginMessages.noPermissions);
                return;
            }

        }
        String message = Stream.of(args).collect(Collectors.joining(" "));
        for(Player p : MSEssentials.getServer().getAllPlayers())
        {
            p.sendMessage(PluginMessages.broadcastPrefix.append(PluginMessages.legacyColor(message)));
        }
    }
}
