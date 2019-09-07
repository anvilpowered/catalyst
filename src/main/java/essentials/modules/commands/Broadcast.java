package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;

import javax.annotation.Nonnull;

public class Broadcast implements Command {

    @Override
    public void execute(CommandSource source,@Nonnull String [] args)
    {
        if(args.length == 0)
        {
            return;
        }
    }
}
