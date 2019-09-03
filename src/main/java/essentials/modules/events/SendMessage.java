package essentials.modules.events;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

public class SendMessage implements ResultedEvent<PlayerChatEvent.ChatResult>
{
    private final Player sender;
    private final Player recipient;
    private final String rawMessage;

    private PlayerChatEvent.ChatResult result;

    public SendMessage(Player sender, Player recipient, String rawMessage, boolean cancelled){
        this.sender = sender;
        this.recipient = recipient;
        this.rawMessage = rawMessage;
        this.result = cancelled ? PlayerChatEvent.ChatResult.denied() : PlayerChatEvent.ChatResult.allowed();
    }

    @Override
    public PlayerChatEvent.ChatResult getResult() {
        return null;
    }

    @Override
    public void setResult(PlayerChatEvent.ChatResult result) {

    }
}
