package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.Utils;
import essentials.modules.server.MSServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerInfoCommand implements Command {


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (args.length == 0) {
            source.sendMessage(PluginMessages.notEnoughArgs);
            return;
        }

        if(MSEssentials.getServer().getPlayer(args[0]).isPresent())
        {
            Player player = (Player) source;
            if(source.hasPermission(PluginPermissions.PLAYERINFO))
            {
                source.sendMessage(Utils.getOnlinePlayerInfo(MSEssentials.getServer().getPlayer(args[0]).get()));
                return;
            }
        }
        else
        {
            source.sendMessage(Utils.getOfflinePlayerInfo(args[0]));
        }
    }
}
