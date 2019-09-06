package essentials.modules.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class UnBanCommand implements Command {

    @Override
    public void execute(CommandSource source, @NonNull String[] args)
    {
        if(args.length == 0)
        {
            source.sendMessage(TextComponent.of("Not enough arguments!"));
            return;
        }
        if(PlayerConfig.checkBan(args[0]))
        {
            PlayerConfig.unBan(args[0]);
            source.sendMessage(TextComponent.of("Unbanned " + args[0]));
        }
    }
}
