package rocks.milspecsg.msessentials.modules.utils;

import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class StaffListUtils {

    public List<TextComponent> staffNames = new ArrayList<>();
    public List<TextComponent> adminNames = new ArrayList<>();
    public List<TextComponent> ownerNames = new ArrayList<>();

    public void getStaffNames(Player player) {
        if (player.hasPermission(PluginPermissions.STAFFLIST_OWNER)) {
            ownerNames.add(TextComponent.of(player.getUsername()));
        } else if (player.hasPermission(PluginPermissions.STAFFLIST_STAFF)) {
            if (player.hasPermission(PluginPermissions.STAFFLIST_ADMIN)) {
                adminNames.add(TextComponent.of(player.getUsername()));
            } else {
                staffNames.add(TextComponent.of(player.getUsername()));
            }
        }
    }

    public void removeStaffNames(Player player) {
        TextComponent playerName = TextComponent.of(player.getUsername());

        ownerNames.remove(playerName);
        adminNames.remove(playerName);
        staffNames.remove(playerName);
    }
}
