package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class DeleteNicknameCommand implements Command {

    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        if(source instanceof Player)
        {
            Player player = (Player) source;
            UUID playerUUID = player.getUniqueId();
            if(player.hasPermission(PluginPermissions.NICKNAME))
            {
                if(args.length == 0)
                {
                    String nick = PlayerConfig.getNickName(player.getUsername());
                    PlayerConfig.deleteNick(player.getUsername());
                    player.sendMessage(PluginMessages.deleteNick());
                }
            }
            else
            {
                player.sendMessage(PluginMessages.noPermissions);
            }
        }else
        {
            source.sendMessage(TextComponent.of("this must be ran by a player!"));
        }
    }


}
