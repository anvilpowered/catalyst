package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.member.MemberManager;


public class NickNameCommand implements Command {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if(source instanceof Player) {
            Player player = (Player) source;
            memberManager.setNickname(player.getUniqueId(), args[0]).thenAcceptAsync(source::sendMessage);
        }
    }
}
