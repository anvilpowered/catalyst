package essentials.modules.events;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.Component;

import java.util.Optional;

public class SendMessage implements ResultedEvent<SendMessage.MessageResult>
{
    private final Player sender;

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

    public SendMessage(Player sender, Player recipient, String rawMessage, boolean cancelled){
        this.sender = sender;
        this.recipient = recipient;
        this.rawMessage = rawMessage;
        this.result = MessageResult.create(cancelled, rawMessage);
    }


    public void setResult(MessageResult result) {
        this.result = result;
    }





    public static final class MessageResult implements ResultedEvent.Result
    {

        private static boolean allowed = false;

        private static String reason;

        public MessageResult(String optional, boolean b) {
            reason = optional;
            allowed = b;
        }

        public static final MessageResult create(boolean allowed, String reason) {
            allowed = allowed;
            reason = reason;
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

        public static void deny(String reason2)
        {
            reason = reason2;
            allowed = false;
        }

    }
}
