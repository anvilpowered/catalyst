package essentials.modules.events;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import net.kyori.text.TextComponent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerMessageEvent implements ResultedEvent<PlayerMessageEvent.MessageResult> {
    private final Player sender;

    public static Set<UUID> socialSpySet;
    public static Map<UUID, UUID> replyMap;

    public Player getSender() {
        return sender;
    }

    public Player getRecipient() {
        return recipient;
    }

    /**
     * Returns the result associated with this event.
     *
     * @return the result of this event
     */
    @Override
    public MessageResult getResult() {
        return result;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    private final Player recipient;
    private final String rawMessage;

    private static MessageResult result;

    public PlayerMessageEvent(Player sender, Player recipient, String rawMessage, boolean cancelled) {
        this.sender = sender;
        this.recipient = recipient;
        this.rawMessage = rawMessage;
        this.result = MessageResult.create(cancelled, rawMessage);
    }

    public static TextComponent message(String sender, String reciever, String rawMessage) {
        TextComponent msg = TextComponent.builder()
                .append(PluginMessages.legacyColor("&8["))
                .append(PluginMessages.legacyColor("&b" + sender))
                .append(PluginMessages.legacyColor("&6 -> "))
                .append(PluginMessages.legacyColor("&b" + reciever))
                .append(PluginMessages.legacyColor("&8] "))
                .append(PluginMessages.legacyColor("&7" + rawMessage))
                .build();
        return msg;
    }

    public static void sendMessage(Player sender, Player recipient, String message) {
        sender.sendMessage(message("Me", recipient.getUsername(), message));
        recipient.sendMessage(message(sender.getUsername(), "Me", message));
        socialSpy(sender, recipient, message);
    }

    public static void socialSpy(Player sender, Player reciever, String rawMessage) {
        TextComponent msg = TextComponent.builder()
                .append(PluginMessages.legacyColor("&7[SocialSpy] "))
                .append(PluginMessages.legacyColor("&8["))
                .append(PluginMessages.legacyColor("&b" + sender.getUsername()))
                .append(PluginMessages.legacyColor("&6 -> "))
                .append(PluginMessages.legacyColor("&b" + reciever.getUsername()))
                .append(PluginMessages.legacyColor("&8] "))
                .append(PluginMessages.legacyColor("&7" + rawMessage))
                .build();


        for (Player player : MSEssentials.getServer().getAllPlayers()) {
            if (socialSpySet.contains(player.getUniqueId())) {
                player.sendMessage(msg);
            }
        }
    }


    public void setResult(MessageResult result) {
        PlayerMessageEvent.result = result;
    }


    public static final class MessageResult implements ResultedEvent.Result {

        private static boolean allowed = false;

        private static String reason;

        public MessageResult(String optional, boolean b) {
            reason = optional;
            allowed = b;
        }

        public static final MessageResult create(boolean allowed2, String reason2) {
            allowed = allowed2;
            reason = reason2;
            return new MessageResult(reason, allowed);
        }

        public static String getReason() {
            return reason;
        }

        public static void setReason(String reason2) {
            reason = reason2;
        }


        @Override
        public boolean isAllowed() {
            allowed = true;
            return allowed;
        }

        public static void deny(String reason2) {
            reason = reason2;
            allowed = false;
        }

    }
}
