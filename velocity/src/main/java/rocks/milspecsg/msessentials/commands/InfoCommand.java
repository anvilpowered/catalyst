package rocks.milspecsg.msessentials.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.api.member.MemberManager;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

public class InfoCommand implements Command {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (args.length == 0) {
            source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Please supply a username!")));
        } else {
            boolean isActive = proxyServer.getPlayer(args[0]).isPresent();
            memberManager.info(args[0], isActive).thenAcceptAsync(source::sendMessage);
        }
    }
}
