package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.discord.DiscordCommandSource;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.Utils;
import essentials.modules.server.MSServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerInfoCommand implements Command {


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (args.length == 0) {
            source.sendMessage(PluginMessages.notEnoughArgs);
            return;
        }
        if(source instanceof ConsoleCommandSource || source instanceof DiscordCommandSource)
        {
            if(MSEssentials.getServer().getPlayer(args[0]).isPresent())
            {
                source.sendMessage( Utils.getOnlinePlayerInfo(MSEssentials.getServer().getPlayer(args[0]).get()));
                return;
            }
            source.sendMessage(Utils.getOfflinePlayerInfo(args[0]));
            return;
        }
        if(source instanceof Player)
        {
            if (!source.hasPermission(PluginPermissions.PLAYERINFO))
                return;
        }

        Player player = (Player) source;
        if(MSEssentials.getServer().getPlayer(args[0]).isPresent())
        {

            if(source.hasPermission(PluginPermissions.PLAYERINFO))
            {
                player.sendMessage(TextComponent.of("Getting playerinfo"));
                player.sendMessage(Utils.getOnlinePlayerInfo(MSEssentials.getServer().getPlayer(args[0]).get()));
                return;
            }
        }
        else
        {
            if(source.hasPermission(PluginPermissions.PLAYERINFO))
            {
            source.sendMessage(Utils.getOfflinePlayerInfo(args[0]));
        }
            else
                player.sendMessage(PluginMessages.noPermissions);
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
