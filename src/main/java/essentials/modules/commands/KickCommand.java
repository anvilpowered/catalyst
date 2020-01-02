package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class KickCommand implements Command
{

    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        Optional<Player> target = MSEssentials.getServer().getPlayer(args[0]);
        if(args.length == 0)
        {
            source.sendMessage(PluginMessages.notEnoughArgs);
            return;
        }
        if(args[0].equalsIgnoreCase(target.get().getUsername()))
        {
            if(source instanceof Player)
            {
                Player src = (Player) source;
                if(source.hasPermission(PluginPermissions.KICK)){

                target.get().disconnect(TextComponent.of("You have been kicked by " + src.getUsername()));
                return;
            }else
                {
                    src.sendMessage(PluginMessages.noPermissions);
                }
            }
            else
                if(source instanceof ConsoleCommandSource) {
                    source.sendMessage(TextComponent.of("Kicked " + target.get().getUsername()));
                    target.get().disconnect(TextComponent.of("You have been kicked by console!"));
                }
            return;

        }

    }
    @Override
    public List<String> suggest(CommandSource src, String[] args)
    {
        if(args.length ==1)
        {
            return MSEssentials.getServer().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Arrays.asList();
    }
}
