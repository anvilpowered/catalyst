package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.modules.messages.CommandUsageMessages;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MuteCommand implements Command {

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private MemberManager<TextComponent> memberManager;

    @Inject
    private CommandUsageMessages commandUsage;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {

        if (!source.hasPermission(PluginPermissions.MUTE)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length == 0) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            source.sendMessage(commandUsage.muteCommandUsage);
            return;
        }
        Optional<Player> player = proxyServer.getPlayer(args[0]);
        if (player.isPresent()) {
            if (player.get().hasPermission(PluginPermissions.MUTE_EXEMPT)) {
                source.sendMessage(pluginMessages.muteExempt);
                return;
            }
        }

       memberManager.mute(args[0]).thenAcceptAsync(source::sendMessage);
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return proxyServer.matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
