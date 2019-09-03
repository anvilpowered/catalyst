package essentials.modules.events;

import com.velocitypowered.api.proxy.Player;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

public class StaffChatFormedEvent {
    private final Player sender;
    private final String rawMessage;
    private final Component message;

    public StaffChatFormedEvent(Player sender, String rawMessage, Component message) {
        this.sender = sender;
        this.rawMessage = rawMessage;
        this.message = TextComponent.of(": ").append( message);
    }

    public Component getMessage() {
        return message;
    }

    public Player getSender() {
        return sender;
    }

    public String getRawMessage() {
        return rawMessage;
    }
}
