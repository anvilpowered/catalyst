package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.server.MSServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerInfoCommand implements Command {


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if(args.length == 0)
        {

            return;
        }

        Player target = (Player) MSEssentials.getServer().getPlayer(args[0]).get();
        if(target == null)
        {
            source.sendMessage(TextComponent.of("The selected player is not online!"));
        }
        if(source instanceof Player)
        {
            if(!source.hasPermission(PluginPermissions.PLAYERINFO))
            {
                source.sendMessage(PluginMessages.noPermissions);
                return;
            }
        }

            source.sendMessage(TextComponent.of("Banned: ").append(TextComponent.of(String.valueOf(PlayerConfig.checkBan(target.getUsername())))));
            if(PlayerConfig.getNickName(target.getUsername()) != null) {
                source.sendMessage(TextComponent.of(PlayerConfig.getNickName(target.getUsername())));
            }
            source.sendMessage(TextComponent.of("IP Address: ").append(TextComponent.of(PlayerConfig.getIP(target.getUsername()))));
            String currentServer = target.getCurrentServer().get().getServer().getServerInfo().getName();
            source.sendMessage(TextComponent.of("Current Server " + currentServer));
    }
}
