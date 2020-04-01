package org.anvilpowered.catalyst.velocity.command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import org.anvilpowered.anvil.api.data.registry.Registry;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.anvilpowered.catalyst.api.service.ChatService;
import org.checkerframework.checker.nullness.qual.NonNull;

public class IgnoreCommand implements Command {

    @Inject
    private Registry registry;

    @Inject
    private ChatService<TextComponent, Player, CommandSource> chatService;

    @Inject
    private ProxyServer proxyServer;

    @Inject
    private PluginMessages<TextComponent> pluginMessages;

    @Inject
    private PluginInfo<TextComponent> pluginInfo;


    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (!source.hasPermission(registry.getOrDefault(CatalystKeys.IGNORE))) {
            source.sendMessage(pluginMessages.getNoPermission());
            return;
        }

        if (args.length == 0) {
            source.sendMessage(pluginMessages.getNotEnoughArgs());
            source.sendMessage(pluginMessages.ignoreCommandUsage());
            return;
        }
        String userName = args[0];

        if (proxyServer.getPlayer(userName).filter(p ->
            p.hasPermission(registry.getOrDefault(CatalystKeys.IGNORE_EXEMPT))).isPresent()) {
            source.sendMessage(pluginMessages.ignoreExempt());
            return;
        }

        if (args.length == 1 && source instanceof Player && proxyServer.getPlayer(userName).isPresent()) {
            Player player = (Player) source;
            player.sendMessage(
                chatService.ignore(player.getUniqueId(), proxyServer.getPlayer(userName).get().getUniqueId()));
        } else {
            source.sendMessage(pluginInfo.getPrefix().append(TextComponent.of("Offline or invalid player."))));
        }
    }
}
