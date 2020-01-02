package rocks.milspecsg.msessentials.events;

import com.velocitypowered.api.proxy.Player;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProxyStaffChatEvent {

    private final Player sender;
    private final String rawMessage;
    public static Set<UUID> staffChatSet = new HashSet<>();

    public Player getSender() {
        return sender;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public TextComponent getMessage() {
        return message;
    }

    private final TextComponent message;

    public ProxyStaffChatEvent(Player sender, String rawMessage, TextComponent message) {
        this.sender = sender;
        this.rawMessage = rawMessage;
        this.message = message;
    }


}
