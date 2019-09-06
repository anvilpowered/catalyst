package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.Utils;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.UUID;

import static essentials.modules.PluginMessages.prefix;

public class NickNameCommand implements Command {


    public static String nick;
    @Override
    public void execute(CommandSource source,  @NonNull String[] args) {
        Player player = (Player) source;
        UUID playerUUID = player.getUniqueId();
        if (player.hasPermission(PluginPermissions.NICKNAME)) {

            if (args.length == 0) {
                if (PlayerConfig.hasNickName(playerUUID)) {
                    player.sendMessage(PluginMessages.nickColorized(PlayerConfig.getNickName(playerUUID)));
                    return;
                } else {
                    player.sendMessage(PluginMessages.nickUsage());
                    return;
                }
            }
            if (args[0].length() > 2) {
                nick = player.getUsername().replace(player.getUsername(), "~" + Arrays.toString(args)
                        .replaceAll("\\[", "")
                        .replaceAll("]", ""));
                if (!player.hasPermission(PluginPermissions.NICKNAMECOLOR)) {
                    if (nick.contains("&")) {
                        nick = Utils.removeColorCodes(nick);
                        player.sendMessage(PluginMessages.noNickColorPermission);
                    } else {
                        player.sendMessage(PluginMessages.setNickName(nick));
                        PlayerConfig.addNick(nick, player.getUniqueId());
                        return;
                    }
                }
                if (nick.contains("&k") && !(player.hasPermission(PluginPermissions.NICKNAMEMAGIC))) {
                    nick = Utils.removeMagicCode(nick);
                    player.sendMessage(PluginMessages.noNickMagicPermissions);
                } else {


                    player.sendMessage(PluginMessages.setNickName(nick));
                    PlayerConfig.addNick(nick, player.getUniqueId());
                    return;
                }
            } else {
                player.sendMessage(PluginMessages.legacyColor(prefix).append(TextComponent.of("nicknames must be longer than 2 characters!").color(TextColor.YELLOW)));
            }
        }
        else
        {
            player.sendMessage(PluginMessages.noPermissions);

        }
    }

    public static String getNick(UUID uuid) {
        return PlayerConfig.getNickName(uuid);
    }
}
