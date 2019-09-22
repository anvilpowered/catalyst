package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginMessages;
import essentials.modules.server.MSServer;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageCommand implements Command {

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        Player target = MSEssentials.getServer().getPlayer(args[0]).get();
        String sourceName = source instanceof Player ? ((Player) source).getUsername() : "Console";
        String targetName = target.getUsername();

        String msg = Stream.of(args).collect(Collectors.joining(" ")).replace(targetName, "");

        Component content = TextComponent.of(msg);
        String srcMsg = "&b" + sourceName + " -> " + targetName + " : " + msg;
        String reciMsg = "&b" + sourceName + " -> " + " me " + ": " + msg;
        Component sourceMessage = PluginMessages.legacyColor(srcMsg);
        Component targetMessage = PluginMessages.legacyColor(reciMsg);

        source.sendMessage(sourceMessage);
        target.sendMessage(targetMessage);
    }
    @Override
    public List<String> suggest(CommandSource source, String[] args)
    {
        if(args.length == 1)
        {
            return MSEssentials.getServer().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Arrays.asList();
    }

}
