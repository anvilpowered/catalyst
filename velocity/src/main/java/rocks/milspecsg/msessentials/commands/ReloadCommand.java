package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentials;

public class ReloadCommand implements Command {

    @Inject
    private MSEssentials msEssentials;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        msEssentials.initServices();
        msEssentials.loadConfig();
    }
}
