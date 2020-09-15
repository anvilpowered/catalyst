package org.anvilpowered.catalyst.common.command;

import com.google.inject.Inject;
import org.anvilpowered.anvil.api.plugin.PluginInfo;
import org.anvilpowered.anvil.api.registry.Registry;
import org.anvilpowered.anvil.api.util.TextService;
import org.anvilpowered.catalyst.api.data.key.CatalystKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonServerCommand<
    TString,
    TPlayer extends TCommandSource,
    TCommandSource> {

    @Inject
    private PluginInfo<TString> pluginInfo;

    @Inject
    private TextService<TString, TCommandSource> textService;

    @Inject
    private Registry registry;

    public void sendAvailableServers(Map<String, Integer> serverInfo, String playerPrefix,
                                     boolean currentServerOnline,
                                     String currentServer,
                                     TPlayer player) {
        List<TString> availableServers = new ArrayList<>();
        boolean useAdvServerInfo = registry.getOrDefault(CatalystKeys.ADVANCED_SERVER_INFO_ENABLED);
        int count = 0;

        for (Map.Entry<String, Integer> entry : serverInfo.entrySet()) {
            String serverName = entry.getKey();
            Integer playerCount = entry.getValue();
            if (useAdvServerInfo) {
                if (serverName.contains(playerPrefix) && currentServerOnline) {
                    if (count >= 8) {
                        availableServers.add(textService.of("\n"));
                        count = 0;
                    }
                    if (currentServer.equals(serverName)) {
                        availableServers.add(textService.builder()
                            .green().append(serverName.replace(playerPrefix, "") + " ")
                            .onHoverShowText(
                                textService.of(
                                    "Online Player: " + playerCount))
                            .build());
                    } else {
                        availableServers.add(textService.builder()
                            .gray().append(serverName.replace(playerPrefix, "") + " ")
                            .onClickRunCommand("/server " + serverName)
                            .onHoverShowText(textService.of(
                                "Online Player: " + playerCount))
                            .build());
                    }
                }
            } else {
                if(currentServer.equals(serverName)) {
                    availableServers.add(textService.builder()
                        .green().append(serverName + " ")
                        .onHoverShowText(textService.of(
                            "Online Players: " + playerCount))
                        .build());
                } else {
                    availableServers.add(textService.builder()
                        .gray().append(serverName + " ")
                        .onClickRunCommand("/server " + serverName)
                        .onHoverShowText(textService.of(
                            "Online Players: " + playerCount))
                        .build());
                }
            }
            textService.builder()
                .append(pluginInfo.getPrefix())
                .green().append("Green = Current").yellow().append(", ")
                .gray().append("Gray = Available").yellow().append(", ")
                .red().append("Red = Offline\n")
                .dark_aqua().append("-----------------------------------------------------\n")
                .append(availableServers)
                .dark_aqua().append("\n-----------------------------------------------------\n")
                .gold().append("Click an available server to join!")
                .sendTo(player);
        }
    }
}
