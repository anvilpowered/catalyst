package rocks.milspecsg.msessentials.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class BanCommand implements Command {


    @Override
    public void execute(CommandSource source, @NonNull @NonNull String[] args) {
        String banReason;

        if(args.length == 0) {
            //TODO Implement PluginMessages class for NotEnoughArgs
            return;
        }

        if (args[1] != null) {
            banReason = args[1];
        } else {
            banReason = "The ban hammer has spoken!";
        }


    }

}
