package rocks.milspecsg.msessentials.commands;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class InfoCommand implements Command {

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages pluginMessages;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if(!source.hasPermission(PluginPermissions.INFO)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length == 0) {
            source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(TextComponent.of("Please supply a username!")));
        } else {
            boolean isActive = proxyServer.getPlayer(args[0]).isPresent();
            memberManager.info(args[0], isActive).thenAcceptAsync(source::sendMessage);
        }
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return proxyServer.matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
