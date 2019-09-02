package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.server.MSServer;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;

public class MessageCommand implements Command {
    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if(source instanceof Player)
        {
            String message = Arrays.toString(args);
            Player src = (Player) source;

            if(args.length == 0)
            {
                src.sendMessage(returnUsage(src));
                return;
            }
        if(MSEssentials.server.getPlayer(args[0]).isPresent())
            {

                message =  message.replace(src.getUsername(), "").replace(",", "").replaceAll("\\[", "")
                .replaceAll("]", "");

                Player recipeant = MSEssentials.server.getPlayer(args[0]).get();

                MSEssentials.logger.info("[" + src.getUsername() + "]" + " -> " + "[" + recipeant.getUsername() + "]" + message);
                source.sendMessage(TextComponent.of("[" + src.getUsername() + "]" + "->" + "[" + recipeant.getUsername() + "]" + message));
            }


        }
    }

    public static TextComponent returnUsage(Player player)
    {
        TextComponent usage = TextComponent.builder()
                .content("Usage:")
                .append("\n/message <recipient> <message>")
                .color(TextColor.RED)
                .build();
        return usage;

    }
}
