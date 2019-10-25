package essentials.modules.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.discordbridge.Bridge;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.StaffChat.StaffChat;
import essentials.modules.Utils;
import essentials.modules.events.MSEssentialsChatFormedEvent;
import essentials.modules.proxychat.ProxyChat;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProxyChatEvent {


    @Subscribe
    public void onChat(PlayerChatEvent e) {
        String message = e.getMessage();
        Player player = e.getPlayer();

        if (PlayerConfig.muted.contains(player.getUsername())) {
            player.sendMessage(PluginMessages.prefix.append(TextComponent.of("You are currently muted!")));
            e.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        if (StaffChat.toggledSet.contains(player.getUniqueId())) {
            e.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        List<String> swearlist = MSEssentials.wordCatch.isswear(message);
        if (swearlist != null) {
            if (e.getResult().isAllowed()) {
                if (!player.hasPermission(PluginPermissions.LANGUAGEADMIN)) {

                    for (String swear : swearlist) {
                        message = message.replace(swear, "****");
                    }
                    sendMessage(e, checkPlayerName(message));

                }
                sendMessage(e, checkPlayerName(message));
            } else {
                e.setResult(PlayerChatEvent.ChatResult.denied());
            }

        } else {
            if (e.getResult().isAllowed())
                sendMessage(e, checkPlayerName(message));
        }
    }

    public static String checkPlayerName(String message) {
        for (Player onlinePlayer : MSEssentials.getServer().getAllPlayers()) {
            if (message.toLowerCase().contains(onlinePlayer.getUsername().toLowerCase())) {
                message = message.replaceAll(onlinePlayer.getUsername().toLowerCase(), "&b@" + onlinePlayer.getUsername() + "&r");
            }
        }
        return message;
    }

    public static String getRank(Player player) {
        User user = MSEssentials.api.getUser(player.getUniqueId());
        Optional<Contexts> context = MSEssentials.api.getContextManager().lookupApplicableContexts(user);
        UserData userData = user.getCachedData();
        MetaData userMeta = userData.getMetaData(context.get());
        String prefix = userMeta.getPrefix();

        return prefix;
    }

    public static void sendMessage(PlayerChatEvent e, String message) {
        e.setResult(PlayerChatEvent.ChatResult.denied());
        Player player = e.getPlayer();
        User user = MSEssentials.api.getUser(player.getUniqueId());

        Optional<Contexts> contextsOptional = MSEssentials.api.getContextManager().lookupApplicableContexts(user);

        UserData cachedData = user.getCachedData();
        MetaData userMeta = cachedData.getMetaData(contextsOptional.get());

        String prefix = getRank(player);
        String chatColor = userMeta.getMeta().get("chat-color");
        String nameColor = userMeta.getMeta().get("name-color");

        e.setResult(PlayerChatEvent.ChatResult.denied());


        TextComponent name = TextComponent.of(player.getUsername());
        if (message.contains("&")) {
            if (!player.hasPermission(PluginPermissions.CHATCOLOR)) {
                message = Utils.removeColorCodes(message);
            }
        }
        if (PlayerConfig.hasNickName(player.getUsername())) {
            name = PluginMessages.legacyColor(PlayerConfig.getNickName(player.getUsername()));
        }
        if (nameColor != null) {
            name = ProxyChat.legacyColor(nameColor + player.getUsername());
            if (PlayerConfig.hasNickName(player.getUsername())) {
                name = PluginMessages.legacyColor(nameColor + PlayerConfig.getNickName(player.getUsername()));
            }
        }
        if (chatColor != null) {
            message = chatColor + message;
        }

        if (prefix == null) {
            prefix = "";
        }
        for (Player p : MSEssentials.server.getAllPlayers()) {
            p.sendMessage(ProxyChat.legacyColor(prefix)
                    .append(name).append(TextComponent.of(": "))
                    .append(ProxyChat.legacyColor(message))
                    .hoverEvent(HoverEvent.showText(TextComponent.of(player.getUsername()).append(TextComponent.of("\n" + player.getCurrentServer().get().getServerInfo().getName())))));
        }
        String finalMessage = message;
        MSEssentialsChatFormedEvent formedEvent = new MSEssentialsChatFormedEvent(player, finalMessage, ProxyChat.legacyColor(message));
        MSEssentials.server.getEventManager().fire(formedEvent).join();
    }
}