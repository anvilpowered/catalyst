package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.PluginPermissions;
import essentials.modules.events.SendMessage;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerMessage implements Command {

    /**
     * Executes the command for the specified {@link CommandSource}.
     *
     * @param source the source of this command
     * @param args   the arguments for this command
     */
    @Override
    public void execute(CommandSource source,  @NonNull String[] args) {
        if(source instanceof Player)
        {
            Player player = (Player) source;

            if(player.hasPermission(PluginPermissions.GOOGLE))
            {
                if(MSEssentials.getServer().getPlayer(args[0]).isPresent())
                {
                    System.out.println("Player is present");
                    System.out.println(args[0]);
                    Player recipient = MSEssentials.getServer().getPlayer(args[0]).get();
                    String ar = String.join("", args).replace(MSEssentials.getServer().getPlayer(args[0]).get().getUsername(), "");
                    System.out.println(ar);

                    SendMessage sendMessage = new SendMessage(player ,recipient, ar, false );
                    MSEssentials.getServer().getEventManager().fire(sendMessage).thenAcceptAsync( resultEvent -> {
                        SendMessage.MessageResult result = resultEvent.getResult();
                        if(result.isAllowed())
                        {
                            source.sendMessage(TextComponent.of(player.getUsername())
                                    .append(TextComponent.of("->")).append(TextComponent.of(recipient.getUsername())).append(TextComponent.of(" : " + ar)));
                        }
                    });
                }
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
