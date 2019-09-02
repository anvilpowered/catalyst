package modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import modules.PluginMessages;
import modules.PluginPermissions;
import org.checkerframework.checker.nullness.qual.NonNull;

public class SendGoogleCommand implements Command {
    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        if(source.hasPermission(PluginPermissions.SENDGOOGLE))
        {
            String playerName = args[0];
            Player player = 
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
