package org.anvilpowered.catalyst.bungee.listeners;

import com.google.inject.Inject;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.chat.PrivateMessageService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.StaffListService;

public class PlayerListener implements Listener {

    @Inject
    private Registry registry;

    @Inject
    private PrivateMessageService<TextComponent> privateMessageService;

    @Inject
    private StaffListService<TextComponent> staffListService;

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.hasPermission(registry.getOrDefault(CatalystKeys.SOCIALSPY_ONJOIN))) {
            privateMessageService.socialSpySet().add(player.getUniqueId());
        }
        staffListService.getStaffNames(player.getDisplayName(), player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_ADMIN)), player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_STAFF)), player.hasPermission(registry.getOrDefault(CatalystKeys.STAFFLIST_OWNER)));

    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        privateMessageService.socialSpySet().remove(player.getUniqueId());
        staffListService.removeStaffNames(player.getDisplayName());
    }
}
