package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import com.mojang.brigadier.context.CommandContext;
import org.anvilpowered.anvil.api.misc.Named;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.server.LocationService;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.anvil.api.util.UserService;
import org.anvilpowered.catalyst.api.plugin.PluginMessages;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
        String server = context.getArgument("server", String.class);
        String userName = userService.getUserName((TPlayer) context.getSource());

        if (target.equals("_a")) {
            int[] total = new int[1];
            for (TPlayer p : userService.getOnlinePlayers()) {
                locationService.getServer(server).map(s -> s.connect(userService.getUUID(p)).thenApply(result -> {
                    if (result) {
                        total[0] = total[0] + 1;
                        textService.builder()
                            .append(pluginInfo.getPrefix())
                            .green().append("You have been sent to ")
                            .gold().append(server)
                            .green().append(" by ")
                            .gold().append(userName)
                            .sendTo(p);
                        return 1;
                    }
                    return 0;
                }).join());
            }
            textService.builder()
                .append(pluginInfo.getPrefix())
                .green().append("You have sent ")
                .gold().append(String.valueOf(total[0]))
                .green().append(" players to ")
                .gold().append(server)
                .sendTo(context.getSource());
            return 1;
        }

        if (target.equals("_r")) {
            Collection<TPlayer> onlinePlayers = userService.getOnlinePlayers();
            int random = (int) ((onlinePlayers.size() - 1) * Math.random());
            if (onlinePlayers instanceof List && random >= 0) {
                TPlayer randomPlayer = ((List<TPlayer>) onlinePlayers).get(random + 1);
                locationService.getServer(server).map(s -> s.connect(userService.getUUID(randomPlayer)).thenApply((result -> {
                    if (result) {
                        textService.builder()
                            .append(pluginInfo.getPrefix())
                            .green().append("You have been sent to ")
                            .gold().append(server)
                            .green().append(" by ")
                            .gold().append(userName)
                            .sendTo(randomPlayer);
                        textService.builder()
                            .append(pluginInfo.getPrefix())
                            .green().append("You have sent ")
                            .gold().append(userService.getUserName(randomPlayer))
                            .green().append(" to ")
                            .gold().append(server)
                            .sendTo(context.getSource());
                        return 1;
                    }
                    return 0;
                })));
                return 0;
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
                TPlayer finalCurrent = current;
                locationService.getServer(server).map(s -> s.connect(userService.getUUID(finalCurrent)).thenApply(result -> {
                    if (result) {
                        textService.builder()
                            .append(pluginInfo.getPrefix())
                            .green().append("You have been sent to ")
                            .gold().append(server)
                            .green().append(" by ")
                            .gold().append(userName)
                            .sendTo(finalCurrent);
                        textService.builder()
                            .append(pluginInfo.getPrefix())
                            .green().append("You have sent ")
                            .gold().append(userService.getUserName(finalCurrent))
                            .green().append(" to ")
                            .gold().append(server)
                            .sendTo(context.getSource());
                        return 1;
                    }
                    return 0;
                }));
            }
            return 0;
        }

        if (target.startsWith("_")) {
            String targetServer = target.replace("_", "");
            for (TPlayer p : userService.getOnlinePlayers()) {
                String targetUserName = userService.getUserName(p);
                if (targetServer.equals(locationService.getServer(targetUserName).map(Named::getName).orElse(null))) {
                    locationService.getServer(server).map(s -> s.connect(targetUserName).thenApply((result -> {
                        if (result) {
                            textService.builder()
                                .append(pluginInfo.getPrefix())
                                .green().append("You were sent to ")
                                .gold().append(targetServer)
                                .green().append(" by ")
                                .gold().append(userName)
                                .sendTo(p);
                        }
                        return 1;
                    })));
                }
            }
            textService.builder()
                .append(pluginInfo.getPrefix())
                .green().append("You have sent all players from ")
                .gold().append(targetServer)
                .green().append(" to ")
                .gold().append(server)
                .sendTo(context.getSource());
            return 1;
        }

        Optional<TPlayer> targetPlayer = userService.getPlayer(target);
        if (targetPlayer.isPresent()) {
            locationService.getServer(server).map(s -> s.connect(target).thenApply(result -> {
                if (result) {
                    textService.builder()
                        .append(pluginInfo.getPrefix())
                        .green().append("You have been sent to ")
                        .gold().append(server)
                        .sendTo(targetPlayer.get());
                    textService.builder()
                        .append(pluginInfo.getPrefix())
                        .gold().append(target)
                        .green().append(" has been sent to ")
                        .gold().append(server)
                        .sendTo(context.getSource());
                    return 1;
                }
                textService.builder()
                    .append(pluginInfo.getPrefix())
                    .green().append("Could not send ")
                    .gold().append(target)
                    .green().append(" to ")
                    .gold().append(server)
                    .sendTo(context.getSource());
                return 1;
            }));
        } else {
            textService.send(textService.of(pluginMessages.offlineOrInvalidPlayer()), context.getSource());
            return 0;
        }
        return 0;
    }
}
