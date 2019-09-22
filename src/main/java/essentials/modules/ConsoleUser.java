package essentials.modules;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import essentials.MSEssentials;

import java.util.Optional;
import java.util.UUID;

public class ConsoleUser implements User<ConsoleCommandSource> {


    private CommandSource replyRecipient;

    @Override
    public void load() throws Exception {

    }

    @Override
    public void save() throws Exception {

    }

    @Override
    public Optional<ConsoleCommandSource> getBase()
    {
        return Optional.of(MSEssentials.getServer().getConsoleCommandSource());
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public CommandSource getReplyRecipient() {
        return null;
    }

    @Override
    public Optional<UUID> getUUID() {
        return Optional.empty();
    }

    @Override
    public void setReplyRecipient(CommandSource source) {

    }

}
