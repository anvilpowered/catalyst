package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import org.anvilpowered.anvil.api.misc.Named;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.server.BackendServer;
import org.anvilpowered.anvil.api.server.LocationService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CommonSendCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private LocationService locationService;

    @Inject
    private PluginInfo<TString> pluginInfo;

    @Inject
    private PluginMessages<TString> pluginMessages;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private UserService<TPlayer, TPlayer> userService;

    public int execute(CommandContext<TCommandSource> context) {
        final String target = context.getArgument("player", String.class);
        String serverName = context.getArgument("server", String.class);
        String userName = userService.getUserName((TPlayer) context.getSource());
        BackendServer server = locationService.getServerForName(serverName).orElse(null);

        if (server == null) {
            textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not find server ")
                .gold().append(serverName)
                .sendTo(context.getSource());
            return 0;
        }

        if (target.equals("_a")) {
            CompletableFuture.runAsync(() -> {
                int total = 0;
                for (TPlayer p : userService.getOnlinePlayers()) {
                    if (locationService.getServerForName(serverName).map(s -> s.connect(p).join()).orElse(false)) {
                        ++total;
                        textService.builder()
                            .append(pluginInfo.getPrefix())
                            .green().append("You have been sent to ")
                            .gold().append(serverName)
                            .green().append(" by ")
                            .gold().append(userName)
                            .sendTo(p);
                    }
                }
                textService.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("You have sent ")
                    .gold().append(total)
                    .green().append(" players to ")
                    .gold().append(serverName)
                    .sendTo(context.getSource());
            });
            return 1;
        }

        if (target.equals("_r")) {
            Collection<TPlayer> onlinePlayers = userService.getOnlinePlayers();
            int random = (int) (onlinePlayers.size() * Math.random()) - 1;
            if (onlinePlayers instanceof List && random >= -1) {
                TPlayer randomPlayer = ((List<TPlayer>) onlinePlayers).get(random + 1);
                connect(server, context.getSource(), randomPlayer);
                return 1;
            }
            Iterator<TPlayer> iterator = onlinePlayers.iterator();
            TPlayer current = null;
            for (int i = 0; i <= random; i++) {
                if (iterator.hasNext()) {
                    current = iterator.next();
                }
                if (i == random) {
                    break;
                }
            }
            if (current != null) {
                return connect(server, context.getSource(), current);
            }
            return 1;
        }

        if (target.startsWith("_")) {
            String targetServer = target.replace("_", "");
            for (TPlayer p : userService.getOnlinePlayers()) {
                String targetUserName = userService.getUserName(p);
                if (targetServer.equals(locationService.getServer(targetUserName).map(Named::getName).orElse(null))) {
                    server.connect(p).thenAccept((result -> {
                        if (result) {
                            textService.builder()
                                .append(pluginInfo.getPrefix())
                                .green().append("You were sent to ")
                                .gold().append(serverName)
                                .green().append(" by ")
                                .gold().append(userName)
                                .sendTo(p);
                        }
                    }));
                }
            }
            textService.builder()
                .append(pluginInfo.getPrefix())
                .green().append("You have sent all players from ")
                .gold().append(targetServer)
                .green().append(" to ")
                .gold().append(serverName)
                .sendTo(context.getSource());
            return 1;
        }

        Optional<TPlayer> targetPlayer = userService.getPlayer(target);
        if (targetPlayer.isPresent()) {
            connect(locationService.getServerForName(serverName).orElse(null), context.getSource(), targetPlayer.get());
        } else {
            textService.send(textService.of(pluginMessages.offlineOrInvalidPlayer()), context.getSource());
            return 0;
        }
        return 0;
    }

    private int connect(@Nullable BackendServer server, TCommandSource source, TPlayer target) {
        if (server == null) {
            return 0;
        }
        String targetName = userService.getUserName(target);
        server.connect(target).thenApply(result -> {
            if (result) {
                textService.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("You have been sent to ")
                    .gold().append(server.getName())
                    .sendTo(target);
                if (!source.equals(target)) {
                    textService.builder()
                        .append(pluginInfo.getPrefix())
                        .gold().append(targetName)
                        .green().append(" has been sent to ")
                        .gold().append(server.getName())
                        .sendTo(source);
                }
                return 1;
            }
            textService.builder()
                .append(pluginInfo.getPrefix())
                .red().append("Could not send ")
                .gold().append(targetName)
                .red().append(" to ")
                .gold().append(server.getName())
                .sendTo(source);
            return 1;
        });
        return 0;
    }
}
