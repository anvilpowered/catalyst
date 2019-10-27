package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FindCommand implements Command {

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        Optional<Player> target = MSEssentials.getServer().getPlayer(args[0]);
        if(target.isPresent())
        {
            if(target.get().getCurrentServer().isPresent())
            {
                String server = target.get().getCurrentServer().get().getServerInfo().getName();
                source.sendMessage(TextComponent.of(target.get().getUsername()).append(TextComponent.of(" is connected to "))
                .append(TextComponent.of(server)));
            }

        }


    }

    @Override
    public List<String> suggest(CommandSource src, String[] args)
    {
        if(args.length ==1)
        {
            return MSEssentials.getServer().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Arrays.asList();
    }
}
