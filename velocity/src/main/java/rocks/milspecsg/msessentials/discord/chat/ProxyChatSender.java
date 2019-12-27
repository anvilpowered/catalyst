package rocks.milspecsg.msessentials.discord.chat;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.api.discord.FormatService;

public class ProxyChatHandler {

    @Inject
    private FormatService formatService;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    public ProxyChatHandler() {
    }

    public void sendToServer(String sender, String message){
        if(sender != null && message != null) {
            for(Player p : proxyServer.getAllPlayers()) {
                p.sendMessage(TextComponent.of(formatService.formatMessageFromDiscord(sender, message)));
            }
        }
    }
}
