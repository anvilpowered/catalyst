/*
 *     MSEssentials - MilSpecSG
 *     Copyright (C) 2020 STG_Allen
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rocks.milspecsg.msessentials.listeners;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.text.TextComponent;
import rocks.milspecsg.msessentials.MSEssentials;
import rocks.milspecsg.msessentials.api.member.MemberManager;
import rocks.milspecsg.msessentials.events.ProxyChatEvent;
import rocks.milspecsg.msessentials.events.ProxyStaffChatEvent;
import rocks.milspecsg.msessentials.modules.utils.LuckPermsUtils;
import rocks.milspecsg.msessentials.modules.messages.PluginMessages;
import rocks.milspecsg.msessentials.modules.utils.PluginPermissions;
import rocks.milspecsg.msessentials.modules.chatutils.ChatFilter;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProxyChatListener {

    @Inject
    public PluginMessages pluginMessages;

    @Inject
    public MemberManager<TextComponent> memberManager;

    @Inject
    public ChatFilter chatFilter;

    @Inject
    private ProxyServer proxyServer;


    @Subscribe
    public void onChat(PlayerChatEvent e) throws ExecutionException, InterruptedException {
        String message = e.getMessage();
        Player player = e.getPlayer();

        if (ProxyStaffChatEvent.staffChatSet.contains(player.getUniqueId())) {
            ProxyStaffChatEvent proxyStaffChatEvent = new ProxyStaffChatEvent(player, message, TextComponent.of(message));
            proxyServer.getEventManager().fire(proxyStaffChatEvent).join();
            e.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        List<String> swearList = chatFilter.isswear(message);
        if (swearList != null) {
            if (e.getResult().isAllowed()) {
                if (!player.hasPermission(PluginPermissions.LANGUAGE_ADMIN)) {
                    for (String swear : swearList) {
                        message = message.replace(swear, "****");
                    }
                    ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), checkPlayerName(message), TextComponent.of(checkPlayerName(message)));
                    proxyServer.getEventManager().fire(proxyChatEvent).join();

                } else {
                    ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), checkPlayerName(message), TextComponent.of(checkPlayerName(message)));
                    proxyServer.getEventManager().fire(proxyChatEvent).join();
                }
            } else {
                e.setResult(PlayerChatEvent.ChatResult.denied());
            }
        } else {
            if (e.getResult().isAllowed()) {
                ProxyChatEvent proxyChatEvent = new ProxyChatEvent(e.getPlayer(), message, TextComponent.of(message));
                proxyServer.getEventManager().fire(proxyChatEvent).join();
                sendMessage(e, checkPlayerName(message));
            }
        }
    }

    public String checkPlayerName(String message) {
        for (Player onlinePlayer : MSEssentials.getServer().getAllPlayers()) {
            if (message.toLowerCase().equalsIgnoreCase(onlinePlayer.getUsername())) {
                message = message.replaceAll(onlinePlayer.getUsername().toLowerCase(), "&b@" + onlinePlayer.getUsername() + "&r");
            }
        }
        return message;
    }

    public String getRank(Player player) {
        return LuckPermsUtils.getPrefix(player);
    }

    public String getSuffix(Player player) {
        return LuckPermsUtils.getSuffix(player);
    }

    public String getChatColor(Player player) {
        return LuckPermsUtils.getChatColor(player);
    }

    public String getNameColor(Player player) {
        return LuckPermsUtils.getNameColor(player);
    }

    public void sendMessage(PlayerChatEvent e, String message) throws ExecutionException, InterruptedException {
        //Set the result to denied to broadcast our own message
        e.setResult(PlayerChatEvent.ChatResult.denied());
        Player player = e.getPlayer();
        //Grab all the information we will need from luckperms
        //The only supported permissions plugin to date
        String prefix = getRank(player);
        String chatColor = getChatColor(player);
        String nameColor = getNameColor(player);
        String suffix = getSuffix(player);
        if (chatColor == null) {
            chatColor = "&r";
        }
        if (nameColor == null) {
            nameColor = "&r";
        }
        String finalNameColor = nameColor;
        Tristate hasColorPermission = player.getPermissionValue(PluginPermissions.CHATCOLOR);

        memberManager.formatMessage(prefix, finalNameColor, player.getUsername(), chatColor + message, suffix, hasColorPermission.asBoolean()).thenAcceptAsync(optionalMessage -> {
            if (optionalMessage.equals(TextComponent.of("You are muted!"))) {
                player.sendMessage(TextComponent.of("You are muted!"));
                return;
            }
            for (Player p : proxyServer.getAllPlayers()) {
                p.sendMessage(optionalMessage);
            }
        });

    }
}
