package essentials.modules.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import essentials.MSEssentials;
import essentials.modules.Config.PlayerConfig;
import essentials.modules.LuckpermsHook;
import essentials.modules.PluginMessages;
import essentials.modules.PluginPermissions;
import essentials.modules.StaffChat.StaffChat;
import essentials.modules.Utils;
import essentials.modules.proxychat.ProxyChat;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

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

                } else {
                    sendMessage(e, checkPlayerName(message));
                }
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
        String prefix =  LuckpermsHook.getPrefix(player);

        return prefix;
    }

    public static void sendMessage(PlayerChatEvent e, String message) {

        e.setResult(PlayerChatEvent.ChatResult.denied());
        Player player = e.getPlayer();

        String prefix = getRank(player);
        String chatColor = LuckpermsHook.getChatColor(player);
        String nameColor = LuckpermsHook.getNameColor(player);

        e.setResult(PlayerChatEvent.ChatResult.denied());

        TextComponent.Builder messageBuilder = TextComponent.builder();
        String[] words = message.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (words[i].matches("[^\\s,]+\\.[a-zA-Z1-9]{2,}(/[^\\s,.]*)*")) {
                // is url
                TextComponent hoverMessage = TextComponent.builder()
                    .append(TextComponent.of("Click to open ", TextColor.GRAY))
                    .append(TextComponent.of(words[i], TextColor.AQUA))
                    .append(TextComponent.of(" in your browser", TextColor.GRAY))
                    .build();

                messageBuilder.append(TextComponent.builder()
                    .append(words[i])
                    .color(TextColor.BLUE)
                    .decoration(TextDecoration.UNDERLINED, true)
                    .clickEvent(ClickEvent.openUrl(words[i]))
                    .hoverEvent(HoverEvent.showText(hoverMessage))
                );
            } else {
                messageBuilder.append(ProxyChat.legacyColor(words[i]));
            }

            // add space between each word
            if (i != words.length - 1) {
                messageBuilder.append(" ");
            }
        }

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
        TextComponent messageToSend = TextComponent.builder()
                .append(ProxyChat.legacyColor(prefix))
                .append(name).append(TextComponent.of(": "))
                .append(PluginMessages.legacyColor(message))
                .hoverEvent(HoverEvent.showText(TextComponent.of(player.getUsername())
                        .append(TextComponent.of("\n" + player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("error")))))
                .build();
        for (Player p : MSEssentials.server.getAllPlayers()) {
            p.sendMessage(messageToSend);
        }
        MSEssentialsChatFormedEvent formedEvent = new MSEssentialsChatFormedEvent(player, message, messageToSend);
        MSEssentials.server.getEventManager().fireAndForget(formedEvent);

    }
}