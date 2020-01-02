package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BanCommand implements Command {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        String banReason;
        String username = null;

        if (!source.hasPermission(PluginPermissions.BAN)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length == 0) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }
        if (username == null) {
            username = args[0];
        }

        if(proxyServer.getPlayer(username).get().hasPermission(PluginPermissions.BAN_EXEMPT)) {
            source.sendMessage(pluginMessages.banExempt);
            return;
        }

        if (args.length > 1) {
            banReason = Stream.of(args).collect(Collectors.joining(" ")).replace(username, "");
        } else {
            banReason = "The ban hammer has spoken!";
        }

        memberManager.setBanned(username, true).thenAcceptAsync(source::sendMessage);
        memberManager.setBanReason(username, banReason).thenAcceptAsync(source::sendMessage);
        proxyServer.getPlayer(username).get().disconnect(TextComponent.of(banReason));
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return proxyServer.matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Arrays.asList();
    }

}
