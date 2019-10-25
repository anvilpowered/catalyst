package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.events.ProxyChatEvent;
import essentials.modules.proxychat.ProxyChat;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command {





    public static Integer playerCount()
    {
        int count = MSEssentials.getServer().getPlayerCount();
        return count;
    }

    public static Integer staffCount()
    {
        int staffOnline = 0;
        for(Player onlinePlayer : MSEssentials.getServer().getAllPlayers())
        {
            if(onlinePlayer.hasPermission(PluginPermissions.STAFFCHAT))
            {
                staffOnline = staffOnline + 1;
            }
        }
        return staffOnline;

    }

    /**
     * Executes the command for the specified {@link CommandSource}.
     *
     * @param source the source of this command
     * @param args   the arguments for this command
     */
    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        if(playerCount() == 0)
        {
            source.sendMessage(PluginMessages.prefix.append(TextComponent.of("There are no players online!")));
            return;
        }
        source.sendMessage(PluginMessages.legacyColor("&b----------------Online Players----------------"));
        source.sendMessage(PluginMessages.legacyColor("&bPlayer Count: ").append(PluginMessages.legacyColor("&a" + playerCount())));
        List<TextComponent> adminList = new ArrayList<>();

        for(Player onlinePlayer : MSEssentials.getServer().getAllPlayers())
        {
          /*  if(onlinePlayer.hasPermission("msessentials.group.admin"))
                adminList.add(PluginMessages.legacyColor(ProxyChatEvent.getRank(onlinePlayer)));*/
            source.sendMessage(TextComponent.of("Admin ").append(TextComponent.of(adminList.toString().join(", ", " "))));
            source.sendMessage(PluginMessages.legacyColor(ProxyChatEvent.getRank(onlinePlayer))
                    .append(TextComponent.of(onlinePlayer.getUsername())));
        }

    }


}
