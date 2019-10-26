package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.events.PlayerMessageEvent;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class SocialSpyCommand implements Command {
    @Override
    public void execute(CommandSource source,  @NonNull String[] args) {

        if(args[0].contains("list"))
        {
            for (Player player2 : MSEssentials.getServer().getAllPlayers())
            {
                if(PlayerMessageEvent.socialSpySet.contains(player2.getUniqueId()))
                {
                    source.sendMessage(TextComponent.of(player2.getUsername()));
                }
            }
            return;
        }
        if(source instanceof Player)
        {
            Player player = (Player) source;
            UUID playerUUID = player.getUniqueId();
            if(player.hasPermission(PluginPermissions.SOCIALSPY))
            {
                if(PlayerMessageEvent.socialSpySet.contains(playerUUID))
                {
                    PlayerMessageEvent.socialSpySet.remove(playerUUID);
                    player.sendMessage(PluginMessages.prefix.append(TextComponent.of("socialspy disabled.")));
                    return;
                }
                PlayerMessageEvent.socialSpySet.add(playerUUID);
                player.sendMessage(PluginMessages.prefix.append(TextComponent.of("socialspy enabled")));
            }
            else {
                player.sendMessage(PluginMessages.noPermissions);
            }




        }
    }
}
