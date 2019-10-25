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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
