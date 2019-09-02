package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.google.MSGoogle;
import org.checkerframework.checker.nullness.qual.NonNull;


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
            source.sendMessage(PluginMessages.noPermissions);
        }
    }

    /*@Override
    public List<String> suggest(CommandSource source, @NonNull @NonNull String[] currentArgs) {
        return null;
    }*/
}
