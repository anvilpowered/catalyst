package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.StaffChat.StaffChat;
import essentials.modules.events.StaffChatFormedEvent;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import sun.plugin2.message.Message;

import java.util.UUID;

import static java.lang.String.join;

public class StaffChatCommand implements Command
{


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if(source instanceof Player)
        {
            Player player = (Player) source;
            UUID pUUID = player.getUniqueId();

            if(player.hasPermission(PluginPermissions.STAFFCHAT))
            {
                if(args.length == 0)
                {
                    if(StaffChat.toggledSet.isEmpty() || (!StaffChat.toggledSet.contains(pUUID)))
                    {
                        StaffChat.toggledSet.add(pUUID);
                        player.sendMessage(PluginMessages.enabledStaffChat);
                        return;
                    }
                    else
                    {
                        StaffChat.toggledSet.remove(pUUID);
                        StaffChat.disable(player);
                        return;
                    }

                }
                else
                {
                    String message = String.join(" ", args);
                    StaffChatFormedEvent formedEvent = new StaffChatFormedEvent(player, message, TextComponent.of(message));
                    return;
                }
            }
            else
            {
                player.sendMessage(PluginMessages.noPermissions);
                return;
            }

        }else{
            if(args.length == 0)
            {
                source.sendMessage(StaffChat.consoleSpecify);
                return;
            }else
            {
                StaffChat.sendConsoleMessage(join(" ", args));
                return;
            }
        }


    }
}
