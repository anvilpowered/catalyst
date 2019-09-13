package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.Utils;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class UnMuteCommand implements Command {


    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        if(args.length == 0)
        {
            source.sendMessage(PluginMessages.notEnoughArgs);
            return;
        }

        Player player = MSEssentials.getServer().getPlayer(args[0]).get();

        if(source instanceof Player)
        {
            if(!source.hasPermission(PluginPermissions.MUTE))
            {
                source.sendMessage(PluginMessages.noPermissions);
                return;
            }
        }

        if(player.isActive())
        {
            PlayerConfig.removeMute(player.getGameProfile().getName());
            source.sendMessage(PluginMessages.prefix.append(TextComponent.of("Unmuted: " + player.getUsername())));
            return;
        }
    }
}
