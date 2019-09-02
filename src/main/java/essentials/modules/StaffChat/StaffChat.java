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
    public static String format;


    public static TextComponent enabled = TextComponent.builder()
            .content(PluginMessages.prefix)
            .append("Staff Chat Enabled")
            .color(TextColor.BLUE)
            .build();
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
        player.sendMessage(enabled);
    }

    public static void disable(Player player)
    {
        player.sendMessage(disabled);
    }

    public static void sendMessage(Player player, String mess){
        MSEssentials.server.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                .forEach(target -> { target.sendMessage(legacyColor("&b[STAFF]&r " + "&5 " + player.getUsername() + "&r:" + mess));
        });
    }

    public static void sendConsoleMessage(String mess){
        MSEssentials.server.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                .forEach(target -> { target.sendMessage(legacyColor("&b[STAFF]&r " + "&5 " + "CONSOLE" + "&r:" + mess));
                });
    }

    public static TextComponent legacyColor(String text){
        return LegacyComponentSerializer.INSTANCE.deserialize(text, '&');
    }
}
