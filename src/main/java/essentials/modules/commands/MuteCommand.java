package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.Utils;
import essentials.modules.server.MSServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MuteCommand implements Command {

    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        if(args.length == 0)
            {
                return;
            }
        Player player = MSEssentials.getServer().getPlayer(args[0]).get();
        if(player.isActive())
        {
            String name = player.getUsername();
            PlayerConfig.addMute(name);
            if(args.length != 2)
            {
                PlayerConfig.permMuteAdd(name);
                source.sendMessage(PluginMessages.prefix.append(TextComponent.of("Muted ").append(PluginMessages.legacyColor(name))));
                return;
            }
            Utils.muteTask(name, Integer.parseInt(args[1]));
            source.sendMessage(PluginMessages.prefix.append(PluginMessages.legacyColor(name).append(TextComponent.of(args[1]))));
            return;
        }
        source.sendMessage(PluginMessages.prefix.append(TextComponent.of("Cannot find a player with that specified name!")));
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
