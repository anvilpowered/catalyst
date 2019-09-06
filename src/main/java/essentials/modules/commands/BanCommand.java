package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class BanCommand implements Command {

    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        Player target = MSEssentials.getServer().getPlayer(args[0]).get();
        UUID targetID = target.getUniqueId();
        String reason;
        if(args[1] != null)
        {
            reason = args[1];
        }
        else
        {
            reason = "The ban hammer has spoken!";
        }
        if(args.length == 0)
        {
            return;
        }

        if(args[0].equalsIgnoreCase(target.getUsername()))
        {
            if(source instanceof Player)
            {
                Player src = (Player) source;
                if(src.hasPermission(PluginPermissions.BAN))
                {
                    PlayerConfig.addBan(target.getUsername(), reason);
                    src.sendMessage(TextComponent.of("Successfully banned " + target.getUsername()));
                    target.disconnect(TextComponent.of(PlayerConfig.getBanReason(target.getUsername())));
                }

            }
            source.sendMessage(TextComponent.of("Successfully banned " + target.getUsername()));
            PlayerConfig.addBan(target.getUsername(),reason);
            target.disconnect(TextComponent.of(PlayerConfig.getBanReason(target.getUsername())));
        }
    }
}
