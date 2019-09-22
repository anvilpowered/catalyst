package essentials.modules.events;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;
import essentials.modules.User;
import net.kyori.text.Component;

import java.util.Optional;

public final class MessageEvent implements ResultedEvent<MessageEvent.MessageResult> {

    private MessageResult result;

    private final Optional<User<? extends CommandSource>> sender;
    private final Optional<User<? extends CommandSource>> recipient;
    private final Component message;
    private final boolean reply;

    private Optional<User<? extends CommandSource>> getSender(){
        return sender;
    }

    private Optional<User<? extends CommandSource>> getRecipient(){
        return recipient;
    }

    private Component getMessage()
    {
        return message;
    }
    private boolean getReply()
    {
        return reply;
    }

    @Override
    public MessageResult getResult() {
        return result;
    }

    @Override
    public void setResult(MessageResult result) {
        this.result = result;
    }

    public MessageEvent(Optional<User<? extends CommandSource>> sender, Optional<User<? extends CommandSource>> recipient, Component message2, boolean reply, Component message)
    {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message2;
        this.reply = reply;

        this.result = MessageResult.create();
    }

    public static final class MessageResult implements ResultedEvent.Result
    {
        private Optional<Component> reason;
        private boolean allowed;

        private MessageResult(Optional op, boolean b)
        {
            this.reason = op;
            this.allowed = b;
        }

        public void allow()
        {
            this.allowed = true;
        }
        public void deny(Component reason){
            this.reason = Optional.ofNullable(reason);
            this.allowed = false;
        }

        public static final MessageResult create()
        {
            return new MessageResult(Optional.empty(), true);
        }

        @Override
        public boolean isAllowed() {
            return allowed;
        }
    }


}
