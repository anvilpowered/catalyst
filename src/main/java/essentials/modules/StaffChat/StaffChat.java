package essentials.modules.StaffChat;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Set;
import java.util.UUID;

public class StaffChat {
    public static Set<UUID> toggledSet;

    public static TextComponent disabled = TextComponent.builder()
            .content("Staff Chat Disabled")
            .color(TextColor.BLUE)
            .build();

    public static TextComponent consoleSpecify = TextComponent.builder()
            .content("Please specify a message!")
            .color(TextColor.RED)
            .build();

    public static void enable(Player player)
    {
        player.sendMessage(PluginMessages.enabledStaffChat);
    }


    public static void disable(Player player)
    {
        player.sendMessage(disabled);
    }

    public static void sendMessage(String username, String mess){
        MSEssentials.server.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                .forEach(target -> { target.sendMessage(PluginMessages.legacyColor("&b[STAFF]&r " + "&5" + username + "&r: &6" + mess));
        });


    }

    public static void sendConsoleMessage(String mess){
        MSEssentials.server.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                .forEach(target -> { target.sendMessage(PluginMessages.legacyColor("&b[STAFF]&r " + "&5 " + "CONSOLE" + "&r:" + mess));
                });
    }


}
