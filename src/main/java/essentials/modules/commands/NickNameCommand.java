package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.modules.Config.NicknameConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;
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
                if(NicknameConfig.hasNickName(playerUUID))
                {
                    player.sendMessage(PluginMessages.nickColorized(NicknameConfig.getNickName(playerUUID)));
                    return;
                }
            }
            nick = player.getUsername().replace(player.getUsername(), "~" + Arrays.toString(args)
                    .replaceAll("\\[", "")
                    .replaceAll("]", ""));
            player.sendMessage(PluginMessages.setNickName(nick));
            NicknameConfig.addNick(nick, player.getUniqueId());
            return;
        }
        player.sendMessage(PluginMessages.noPermissions);
    }

    public static String getNick(UUID uuid) {
        return NicknameConfig.getNickName(uuid);
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
