package rocks.milspecsg.msessentials.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import rocks.milspecsg.msessentials.events.ProxyStaffChatEvent;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;

public class StaffChatCommand implements Command {

    @Inject
    private PluginMessages pluginMessages;

    @Inject
    private ProxyServer proxyServer;

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(PluginPermissions.STAFFCHAT)) {
            source.sendMessage(pluginMessages.noPermission);
        }

        if (source instanceof Player) {
            Player player = (Player) source;
            if (args.length == 0) {
                if (ProxyStaffChatEvent.staffChatSet.contains(player.getUniqueId())) {
                    ProxyStaffChatEvent.staffChatSet.remove(player.getUniqueId());
                    player.sendMessage(pluginMessages.staffChatToggle(false));
                } else {
                    ProxyStaffChatEvent.staffChatSet.add(player.getUniqueId());
                    source.sendMessage(pluginMessages.staffChatToggle(true));
                }
            } else {
                ProxyStaffChatEvent.staffChatSet.add(player.getUniqueId());
                String message = String.join(" ", args);
                ProxyStaffChatEvent proxyStaffChatEvent = new ProxyStaffChatEvent(player, message, TextComponent.of(message));
                proxyServer.getEventManager().fire(proxyStaffChatEvent).join();
            }
        } else {
            if (args.length == 0) {
                source.sendMessage(pluginMessages.notEnoughArgs);
            } else {
                String message = String.join(" ", args);
                proxyServer.getAllPlayers().stream().filter(target -> target.hasPermission(PluginPermissions.STAFFCHAT))
                        .forEach(target ->
                                target.sendMessage(pluginMessages.staffChatMessageFormattedConsole(TextComponent.of(message)))
                        );
            }
        }
    }
}
