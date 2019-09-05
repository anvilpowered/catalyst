package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public class KickCommand implements Command
{

    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        Player target = MSEssentials.getServer().getPlayer(args[0]).get();
        if(args.length == 0)
        {
            return;
        }
        if(args[0].equalsIgnoreCase(target.getUsername()))
        {
            if(source instanceof Player)
            {
                if(source.hasPermission(PluginPermissions.KICK)){
                Player src = (Player) source;
                target.disconnect(TextComponent.of("You have been kicked by " + src.getUsername()));
                return;
            }else
                {
                    source.sendMessage(PluginMessages.noPermissions);
                }
            }
            target.disconnect(TextComponent.of("You have been kicked by console!"));
            return;
        }
    }
}
