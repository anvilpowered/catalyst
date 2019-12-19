package rocks.milspecsg.msessentials.events;

import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

@Singleton
public class ProxyChatEvent {

    private final Player sender;
    private final String rawMessage;
    private final Component message;

    public ProxyChatEvent(Player sender, String rawMessage, Component message) {
        this.sender = sender;
        this.rawMessage = rawMessage;
        this.message = TextComponent.of(":").append(message);
    }

    public Player getSender() {
        return sender;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public Component getMessage() {
        return message;
    }

}
