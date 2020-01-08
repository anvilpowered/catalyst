package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.MSEssentialsPluginInfo;
import rocks.milspecsg.msessentials.events.ProxyMessageEvent;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

import java.util.Optional;
import java.util.UUID;

public class ReplyCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.MESSAGE)) {
            source.sendMessage(pluginMessages.noPermission);
            return;
        }

        if (args.length == 0) {
            source.sendMessage(pluginMessages.notEnoughArgs);
            return;
        }

        if (source instanceof Player) {
            String message = String.join(" ", args);
            Player sender = (Player) source;
            UUID senderUUID = sender.getUniqueId();

            if (ProxyMessageEvent.replyMap.containsKey(senderUUID)) {
                UUID recipientUUID = ProxyMessageEvent.replyMap.get(senderUUID);
                Optional<Player> recipient = proxyServer.getPlayer(recipientUUID);

                if (recipient.isPresent()) {
                    ProxyMessageEvent.sendMessage(sender, recipient.get(), message, proxyServer);
                    ProxyMessageEvent.replyMap.put(recipientUUID, senderUUID);
                } else {
                    source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(pluginMessages.legacyColor("&4Invalid of offline player!")));
                }
            } else {
                source.sendMessage(MSEssentialsPluginInfo.pluginPrefix.append(pluginMessages.legacyColor("Nobody to reply to!")));
            }
        }
    }
}
