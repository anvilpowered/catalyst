package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.google.MSGoogle;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SendGoogleCommand implements Command {
    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        if(source.hasPermission(PluginPermissions.SENDGOOGLE))
        {
            String playerName = args[0];

            Player player = MSEssentials.server.getPlayer(playerName).get();
            if(source instanceof Player)
            {

                Player src = (Player) source;

                player.sendMessage(MSGoogle.senderName(src.getUsername()));
                player.sendMessage(MSGoogle.sendGoogleLink(args, playerName));

            }
        }
        else
        {
            if(source instanceof Player)
            {
                Player player = (Player) source;
                player.sendMessage(PluginMessages.noPermissions);
                return;
            }
            source.sendMessage(PluginMessages.noPermissions);
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
