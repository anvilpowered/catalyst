package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.events.PlayerMessageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class SocialSpyCommand implements Command {
    @Override
    public void execute(CommandSource source,  @NonNull String[] args) {
        if(source instanceof Player)
        {
            Player player = (Player) source;
            UUID playerUUID = player.getUniqueId();
            if(player.hasPermission(PluginPermissions.SOCIALSPY))
            {
                if(PlayerMessageEvent.toggledSet.contains(playerUUID))
                {
                    PlayerMessageEvent.toggledSet.remove(playerUUID);
                    return;
                }
                PlayerMessageEvent.toggledSet.add(playerUUID);
            }
            else {
                player.sendMessage(PluginMessages.noPermissions);
            }

        }
    }
}
