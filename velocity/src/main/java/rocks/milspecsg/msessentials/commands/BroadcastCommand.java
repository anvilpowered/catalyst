package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;

import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msrepository.api.tools.resultbuilder.StringResult;


public class BroadcastCommand implements Command {

    @Inject
    private StringResult<TextComponent, CommandSource> stringResult;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        TextComponent textComponent = stringResult.builder().blue().append("hello").red().append(" World").build();
        source.sendMessage(textComponent);
    }
}
