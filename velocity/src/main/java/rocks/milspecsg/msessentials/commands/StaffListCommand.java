package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import java.util.ArrayList;
import java.util.List;

//This class was initally written by LGC_McLovin of MilspecSG
public class StaffListCommand implements Command {

    @Inject
    private ProxyServer proxyServer;

    List<TextComponent> staffNames = new ArrayList<>();
    List<TextComponent> moderatorNames = new ArrayList<>();
    List<TextComponent> adminNames = new ArrayList<>();
    List<TextComponent> ownerNames = new ArrayList<>();

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        staffNames.clear();
        moderatorNames.clear();
        adminNames.clear();
        ownerNames.clear();

        if(isStaffOnline()) {
            getStaffNames();
            source.sendMessage(getLine());
            if(!staffNames.isEmpty()) {
                source.sendMessage(getStaffTitle());
                for (TextComponent text : staffNames) {
                    source.sendMessage(text);
                }
            }
            if(!adminNames.isEmpty()) {
                source.sendMessage(getAdminTitle());
                for(TextComponent text : adminNames) {
                    source.sendMessage(text);
                }
            }
            if(!ownerNames.isEmpty()) {
                source.sendMessage(getOwnerTitle());
                for(TextComponent text : ownerNames) {
                    source.sendMessage(text);
                }
            }
            source.sendMessage(getLine());
        } else {
            source.sendMessage(TextComponent.builder()
                    .append(MSEssentialsPluginInfo.pluginPrefix)
                    .append("There are no staff members currently online.")
                    .build());
        }
    }

    private boolean isStaffOnline() {
        for (Player player : proxyServer.getAllPlayers()) {
            if (player.hasPermission(PluginPermissions.STAFFLIST_BASE)) {
                return true;
            }
        }
        return false;
    }

    public void getStaffNames() {
        for (Player player : proxyServer.getAllPlayers()) {
            if ((player.hasPermission(PluginPermissions.STAFFLIST_STAFF)
                    && player.hasPermission(PluginPermissions.STAFFLIST_MODERATOR)
                    && player.hasPermission(PluginPermissions.STAFFLIST_ADMIN)
                    && player.hasPermission(PluginPermissions.STAFFLIST_OWNER) || player.hasPermission(PluginPermissions.STAFFLIST_OWNER))) {
                ownerNames.add(TextComponent.of(player.getUsername()));
            } else {
                if (player.hasPermission(PluginPermissions.STAFFLIST_STAFF)) {
                    if (player.hasPermission(PluginPermissions.STAFFLIST_MODERATOR)) {
                        if (player.hasPermission(PluginPermissions.STAFFLIST_ADMIN)) {
                            adminNames.add(TextComponent.of(player.getUsername()));
                        } else {
                            moderatorNames.add(TextComponent.of(player.getUsername()));
                        }
                    } else {
                        staffNames.add(TextComponent.of(player.getUsername()));
                    }
                }
            }
        }
    }

    public TextComponent getLine() {
        return TextComponent.builder()
                .content("-----------------------------------------------------")
                .color(TextColor.DARK_AQUA)
                .build();
    }

    public TextComponent getStaffTitle() {
        return TextComponent.builder()
                .content("Staff:")
                .color(TextColor.GOLD)
                .build();
    }

    public TextComponent getAdminTitle() {
        return TextComponent.builder()
                .content("Admin:")
                .color(TextColor.GOLD)
                .build();
    }

    public TextComponent getOwnerTitle() {
        return TextComponent.builder()
                .content("Owner:")
                .color(TextColor.GOLD)
                .build();
    }
}
