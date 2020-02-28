package org.anvilpowered.catalyst.velocity.discord;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.velocity.events.ProxyChatEvent;
import org.anvilpowered.catalyst.velocity.events.ProxyStaffChatEvent;
import org.anvilpowered.catalyst.velocity.utils.LuckPermsUtils;

public class ProxyListener {

    @Inject
    private Registry registry;

    @Inject
    private WebhookSender webhookSender;

    @Inject
    private LuckPermsUtils luckPermsUtils;

    @Subscribe
    public void onChatEvent(ProxyChatEvent event) {
        if (event.getSender().isActive()) {
            String message = event.getRawMessage();
            String name = registry.getOrDefault(CatalystKeys.PLAYER_CHAT_FORMAT).replace("%player%", event.getSender().getUsername())
                .replace("%prefix%", luckPermsUtils.getPrefix(event.getSender()))
                .replace("%suffix%", luckPermsUtils.getSuffix(event.getSender()));
            webhookSender.sendWebhookMessage(registry.getOrDefault(CatalystKeys.WEBHOOK_URL), name, message, registry.getOrDefault(CatalystKeys.MAIN_CHANNEL), event.getSender());
        }
    }

    @Subscribe
    public void onStaffChatEvent(ProxyStaffChatEvent event) {
        String name = registry.getOrDefault(CatalystKeys.PLAYER_CHAT_FORMAT)
            .replace("%prefix%", luckPermsUtils.getPrefix(event.getSender()))
            .replace("%suffix%", luckPermsUtils.getSuffix(event.getSender()))
            .replace("%player%", event.getSender().getUsername());
        String message = event.getRawMessage();

        webhookSender.sendWebhookMessage(registry.getOrDefault(CatalystKeys.WEBHOOK_URL), name, message, registry.getOrDefault(CatalystKeys.STAFF_CHANNEL), event.getSender());
    }

    @Subscribe
    public void onPlayerJoinEvent(PostLoginEvent event) {
        webhookSender.sendWebhookMessage(registry.getOrDefault(CatalystKeys.WEBHOOK_URL), registry.getOrDefault(CatalystKeys.BOT_NAME), event.getPlayer().getUsername() + registry.getOrDefault(CatalystKeys.JOIN_FORMAT), registry.getOrDefault(CatalystKeys.MAIN_CHANNEL), event.getPlayer());
    }

    @Subscribe
    public void onPlayerLeaveEvent(DisconnectEvent event) {
        webhookSender.sendWebhookMessage(registry.getOrDefault(CatalystKeys.WEBHOOK_URL), registry.getOrDefault(CatalystKeys.BOT_NAME), event.getPlayer().getUsername() + registry.getOrDefault(CatalystKeys.LEAVE_FORMAT), registry.getOrDefault(CatalystKeys.MAIN_CHANNEL), event.getPlayer());
    }
}
