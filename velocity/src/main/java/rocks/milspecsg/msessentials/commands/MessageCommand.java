package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentials;
import rocks.milspecsg.msessentials.events.ProxyMessageEvent;
import rocks.milspecsg.msessentials.misc.PluginMessages;
import rocks.milspecsg.msessentials.misc.PluginPermissions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessageCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        String name;
        if (args.length < 1) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }

        Optional<Player> recipient = MSEssentials.getServer().getPlayer(args[0]);
        if (source instanceof ConsoleCommandSource) {
            if (recipient.isPresent()) {
                name = args[0];
                args[0] = "";

                String message = String.join(" ", args);
                source.sendMessage(ProxyMessageEvent.message("Me", name, message));
                recipient.get().sendMessage(ProxyMessageEvent.message("Console", "Me", message));
            }
        } else if (source instanceof Player) {
            Player sender = (Player) source;

            if (sender.hasPermission(PluginPermissions.MESSAGE)) {
                if (recipient.isPresent()) {
                    if (args[0].equalsIgnoreCase(recipient.get().getUsername())) {
                        args[0] = args[0].toLowerCase();
                        String recipientName = recipient.get().getUsername();
                        String message = String.join("", args).replace(recipientName.toLowerCase(), "");
                        ProxyMessageEvent.sendMessage(sender, recipient.get(), message, proxyServer);
                        if (sender.getUniqueId().equals(recipient.get().getUniqueId())) {
                            return;
                        }
                        ProxyMessageEvent.replyMap.put(recipient.get().getUniqueId(), sender.getUniqueId());
                    }
                }
            }
        }
    }

    @Override
    public List<String> suggest(CommandSource src, String[] args) {
        if (args.length == 1) {
            return MSEssentials.getServer().matchPlayer(args[0]).stream().map(Player::getUsername).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}