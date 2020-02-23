package org.anvilpowered.catalyst.velocity.discord;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;

public class DiscordListener extends ListenerAdapter {

    @Inject
    private Registry registry;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private DiscordCommandSource discordCommandSource;

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()) || event.isWebhookMessage() || event.getMember().isFake()) {
            return;
        }
        if (event.getChannel().getId().equals(registry.getOrDefault(CatalystKeys.MAIN_CHANNEL))) {
            if (event.getMessage().toString().contains("!cmd") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                String command = event.getMessage().getContentRaw().replace("!cmd ", "");
                proxyServer.getCommandManager().execute(discordCommandSource, command);
                return;
            } else {
                String message = registry.getOrDefault(CatalystKeys.DISCORD_CHAT_FORMAT).replace("%name%", event.getAuthor().getName()).replace("%message%", event.getMessage().getContentRaw());
                proxyServer.broadcast(
                    legacyColor(message)
                        .clickEvent(ClickEvent.openUrl(registry.getOrDefault(CatalystKeys.DISCORD_URL)))
                );
            }
        }

        if (event.getChannel().getId().equals(registry.getOrDefault(CatalystKeys.STAFF_CHANNEL))) {

            String message = registry.getOrDefault(CatalystKeys.DISCORD_STAFF_FORMAT).replace("%name%", event.getAuthor().getName()).replace("%message%", event.getMessage().getContentRaw());
            for (Player p : proxyServer.getAllPlayers()) {
                if(p.hasPermission("catalyst.admin.command.staffchat")) {
                    p.sendMessage(legacyColor(message).clickEvent(ClickEvent.openUrl(registry.getOrDefault(CatalystKeys.DISCORD_URL))));
                }
            }
        }
    }

    public TextComponent legacyColor(String text) {
        return LegacyComponentSerializer.legacy().deserialize(text, '&');
    }


}
