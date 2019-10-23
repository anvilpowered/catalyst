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
        source.sendMessage(PluginMessages.legacyColor("&b----------------Player List----------------"));
        source.sendMessage(PluginMessages.legacyColor("Player Count: ").append(PluginMessages.legacyColor("&a" + playerCount())));
        source.sendMessage(TextComponent.of("Staff Count: ").append(TextComponent.of(staffCount())));
        for(Player onlinePlayer : MSEssentials.getServer().getAllPlayers())
        {
            source.sendMessage(PluginMessages.legacyColor(ProxyChatEvent.getRank(onlinePlayer))
                    .append(TextComponent.of(onlinePlayer.getUsername())));
        }
    }

}
