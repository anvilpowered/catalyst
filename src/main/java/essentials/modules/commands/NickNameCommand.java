package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.UUID;

public class NickNameCommand implements Command {


    public static String nick;
    @Override
    public void execute(CommandSource source,  @NonNull String[] args) {
        Player player = (Player) source;
        UUID playerUUID = player.getUniqueId();
        if (player.hasPermission(PluginPermissions.NICKNAME)) {

            if(args.length == 0)
            {
                if(PlayerConfig.hasNickName(playerUUID))
                {
                    player.sendMessage(PluginMessages.nickColorized(PlayerConfig.getNickName(playerUUID)));
                    return;
                }else
                {
                    player.sendMessage(PluginMessages.nickUsage());
                    return;
                }
            }
            nick = player.getUsername().replace(player.getUsername(), "~" + Arrays.toString(args)
                    .replaceAll("\\[", "")
                    .replaceAll("]", ""));
            if(!player.hasPermission(PluginPermissions.NICKNAMECOLOR))
            {
                if(nick.contains("&"))
                {
                    player.sendMessage(PluginMessages.noNickColorPermission);
                    return;
                }else
                {
                    player.sendMessage(PluginMessages.setNickName(nick));
                    PlayerConfig.addNick(nick, player.getUniqueId());
                    return;
                }
            }
            if(nick.contains("&k") && !(player.hasPermission(PluginPermissions.NICKNAMEMAGIC)))
            {
                player.sendMessage(PluginMessages.noNickMagicPermissions);
                return;
            }else
            {


                player.sendMessage(PluginMessages.setNickName(nick));
                PlayerConfig.addNick(nick, player.getUniqueId());
                return;
            }
        }
        player.sendMessage(PluginMessages.noPermissions);
    }

    public static String getNick(UUID uuid) {
        return PlayerConfig.getNickName(uuid);
    }
}
